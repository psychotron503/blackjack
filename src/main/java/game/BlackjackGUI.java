package game;

import card.Card;

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

    JPanel topPanel = new JPanel();
    JPanel dcardPanel = new JPanel();
    JPanel pcardPanel = new JPanel();
    JTextPane winlosebox = new JTextPane();
    JButton hitbutton = new JButton();
    JButton dealbutton = new JButton();
    JButton stickbutton = new JButton();
    JButton playagainbutton = new JButton();
    JLabel dealerlabel = new JLabel();
    JLabel playerlabel = new JLabel();

    Blackjack game = new Blackjack();

    /*************************************************************
     the labels to represent the cards for the game
     *************************************************************/
    JLabel playercard1;
    JLabel playercard2;
    JLabel playercardhit;
    JLabel dealercard0;
    JLabel dealercard2;
    JLabel dealercard1;
    JLabel dealercardhit;

    /*************************************************************
     Constructs the screen
     *************************************************************/
    public BlackjackGUI () {

      topPanel.setBackground(new Color(0, 122, 0));
      dcardPanel.setBackground(new Color(0, 122, 0));
      pcardPanel.setBackground(new Color(0, 122, 0));

      topPanel.setLayout(new FlowLayout());
      winlosebox.setText(" ");
      winlosebox.setFont(new java.awt.Font("Helvetica Bold", 1, 20));
      dealbutton.setText("  Deal");
      dealbutton.addActionListener(new dealbutton());
      hitbutton.setText("  Hit");
      hitbutton.addActionListener(new hitbutton());
      hitbutton.setEnabled(false);
      stickbutton.setText("  Stick");
      stickbutton.addActionListener(new stickbutton());
      stickbutton.setEnabled(false);
      playagainbutton.setText("  Play Again");
      //playagainbutton.addActionListener(new playagainbutton());
      playagainbutton.setEnabled(false);

      dealerlabel.setText("  Dealer:  ");
      playerlabel.setText("  Player:  ");

      topPanel.add(winlosebox);
      topPanel.add(dealbutton);
      topPanel.add(hitbutton);
      topPanel.add(stickbutton);
      topPanel.add(playagainbutton);
      pcardPanel.add(playerlabel);
      dcardPanel.add(dealerlabel);

      setLayout(new BorderLayout());
      add(topPanel,BorderLayout.NORTH);
      add(dcardPanel,BorderLayout.CENTER);
      add(pcardPanel,BorderLayout.SOUTH);

    }

  /**
   * Show the game.
   */
    public void display() {
      JFrame myFrame = new JFrame("BlackJack");
      myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      myFrame.setContentPane(this);
      myFrame.setPreferredSize(new Dimension(700,550));

      //Display the window.
      myFrame.pack();
      myFrame.setVisible(true);

    }//end display

    /*************************************************************
     DealButton
     @param e Deal button pressed
     *************************************************************/
    class dealbutton implements ActionListener {
      public void actionPerformed(ActionEvent e) {

        dcardPanel.add(dealerlabel);
        pcardPanel.add(playerlabel);


        // Get's dealer and player cards from Hand
        // and the image associated with that random
        // card and puts them on the screen.


        dealercard0 = new JLabel(new ImageIcon(this.getClass().getResource("../card/card_images/back.jpg")));

        //game.dealInitialCards();

        //to iterate set and get current dealer cards
        Card dcard=null;

        Iterator<Card> dscan = (game.dealer.getHands().get(0)).iterator();
        int count = 0;

        while (dscan.hasNext()) {
          dcard = dscan.next();

          if(count==0)
            dealercard1 = new JLabel(dcard.getCardImage());
          else
            dealercard2 = new JLabel(dcard.getCardImage());

          count++;
        }

        //to iterate set and get current player cards
        Iterator<Card> pscan = (game.player1.getHands().get(0)).iterator();
        count = 0;

        while (pscan.hasNext()) {
          Card pcard = pscan.next();

          if(count==0)
            playercard1 = new JLabel(pcard.getCardImage());
          else
            playercard2 = new JLabel(pcard.getCardImage());

          count++;
        }

        dcardPanel.add(dealercard0);
        dcardPanel.add(dealercard2);

        pcardPanel.add(playercard1);
        pcardPanel.add(playercard2);

        dealerlabel.setText("  Dealer:  " + dcard);
        playerlabel.setText("  Player:  " + game.player1.getCardTotal());

        hitbutton.setEnabled(true);
        stickbutton.setEnabled(true);
        dealbutton.setEnabled(false);

        /*if(game.blackj())
        {stickbutton
          hitbutton.setEnabled(false);
          stickbutton.setEnabled(false);
          dealbutton.setEnabled(false);
          playagainbutton.setEnabled(true);
          winlosebox.setText("BlackJack");
        }*/

        add(dcardPanel,BorderLayout.CENTER);
        add(pcardPanel,BorderLayout.SOUTH);

      }
    }//end dealbutton

    /*************************************************************
     HitButton
     every time the player wants another card
     until hand value is over 21.
     @param e Hit button pressed
     *************************************************************/
    class hitbutton implements ActionListener {
      public void actionPerformed(ActionEvent e) {
        Card hitCard = game.shoe.removeLast();
        game.player1.hit(hitCard);
        playercardhit = new JLabel(hitCard.getCardImage());
        pcardPanel.add(playercardhit);
        pcardPanel.repaint();

        if(game.player1.getHands().get(0).isBust()) {
          winlosebox.setText("Bust");
          hitbutton.setEnabled(false);
          dealbutton.setEnabled(false);
          stickbutton.setEnabled(false);
          playagainbutton.setEnabled(true);
        }

        playerlabel.setText("  Player:   " + game.player1.getCardTotal());
      }
    }//end hitbutton

    /*************************************************************
     stickButton
     dealer must hit on 16 or lower. determines the winner,
     player wins if under 21 and above dealer.
     Tie goes to dealer.
     @param e stick button pressed
     *************************************************************/
    class stickbutton implements ActionListener {
      public void actionPerformed(ActionEvent e) {

        dcardPanel.remove(dealercard0);
        dcardPanel.add(dealercard1);

        //dealer = game.dealerPlays();
        dcardPanel.removeAll();
        dcardPanel.add(dealerlabel);
        dealerlabel.setText(" " + dealerlabel.getText());

        //iterate through cards and re-display
        Card dhitcard = null;
        Iterator<Card> scan = (game.dealer.getHands().get(0).iterator());
        
        while (scan.hasNext()) {
          dhitcard = scan.next();
          dealercardhit = new JLabel(dhitcard.getCardImage());
          dcardPanel.add(dealercardhit);
        }

        dealerlabel.setText("Dealer: " + game.dealer.getHands().get(0));
        playerlabel.setText("Player: " + game.player1.getHands().get(0));

        //winlosebox.setText(game.winner());
        hitbutton.setEnabled(false);
        stickbutton.setEnabled(false);

        playagainbutton.setEnabled(true);
        dcardPanel.repaint();
      }
    }//end stickbutton

    /*************************************************************
     PlayAgainButton
     resets screen
     @param e Play Again button pressed
     *************************************************************/
    /*class playagainbutton implements ActionListener {
      public void actionPerformed(ActionEvent e) {

        dealerlabel.setText("Dealer: ");
        playerlabel.setText("Player: ");
        winlosebox.setText("");
        dealer = new Hand();
        player = new Hand();
        game=new Blackjack(dealer, player);

        dcardPanel.removeAll();
        pcardPanel.removeAll();

        hitbutton.setEnabled(false);
        stickbutton.setEnabled(false);
        playagainbutton.setEnabled(false);
        dealbutton.setEnabled(true);

      }
    }//end playagainbutton */





}
