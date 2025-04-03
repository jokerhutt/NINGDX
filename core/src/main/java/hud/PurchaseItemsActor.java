package hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import jokerhut.main.MainScreen;
import objects.GameObject;
import objects.OBJ_Food;
import objects.OBJ_Weapon;

public class PurchaseItemsActor extends Table {

    MainScreen screen;
    private final Texture choiceBoxTexture;
    private final Texture infoBoxTexture;
    TextureRegionDrawable choiceDrawable;
    private final BitmapFont font = new BitmapFont();
    public CoinActor coinActor;
    public Table vendorNameTable;

    private final Table itemListTable;

    public PurchaseItemsActor(MainScreen screen) {
        this.screen = screen;
        this.infoBoxTexture = new Texture("DialogueBoxSimple.png");
        this.choiceBoxTexture = new Texture("ChoiceBox.png");

        this.setFillParent(true);
        this.top().left().pad(10);
        this.setDebug(true);



        choiceDrawable = new TextureRegionDrawable(new TextureRegion(choiceBoxTexture));

        vendorNameTable = new Table();
        vendorNameTable.setBackground(choiceDrawable);
        vendorNameTable.pad(10);
        vendorNameTable.left().center();
        this.add(vendorNameTable).width(420).left();
        this.row().padTop(10);


        itemListTable = new Table();
        itemListTable.left().top();
        this.add(itemListTable).width(420).left();

        this.row().padTop(10);

        Image choiceBoxBg = new Image(choiceDrawable);
        choiceBoxBg.setScaling(Scaling.stretch);
        this.row().padTop(10);

        coinActor = new CoinActor(screen, 'S');

        Table coinContentTable = new Table();
        coinContentTable.setFillParent(true);
        coinContentTable.align(Align.left | Align.center);

        coinContentTable.row();
        this.coinActor.coinLabel.setColor(Color.BLACK);
        coinContentTable.add(coinActor).padLeft(40).left();

        Stack choiceStack = new Stack();
        choiceStack.add(choiceBoxBg);
        choiceStack.add(coinContentTable);

        this.add(choiceStack).size(320, 100).colspan(3).left();

    }

    public void purchaseItem (GameObject chosenItem) {
        System.out.println("Bought " + chosenItem.name);
        System.out.println("Bought " + chosenItem.name);

        if (screen.player.coins >= chosenItem.cost) {
            screen.player.coins -= chosenItem.cost;

            GameObject[] inventory = screen.player.inventory.inventoryArray;

            int foundIndex = -1;
            int emptyIndex = -1;

            for (int i = 0; i < inventory.length; i++) {
                if (inventory[i] != null) {
                    if (inventory[i].name.equals(chosenItem.name)) {
                        foundIndex = i;
                        break;
                    }
                } else if (emptyIndex == -1) {
                    emptyIndex = i;
                }
            }

            System.out.println(foundIndex);
            System.out.println(emptyIndex);

            if (foundIndex != -1) {
                inventory[foundIndex].count += 1;
                screen.currentNPC.isEmoting = true;
            } else if (emptyIndex != -1) {
                // Add new item to first empty slot
                GameObject newItem;
                if (chosenItem.type.equals("weapon")) {
                    newItem = new OBJ_Weapon(chosenItem.name); // Deep copy constructor?
                    inventory[emptyIndex] = newItem;
                    screen.currentNPC.isEmoting = true;
                } else if (chosenItem.type.equals("consumable")) {
                    newItem = new OBJ_Food(chosenItem.name);
                    inventory[emptyIndex] = newItem;
                    screen.currentNPC.isEmoting = true;
                }
            } else {
                System.out.println("Inventory is full!");
            }

            screen.hud.inventoryActor.refreshInventory();
            System.out.println("Remaining coins: " + screen.player.coins);

        } else {
            System.out.println("Not enough coins!");
        }


    }

    public void setVendorNameTable () {
        vendorNameTable.clear();

        Image portrait = null;
        if (screen.currentNPC != null && screen.currentNPC.portrait != null) {
            TextureRegionDrawable portraitDrawable = new TextureRegionDrawable(new TextureRegion(screen.currentNPC.portrait));
            portrait = new Image(portraitDrawable);
            portrait.setSize(48, 48);
            vendorNameTable.add(portrait).size(48).padRight(10);
        }

        String stringForLabel = screen.currentNPC.name + "'s Shop";

        Label vendorNameLabel = new Label(stringForLabel, new Label.LabelStyle(font, Color.BLACK));
        vendorNameLabel.setFontScale(1.4f);
        vendorNameTable.add(vendorNameLabel).left().center().expandX();
    }

    public void setInventoryItems() {
        itemListTable.clear();

        GameObject[] npcInventory = screen.currentNPC.inventory;

        for (GameObject item : npcInventory) {
            if (item == null || item.image == null) continue;

            ForSaleItemRow itemRow = new ForSaleItemRow("ChoiceBox.png", item, font);
//            itemRow.setTouchable(Touchable.enabled);
            itemRow.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println("Touch down on item: " + item.name);
                    purchaseItem(itemRow.item);
                    return true;
                }
            });
            itemListTable.row();

            itemListTable.add(itemRow).width(420).height(64).padBottom(6).left();
        }
    }
}
