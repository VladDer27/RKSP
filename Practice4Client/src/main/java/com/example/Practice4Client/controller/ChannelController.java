package com.example.Practice4Client.controller;

import com.example.Practice4Client.model.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
public class ChannelController {
    private final RSocketRequester rSocketRequester;

    @Autowired
    public ChannelController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @PostMapping("/exp")
    public Flux<Tournament> addTournamentMultiple(@RequestBody TournamentListWrapper
                                             tournamentListWrapper) {
        List<Tournament> tournamentList = tournamentListWrapper.getTournaments();
        Flux<Tournament> tournaments = Flux.fromIterable(tournamentList);
        return rSocketRequester
                .route("tournamentChannel")
                .data(tournaments)
                .retrieveFlux(Tournament.class);
    }
}