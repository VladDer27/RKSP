package com.example.demo;

import com.example.demo.model.Tournament;
import com.example.demo.repository.TournamentRepository;
import io.rsocket.frame.decoder.PayloadDecoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TournamentControllerTests {
    @Autowired
    private TournamentRepository tournamentRepository;
    private RSocketRequester requester;

    @BeforeEach
    public void setup() {
        requester = RSocketRequester.builder()
                .rsocketStrategies(builder -> builder.decoder(new
                        Jackson2JsonDecoder()))
                .rsocketStrategies(builder -> builder.encoder(new
                        Jackson2JsonEncoder()))
                .rsocketConnector(connector -> connector
                        .payloadDecoder(PayloadDecoder.ZERO_COPY)
                        .reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .tcp("localhost", 7000);
    }

    @AfterEach
    public void cleanup() {
        requester.dispose();
    }

    @Test
    public void testGetTournament() {
        Tournament tournament = new Tournament();
        tournament.setName("TestTournament");
        tournament.setPrizePool(49);
        tournament.setStartDate(LocalDate.parse("2021-03-21"));
        tournament.setEndDate(LocalDate.parse("2024-05-16"));
        Tournament savedTournament = tournamentRepository.save(tournament);
        Mono<Tournament> result = requester.route("getTournament")
                .data(savedTournament.getId())
                .retrieveMono(Tournament.class);
        assertNotNull(result.block());
    }

    @Test
    public void testAddTournament() {
        Tournament tournament = new Tournament();
        tournament.setName("TestTournament");
        tournament.setPrizePool(49);
        tournament.setStartDate(LocalDate.parse("2021-03-21"));
        tournament.setEndDate(LocalDate.parse("2024-05-16"));
        Mono<Tournament> result = requester.route("addTournament")
                .data(tournament)
                .retrieveMono(Tournament.class);
        Tournament savedTournament = result.block();
        assertNotNull(savedTournament);
        assertNotNull(savedTournament.getId());
        assertTrue(savedTournament.getId() > 0);
    }

    @Test
    public void testGetTournaments() {
        Flux<Tournament> result = requester.route("getTournaments")
                .retrieveFlux(Tournament.class);
        assertNotNull(result.blockFirst());
    }
    @Test
    public void testDeleteTournament() {
        Tournament tournament = new Tournament();
        tournament.setName("TestTournament");
        tournament.setPrizePool(49);
        tournament.setStartDate(LocalDate.parse("2021-03-21"));
        tournament.setEndDate(LocalDate.parse("2024-05-16"));
        Tournament savedTournament = tournamentRepository.save(tournament);
        Mono<Void> result = requester.route("deleteTournament")
                .data(savedTournament.getId())
                .send();
        result.block();
        Tournament deletedTournament = tournamentRepository.findById(tournament.getId()).orElse(null);
        assertNotSame(deletedTournament, savedTournament);
    }
}
