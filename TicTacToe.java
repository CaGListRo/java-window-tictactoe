
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe {

    // Custom JButton class representing a single tile on the game board
    private class ClickTile extends JButton {

        int row, col; // Position of the tile in the grid

        public ClickTile(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    // Variables for game state
    int currentPlayer = 1; // 1 for player one, -1 for player two
    JFrame frame = new JFrame("TicTacToe");
    JPanel buttonPanel = new JPanel(); // Panel holding the game tiles
    JLabel textLabel = new JLabel(); // Label displaying game status
    int textLabelHeight = 32; // Height of the status label

    // Board dimensions and other configurations
    int rows = 3; // Number of rows and columns
    int clickTileSize = 100; // Size of each tile
    int boardSize = (rows * clickTileSize) + 100 + ((rows - 1) * 50);
    int frameHeight = boardSize + textLabelHeight;
    int moveCounter = 0; // Counts the total number of moves
    int[][] logicBoard = new int[rows][rows]; // Logical representation of the board
    String playerOne = "\u274C"; // Player one's symbol (X)
    String playerTwo = "\u2B55"; // Player two's symbol (O)
    boolean gameOver = false;
    boolean tie = false;

    ClickTile[][] gameBoard = new ClickTile[rows][rows]; // Array of ClickTile buttons

    // Constructor initializes the game
    TicTacToe() {
        clearLogicBoard(); // Reset the logic board

        // Configure the main game frame
        frame.setSize(boardSize, frameHeight);
        frame.setPreferredSize(new Dimension(boardSize, frameHeight));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        // Configure the status label
        textLabel.setFont(new Font("Comic Sans", Font.PLAIN, 23));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Player " + getRealPlayerNumber() + "'s turn.");
        textLabel.setOpaque(true);
        frame.add(textLabel, BorderLayout.NORTH);

        // Configure the button panel with a grid layout
        buttonPanel.setLayout(new GridLayout(rows, rows));
        frame.add(buttonPanel, BorderLayout.CENTER);

        // Initialize the game board tiles
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                ClickTile clickTile = new ClickTile(i, j);
                gameBoard[i][j] = clickTile;

                clickTile.setFocusable(false);
                clickTile.setMargin(new Insets(0, 0, 0, 0));

                // Add mouse listener for tile clicks
                clickTile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) {
                            return;
                        }
                        ClickTile clickTile = (ClickTile) e.getSource();

                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (logicBoard[clickTile.row][clickTile.col] == 0) {
                                // Update logical board and display
                                logicBoard[clickTile.row][clickTile.col] = currentPlayer;
                                if (currentPlayer > 0) {
                                    clickTile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 100));
                                    clickTile.setText(playerOne);
                                } else {
                                    clickTile.setFont(new Font("Arial Unicode MS", Font.BOLD, 123));
                                    clickTile.setText(playerTwo);
                                }

                                // Check for win or tie
                                gameOver = checkWinning();
                                if (moveCounter == (rows * rows) - 1) {
                                    gameOver = true;
                                    tie = true;
                                }

                                if (!gameOver) {
                                    moveCounter++;
                                    swapPlayer();
                                    textLabel.setText("Player " + getRealPlayerNumber() + "'s turn.");
                                } else {
                                    // Display game over message
                                    if (tie) {
                                        textLabel.setText("Game Over! It's a tie!");
                                    } else {
                                        textLabel.setText("Game Over! Player " + getRealPlayerNumber() + " won!");
                                    }

                                    // Ask if the players want to play again
                                    boolean againAnswer = againDialog(frame);
                                    if (againAnswer) {
                                        moveCounter = 0;
                                        currentPlayer = 1;
                                        gameOver = false;
                                        clearLogicBoard();
                                        clearGameBoard();
                                    } else {
                                        System.exit(0);
                                    }
                                }
                            } else {
                                return;
                            }
                        }
                    }
                });
                buttonPanel.add(clickTile);
            }
        }
        frame.setVisible(true);
    }

    // Reset the logical board
    void clearLogicBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                logicBoard[i][j] = 0;
            }
        }
    }

    // Reset the game board visuals
    void clearGameBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                gameBoard[i][j].setText("");
                gameBoard[i][j].setEnabled(true);
            }
        }
    }

    // Swap the current player
    void swapPlayer() {
        currentPlayer *= -1;
    }

    // Get the real player number (1 or 2)
    int getRealPlayerNumber() {
        return currentPlayer == -1 ? 2 : 1;
    }

    // Check if a player has won
    boolean checkWinning() {
        // Check rows and columns
        for (int i = 0; i < rows; i++) {
            int rowSum = 0, colSum = 0;
            for (int j = 0; j < rows; j++) {
                rowSum += logicBoard[i][j];
                colSum += logicBoard[j][i];
            }
            if (Math.abs(rowSum) == rows || Math.abs(colSum) == rows) {
                return true;
            }
        }

        // Check diagonals
        int mainDiag = 0, antiDiag = 0;
        for (int i = 0; i < rows; i++) {
            mainDiag += logicBoard[i][i];
            antiDiag += logicBoard[i][rows - 1 - i];
        }
        return Math.abs(mainDiag) == rows || Math.abs(antiDiag) == rows;
    }

    // Display a dialog asking if the players want to play again
    private static boolean againDialog(JFrame parent) {
        final boolean[] userChoice = {false};

        JDialog dialog = new JDialog(parent, "Confirmation", true);
        dialog.setSize(300, 150);
        dialog.setLayout(new GridLayout(2, 1, 23, 23));
        dialog.setFont(new Font("Arial Unicode MS", Font.BOLD, 32));

        JPanel confirmButtonPanel = new JPanel();
        confirmButtonPanel.setLayout(new FlowLayout());

        JLabel message = new JLabel("Play Again?");
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setVerticalAlignment(SwingConstants.CENTER);
        dialog.add(message);

        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");

        yesButton.addActionListener(e -> {
            userChoice[0] = true;
            dialog.dispose();
        });

        noButton.addActionListener(e -> {
            userChoice[0] = false;
            dialog.dispose();
        });

        confirmButtonPanel.add(yesButton);
        confirmButtonPanel.add(noButton);
        dialog.add(confirmButtonPanel);

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        return userChoice[0];
    }
}
