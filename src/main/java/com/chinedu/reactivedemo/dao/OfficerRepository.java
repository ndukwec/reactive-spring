package com.chinedu.reactivedemo.dao;


import com.chinedu.reactivedemo.entities.Officer;
import com.chinedu.reactivedemo.entities.Rank;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;


public interface OfficerRepository extends ReactiveMongoRepository<Officer, String> {
    Flux<Officer> findByRank(Rank rank);
    Flux<Officer> findByLastName(String lastName);
}
