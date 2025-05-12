package am.aua.game;

import am.aua.game.consoleBased.GameConsole;
import am.aua.game.exceptions.*;
import am.aua.game.gameUI.MainMenu;

public class Main{
    public static void main(String[] args) throws MalformedStringException, NotYourTerritoryException, NotYourUnitException, PathNotClearException, InvalidPathException, CoordinateBlockedException {
        if (args.length == 0) {
            MainMenu.main(args);
        }
        else if (args.length == 1 && args[0].equals("-cli")) {
            GameConsole.startGame();
        }
    }
}

