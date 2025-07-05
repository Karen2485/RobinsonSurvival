package game.logic;

import game.model.Inventory;

import static game.logic.ItemRegistry.*;

public class StartingItems {

    public static void addInitialItems(Inventory inventory) {
        inventory.addItem(BREAD, 3);
        inventory.addItem(WATER, 3);
        inventory.addItem(KNIFE, 1);
        inventory.addItem(ROPE, 2);
        inventory.addItem(EMPTY_BAG, 2);
        inventory.addItem(AXE, 1);
        inventory.addItem(MEDKIT, 1);
    }
}