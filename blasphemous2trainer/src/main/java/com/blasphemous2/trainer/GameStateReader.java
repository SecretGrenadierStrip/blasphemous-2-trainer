package com.blasphemous2.trainer;

/**
 * Reads and interprets game state from Blasphemous 2 memory.
 * Provides high-level access to player stats like health, fervour, and tears.
 */
public class GameStateReader {

    // Base addresses (example offsets, would be found via reverse engineering)
    private static final long HEALTH_ADDRESS = 0x7FFE0000A4B0L;
    private static final long FERVOUR_ADDRESS = 0x7FFE0000A4B4L;
    private static final long TEARS_ADDRESS = 0x7FFE0000A4B8L;
    private static final long MAX_HEALTH_ADDRESS = 0x7FFE0000A4C0L;

    private final MemoryTrainer trainer;

    /**
     * Constructs a GameStateReader with a given MemoryTrainer.
     * @param trainer An active MemoryTrainer connected to the game process.
     */
    public GameStateReader(MemoryTrainer trainer) {
        this.trainer = trainer;
    }

    /**
     * Reads the player's current health.
     * @return Health value as integer.
     */
    public int getHealth() {
        return trainer.readInt(HEALTH_ADDRESS);
    }

    /**
     * Sets the player's health to a specific value.
     * @param health New health value.
     */
    public void setHealth(int health) {
        trainer.writeInt(HEALTH_ADDRESS, health);
    }

    /**
     * Reads the player's current fervour (mana).
     * @return Fervour value as integer.
     */
    public int getFervour() {
        return trainer.readInt(FERVOUR_ADDRESS);
    }

    /**
     * Sets the player's fervour to a specific value.
     * @param fervour New fervour value.
     */
    public void setFervour(int fervour) {
        trainer.writeInt(FERVOUR_ADDRESS, fervour);
    }

    /**
     * Reads the player's current tears of atonement (currency).
     * @return Tears value as integer.
     */
    public int getTears() {
        return trainer.readInt(TEARS_ADDRESS);
    }

    /**
     * Sets the player's tears of atonement to a specific value.
     * @param tears New tears value.
     */
    public void setTears(int tears) {
        trainer.writeInt(TEARS_ADDRESS, tears);
    }

    /**
     * Reads the player's maximum health.
     * @return Max health value.
     */
    public int getMaxHealth() {
        return trainer.readInt(MAX_HEALTH_ADDRESS);
    }

    /**
     * Fills health to maximum.
     */
    public void fillHealth() {
        int maxHp = getMaxHealth();
        setHealth(maxHp);
    }

    /**
     * Adds a specified amount of tears to the player's currency.
     * @param amount Amount of tears to add.
     */
    public void addTears(int amount) {
        int current = getTears();
        setTears(current + amount);
    }
}
