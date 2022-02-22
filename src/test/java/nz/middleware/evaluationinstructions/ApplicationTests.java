package nz.middleware.evaluationinstructions;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@SpringBootTest
class ApplicationTests {
	public static final Logger LOGGER = LoggerFactory.getLogger(ApplicationTests.class);

	private RestTemplate restTemplate = new RestTemplate();
	private String URL = "http://localhost:8080/companies/";

	/**
	 * Check different scenarios, including a "404".
	 * In an ideal world, we'd use Wiremock to stub the remote service.
	 */
	@Test
	public void testCompaniesService() {
		assertThat(
				restTemplate.getForObject(URL + "1", String.class),
				is("{\"id\":1,\"name\":\"MWNZ\",\"description\":\"..is awesome\"}"));
		assertThat(
				restTemplate.getForObject(URL + "2", String.class),
				is("{\"id\":2,\"name\":\"Other\",\"description\":\"....is not\"}"));
		assertThat(
				restTemplate.getForObject(URL + "3", String.class),
				containsString("\"error\":\"404 Not Found"));
	}

}
