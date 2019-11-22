package com.chinedu.reactivedemo;

import com.chinedu.reactivedemo.dao.OfficerRepository;
import com.chinedu.reactivedemo.entities.Officer;
import com.chinedu.reactivedemo.entities.Rank;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OfficerRepositoryTest {

    @Autowired
    OfficerRepository officerRepository;


    private List<Officer> millenniumFalconOfficers = Arrays.asList(
            new Officer(Rank.CAPTAIN, "Hans", "Solo"),
            new Officer(Rank.LIEUTENANT, "chewy", "Chewbacca")
    );

    @Before
    public void setUp() throws Exception {
        officerRepository.deleteAll()
                .thenMany(Flux.fromIterable(millenniumFalconOfficers))
                .flatMap(officerRepository::save)
                .then()
                .block();
    }

    @Test
    public void save() {
        Officer me = new Officer(Rank.CAPTAIN, "Chinedu", "Amadi-Ndukwe");
        StepVerifier.create(officerRepository.save(me))
                .expectNextMatches(officer -> !officer.getId().equals(""))
                .verifyComplete();
    }

    @Test
    public void findAll() {
        StepVerifier.create(officerRepository.findAll()).expectNextCount(2).verifyComplete();
    }

    @Test
    public void findById() {
        millenniumFalconOfficers.stream().map(Officer::getId).
                forEach(id -> StepVerifier.create(officerRepository.findById(id)).expectNextCount(1).verifyComplete());
    }
}
