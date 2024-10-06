package com.example.demo.controller;

import com.example.demo.model.Tournament;
import com.example.demo.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class TournamentController {
    private final TournamentRepository tournamentRepository;

    @Autowired
    public TournamentController(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @MessageMapping("getTournament")
    public Mono<Tournament> getTournament(Long id) {
        return Mono.justOrEmpty(tournamentRepository.findById(id));
    }

    @MessageMapping("addTournament")
    public Mono<Tournament> addTournament(Tournament tournament) {
        return Mono.justOrEmpty(tournamentRepository.save(tournament));
    }

    @MessageMapping("getTournaments")
    public Flux<Tournament> getTournaments() {
        return Flux.fromIterable(tournamentRepository.findAll());
    }

    @MessageMapping("deleteTournament")
    public Mono<Void> deleteTournament(Long id) {
        Tournament tournament = tournamentRepository.findById(id).orElse(null);
        assert tournament != null;
        tournamentRepository.delete(tournament);
        return Mono.empty();
    }

    @MessageMapping("tournamentChannel")
    public Flux<Tournament> tournamentChannel(Flux<Tournament> tournaments) {
        return tournaments
                .flatMap(tournament -> Mono.fromCallable(() -> tournamentRepository.save(tournament)));
    }


}

