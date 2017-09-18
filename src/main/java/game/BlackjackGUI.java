package game;

import card.Card;
import player.BlackjackDealer;
import player.BlackjackPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Class that adds the game to a proper GUI.
 *
 *  @author David Stewart
 */
public class BlackjackGUI extends JPanel {
  
  //-------------------------------------------------------------
  // CONSTANTS
  //-------------------------------------------------------------
  
  //-------------------------------------------------
  // Game Panels
  //-------------------------------------------------
  
  /** Panel at top of GUI that will hold the action buttons. */
  private JPanel topPanel = new JPanel();
  
  /** Panel that will hold the dealer's cards. */
  private JPanel dealerCardPanel = new JPanel();
  
  /** Panel that will hold the player's cards. */
  private JPanel playerCardPanel = new JPanel();

  /** Panel that will hold the player's available betting pool. */
  private JPanel playerBankPanel = new JPanel();
  
  //-------------------------------------------------
  // Game Labels
  //-------------------------------------------------
  
  /** Label that indicates that the player is the dealer. */
  private JLabel dealerLabel = new JLabel();
  
  /** Label that indicates that the player is the player. */
  private JLabel playerLabel = new JLabel();
  
  /** Label that displays the player total. */
  private JLabel playerBankLabel = new JLabel();
  
  /** Text box that displays the outcome of the game. */
  private JTextPane statusTextBox = new JTextPane();
  
  //-------------------------------------------------
  // Game Buttons
  //-------------------------------------------------
  
  /** Button that performs a hit for the player. */
  private JButton hitButton = new JButton();
  
  /** Button that deals the cards at the start of the game. */
  private JButton dealButton = new JButton();
  
  /** Button that performs a stick for the player. */
  private JButton stickButton = new JButton();

  /** Button that performs a double down for the player. */
  private JButton doubleDownButton = new JButton();

  /** Button that resets the game. */
  private JButton playAgainButton = new JButton();
  
  //-------------------------------------------------
  // Action commands.
  //-------------------------------------------------
  
  private static final String DEAL_ACTION_COMMAND = "DEAL";
  
  private static final String STICK_ACTION_COMMAND = "STICK";
  
  private static final String DOUBLE_DOWN_ACTION_COMMAND = "DOUBLE_DOWN";
  
  private static final String HIT_ACTION_COMMAND = "HIT";
  
  private static final String PLAY_AGAIN_ACTION_COMMAND = "PLAY_AGAIN";

  //-------------------------------------------------
  // The labels to represent the cards for the game.
  //-------------------------------------------------
  
  /** This will represent the back of the card. */
  private JLabel dealerInitialCard0 = null;
  
  /** This will represent the dealer's first card. */
  private JLabel dealerInitialCard1 = null;
  
  /** This will represent the dealer's second card. */
  private JLabel dealerInitialCard2 = null;
  
  /** This will represent the player's first card. */
  private JLabel playerInitialCard1 = null;
  
  /** This will represent the player's second card. */
  private JLabel playerInitialCard2 = null;
  
  //-------------------------------------------------------------
  // MEMBERS
  //-------------------------------------------------------------
  
  /** The game instance. */
  private Blackjack game = new Blackjack();
  
  //-------------------------------------------------------------
  // METHODS
  //-------------------------------------------------------------
  
  /**
   * Show the game.
   */
    public void initialiseAndDisplayGame() {
      topPanel.setBackground(new Color(0, 122, 0));
      dealerCardPanel.setBackground(new Color(0, 122, 0));
      playerCardPanel.setBackground(new Color(0, 122, 0));
  
      topPanel.setLayout(new FlowLayout());
  
      statusTextBox.setText(" ");
      statusTextBox.setFont(new java.awt.Font("Helvetica Bold", 1, 20));
      statusTextBox.setVisible(false);
  
      createGameButtons();
  
      dealerLabel.setText("  Dealer:  ");
      playerLabel.setText("  Player:  ");
      updateBankLabel();
  
      topPanel.add(statusTextBox);
      topPanel.add(dealButton);
      topPanel.add(hitButton);
      topPanel.add(stickButton);
      topPanel.add(doubleDownButton);
      topPanel.add(playAgainButton);
  
      playerCardPanel.add(playerLabel);
      dealerCardPanel.add(dealerLabel);
  
      setLayout(new BorderLayout());
  
      add(topPanel,BorderLayout.NORTH);
      add(dealerCardPanel,BorderLayout.CENTER);
      add(playerCardPanel,BorderLayout.SOUTH);
      
      JFrame myFrame = new JFrame("BlackJack");
      myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      myFrame.setContentPane(this);
      myFrame.setPreferredSize(new Dimension(800,650));
      myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      //Display the window.
      myFrame.pack();
      myFrame.setVisible(true);
      myFrame.setLocationRelativeTo(null);
    }//end display

  /**
   * Deal out the dealer and player cards.
   */
  public void deal() {
    game.getDealer().dealNewGame(game.getPlayers(), game.getShoe());

    placeBet();

    dealerCardPanel.add(dealerLabel);
    playerCardPanel.add(playerLabel);
    playerBankPanel.add(playerBankLabel);

    dealerInitialCard0 = new JLabel(new ImageIcon(this.getClass().getResource("/card_images/back.jpg")));
  
    dealInitialCards(game.getDealer());
    dealInitialCards(game.getPlayers().get(0));

    dealerCardPanel.add(dealerInitialCard0);
    dealerCardPanel.add(dealerInitialCard2);

    playerCardPanel.add(playerInitialCard1);
    playerCardPanel.add(playerInitialCard2);

    dealerLabel.setText("  Dealer:  " + game.getDealer().getCardTotal());
    playerLabel.setText("  Player:  " + game.getPlayers().get(0).getCardTotal());

    hitButton.setEnabled(true);
    stickButton.setEnabled(true);
    dealButton.setEnabled(false);
    doubleDownButton.setEnabled(true);

    // TODO: If dealer has blackjack here, he wins.

    if (game.getPlayers().get(0).getCardTotal() == 21) {
      hitButton.setEnabled(false);
      stickButton.setEnabled(false);
      dealButton.setEnabled(false);
      playAgainButton.setEnabled(true);

      dealersTurn();
    }

    add(dealerCardPanel, BorderLayout.CENTER);
    add(playerCardPanel, BorderLayout.SOUTH);
    add(playerBankPanel, BorderLayout.EAST);
  }
  
  /**
   * This method retrieves the initial cards in the dealer's/player's
   * hand and sets the corresponding image in order to be displayed
   * in the GUI.
   *
   * @param pPlayer
   *   The player whose cards will be retrieved and displayed.
   */
  protected void dealInitialCards(BlackjackPlayer pPlayer) {
    Card card = null;
  
    for (int i = 0; i < 2; i++) {
      card = pPlayer.getHands().get(0).get(i);
    
      if (i == 0) {
        if (pPlayer instanceof BlackjackDealer) {
          dealerInitialCard1 = new JLabel(card.getCardImage());
        }
        else {
          playerInitialCard1 = new JLabel(card.getCardImage());
        }
      }
      else {
        if (pPlayer instanceof BlackjackDealer) {
          dealerInitialCard2 = new JLabel(card.getCardImage());
        }
        else {
          playerInitialCard2 = new JLabel(card.getCardImage());
        }
      }
    }
  }
  
  /**
   * Deals the payer another card.
   */
  public void hit() {
    doubleDownButton.setEnabled(false);

    Card hitCard = game.getDealer().dealCard(game.getPlayers().get(0), game.getShoe());

    JLabel hitCardImage = new JLabel(hitCard.getCardImage());
    playerCardPanel.add(hitCardImage);
    playerCardPanel.repaint();

    if (game.getPlayers().get(0).getHands().get(0).isBust() || game.getPlayers().get(0).getCardTotal() == 21) {
      hitButton.setEnabled(false);
      dealButton.setEnabled(false);
      stickButton.setEnabled(false);
      playAgainButton.setEnabled(true);

      dealersTurn();
    }

    playerLabel.setText("  Player:   " + game.getPlayers().get(0).getCardTotal());
  }

  /**
   * Sets the player's hand to stick and switches to the dealer.
   */
  public void stick() {
    game.getPlayers().get(0).getHands().get(0).setSticking(true);
    dealersTurn();
  }

  /**
   * Double down on the player's bet.
   */
  public void doubleDown() {
    doubleDownButton.setEnabled(false);
    game.getPlayers().get(0).doubleDown(game.getPlayers().get(0).getHands().get(0));
    updateBankLabel();
    hit();
    stick();
  }

  /**
   * Captures user input for placing a bet.
   */
  public void placeBet() {
    boolean isValidChoice = false;

    while (!isValidChoice) {
      String userInput = JOptionPane.showInputDialog("How much do you want to bet? Press \"M\" for minimum bet.");

      if (userInput.equalsIgnoreCase("M") || userInput.isEmpty()) {
        game.getPlayers().get(0).getHands().get(0).setBet(Blackjack.sTableMinimumBet);
        game.getPlayers().get(0).setPlayerBank(game.getPlayers().get(0).getPlayerBank() - Blackjack.sTableMinimumBet);
        updateBankLabel();
        isValidChoice = true;
      }
      else {
        try {
          game.getPlayers().get(0).getHands().get(0).setBet(Double.parseDouble(userInput));
          game.getPlayers().get(0).setPlayerBank(game.getPlayers().get(0).getPlayerBank() - Double.valueOf(userInput));
          updateBankLabel();
          isValidChoice = true;
        }
        catch (NumberFormatException nfe) {
          JOptionPane.showMessageDialog(this.getParent(), "Enter \"M\" for minimum bet or a numerical value.");
        }
      }
    }
  }

  /**
   * Reset the GUI for a new game. This doesn't reset the bank.
   */
  public void playagain() {
    dealerLabel.setText("Dealer: ");
    playerLabel.setText("Player: ");
    statusTextBox.setText("");

    game.getDealer().getHands().clear();
    game.getPlayers().get(0).getHands().clear();

    dealerCardPanel.removeAll();
    playerCardPanel.removeAll();

    hitButton.setEnabled(false);
    stickButton.setEnabled(false);
    playAgainButton.setEnabled(false);
    doubleDownButton.setEnabled(false);
    dealButton.setEnabled(true);
    statusTextBox.setVisible(false);

    deal();
  }

  /**
   * Switches to the dealer.
   */
  public void dealersTurn() {
    dealerCardPanel.remove(dealerInitialCard0);
    dealerCardPanel.add(dealerInitialCard1);
    dealerCardPanel.removeAll();
    dealerCardPanel.add(dealerLabel);
    dealerLabel.setText(" " + dealerLabel.getText() + ": " + game.getDealer().getCardTotal());

    //iterate through cards and re-display
    Card dealerHitCard = null;
    Iterator<Card> scan = (game.getDealer().getHands().get(0).iterator());
  
    JLabel dealerHitCardImage = null;
    
    while (scan.hasNext()) {
      dealerHitCard = scan.next();
      dealerHitCardImage = new JLabel(dealerHitCard.getCardImage());
      dealerCardPanel.add(dealerHitCardImage);
    }

    playerLabel.setText("Player: " + game.getPlayers().get(0).getHands().get(0));

    hitButton.setEnabled(false);
    stickButton.setEnabled(false);

    playAgainButton.setEnabled(true);
    dealerCardPanel.repaint();

    boolean isGameFinished = game.getPlayers().get(0).getHands().get(0).isBust();

    while(!isGameFinished) {
      if (!game.getDealer().getHands().get(0).isSticking() && !game.getDealer().getHands().get(0).isBust()) {
        if (game.getDealer().getCardTotal() < 17) {
          Card card = game.getDealer().dealCard(game.getDealer(), game.getShoe());
          dealerHitCardImage = new JLabel(card.getCardImage());
          dealerCardPanel.add(dealerHitCardImage);
          playerCardPanel.repaint();
        }
        else {
          game.getDealer().getHands().get(0).setSticking(true);
        }
      }

      if (game.getDealer().getHands().get(0).isSticking() || game.getDealer().getHands().get(0).isBust()) {
        isGameFinished = true;
      }
    }

    dealerLabel.setText("Dealer: " + game.getDealer().getCardTotal());
    playerLabel.setText("Player: " + game.getPlayers().get(0).getCardTotal());
    determineGameOutcome();
  }

  /**
   * This method contains the logic for deciding who won the game and updates
   * the player's bank accordingly.
   */
  public void determineGameOutcome() {
    hitButton.setEnabled(false);
    dealButton.setEnabled(false);
    stickButton.setEnabled(false);
    playAgainButton.setEnabled(true);
    doubleDownButton.setEnabled(false);
    statusTextBox.setVisible(true);

    statusTextBox.setText(game.determineOutcome());

    updateBankLabel();
  }

  /**
   * Convenience method for updating the player's bank status.
   */
  private void updateBankLabel() {
    playerBankLabel.setText("  Bank:  " + game.getPlayers().get(0).getPlayerBank());
  }
  
  /**
   * Create and initialise all of the game's buttons.
   */
  protected void createGameButtons() {
    dealButton.setText("  Deal");
    dealButton.addActionListener(new GameButtonListener());
    dealButton.setActionCommand(DEAL_ACTION_COMMAND);
    
    hitButton.setText("  Hit");
    hitButton.addActionListener(new GameButtonListener());
    hitButton.setActionCommand(HIT_ACTION_COMMAND);
    hitButton.setEnabled(false);
    
    stickButton.setText("  Stick");
    stickButton.addActionListener(new GameButtonListener());
    stickButton.setActionCommand(STICK_ACTION_COMMAND);
    stickButton.setEnabled(false);
    
    doubleDownButton.setText("  Double Down");
    doubleDownButton.addActionListener(new GameButtonListener());
    doubleDownButton.setActionCommand(DOUBLE_DOWN_ACTION_COMMAND);
    doubleDownButton.setEnabled(false);
    
    playAgainButton.setText("  Play Again");
    playAgainButton.addActionListener(new GameButtonListener());
    playAgainButton.setActionCommand(PLAY_AGAIN_ACTION_COMMAND);
    playAgainButton.setEnabled(false);
  }
  
  /**
   * Action event listener class for all game buttons such as hit, deal, stick etc.
   */
  class GameButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent pEvent) {
      
      switch (pEvent.getActionCommand()) {
        case DEAL_ACTION_COMMAND:
          deal();
          break;
        case HIT_ACTION_COMMAND:
          hit();
          break;
        case STICK_ACTION_COMMAND:
          stick();
          break;
        case DOUBLE_DOWN_ACTION_COMMAND:
          doubleDown();
          break;
        case PLAY_AGAIN_ACTION_COMMAND:
          playagain();
      }
    }
  }

}
