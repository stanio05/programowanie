package pl.edu.pw.elka.prm2t.numberlink;

import java.util.List;

public class LevelManager {
    public List<Board> exampleLevels;
    public int currentLevel;

    public Board loadExampleLevel(int levelNumber) {
        return new Board();
    }

    public Board loadFromFile(String path) {
        return new Board();
    }

    public List<DifficultyLevel> getAvailableDifficulties() {
        return List.of(DifficultyLevel.EASY, DifficultyLevel.MEDIUM, DifficultyLevel.HARD);
    }
}