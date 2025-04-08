package am.aua.game.units;

import am.aua.game.players.Player;

public abstract class Unit {
    private String symbol;
    protected int health;
    protected int attackPower;
    protected int movementRange;
    protected int attackRange;
    private Player owner;

    public Unit(String symbol, int health, int attackPower, int movementRange, int attackRange, Player owner) {
        this.symbol = symbol;
        this.health = health;
        this.attackPower = attackPower;
        this.movementRange = movementRange;
        this.attackRange = attackRange;
        this.owner = owner;
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

    public abstract void attack(Unit enemy);

    // move
    // isinrange
    // calculatedistance
}
