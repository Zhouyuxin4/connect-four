package connect;

import java.io.IOException;

/**
 * This class represents a console-based controller for a Connect Four game. It handles user inputs
 * and interactions with the model and view, following the Model-View-Controller design pattern.
 */
public class SwingConnectFourController implements ConnectFourController {
  private final ConnectFourModel model;
  private final ConnectFourView view;

  /**
   * Constructs a SwingConnectFourController.
   *
   * @param model the model to handle game functions
   * @param view  the view to display game messages and state
   */
  public SwingConnectFourController(ConnectFourModel model, ConnectFourView view) {
    this.model = model;
    this.view = view;
    view.setController(this);
    this.view.initializeGrid(model. getRows(), model.getColumns());
  }


  @Override
  public void playGame() throws IllegalArgumentException, IOException {
    view.displayPlayerTurn(model.getTurn().toString());
    view.displayGameState(model.toString());
  }

  @Override
  public void makeMove(int col) throws IOException {
    try {
      model.makeMove(col);
      view.displayGameState(model.toString());
      if (model.isGameOver()) {
        view.displayGameOver(model.getWinner() != null ? model.getWinner().toString() : null);
        view.askPlayAgain();
      } else {
        view.displayPlayerTurn(model.getTurn().toString());
      }
    } catch (IllegalArgumentException | IOException e) {
      view.displayErrorMessage("Invalid move. Try again.");
    }
  }

  @Override
  public void quitGame() {
    System.exit(0);
  }

  @Override
  public void resetGame() throws IOException {
    model.resetBoard();
    view.displayGameState(model.toString());
  }

  @Override
  public String getCurrentPlayer() {
    return model.getTurn().toString();
  }
}