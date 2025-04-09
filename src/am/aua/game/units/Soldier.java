package am.aua.game.units;

import am.aua.game.players.Player;

public class Soldier extends Unit {
    

    public Soldier(Player owner) {
        super("S", 100, 10, 3, 1, owner,150);
    }
    
    public void attack(Unit enemy) {
        
    }
}
