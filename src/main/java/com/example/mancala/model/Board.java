package com.example.mancala.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Board {
    private List<Pit> pits = new ArrayList<>(14);
}