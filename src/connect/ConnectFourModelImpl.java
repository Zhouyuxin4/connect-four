
package connect;

/**
 * Implementation of the Connect Four game model.
 */
public class ConnectFourModelImpl implements ConnectFourModel {
  private final Player[][] board;
  private final int rows;
  private final int columns;
  private Player currentPlayer;
  private boolean gameOver;
  private Player winner;

  /**
   * Constructs a ConnectFourModelImpl with the specified number of rows and columns.
   *
   * @param rows    the number of rows in the game board
   * @param columns the number of columns in the game board
   * @throws IllegalArgumentException if rows or columns are less than 4
   */
  public ConnectFourModelImpl(int rows, int columns) {
    if (rows < 4 || columns < 4) {
      throw new IllegalArgumentException("Board size cannot be smaller than 4x4");
    }
    this.rows = rows;
    this.columns = columns;
    this.board = new Player[rows][columns];
    this.currentPlayer = Player.RED;
    this.gameOver = false;
    this.winner = null;
    initializeBoard();
  }

  /**
   * Checks if the game is over after the most recent move.
   *
   * @param row    the row of the most recent move
   * @param column the column of the most recent move
   */
  private void checkGameOver(int row, int column) {
    if (isWinningMove(row, column)) {
      gameOver = true;
      winner = currentPlayer;
    } else if (isBoardFull()) {
      gameOver = true;
    }
  }

  /**
   * Checks if the most recent move is a winning move.
   *
   * @param row    the row of the most recent move
   * @param column the column of the most recent move
   * @return true if the move results in a win, false otherwise
   */
  private boolean isWinningMove(int row, int column) {
    return checkDirection(row, column, 1, 0)
        || checkDirection(row, column, 0, 1)
        || checkDirection(row, column, 1, 1)
        || checkDirection(row, column, 1, -1);
  }

  /**
   * Checks if there are four consecutive tokens in a specified direction.
   *
   * @param row      the starting row
   * @param column   the starting column
   * @param rowDelta the row increment for each step
   * @param colDelta the column increment for each step
   * @return true if there are four consecutive tokens, false otherwise
   */
  private boolean checkDirection(int row, int column, int rowDelta, int colDelta) {
    int count = 1;
    for (int i = 1; i < 4; i++) {
      int r = row + i * rowDelta;
      int c = column + i * colDelta;
      if (r >= 0 && r < rows && c >= 0 && c < columns && board[r][c] == currentPlayer) {
        count++;
      } else {
        break;
      }
    }
    for (int i = 1; i < 4; i++) {
      int r = row - i * rowDelta;
      int c = column - i * colDelta;
      if (r >= 0 && r < rows && c >= 0 && c < columns && board[r][c] == currentPlayer) {
        count++;
      } else {
        break;
      }
    }
    return count >= 4;
  }

  /**
   * Checks if the game board is full.
   *
   * @return true if the board is full, false otherwise
   */
  private boolean isBoardFull() {
    for (int col = 0; col < columns; col++) {
      if (board[0][col] == null) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void initializeBoard() {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        board[row][col] = null;
      }
    }
  }

  @Override
  public void makeMove(int column) {
    if (column < 0 || column >= columns || gameOver) {
      throw new IllegalArgumentException("Invalid move");
    }

    for (int row = rows - 1; row >= 0; row--) {
      if (board[row][column] == null) {
        board[row][column] = currentPlayer;
        checkGameOver(row, column);
        currentPlayer = (currentPlayer == Player.RED) ? Player.YELLOW : Player.RED;
        return;
      }
    }
    throw new IllegalArgumentException("Column is full");
  }

  @Override
  public int getRows() {
    return rows;
  }

  @Override
  public int getColumns() {
    return columns;
  }

  @Override
  public Player getWinner() {
    return winner;
  }

  @Override
  public boolean isGameOver() {
    return gameOver;
  }

  @Override
  public void resetBoard() {
    initializeBoard();
    currentPlayer = Player.RED;
    gameOver = false;
    winner = null;
  }

  @Override
  public Player getTurn() {
    if (gameOver) {
      return null;
    }
    return currentPlayer;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        sb.append(board[row][col] == null ? "." : board[row][col].getDisplayName().charAt(0));
        if (col < columns - 1) {
          sb.append(" ");
        }
      }
      sb.append("\n");
    }
    return sb.toString();
  }
}