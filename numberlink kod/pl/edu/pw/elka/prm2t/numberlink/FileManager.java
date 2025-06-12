package pl.edu.pw.elka.prm2t.numberlink;

public class FileManager {
    public void saveGameState(Game game, String path) {
        // Save game state to file
    }

    public Game loadGameState(String path) {
        // Load game state from file
        return new Game();
    }

    public void saveBoardToFile(Board board, String path) {
        // Save board to file
    }

    public Board loadBoardFromFile(String path) {
        // Load board from file
        return new Board();
    }
}
