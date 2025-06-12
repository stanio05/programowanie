package pl.edu.pw.elka.prm2t.numberlink;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GameUI extends JFrame {
    private Game game;
    private JPanel boardPanel;
    private JPanel menuPanel;
    private JPanel controlPanel;
    private boolean isInGame = false;
    private DifficultyLevel selectedDifficulty = DifficultyLevel.EASY;

    public GameUI(Game game) {
        this.game = game;
        setTitle("Numberlink Game");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupMenuUI();
        setVisible(true);
    }

    private void setupMenuUI() {
        getContentPane().removeAll();

        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(240, 248, 255));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titleLabel = new JLabel("NUMBERLINK");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(new Color(25, 25, 112));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Połącz wszystkie pary numerów!");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setOpaque(false);
        difficultyPanel.setLayout(new FlowLayout());
        difficultyPanel.setForeground(new Color(25, 25, 112));
        JLabel difficultyLabel = new JLabel("Poziom trudności: ");
        difficultyLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JComboBox<DifficultyLevel> difficultyCombo = new JComboBox<>(DifficultyLevel.values());
        difficultyCombo.setSelectedItem(selectedDifficulty);
        difficultyCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        difficultyCombo.addActionListener(e -> {
            selectedDifficulty = (DifficultyLevel) difficultyCombo.getSelectedItem();
        });

        difficultyPanel.add(difficultyLabel);
        difficultyPanel.add(difficultyCombo);

        JButton startGameButton = createMenuButton("Rozpocznij grę");
        startGameButton.addActionListener(e -> startGame());
        startGameButton.setForeground(new Color(25, 25, 112));
        JButton exitButton = createMenuButton("Wyjście");
        exitButton.addActionListener(e -> System.exit(0));
        exitButton.setForeground(new Color(25, 25, 112));
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(subtitleLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        menuPanel.add(difficultyPanel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(startGameButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(exitButton);
        menuPanel.add(Box.createVerticalGlue());

        add(menuPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(250, 50));
        button.setMaximumSize(new Dimension(250, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(25, 25, 112));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    private void startGame() {
        isInGame = true;
        game.difficulty = selectedDifficulty;
        game.startNewGame();
        setupGameUI();
    }

    private void setupGameUI() {
        getContentPane().removeAll();

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

        JButton backToMenuButton = new JButton("Powrót do menu");
        backToMenuButton.addActionListener(e -> {
            isInGame = false;
            setupMenuUI();
        });


        JLabel difficultyDisplayLabel = new JLabel("Poziom: " + selectedDifficulty);
        difficultyDisplayLabel.setFont(new Font("Arial", Font.BOLD, 14));

        controlPanel = new JPanel();
        controlPanel.add(difficultyDisplayLabel);
        controlPanel.add(resetButton);
        controlPanel.add(undoButton);
        controlPanel.add(checkButton);
        controlPanel.add(backToMenuButton);

        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void handleMouseClick(MouseEvent e) {
        if (!isInGame) return;

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