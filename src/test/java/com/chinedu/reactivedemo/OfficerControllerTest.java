package com.chinedu.reactivedemo;

import com.chinedu.reactivedemo.dao.OfficerRepository;
import com.chinedu.reactivedemo.entities.Officer;
import com.chinedu.reactivedemo.entities.Rank;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OfficerControllerTest {

    @Autowired
    private WebTestClient webTestClient; // ignore this error being thrown here lol intellij i love you

    @Autowired
    private OfficerRepository officerRepository;


    private List<Officer> millenniumFalconOfficers = Arrays.asList(
            new Officer(Rank.CAPTAIN, "Hans", "Solo"),
            new Officer(Rank.LIEUTENANT, "chewy", "Chewbacca")
    );

    @Before
    public void setUp() throws Exception {
        officerRepository.deleteAll()
                .thenMany(Flux.fromIterable(millenniumFalconOfficers))
                .flatMap(officerRepository::save)
                .doOnNext(System.out::println)
                .then()
                .block();
    }


    @Test
    public void testGetAllOfficers() {
        webTestClient.get().uri("/officers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Officer.class)
                .hasSize(2)
                .consumeWith(System.out::println);
    }

    @Test
    public void testGetOfficer() {
        webTestClient.get().uri("/officers/{id}", millenniumFalconOfficers.get(0).getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Officer.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void testPostOfficer() {
        Officer luke = new Officer(Rank.LIEUTENANT, "Luke", "Skywalker");

        webTestClient.post().uri("/officers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(luke), Officer.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.firstName").isEqualTo("Luke")
                .jsonPath("$.lastName").isEqualTo("Skywalker")
                .consumeWith(System.out::println);
    }
}
