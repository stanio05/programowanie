package pl.edu.pw.elka.prm2t.numberlink;

import java.awt.*;
import java.util.*;

public class Board {
    public Tile[][] tiles;
    public int width, height;
    public void initialize(DifficultyLevel difficulty) {

        switch (difficulty) {
            case EASY:
                this.width = 3;
                this.height = 3;
                break;
            case MEDIUM:
                this.width = 4;
                this.height = 4;
                break;
            case HARD:
                this.width = 5;
                this.height = 5;
                break;
            default:
                this.width = 4;
                this.height = 4;
        }

        tiles = new Tile[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tiles[i][j] = new Tile(i, j, null);
            }
        }


        setupPuzzle(difficulty);
    }


    private void setupPuzzle(DifficultyLevel difficulty) {
        switch (difficulty) {
            case EASY:
                setupEasyPuzzle();
                break;
            case MEDIUM:
                setupMediumPuzzle();
                break;
            case HARD:
                setupHardPuzzle();
                break;
        }
    }

    private void setupEasyPuzzle() {
        tiles[0][0].value = 1;
        tiles[0][2].value = 1;


        tiles[2][0].value = 2;
        tiles[2][2].value = 2;


    }


    private void setupMediumPuzzle() {
        tiles[0][0].value = 1;
        tiles[3][0].value = 1;

        tiles[0][3].value = 2;
        tiles[3][3].value = 2;


    }

    private void setupHardPuzzle() {

        tiles[0][0].value = 1;
        tiles[0][4].value = 1;


        tiles[4][0].value = 2;
        tiles[4][4].value = 2;


        tiles[1][2].value = 3;
        tiles[3][2].value = 3;


    }

    public void initialize(int level) {
        initialize(DifficultyLevel.MEDIUM);
    }

    public void drawPath(Tile from, Tile to) {
        from.connectTo(to);
        to.connectTo(from);

        if (from.value != null && to.value != null) {
            Color color = getColorForValue(from.value);
            for (Tile tile : Arrays.asList(from, to)) {
                tile.pathColor = color;
            }
        }
    }


    public void removeConnection(Tile from, Tile to) {
        if (from.connectedTo == to) {
            from.connectedTo = null;
        }
        if (to.connectedTo == from) {
            to.connectedTo = null;
        }

        if (from.value == null) {
            from.pathColor = null;
        }
        if (to.value == null) {
            to.pathColor = null;
        }
    }

    private Color getColorForValue(int value) {
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW};
        return colors[(value - 1) % colors.length];
    }

    public void clearPath() {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                tile.disconnect();
                tile.pathColor = null;  // Resetuj kolor
            }
        }
    }

    public boolean isComplete() {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile.value != null && tile.connectedTo == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isValidNextStep(Tile from, Tile to) {
        int dx = Math.abs(from.x - to.x);
        int dy = Math.abs(from.y - to.y);
        return ((dx == 1 && dy == 0) || (dx == 0 && dy == 1)) && !to.isConnected();
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }
}