package br.com.lcn.goldenRaspberryAwards.controller;

import br.com.lcn.goldenRaspberryAwards.dto.PatchDTO;
import br.com.lcn.goldenRaspberryAwards.dto.ProducerWinnerFollowingDTO;
import br.com.lcn.goldenRaspberryAwards.model.producer.Producer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProducerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Order(1)
    @DisplayName("List all producers with success")
    public void GetAllProducers() {
        ResponseEntity<List<Producer>> response = testRestTemplate.exchange("/producer", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                }
        );
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().size() > 0);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    @Order(2)
    @DisplayName("Find producer by id with success")
    public void GetByIdWithSuccess() {
        ResponseEntity<Producer> response = testRestTemplate.getForEntity("/producer/1", Producer.class);

        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().getId());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    @Order(3)
    @DisplayName("Find producer by id with error NOT FOUND")
    public void GetByIdWithErrorNotFound() {
        ResponseEntity<Void> response = testRestTemplate.getForEntity("/producer/500", Void.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(4)
    @DisplayName("Create a new producer with Success")
    public void createNewProducerWithSuccess() {
        Producer producer = createProducerTest();
        ResponseEntity<Producer> response = testRestTemplate.exchange("/producer", HttpMethod.POST, new HttpEntity<>(producer),
                new ParameterizedTypeReference<>() {
                }
        );
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(5)
    @DisplayName("Create a new producer with BadRequest")
    public void createNewProducerWithBadRequest() {
        Producer producer = createProducerTestBadRequest();
        ResponseEntity<Void> response = testRestTemplate.exchange("/producer", HttpMethod.POST,
                new HttpEntity<>(producer), Void.class
        );
        Assertions.assertNull(response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(6)
    @DisplayName("Update a new producer with Success")
    public void updateProducerWithSuccess() {
        Producer producer = testRestTemplate.getForEntity("/producer/1", Producer.class).getBody();
        assert producer != null;
        producer.setName("Test update");

        ResponseEntity<Producer> response = testRestTemplate.exchange("/producer/{id}", HttpMethod.PUT, new HttpEntity<>(producer),
                Producer.class, producer.getId());
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    @Order(7)
    @DisplayName("Update producer with error BAD REQUEST")
    public void updateProducerWithErrorBadRequest() {
        Producer producer = testRestTemplate.getForEntity("/producer/1", Producer.class).getBody();
        assert producer != null;
        producer.setName("");

        ResponseEntity<Producer> response = testRestTemplate.exchange("/producer/{id}", HttpMethod.PUT, new HttpEntity<>(producer),
                Producer.class, producer.getId());

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(8)
    @DisplayName("Update producer with error NOT FOUND")
    public void updateProducerWithErrorNotFound() {
        Producer producer = testRestTemplate.getForEntity("/producer/1", Producer.class).getBody();
        assert producer != null;
        ResponseEntity<Producer> response = testRestTemplate.exchange("/producer/500", HttpMethod.PUT, new HttpEntity<>(producer),
                Producer.class);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(9)
    @DisplayName("Partial Update a new producer with Success")
    public void partialUpdateProducerWithSuccess() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<Producer> response = testRestTemplate.exchange("/producer/1", HttpMethod.PATCH,
                new HttpEntity<>(PatchDTO.builder().field("name").value("Test Integration").build()),
                Producer.class);


        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    @Order(10)
    @DisplayName("Partial Update producer with error BAD REQUEST")
    public void partialUpdateProducerWithErrorBadRequest() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<Producer> response = testRestTemplate.exchange("/producer/1", HttpMethod.PATCH,
                new HttpEntity<>(PatchDTO.builder().field("name").value("").build()),
                Producer.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(11)
    @DisplayName("Partial Update producer with error NOT FOUND")
    public void partialUpdateProducerWithErrorNotFound() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<Producer> response = testRestTemplate.exchange("/producer/500", HttpMethod.PATCH,
                new HttpEntity<>(PatchDTO.builder().field("launchYear").value("1985").build()),
                Producer.class);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(12)
    @DisplayName("Delete producer by id with error BAD REQUEST")
    public void DeleteByIdWithErrorBadRequest() {
        ResponseEntity<Void> response = testRestTemplate.exchange("/producer/10", HttpMethod.DELETE,
                null, Void.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    @Order(12)
    @DisplayName("Delete producer by id with success")
    public void DeleteByIdWithSuccess() {

        Producer producer = createProducerTest();
        ResponseEntity<Producer> include = testRestTemplate.exchange("/producer", HttpMethod.POST, new HttpEntity<>(producer),
                Producer.class
        );
        assert include.getBody() != null;
        ResponseEntity<Void> response = testRestTemplate.exchange("/producer/{id}", HttpMethod.DELETE,
                null, Void.class, include.getBody().getId());


        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }

    @Test
    @Order(13)
    @DisplayName("Delete producer by id with erro NOT FOUND")
    public void DeleteByIdWithErroNotFound() {
        ResponseEntity<Void> response = testRestTemplate.exchange("/producer/500", HttpMethod.DELETE,
                null, Void.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    @Order(1)
    @DisplayName("List following winners with the maximum and minimum interval")
    public void GetAllWinnerFollowing() {
        ResponseEntity<ProducerWinnerFollowingDTO> response = testRestTemplate.exchange("/producer/winner", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                }
        );
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getMax());
        Assertions.assertNotNull(response.getBody().getMin());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private Producer createProducerTest() {
        return Producer.builder().name("Producer Teste1").build();
    }


    private Producer createProducerTestBadRequest() {
        return Producer.builder().name("").build();
    }
}
