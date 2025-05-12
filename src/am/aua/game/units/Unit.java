package am.aua.game.units;
import am.aua.game.players.Player;

/**
 * Represents a generic unit in the game. All specific unit types should extend this abstract class.
 * A unit has core attributes like health, attack power, movement and attack ranges, ownership, and a symbolic representation.
 */
public abstract class Unit {
    /** Symbol representing the unit on the game map (e.g., a letter or character). */
    private final String symbol;

    /** The current health of the unit. */
    protected int health;

    /** The amount of damage this unit can inflict when attacking. */
    protected int attackPower;

    /** The number of cells this unit can move per turn. */
    protected int movementRange;

    /** The number of cells within which this unit can attack other units. */
    protected int attackRange;

    /** The player who owns this unit. */
    private final Player owner;

    /** The cost required to purchase or deploy this unit. */
    private final int price;

    /**
     * Constructs a new Unit with the specified attributes.
     *
     * @param symbol         the symbol used to represent this unit
     * @param health         the initial health of the unit
     * @param attackPower    the attack strength of the unit
     * @param movementRange  how far the unit can move per turn
     * @param attackRange    how far the unit can attack
     * @param owner          the {@link Player} who owns this unit
     * @param price          the cost of this unit
     */
    public Unit(String symbol, int health, int attackPower, int movementRange, int attackRange, Player owner, int price) {
        this.symbol = symbol;
        this.health = health;
        this.attackPower = attackPower;
        this.movementRange = movementRange;
        this.attackRange = attackRange;
        this.owner = owner;
        this.price = price;
    }

    /**
     * Returns the current health of the unit.
     *
     * @return the unit's health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the health of the unit.
     *
     * @param health the new health value
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Returns the unit's attack power.
     *
     * @return the unit's attack power
     */
    public int getAttackPower() {
        return attackPower;
    }

    /**
     * Returns how far the unit can move.
     *
     * @return the movement range
     */
    public int getMovementRange() {
        return movementRange;
    }

    /**
     * Returns how far the unit can attack.
     *
     * @return the attack range
     */
    public int getAttackRange() {
        return attackRange;
    }

    /**
     * Returns the player that owns this unit.
     *
     * @return the owner of the unit
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Returns the symbol representing this unit.
     *
     * @return the unit's symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the price of this unit.
     *
     * @return the unit's price
     */
    public int getPrice() {
        return price;
    }
}
