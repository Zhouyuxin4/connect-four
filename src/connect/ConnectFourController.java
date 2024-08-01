package connect;

import java.io.IOException;

/**
 * Represents a Controller for Connect Four: handles user moves by executing them
 * using the model and conveys move outcomes to the user in some form.
 */
public interface ConnectFourController {

  /**
   * Executes a single game of Connect Four using the provided model.
   * The game continues until it is over, at which point the method ends.
   *
   * @throws IllegalArgumentException if the model is null
   * @throws IOException if an input/output error occurs during the game
   */
  void playGame() throws IllegalArgumentException, IOException;

  /**
   * Makes a move in the specified column.
   *
   * @param col the column in which to make the move
   * @throws IOException if an input/output error occurs while making the move
   */
  void makeMove(int col) throws IOException;

  /**
   * Quits the current game.
   * Prompts the user for confirmation before exiting.
   */
  void quitGame();

  /**
   * Resets the game to its initial state, allowing for a new game to start.
   *
   * @throws IOException if an input/output error occurs during the reset
   */
  void resetGame() throws IOException;

  /**
   * Returns the current player's identifier (e.g., "RED" or "YELLOW").
   *
   * @return the current player's identifier
   */
  String getCurrentPlayer();
}
