package am.aua.game.units;

import am.aua.game.navigation.Cell;
import am.aua.game.players.Player;

public abstract class Unit {
    private final String symbol;
    protected int health;
    protected int attackPower;
    protected int movementRange;
    protected int attackRange;
    private final Player owner;
    private final int price;


    public Unit(String symbol, int health, int attackPower, int movementRange, int attackRange, Player owner, int price) {
        this.symbol = symbol;
        this.health = health;
        this.attackPower = attackPower;
        this.movementRange = movementRange;
        this.attackRange = attackRange;
        this.owner = owner;
        this.price = price;
    }

    public int getHealth() {
        return health;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getMovementRange() {
        return movementRange;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public Player getOwner() {
        return owner;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getPrice(){
        return price;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    

}
