package connect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Represents the graphical user interface for the Connect Four game.
 * This class extends JFrame and implements the ConnectFourView interface.
 */
public class SwingConnectFourView extends JFrame implements ConnectFourView {

  private JButton[][] grid;
  private ConnectFourController controller;
  private final ImageIcon redIcon;
  private final ImageIcon yellowIcon;

  /**
   * Constructs a SwingConnectFourView with the specified title.
   * It initializes the window and loads the icons for the players.
   *
   * @param title the title of the game window
   */
  public SwingConnectFourView(String title) {
    super(title);
    redIcon = loadAndScaleIcon("/images/red.png", 100, 100);
    yellowIcon = loadAndScaleIcon("/images/yellow.png", 100, 100);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());
  }

  @Override
  public void setController(ConnectFourController controller) {
    this.controller = controller;
  }

  @Override
  public void initializeGrid(int rows, int columns) {
    this.grid = new JButton[rows][columns];
    JPanel gridPanel = new JPanel();
    gridPanel.setLayout(new GridLayout(rows, columns));
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        grid[row][col] = new JButton();
        grid[row][col].setPreferredSize(new Dimension(100, 100));
        grid[row][col].setBackground(Color.WHITE);
        grid[row][col].setOpaque(true);
        grid[row][col].addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            try {
              handleMouseClick(e);
            } catch (IOException ex) {
              throw new RuntimeException(ex);
            }
          }
        });
        gridPanel.add(grid[row][col]);
      }
    }
    this.add(gridPanel, BorderLayout.CENTER);
    JPanel buttonPanel = new JPanel(new FlowLayout());
    JButton resetButton = new JButton("Reset");
    resetButton.addActionListener((ActionEvent e) -> {
      try {
        resetGame();
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });
    buttonPanel.add(resetButton);
    JButton exitButton = new JButton("Exit");
    exitButton.addActionListener(e -> quitGame());
    buttonPanel.add(exitButton);
    this.add(buttonPanel, BorderLayout.SOUTH);
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void displayGameState(String gameState) {
    String[] lines = gameState.split("\n");
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        char cell = lines[row].charAt(col * 2);
        if (cell == 'R') {
          grid[row][col].setIcon(redIcon);
        } else if (cell == 'Y') {
          grid[row][col].setIcon(yellowIcon);
        } else {
          grid[row][col].setIcon(null);
          grid[row][col].setBackground(Color.WHITE);
        }
        grid[row][col].setOpaque(true);
        grid[row][col].repaint();
      }
    }
    revalidate();
    repaint();
  }

  @Override
  public void displayPlayerTurn(String player) {
    ImageIcon icon;
    if ("RED".equals(player)) {
      icon = loadAndScaleIcon("/images/red.png", 50, 50);
    } else {
      icon = loadAndScaleIcon("/images/yellow.png", 50, 50);
    }
    JOptionPane.showMessageDialog(this,
        "Player " + player + ", make your move: " + "\n",
        "Player Turn",
        JOptionPane.INFORMATION_MESSAGE,
        icon);
  }

  @Override
  public void displayErrorMessage(String message) {
    ImageIcon icon = loadAndScaleIcon("/images/other.png", 50, 50);
    JOptionPane.showMessageDialog(this, message + "\n", "Error", JOptionPane.ERROR_MESSAGE, icon);
  }

  @Override
  public void displayGameOver(String winner) {
    String message;
    ImageIcon icon;
    if (winner == null) {
      message = "Game over! It's a tie!";
      icon = loadAndScaleIcon("/images/other.png", 50, 50);
    } else {
      message = "Game over! The winner is: " + winner + "!";
      if ("RED".equalsIgnoreCase(winner)) {
        icon = loadAndScaleIcon("/images/red.png", 50, 50);
      } else if ("YELLOW".equalsIgnoreCase(winner)) {
        icon = loadAndScaleIcon("/images/yellow.png", 50, 50);
      } else {
        icon = null;
      }
    }
    JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE,
        icon);
  }

  @Override
  public void askPlayAgain() throws IOException {
    ImageIcon icon = loadAndScaleIcon("/images/other.png", 50, 50);
    int response = JOptionPane.showConfirmDialog(this,
        "Play again?",
        "Game Over",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        icon);

    if (response == JOptionPane.YES_OPTION) {
      controller.resetGame();
    } else {
      System.exit(0);
    }
  }

  /**
   * Displays a confirmation dialog asking the user if they are sure they want to exit the game.
   * If the user confirms, the application will terminate.
   */
  private void quitGame() {
    ImageIcon icon = loadAndScaleIcon("/images/other.png", 50, 50);
    int response = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to exit?",
        "Exit Confirmation",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        icon);
    if (response == JOptionPane.YES_OPTION) {
      System.exit(0);
    }
  }

  /**
   * Resets the game, clearing the current game state and prompting the current player
   * to make their next move.
   *
   * @throws IOException if an input/output exception occurs during the reset process
   */
  private void resetGame() throws IOException {
    if (controller != null) {
      controller.resetGame();
      displayPlayerTurn(controller.getCurrentPlayer());
    }
  }

  /**
   * Loads and scales an icon from the specified resource path.
   *
   * @param resourcePath the path to the icon resource
   * @param width        the width to scale the icon to
   * @param height       the height to scale the icon to
   * @return the scaled icon, or null if the resource was not found
   */
  private ImageIcon loadAndScaleIcon(String resourcePath, int width, int height) {
    URL url = getClass().getResource(resourcePath);
    if (url != null) {
      ImageIcon icon = new ImageIcon(url);
      Image image = icon.getImage();
      Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
      return new ImageIcon(scaledImage);
    } else {
      System.err.println("Could not find resource: " + resourcePath);
      return null;
    }
  }

  /**
   * Handles a mouse click event by converting the player's click action
   * into a corresponding move in the game.
   *
   * @param e the mouse click event
   * @throws IOException if an input/output exception occurs while handling the click
   */
  private void handleMouseClick(MouseEvent e) throws IOException {
    JButton button = (JButton) e.getSource();
    for (JButton[] buttons : grid) {
      for (int col = 0; col < buttons.length; col++) {
        if (button == buttons[col]) {
          if (controller != null) {
            controller.makeMove(col);
          }
          break;
        }
      }
    }
  }
}
