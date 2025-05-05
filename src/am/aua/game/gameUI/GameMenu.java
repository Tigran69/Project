package am.aua.game.gameUI;

import am.aua.game.exceptions.InvalidPlayerNameException;
import am.aua.game.fileIO.SaveLoadManager;
import am.aua.game.gameLogic.GameCore;
import am.aua.game.players.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class GameMenu extends JFrame {

    public GameMenu() {
        setTitle("Strategy Game Menu");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton startButton = new JButton("Start New Game");
        JButton loadButton = new JButton("Load Game");
        JButton exitButton = new JButton("Exit");



            startButton.addActionListener(e -> {
                try {
                    startGame(e);
                } catch (InvalidPlayerNameException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            });



        loadButton.addActionListener(this::loadGame);
        exitButton.addActionListener(e -> System.exit(0));

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.add(startButton);
        panel.add(loadButton);
        panel.add(exitButton);

        add(panel, BorderLayout.CENTER);
    }

    private void startGame(ActionEvent e) throws InvalidPlayerNameException{
        String player1 = JOptionPane.showInputDialog(this, "Enter Player 1 name:");
        String player2 = JOptionPane.showInputDialog(this, "Enter Player 2 name:");
        if (player1.isEmpty() || player2.isEmpty()) {
            throw new InvalidPlayerNameException();
        }
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(player1));
        players.add(new Player(player2));
        GameCore gameCore = new GameCore(players);

        if (player1 != null && player2 != null) {
            new GameWindow(gameCore);
            this.dispose();
        }
    }

    public void loadGame(ActionEvent e){
        GameCore gameCore = SaveLoadManager.loadGame("C:\\Users\\Tiko\\Desktop\\gameSave.txt");
        new GameWindow(gameCore);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameMenu().setVisible(true));
    }
}

