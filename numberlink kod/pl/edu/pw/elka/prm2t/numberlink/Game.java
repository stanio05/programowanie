package pl.edu.pw.elka.prm2t.numberlink;

import java.awt.Color;
import java.util.*;
import java.util.List;

public class Game {
    public Board board;
    public Stack<Move> moveHistory = new Stack<>();
    public DifficultyLevel difficulty = DifficultyLevel.EASY;
    public String currentStateFilePath;

    private List<Tile> activePath = new ArrayList<>();

    private Stack<Tile> activePathHistory = new Stack<>();

    public List<Tile> getActivePath() {
        return activePath;
    }

    public void startNewGame() {
        board = new Board();
        board.initialize(difficulty);
        moveHistory.clear();
        activePath.clear();
        activePathHistory.clear();
    }


    public void undoMove() {

        if (!activePath.isEmpty() && !activePathHistory.isEmpty()) {
            undoActivePathStep();
        }

        else if (!moveHistory.isEmpty()) {
            Move lastMove = moveHistory.pop();
            lastMove.reverse();
        }
    }


    private void undoActivePathStep() {
        if (activePath.isEmpty() || activePathHistory.isEmpty()) return;


        Tile lastTile = activePathHistory.pop();
        activePath.remove(lastTile);


        lastTile.isHighlighted = false;


        if (!activePath.isEmpty()) {
            Tile previousTile = activePath.get(activePath.size() - 1);
            board.removeConnection(previousTile, lastTile);
        }


        if (activePath.isEmpty()) {
            activePathHistory.clear();
        }
    }

    public void resetBoard() {
        if (board != null) {
            board.clearPath();
            moveHistory.clear();
            activePath.clear();
            activePathHistory.clear();
            for (Tile[] row : board.tiles) {
                for (Tile tile : row) {
                    tile.isHighlighted = false;
                    tile.highlightColor = Color.YELLOW;
                }
            }
        }
    }

    public boolean checkSolution() {
        if (board == null) return false;


        boolean allNumbersConnected = board.isComplete();

        boolean noEmptyTiles = true;
        for (Tile[] row : board.tiles) {
            for (Tile tile : row) {
                if (tile.isEmpty()) {
                    noEmptyTiles = false;
                    break;
                }
            }
            if (!noEmptyTiles) break;
        }

        return allNumbersConnected && noEmptyTiles;
    }


    public void step(Tile next) {
        if (activePath.isEmpty()) {
            if (next.value != null && !next.isConnected()) {
                if (next.connectedTo != null) {
                    removePathForTile(next);
                    return;
                }
                activePath.add(next);
                activePathHistory.push(next);
                next.isHighlighted = true;
                next.highlightColor = getColorForValue(next.value);
            }
            return;
        }

        Tile last = activePath.get(activePath.size() - 1);
        if (board.isValidNextStep(last, next)) {
            if (next.value != null && !next.value.equals(activePath.get(0).value)) {
                return;
            }

            board.drawPath(last, next);
            activePath.add(next);
            activePathHistory.push(next);
            next.isHighlighted = true;
            next.highlightColor = activePath.get(0).highlightColor;

            if (next.value != null && next.value.equals(activePath.get(0).value)) {

                moveHistory.push(new Move(activePath.get(0), next, new ArrayList<>(activePath)));
                for (Tile t : activePath) t.isHighlighted = false;
                activePath.clear();
                activePathHistory.clear();
            }
        }
    }

    private void removePathForTile(Tile tile) {
        for (Iterator<Move> iterator = moveHistory.iterator(); iterator.hasNext();) {
            Move move = iterator.next();
            if (move.path.contains(tile)) {
                for (Tile t : move.path) {
                    t.disconnect();
                }
                iterator.remove();
                break;
            }
        }
    }

    private Color getColorForValue(Integer value) {
        if (value == null) return Color.BLUE;
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW};
        return colors[(value - 1) % colors.length];
    }
}