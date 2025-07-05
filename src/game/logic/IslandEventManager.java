package game.logic;

import game.model.Inventory;
import game.model.Player;

import java.util.Random;

import static game.logic.ItemRegistry.*;

public class IslandEventManager {

    private Random random = new Random();

    public void explore(Player player, Inventory inventory) {
        int event = random.nextInt(5);

        switch (event) {
            case 0 -> findFood(inventory);
            case 1 -> findItem(inventory);
            case 2 -> getInjured(player);
            case 3 -> loseResources(inventory);
            case 4 -> nothingHappened();
        }
    }

    private void findFood(Inventory inventory) {
        System.out.println("You found some wild berries!");
        inventory.addItem(BERRIES, 2);
    }

    private void findItem(Inventory inventory) {
        System.out.println("You found some useful wood!");
        inventory.addItem(WOOD, 3);
    }

    private void getInjured(Player player) {
        System.out.println("You injured yourself while exploring...");
        player.setHealth(player.getHealth() - 20);
        if (player.getHealth() <= 0) {
            player.setAlive(false);
            System.out.println("You died from your injuries...");
        } else {
            System.out.println("Your health is now: " + player.getHealth());
        }
    }

    private void loseResources(Inventory inventory) {
        System.out.println("You lost some supplies during a storm!");
        inventory.removeItem(BREAD, 1);
        inventory.removeItem(WATER, 1);
    }

    private void nothingHappened() {
        System.out.println("You explored the island but found nothing interesting...");
    }
}