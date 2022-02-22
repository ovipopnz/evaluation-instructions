package nz.middleware.evaluationinstructions;

import nz.middleware.evaluationinstructions.model.Company;
import nz.middleware.evaluationinstructions.model.Error;
import nz.middleware.evaluationinstructions.model.XmlCompany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@RestController
public class CompaniesService {
    public static final Logger LOGGER = LoggerFactory.getLogger(CompaniesService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${xml.service.url}")
    private String serviceUrl;


    @GetMapping("/companies/{id}")
    public Company getCompany(@PathVariable("id") Integer id) {
        XmlCompany xmlCompany = getXmlCompany(id);
        return new Company().id(xmlCompany.getId()).description(xmlCompany.getDescription()).name(xmlCompany.getName());
    }

    @ExceptionHandler({HttpClientErrorException.class, ResponseStatusException.class})
    public Error handleNotFoundException(Exception ex, WebRequest request) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));

        return new Error().error(ex.getMessage()).errorDescription(sw.toString());
    }

    /**
     * @param id
     * @return
     */
    private XmlCompany getXmlCompany(Integer id) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_XML));
        // Request to return XML format
        headers.setContentType(MediaType.APPLICATION_XML);

        HttpEntity<XmlCompany> entity = new HttpEntity<>(headers);

        // Send request with GET method, and Headers.
        ResponseEntity<XmlCompany> response = restTemplate.exchange(String.format(serviceUrl, id),
                HttpMethod.GET, entity, XmlCompany.class);

        HttpStatus statusCode = response.getStatusCode();

        // Status Code: 200
        if (statusCode == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new ResponseStatusException(statusCode, "Could not retrieve XmlCompany " + id);
        }
    }
}
