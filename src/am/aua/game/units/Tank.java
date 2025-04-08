package am.aua.game.units;

import am.aua.game.players.Player;

public class Tank extends Unit {
    

    public Tank(Player owner) {
        super("T", 200, 20, 2, 1, owner);
    }

    public void attack(Unit enemy) {

    }

}
