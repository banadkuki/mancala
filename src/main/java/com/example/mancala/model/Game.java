package com.example.mancala.model;

import lombok.Data;

@Data
public class Game {
    private String id;
    private Player player1;
    private Player player2;
    private String currentPlayerId;
    private Board board;
    private GameStatus status;
    private String winnerId;
}