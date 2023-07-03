package br.com.lcn.goldenRaspberryAwards.controller;

import br.com.lcn.goldenRaspberryAwards.dto.PatchDTO;
import br.com.lcn.goldenRaspberryAwards.model.movie.Movie;
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

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovieTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Order(1)
    @DisplayName("List all movies with success")
    public void GetAllMovies() {
        ResponseEntity<List<Movie>> response = testRestTemplate.exchange("/movie", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().size() > 0);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    @Order(2)
    @DisplayName("Find movie by id with success")
    public void GetByIdWithSuccess() {
        ResponseEntity<Movie> response = testRestTemplate.getForEntity("/movie/1", Movie.class);

        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().getId());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    @Order(3)
    @DisplayName("Find movie by id with error NOT FOUND")
    public void GetByIdWithErrorNotFound() {
        ResponseEntity<Void> response = testRestTemplate.getForEntity("/movie/300", Void.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(4)
    @DisplayName("Create a new movie with Success")
    public void createNewMovieWithSuccess() {
        Movie movie = createMovieTest();
        ResponseEntity<Movie> response = testRestTemplate.exchange("/movie", HttpMethod.POST, new HttpEntity<>(movie),
                new ParameterizedTypeReference<Movie>() {
                }
        );
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(5)
    @DisplayName("Create a new movie with BadRequest")
    public void createNewMovieWithBadRequest() {
        Movie movie = createMovieTestBadRequest();
        ResponseEntity<Void> response = testRestTemplate.exchange("/movie", HttpMethod.POST,
                new HttpEntity<>(movie), Void.class
        );
        Assertions.assertNull(response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(6)
    @DisplayName("Update a new movie with Success")
    public void updateMovieWithSuccess() {
        Movie movie = testRestTemplate.getForEntity("/movie/1", Movie.class).getBody();
        assert movie != null;
        movie.setTitle("Test update");

        ResponseEntity<Movie> response = testRestTemplate.exchange("/movie/{id}", HttpMethod.PUT, new HttpEntity<>(movie),
                Movie.class, movie.getId());
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    @Order(7)
    @DisplayName("Update a new movie with error BAD REQUEST")
    public void updateMovieWithErrorBadRequest() {
        Movie movie = testRestTemplate.getForEntity("/movie/1", Movie.class).getBody();
        assert movie != null;
        movie.getProducers().add(Producer.builder().name("Test Integration error").build());

        ResponseEntity<Movie> response = testRestTemplate.exchange("/movie/{id}", HttpMethod.PUT, new HttpEntity<>(movie),
                Movie.class, movie.getId());
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(8)
    @DisplayName("Update a new movie with error NOT FOUND")
    public void updateMovieWithErrorNotFound() {
        Movie movie = testRestTemplate.getForEntity("/movie/1", Movie.class).getBody();
        assert movie != null;
        ResponseEntity<Movie> response = testRestTemplate.exchange("/movie/300", HttpMethod.PUT, new HttpEntity<>(movie),
                Movie.class);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    @Order(9)
    @DisplayName("Partial Update a new movie with Success")
    public void partialUpdateMovieWithSuccess() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<Movie> response = testRestTemplate.exchange("/movie/1", HttpMethod.PATCH,
                new HttpEntity<>(PatchDTO.builder().field("launchYear").value("1985").build()),
                Movie.class);


        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    @Order(10)
    @DisplayName("Partial Update movie with error BAD REQUEST")
    public void partialUpdateMovieWithErrorBadRequest() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<Movie> response = testRestTemplate.exchange("/movie/1", HttpMethod.PATCH,
                new HttpEntity<>(PatchDTO.builder().field("launchYear").value("ABC").build()),
                Movie.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(11)
    @DisplayName("Partial Update movie with error NOT FOUND")
    public void partialUpdateMovieWithErrorNotFound() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<Movie> response = testRestTemplate.exchange("/movie/300", HttpMethod.PATCH,
                new HttpEntity<>(PatchDTO.builder().field("launchYear").value("1985").build()),
                Movie.class);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(12)
    @DisplayName("Delete movie by id with success")
    public void DeleteByIdWithSuccess() {
        ResponseEntity<Void> response = testRestTemplate.exchange("/movie/10", HttpMethod.DELETE,
                null, Void.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }

    @Test
    @Order(13)
    @DisplayName("Delete movie by id with erro NOT FOUND")
    public void DeleteByIdWithErroNotFound() {
        ResponseEntity<Void> response = testRestTemplate.exchange("/movie/300", HttpMethod.DELETE,
                null, Void.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    private Movie createMovieTest() {
        Producer producer1 = Producer.builder().id(1).build();
        Producer producer2 = Producer.builder().id(2).build();
        List<Producer> producerList = new ArrayList<>();
        producerList.add(producer1);
        producerList.add(producer2);
        return Movie.builder()
                .launchYear(2023)
                .studios("Integration")
                .title("Integration Test")
                .winner(false)
                .producers(producerList).build();
    }


    private Movie createMovieTestBadRequest() {
        Producer producer1 = Producer.builder().name("Producer Teste1").build();
        Producer producer2 = Producer.builder().name("Producer Teste2").build();
        List<Producer> producerList = new ArrayList<>();
        producerList.add(producer1);
        producerList.add(producer2);
        return Movie.builder()
                .launchYear(2023)
                .studios("Integration")
                .winner(false)
                .producers(producerList).build();
    }
}
