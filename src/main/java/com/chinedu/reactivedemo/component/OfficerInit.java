package com.chinedu.reactivedemo.component;


import com.chinedu.reactivedemo.dao.OfficerRepository;
import com.chinedu.reactivedemo.entities.Officer;
import com.chinedu.reactivedemo.entities.Rank;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Log4j2
public class OfficerInit implements ApplicationRunner {

    private final OfficerRepository officerRepository;


    public OfficerInit(OfficerRepository officerRepository) {
        this.officerRepository = officerRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        officerRepository.deleteAll()
                .thenMany(Flux.just(new Officer(Rank.ADMIRAL, "Anakin", "Skywalker"),
                                    new Officer(Rank.ENSIGN, "Erica", "Amadi")))
                .flatMap(officerRepository::save).then()
                .doOnEach(System.out::println).block();
    }
}
