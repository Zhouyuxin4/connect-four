package connect;

import java.io.IOException;

/**
 * Main class to run a Connect 4 game interactively on the console.
 * This class initializes the game model, view, and controller.
 * The number of rows and columns for the Connect 4 grid can be configured
 * through command-line arguments, but defaults to 6 rows and 7 columns.
 */
public class Main {

  /**
   * The entry point of the Connect 4 game application.
   * It initializes the game model with 6 rows and 7 columns,
   * sets up the graphical view, and creates the game controller.
   * Finally, it starts the game loop allowing players to interactively play Connect 4.
   *
   * @param args command-line arguments (not used)
   * @throws IOException if an input or output error occurs during game execution
   */
  public static void main(String[] args) throws IOException {
    ConnectFourModel model = new ConnectFourModelImpl(6, 7);
    ConnectFourView view = new SwingConnectFourView("Connect 4");
    ConnectFourController controller = new SwingConnectFourController(model, view);
    controller.playGame();
  }
}