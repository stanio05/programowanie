package pl.edu.pw.elka.prm2t.numberlink;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GameUI extends JFrame {
    private Game game;
    private JPanel boardPanel;

    public GameUI(Game game) {
        this.game = game;
        setTitle("Numberlink Game");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupUI();
        game.startNewGame();
        repaint();
    }

    private void setupUI() {
        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g);
            }
        };
        boardPanel.setPreferredSize(new Dimension(500, 500));
        boardPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                handleMouseClick(e);
            }
        });

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            game.resetBoard();
            boardPanel.repaint();
        });

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> {
            game.undoMove();
            boardPanel.repaint();
        });

        JButton checkButton = new JButton("Check Solution");
        checkButton.addActionListener(e -> {
            if (game.checkSolution()) {
                JOptionPane.showMessageDialog(GameUI.this,
                        "Gratulacje! Rozwiązałeś łamigłówkę poprawnie!\nWszystkie ścieżki są połączone i nie ma pustych pól.",
                        "Wygrana!",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(GameUI.this,
                        "Jeszcze nie rozwiązane.\nSprawdź czy:\n1. Wszystkie pary są połączone\n2. Nie ma pustych pól",
                        "W trakcie rozwiązywania",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        JPanel controlPanel = new JPanel();
        controlPanel.add(resetButton);
        controlPanel.add(undoButton);
        controlPanel.add(checkButton);

        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    private void handleMouseClick(MouseEvent e) {
        int tileSize = boardPanel.getWidth() / game.board.tiles[0].length;
        int col = e.getX() / tileSize;
        int row = e.getY() / tileSize;

        if (row >= 0 && row < game.board.tiles.length && col >= 0 && col < game.board.tiles[0].length) {
            Tile clickedTile = game.board.tiles[row][col];
            game.step(clickedTile);
            boardPanel.repaint();
        }
    }

    private void drawBoard(Graphics g) {
        if (game.board == null || game.board.tiles == null) return;

        int rows = game.board.tiles.length;
        int cols = game.board.tiles[0].length;
        int tileSize = Math.min(boardPanel.getWidth() / cols, boardPanel.getHeight() / rows);

        // Draw tiles
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Tile tile = game.board.tiles[i][j];
                int x = j * tileSize;
                int y = i * tileSize;

                if (tile.isHighlighted) {
                    g.setColor(new Color(tile.highlightColor.getRed(),
                            tile.highlightColor.getGreen(),
                            tile.highlightColor.getBlue(),
                            150));
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                }
                g.fillRect(x, y, tileSize, tileSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, tileSize, tileSize);

                if (tile.value != null) {
                    g.drawString(String.valueOf(tile.value), x + tileSize / 2 - 4, y + tileSize / 2 + 4);
                }
            }
        }

        // Draw paths
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(3));

        for (Move move : game.moveHistory) {
            if (move.pathColor != null) {
                g2.setColor(move.pathColor);
            } else {
                g2.setColor(Color.BLUE);
            }

            List<Tile> path = move.path;
            for (int i = 0; i < path.size() - 1; i++) {
                Tile t1 = path.get(i);
                Tile t2 = path.get(i + 1);
                int x1 = t1.y * tileSize + tileSize / 2;
                int y1 = t1.x * tileSize + tileSize / 2;
                int x2 = t2.y * tileSize + tileSize / 2;
                int y2 = t2.x * tileSize + tileSize / 2;
                g2.drawLine(x1, y1, x2, y2);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameUI(new Game()));
    }
}