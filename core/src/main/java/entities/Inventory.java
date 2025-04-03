package entities;

import objects.GameObject;
import objects.OBJ_Weapon;

public class Inventory {

    public GameObject[] inventoryArray;
    public GameObject currentItem;
    public int currentSlotIndex;

    public Inventory (Player player) {

        this.inventoryArray = new GameObject[12];
        this.currentSlotIndex = 0;
        this.currentItem = inventoryArray[currentSlotIndex];
        initialiseInventory();

    }

    public void initialiseInventory () {
        this.inventoryArray[0] = new OBJ_Weapon("stick");
    }

    public void previousItem () {
        if (currentSlotIndex > 0) {
            currentSlotIndex--;
        }
    }

    public void nextItem () {
        if (currentSlotIndex < 11) {
            currentSlotIndex++;
        }
    }

}
