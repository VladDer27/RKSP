package com.example.Practice4Client.controller;

import com.example.Practice4Client.model.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tournaments")
public class RequestResponseController {
    private final RSocketRequester rSocketRequester;

    @Autowired
    public RequestResponseController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @GetMapping("/{id}")
    public Mono<Tournament> getTournament(@PathVariable Long id) {
        return rSocketRequester
                .route("getTournament")
                .data(id)
                .retrieveMono(Tournament.class);
    }

    @PostMapping
    public Mono<Tournament> addTournament(@RequestBody Tournament tournament) {
        return rSocketRequester
                .route("addTournament")
                .data(tournament)
                .retrieveMono(Tournament.class);
    }
}
