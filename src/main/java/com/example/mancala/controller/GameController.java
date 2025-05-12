package com.example.mancala.controller;

import com.example.mancala.model.Game;
import com.example.mancala.service.GameService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // 1. ساخت بازی جدید
    @PostMapping
    public Game createGame(@RequestBody CreateGameRequest request) {
        return gameService.createNewGame(request.getPlayer1(), request.getPlayer2());
    }

    // 2. مشاهده وضعیت بازی
    @GetMapping("/{gameId}")
    public Game getGame(@PathVariable String gameId) {
        return gameService.getGameById(gameId);
    }

    // 3. انجام حرکت
    @PostMapping("/{gameId}/move/{pitIndex}")
    public Game makeMove(@PathVariable String gameId, @PathVariable int pitIndex) {
        return gameService.makeMove(gameId, pitIndex);
    }

    // کلاس داخلی برای درخواست ساخت بازی
    @Data
    static class CreateGameRequest {
        private String player1;
        private String player2;
    }
}