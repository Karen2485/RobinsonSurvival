package game.model;

/**
 * Класс Player - состояние игрока: здоровье, голод, жив/мертв.
 */
public class Player {

    private int health;
    private int hunger;
    private boolean isAlive;

    public Player() {
        this.health = 100;
        this.hunger = 100;
        this.isAlive = true;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public void increaseHunger(int amount) {
        hunger = Math.min(100, hunger + amount);
    }

    public void reduceHunger(int amount) {
        hunger = Math.max(0, hunger - amount);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}