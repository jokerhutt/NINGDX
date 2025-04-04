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
        initialiseInventory();
        this.currentItem = inventoryArray[currentSlotIndex];
    }

    public void initialiseInventory () {
        this.inventoryArray[0] = new OBJ_Weapon("stick");
        this.inventoryArray[1] = new OBJ_Weapon("lance");
        this.inventoryArray[2] = new OBJ_Weapon("sword");
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

    public void updateItem (int i) {
        currentSlotIndex = i;
        currentItem = this.inventoryArray[i];
        if (currentItem != null) {
            System.out.println(" SELECTED A " + currentItem.name);
        }

    }

}
