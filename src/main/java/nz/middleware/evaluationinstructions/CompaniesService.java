package nz.middleware.evaluationinstructions;

import nz.middleware.evaluationinstructions.api.CompaniesApi;
import nz.middleware.evaluationinstructions.model.Company;
import nz.middleware.evaluationinstructions.model.XmlCompany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CompaniesService implements CompaniesApi {
    public static final Logger LOGGER = LoggerFactory.getLogger(CompaniesService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${xml.service.url}")
    private String serviceUrl;


    /**
     * Retrieves the XmlCompany from the remote service.
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

    @Override
    public ResponseEntity<Company> companiesIdGet(BigDecimal id) {
        XmlCompany xmlCompany = getXmlCompany(id.intValue());
        return ResponseEntity.of(Optional.of(new Company().id(xmlCompany.getId()).description(xmlCompany.getDescription()).name(xmlCompany.getName())));
    }
}
