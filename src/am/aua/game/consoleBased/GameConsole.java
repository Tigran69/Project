package am.aua.game.consoleBased;

import am.aua.game.gameLogic.GameLoop;
import am.aua.game.navigation.Map;
import am.aua.game.players.Player;

import java.util.Scanner;

public class GameConsole {
    public static void main(String[] args) {

        GameLoop gameLoop = new GameLoop();
        gameLoop.startGame();

    }
}