package pl.edu.pw.elka.prm2t.numberlink;

import java.awt.Color;
import java.util.List;

public class Move {
    public Tile from;
    public Tile to;
    public List<Tile> path;
    public Color pathColor;

    public Move(Tile from, Tile to, List<Tile> path) {
        this.from = from;
        this.to = to;
        this.path = path;
        this.pathColor = getColorForValue(from.value);
    }

    private Color getColorForValue(Integer value) {
        if (value == null) return Color.BLUE;
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA};
        return colors[(value - 1) % colors.length];
    }

    public void reverse() {
        for (Tile tile : path) {
            tile.disconnect();
        }
    }
}