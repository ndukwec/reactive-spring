package com.chinedu.reactivedemo.controller;


import com.chinedu.reactivedemo.dao.OfficerRepository;
import com.chinedu.reactivedemo.entities.Officer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/officers")
public class OfficerController {

    private final OfficerRepository officerRepository;

    public OfficerController(OfficerRepository officerRepository) {
        this.officerRepository = officerRepository;
    }

    @GetMapping
    public Flux<Officer> getAllOfficers(){
        return officerRepository.findAll();
    }

    @GetMapping("{id}")
    public Mono<Officer> getOfficer(@PathVariable String id){
        return officerRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Officer> saveOfficer(@RequestBody Officer officer){
        return officerRepository.save(officer);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<Officer>> updateOfficer(@PathVariable(value = "id") String id,
                                                       @RequestBody Officer officer){
        return officerRepository.findById(id)
                .flatMap(existingOfficer -> {
                    existingOfficer.setRank(officer.getRank());
                    existingOfficer.setFirstName(officer.getFirstName());
                    existingOfficer.setLastName(officer.getLastName());
                    return officerRepository.save(existingOfficer);
                })
                .map(updateOfficer -> new ResponseEntity<>(updateOfficer, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteOfficer(@PathVariable(value = "id") String id) {
        return officerRepository.deleteById(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping
    public Mono<Void> deleteAllOfficers(){
        return officerRepository.deleteAll();
    }
}
