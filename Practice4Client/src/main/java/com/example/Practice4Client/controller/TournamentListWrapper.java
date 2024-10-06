package com.example.Practice4Client.controller;


import com.example.Practice4Client.model.Tournament;
import lombok.Data;

import java.util.List;

@Data
public class TournamentListWrapper {
    private List<Tournament> tournaments;

}

