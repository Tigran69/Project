package am.aua.game.gameLogic;

import am.aua.game.navigation.Map;
import am.aua.game.players.Player;
import am.aua.game.units.Archer;
import am.aua.game.units.Soldier;
import am.aua.game.units.Tank;
import am.aua.game.units.Unit;

import java.util.Scanner;

public class GameCore {

    private Player[] players;
    private Player currentPlayer;
    private int turnCount;
    private Map map;

    public GameCore(String player1Name, String player2Name) {
        this.players = new Player[2];
        this.players[0] = new Player(player1Name);
        this.players[1] = new Player(player2Name);
        this.currentPlayer = players[0];
        this.turnCount = 0;
        this.map = new Map();
    }



    public void nextTurn(){
        turnCount++;
        if (this.currentPlayer.getName().equals(players[0].getName())){
            this.currentPlayer = players[1];
            return;
        }
        this.currentPlayer = players[0];
    }

    public boolean checkLooseCondition(){
        return this.currentPlayer.getUnits().isEmpty() || this.currentPlayer.getTerritory().isEmpty();
    }

    public int getTurnCount(){
        return this.turnCount;
    }

    public void saveGame(String filePath){}

    public void loadGame(String filePath){}


}
