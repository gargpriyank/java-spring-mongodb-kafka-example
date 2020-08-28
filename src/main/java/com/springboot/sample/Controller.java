package com.springboot.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/employee")
public class Controller {

    @Autowired
    private AppService appService;

    @Autowired
    private ESKafkaUtil esKafkaUtil;

    @PostMapping
    public ResponseEntity<Mono<Employee>> createEmployee(@RequestBody Employee employee) {
        log.info("New employee record: {}", employee);
        appService.sendMessageToESKafka(esKafkaUtil.convertToJSON(employee));
        return new ResponseEntity<>(Mono.just(employee), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Flux<Employee>> getAllEmployees() {
        return new ResponseEntity<>(appService.getAllEmployees(), HttpStatus.OK);
    }
}
