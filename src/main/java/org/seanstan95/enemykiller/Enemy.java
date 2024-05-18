package org.seanstan95.enemykiller;

public class Enemy {
    private int originalHealth; //tracked to allow reset() to work
    public String name;
    public int health;

    /**
     * Creates an enemy. A copy of the health value is stored for later reference.
     * @param name
     * @param health
     */
    public Enemy(String name, int health) {
        this.originalHealth = health;
        this.name = name;
        this.health = health;
    }

    /**
     * Inflicts damage to the enemy based on the given integer parameter.
     * @param damage how much damage is to be taken.
     */
    public void takeDamage(int damage) {
        health -= damage;
    }

    /**
     * Returns this enemy's name and health. Scene is set up to allow for a multiline string for enemy info.
     * If enemy is dead, the returned string states it.
     * @return
     */
    public String toString() {
        if(health > 0)
            return String.format("Name: %s\nHealth: %d", name, health);
        else
            return String.format("%s is dead.", name);
    }

    /**
     * Resets this enemy's health back to the original value when created.
     */
    public void reset() {
        health = originalHealth;
    }
}