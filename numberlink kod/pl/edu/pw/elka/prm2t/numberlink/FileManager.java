package pl.edu.pw.elka.prm2t.numberlink;

public class FileManager {
    public void saveGameState(Game game, String path) {
    }

    public Game loadGameState(String path) {
        return new Game();
    }

    public void saveBoardToFile(Board board, String path) {
    }

    public Board loadBoardFromFile(String path) {
        return new Board();
    }
}
