package nz.middleware.evaluationinstructions;

import nz.middleware.evaluationinstructions.model.XmlCompany;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;


@SpringBootApplication
public class EvaluationInstructionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvaluationInstructionsApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(@Value("${xml.root.element}") String rootElement) {

        RestTemplate restTemplate = new RestTemplate();
        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliasesByType(Map.of(rootElement, XmlCompany.class));

        MarshallingHttpMessageConverter marshallingConverter =
                new MarshallingHttpMessageConverter(marshaller);
        marshallingConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        restTemplate.setMessageConverters(Collections.singletonList(marshallingConverter));

        return restTemplate;
    }
}
