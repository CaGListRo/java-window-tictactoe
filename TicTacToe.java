
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe {

    private class ClickTile extends JButton {

        int row, col;

        public ClickTile(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
    int currentPlayer = 1;
    JFrame frame = new JFrame("TicTacToe");
    JPanel buttonPanel = new JPanel();
    JLabel textLabel = new JLabel();
    int textLabelHeight = 32;

    int rows = 3;  // rows = columns
    int clickTileSize = 100;
    int boardSize = (rows * clickTileSize) + 100 + ((rows - 1) * 50);
    int frameHeight = boardSize + textLabelHeight;
    int moveCounter = 0;
    int[][] logicBoard = new int[rows][rows];
    String playerOne = "❌";
    String playerTwo = "⭕";
    boolean gameOver = false;
    boolean tie = false;

    ClickTile[][] gameBoard = new ClickTile[rows][rows];

    TicTacToe() {
        clearLogicBoard();
        frame.setSize(boardSize, frameHeight);
        frame.setPreferredSize(new Dimension(boardSize, frameHeight));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Comic Sans", Font.PLAIN, 23));
        frame.setPreferredSize(new Dimension(boardSize, textLabelHeight));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Player " + Integer.toString(getRealPlayerNumber()) + "'s turn.");
        textLabel.setOpaque(true);
        frame.add(textLabel, BorderLayout.NORTH);

        buttonPanel.setLayout(new GridLayout(rows, rows));
        frame.add(buttonPanel, BorderLayout.CENTER);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                ClickTile clickTile = new ClickTile(i, j);
                gameBoard[i][j] = clickTile;

                clickTile.setFocusable(false);
                clickTile.setMargin(new Insets(0, 0, 0, 0));

                // actionlistener
                clickTile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) {
                            return;
                        }
                        ClickTile clickTile = (ClickTile) e.getSource();

                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (logicBoard[clickTile.row][clickTile.col] == 0) {
                                logicBoard[clickTile.row][clickTile.col] = currentPlayer;
                                if (currentPlayer > 0) {
                                    clickTile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 100));
                                    clickTile.setText(playerOne);
                                } else {
                                    clickTile.setFont(new Font("Arial Unicode MS", Font.BOLD, 123));
                                    clickTile.setText(playerTwo);
                                }
                                gameOver = checkWinning();
                                if (moveCounter == (rows * rows) - 1) {
                                    gameOver = true;
                                    tie = true;
                                }
                                if (!gameOver) {
                                    moveCounter++;
                                    swapPlayer();
                                    textLabel.setText("Player " + Integer.toString(getRealPlayerNumber()) + "'s turn.");
                                } else {
                                    // logic for winning/playing again
                                    if (tie) {
                                        textLabel.setText("Game Over! It's a tie!");
                                    } else {
                                        textLabel.setText("Game Over! Player " + Integer.toString(getRealPlayerNumber()) + " won!");
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

    void clearLogicBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                logicBoard[i][j] = 0;
            }
        }
    }

    void swapPlayer() {
        currentPlayer *= -1;
    }

    int getRealPlayerNumber() {
        if (currentPlayer == -1) {
            return 2;
        } else {
            return 1;
        }
    }

    boolean checkWinning() {
        // check columns
        for (int i = 0; i < rows; i++) {
            int checkSum = 0;
            for (int j = 0; j < rows; j++) {
                checkSum += logicBoard[i][j];
            }
            if (Math.abs(checkSum) == rows) {
                return true;
            }
        }
        // check rows
        for (int i = 0; i < rows; i++) {
            int checkSum = 0;
            for (int j = 0; j < rows; j++) {
                checkSum += logicBoard[j][i];
            }
            if (Math.abs(checkSum) == rows) {
                return true;
            }
        }
        // check diagonals
        int checkSum = 0;
        for (int i = 0; i < rows; i++) {
            checkSum += logicBoard[i][i];
            if (Math.abs(checkSum) == rows) {
                return true;
            }
        }
        checkSum = 0;
        int j = rows;
        for (int i = 0; i < rows; i++) {
            j--;
            checkSum += logicBoard[i][j];
            if (Math.abs(checkSum) == rows) {
                return true;
            }
        }
        return false;
    }

}
