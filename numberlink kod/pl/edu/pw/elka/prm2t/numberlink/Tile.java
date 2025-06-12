package pl.edu.pw.elka.prm2t.numberlink;

import java.awt.Color;

public class Tile {
    public int x, y;
    public Integer value;
    public boolean isHighlighted;
    public Tile connectedTo;
    public Color highlightColor = Color.YELLOW;
    public Color pathColor;

    public Tile(int x, int y, Integer value) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.isHighlighted = false;
        this.connectedTo = null;
        this.pathColor = null;
    }

    public void connectTo(Tile tile) {
        this.connectedTo = tile;
    }

    public void disconnect() {
        this.connectedTo = null;
        this.pathColor = null;
    }

    public boolean isEmpty() {
        return value == null && connectedTo == null;
    }

    public boolean isConnected() {
        return connectedTo != null;
    }
}