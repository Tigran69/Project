package am.aua.game.gameUI;

import am.aua.game.exceptions.CoordinateBlockedException;
import am.aua.game.exceptions.MalformedStringException;
import am.aua.game.exceptions.NotEnoughMoneyException;
import am.aua.game.gameLogic.GameCore;
import am.aua.game.navigation.Cell;
import am.aua.game.players.Player;
import am.aua.game.units.Archer;
import am.aua.game.units.Soldier;
import am.aua.game.units.Tank;
import am.aua.game.units.Unit;

import javax.swing.*;
import java.awt.*;


public class GameWindow extends JFrame {

    private final int GRID_SIZE = 20;
    private JButton[][] gridButtons = new JButton[GRID_SIZE][GRID_SIZE];
    private JLabel currentPlayerLabel;
    private JTextArea unitInfoArea;

    public GameWindow(GameCore gameCore) {
        setTitle("Strategy Game - " + gameCore.getCurrentPlayer().getName() + "'s Turn");
        setSize(1200, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        currentPlayerLabel = new JLabel("Current Player: " + gameCore.getCurrentPlayer().getName());
        unitInfoArea = new JTextArea(6, 20);
        unitInfoArea.setEditable(false);


        sidePanel.add(currentPlayerLabel);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(new JScrollPane(unitInfoArea));



        setVisible(true);

        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JButton cell = new JButton();
                if (gameCore.getMap().getCellAt(i,j).getTerrain() == Cell.TerrainType.TREE) {
                    cell.setBackground(Color.GREEN);
                    cell.setText("T");
                }
                else if (gameCore.getMap().getCellAt(i,j).getTerrain() == Cell.TerrainType.ROCK){
                    cell.setBackground(Color.LIGHT_GRAY);
                    cell.setText("R");
                }
                else {
                    cell.setBackground(Color.WHITE);
                }
                cell.setMargin(new Insets(0, 0, 0, 0));
                int row = i;
                int col = j;
                cell.addActionListener(e -> handleCellClick(row, col,gameCore,sidePanel));
                gridButtons[i][j] = cell;
                gridPanel.add(cell);
            }
        }
        add(gridPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);


    }

    private void handleCellClick(int row, int col, GameCore gameCore, JPanel sidePanel) {
        Unit unit = gameCore.getMap().getCellAt(row, col).getUnit();
        sidePanel.removeAll();

        updatePlayerInfo(gameCore, sidePanel);

        if (unit != null) {
            unitInfoArea.setText("Unit Type: " + unit.getClass().getSimpleName()
                    + "\nHP: " + unit.getHealth()
                    + "\nOwner: " + unit.getOwner().getName());

            if (unit.getOwner().equals(gameCore.getCurrentPlayer())) {
                JButton someActionButton = new JButton("Some Unit Action"); // Optional
                sidePanel.add(Box.createVerticalStrut(10));
                sidePanel.add(someActionButton);
            }

        } else {
            unitInfoArea.setText("Empty cell at (" + row + "," + col + ")");
            System.out.println("Clicked terrain: " + gameCore.getMap().getCellAt(row, col).getTerrain());
            JButton buyUnitButton = new JButton("Buy Unit");
                buyUnitButton.addActionListener(e -> {
                    try {
                        buyUnit(gameCore, row, col);
                    } catch (MalformedStringException | CoordinateBlockedException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

            sidePanel.add(Box.createVerticalStrut(10));
            sidePanel.add(buyUnitButton);
        }

        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(new JScrollPane(unitInfoArea));
        sidePanel.revalidate();
        sidePanel.repaint();
    }

    private void updatePlayerInfo(GameCore gameCore, JPanel sidePanel) {
        Player p = gameCore.getCurrentPlayer();
        JLabel nameLabel = new JLabel("Current Player: " + p.getName());
        JLabel moneyLabel = new JLabel("Money: " + p.getResources());
        int unitCount = p.getUnits().size();


        JLabel unitsLabel = new JLabel("Units: " + unitCount);
        sidePanel.add(nameLabel);
        sidePanel.add(moneyLabel);
        sidePanel.add(unitsLabel);
    }





    private void buyUnit(GameCore gameCore, int row, int col) throws MalformedStringException, CoordinateBlockedException {
        String unit = JOptionPane.showInputDialog("Enter Unit Type: T (Tank), A (Archer), S (Soldier)");

        if (unit == null || unit.trim().isEmpty()) {
            return;
        }

        Unit unitToBuy;
        try {
            if (unit.equals("T")) {
                unitToBuy = new Tank(gameCore.getCurrentPlayer());
            } else if (unit.equals("A")) {
                unitToBuy = new Archer(gameCore.getCurrentPlayer());
            } else if (unit.equals("S")) {
                unitToBuy = new Soldier(gameCore.getCurrentPlayer());
            } else {
                throw new MalformedStringException();
            }

            gameCore.getCurrentPlayer().buyUnit(unitToBuy, row, col, gameCore.getMap());

            gridButtons[row][col].setText(unitToBuy.getSymbol());
            gridButtons[row][col].setBackground(
                    gameCore.getCurrentPlayer() == gameCore.getPlayers().get(0) ? Color.RED : Color.YELLOW);

            nextTurn(gameCore);

        } catch (CoordinateBlockedException | NotEnoughMoneyException | MalformedStringException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }


    private void nextTurn(GameCore gameCore) {
        gameCore.nextTurn();

        currentPlayerLabel.setText("Current Player: " + gameCore.getCurrentPlayer().getName());
        setTitle("Strategy Game - " + gameCore.getCurrentPlayer().getName() + "'s Turn");
        updateSidePanelInfo(gameCore); // optional: show money and unit count
    }

    private void updateSidePanelInfo(GameCore gameCore) {
        String sb = "";
        sb += ("Current Player: ") + (gameCore.getCurrentPlayer().getName()) + ("\n");
        sb += ("Money: ") + (gameCore.getCurrentPlayer().getResources()) + ("\n");
        sb += ("Units: ") + (gameCore.getCurrentPlayer().getUnits().size()) + ("\n");
        unitInfoArea.setText(sb);
    }





}

