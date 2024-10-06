package com.example.Practice4Client.controller;

import com.example.Practice4Client.model.Tournament;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tournaments")
public class RequestStreamController {
    private final RSocketRequester rSocketRequester;

    @Autowired
    public RequestStreamController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @GetMapping
    public Publisher<Tournament> getTournaments() {
        return rSocketRequester
                .route("getTournaments")
                .data(new Tournament())
                .retrieveFlux(Tournament.class);
    }
}
