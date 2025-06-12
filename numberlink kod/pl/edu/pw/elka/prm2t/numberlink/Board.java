package pl.edu.pw.elka.prm2t.numberlink;

import java.awt.*;
import java.util.*;

public class Board {
    public Tile[][] tiles;
    public int width, height;

    public void initialize(int level) {
        this.width = 5;
        this.height = 5;
        tiles = new Tile[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tiles[i][j] = new Tile(i, j, null);
            }
        }
        // Sample points for demo
        tiles[0][4].value = 3;
        tiles[2][1].value = 2;
        tiles[2][3].value = 3;
        tiles[3][3].value = 1;
        tiles[4][0].value = 1;
        tiles[4][4].value = 2;
    }

    public void drawPath(Tile from, Tile to) {
        from.connectTo(to);
        to.connectTo(from);
        // Przypisz kolor jeśli łączymy końce
        if (from.value != null && to.value != null) {
            Color color = getColorForValue(from.value);
            for (Tile tile : Arrays.asList(from, to)) {
                tile.pathColor = color;
            }
        }
    }

    private Color getColorForValue(int value) {
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA};
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
