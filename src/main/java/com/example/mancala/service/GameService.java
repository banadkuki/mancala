package com.example.mancala.service;

import com.example.mancala.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {

    private final Map<String, Game> games = new HashMap<>();

    public Game createNewGame(String player1Name, String player2Name) {
        Game game = new Game();
        game.setId(UUID.randomUUID().toString());

        Player player1 = new Player(UUID.randomUUID().toString(), player1Name);
        Player player2 = new Player(UUID.randomUUID().toString(), player2Name);

        game.setPlayer1(player1);
        game.setPlayer2(player2);
        game.setCurrentPlayerId(player1.getId());
        game.setStatus(GameStatus.IN_PROGRESS);

        // ساخت برد
        Board board = new Board();
        List<Pit> pits = new ArrayList<>();

        for (int i = 0; i < 14; i++) {
            Pit pit = new Pit();
            pit.setIndex(i);

            if (i == 6) {
                pit.setBigPit(true);
                pit.setOwnerId(player1.getId());
                pit.setStones(0);
            } else if (i == 13) {
                pit.setBigPit(true);
                pit.setOwnerId(player2.getId());
                pit.setStones(0);
            } else if (i >= 0 && i <= 5) {
                pit.setOwnerId(player1.getId());
                pit.setBigPit(false);
                pit.setStones(6);
            } else {
                pit.setOwnerId(player2.getId());
                pit.setBigPit(false);
                pit.setStones(6);
            }

            pits.add(pit);
        }

        board.setPits(pits);
        game.setBoard(board);

        games.put(game.getId(), game);

        return game;
    }

    public Game getGameById(String gameId) {
        return games.get(gameId);
    }

    public Game makeMove(String gameId, int pitIndex) {
        Game game = getGameById(gameId);
        if (game == null) throw new IllegalArgumentException("Game not found");
        if (game.getStatus() == GameStatus.FINISHED) throw new IllegalStateException("Game already finished");

        List<Pit> pits = game.getBoard().getPits();
        Pit selectedPit = pits.get(pitIndex);
        String currentPlayerId = game.getCurrentPlayerId();

        if (!selectedPit.getOwnerId().equals(currentPlayerId) || selectedPit.isBigPit()) {
            throw new IllegalArgumentException("Invalid pit selected");
        }

        int stones = selectedPit.getStones();
        if (stones == 0) throw new IllegalArgumentException("Selected pit is empty");

        selectedPit.setStones(0);
        int currentIndex = pitIndex;

        while (stones > 0) {
            currentIndex = (currentIndex + 1) % 14;
            Pit pit = pits.get(currentIndex);

            // پرش از big pit حریف
            if (pit.isBigPit() && !pit.getOwnerId().equals(currentPlayerId)) continue;

            pit.setStones(pit.getStones() + 1);
            stones--;
        }

        Pit lastPit = pits.get(currentIndex);

        // نوبت اضافه اگر توی big pit خودش افتاد
        if (lastPit.isBigPit() && lastPit.getOwnerId().equals(currentPlayerId)) {
            return game; // نوبتش حفظ می‌شه
        }

        // گرفتن مهره (capture)
        if (!lastPit.isBigPit()
                && lastPit.getOwnerId().equals(currentPlayerId)
                && lastPit.getStones() == 1) {

            int oppositeIndex = 12 - currentIndex;
            Pit oppositePit = pits.get(oppositeIndex);

            if (oppositePit.getStones() > 0) {
                Pit bigPit = getBigPitForPlayer(pits, currentPlayerId);
                bigPit.setStones(bigPit.getStones() + oppositePit.getStones() + 1);
                lastPit.setStones(0);
                oppositePit.setStones(0);
            }
        }

        // تغییر نوبت
        game.setCurrentPlayerId(getOpponentId(game, currentPlayerId));

        // بررسی پایان بازی
        if (isSideEmpty(pits, game.getPlayer1().getId()) || isSideEmpty(pits, game.getPlayer2().getId())) {
            finishGame(game);
        }

        return game;
    }

    private Pit getBigPitForPlayer(List<Pit> pits, String playerId) {
        return pits.stream()
                .filter(p -> p.isBigPit() && p.getOwnerId().equals(playerId))
                .findFirst()
                .orElseThrow();
    }

    private String getOpponentId(Game game, String currentPlayerId) {
        return currentPlayerId.equals(game.getPlayer1().getId())
                ? game.getPlayer2().getId()
                : game.getPlayer1().getId();
    }

    private boolean isSideEmpty(List<Pit> pits, String playerId) {
        return pits.stream()
                .filter(p -> !p.isBigPit() && p.getOwnerId().equals(playerId))
                .allMatch(p -> p.getStones() == 0);
    }

    private void finishGame(Game game) {
        List<Pit> pits = game.getBoard().getPits();
        String p1 = game.getPlayer1().getId();
        String p2 = game.getPlayer2().getId();

        Pit bigPit1 = getBigPitForPlayer(pits, p1);
        Pit bigPit2 = getBigPitForPlayer(pits, p2);

        for (Pit pit : pits) {
            if (!pit.isBigPit()) {
                if (pit.getOwnerId().equals(p1)) {
                    bigPit1.setStones(bigPit1.getStones() + pit.getStones());
                } else {
                    bigPit2.setStones(bigPit2.getStones() + pit.getStones());
                }
                pit.setStones(0);
            }
        }

        game.setStatus(GameStatus.FINISHED);
        if (bigPit1.getStones() > bigPit2.getStones()) {
            game.setWinnerId(p1);
        } else if (bigPit2.getStones() > bigPit1.getStones()) {
            game.setWinnerId(p2);
        } else {
            game.setWinnerId("draw");
        }
    }
}

