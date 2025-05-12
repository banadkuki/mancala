
# 🕹 Mancala Game - Java Spring Boot

This project is a simple implementation of the classic **Mancala (Kalaha)** board game, built using **Spring Boot**. It provides a REST API for two players to play the game turn-by-turn.

---

## 🚀 How to Run

### Prerequisites
- Java 17+
- Maven 3.8+
- (Optional) Postman or curl for testing

### Steps

```bash
# Clone the project
git clone https://github.com/banadkuki/mancala
cd mancala

# Run the application
./mvnw spring-boot:run
```

The server will start on:  
`http://localhost:8080`

---

## 🔗 API Endpoints

### 1. Create a New Game

**POST** `/api/games`  
Creates a new Mancala game with two players.

**Request Body:**
```json
{
  "player1": "Ali",
  "player2": "Sara"
}
```

**Response:**
A full `Game` object including board state.

---

### 2. View Game Status

**GET** `/api/games/{gameId}`  
Returns current game state (pits, player turn, winner, etc.).

---

### 3. Make a Move

**POST** `/api/games/{gameId}/move/{pitIndex}`  
Plays a move from the given pit index for the current player.

- `pitIndex` must be between 0–5 for player 1, or 7–12 for player 2.

---

## 🧠 Game Rules (Summary)

- Each player has 6 pits and 1 big pit.
- Players distribute stones from their pits clockwise.
- If the last stone lands in the player's own big pit, they play again.
- Landing in an empty pit on their own side captures opposite pit's stones.
- Game ends when one side's pits are all empty.
- The winner is the player with the most stones in their big pit.

---

## 📂 Project Structure

```
src/main/java/com/example/mancala/
├── controller/   # REST API layer
├── service/      # Game logic
├── model/        # Data models (Game, Player, Pit, etc.)
└── MancalaApp.java  # Main class
```

---

## 🧪 Testing

You can test the game logic using:
- Postman
- curl
- Integration test classes (optional)

---

## 🤝 Contact

If you have any questions, feel free to reach out.

Good luck & have fun playing Mancala!
