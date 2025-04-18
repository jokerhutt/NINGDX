package hud;

import Constants.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import jokerhut.main.MainScreen;

public class HUD {

    protected Stage stage;
    protected Table inventoryTable;

    protected ImageButton soundToggleButton;
    protected Texture slotTexture;
    protected Texture selectedSlotTexture;
    TextureRegionDrawable selectedSlotDrawable;
    TextureRegionDrawable slotDrawable;

    MainScreen screen;
    private BitmapFont font = new BitmapFont();
    Texture dialogueBoxTexture = new Texture("DialogBoxFaceset.png");
    ProgressBarStack healthBar;
    PurchaseItemsActor purchaseItemsActor;
    CoinActor coinDisplay;
    InventoryActor inventoryActor;
    Table coinTable;

    public int coinCount;

    public HUD(Viewport viewport, SpriteBatch batch, MainScreen screen) {

        stage = new Stage(new ScreenViewport(), batch);
        this.screen = screen;
        Gdx.input.setInputProcessor(stage);
        createHealthBar();
        //Initialise Coins
        coinDisplay = new CoinActor(screen, 'M');
        coinTable = new Table();
        coinTable.top().right().pad(10).padRight(30);
        coinTable.setFillParent(true);
        coinTable.add(healthBar).padRight(20).padTop(10).size(22 * 1.5f, 40 * 1.5f);
        coinTable.add(coinDisplay).padTop(10); // no need to right-align here
        stage.addActor(coinTable);

        inventoryActor = new InventoryActor(screen);
        inventoryActor.setFillParent(true);
        stage.addActor(inventoryActor);

        purchaseItemsActor = new PurchaseItemsActor(screen);
        purchaseItemsActor.setFillParent(true);
        stage.addActor(purchaseItemsActor);


//        Table healthTable = new Table();
//        healthTable.bottom().left().pad(30);
//        healthTable.setFillParent(true);
//        healthTable.add(healthBar).size(22 * 2, 40 * 2);
//
//        stage.addActor(healthTable);

    }

    public void render(float delta) {
        stage.getViewport().apply();
        updateShopVisibility();
        if (screen.currentNPC != null && screen.currentNPC.isEmoting) {
            screen.currentNPC.emoteHandler.runEmoting();
        }
        stage.act(delta);
        stage.draw();
    }

    public void updateShopVisibility() {
        if (screen.currentNPC != null && screen.currentNPC.type == "merchant" && screen.currentNPC.isInPurchaseScreen) {
            purchaseItemsActor.setVisible(true);
            inventoryActor.setVisible(true);
            inventoryActor.refreshInventory();
            purchaseItemsActor.setInventoryItems();
            purchaseItemsActor.setVendorNameTable();
            purchaseItemsActor.coinActor.coinLabel.setText(screen.hud.coinCount + " G");
        } else if (screen.isViewingInventory) {
            purchaseItemsActor.setVisible(false);
            inventoryActor.setVisible(true);
            inventoryActor.refreshInventory();
        } else {
            inventoryActor.setVisible(false);
            purchaseItemsActor.setVisible(false);
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void drawDialogue(String line, SpriteBatch batch) {
        float screenWidth = Gdx.graphics.getWidth();

        float slotSize = 64f;
        float padding = 4f;
        float inventoryWidth = 4 * slotSize + 3 * padding * 2;
        float boxHeight = 2 * slotSize + padding * 2;
        float boxWidth = screenWidth - inventoryWidth - 40;
        float boxX = inventoryWidth + 30;
        float boxY = 10;

        batch.draw(dialogueBoxTexture, boxX, boxY, boxWidth, boxHeight);

        float portraitSize = 72f;
        float portraitX = boxX + Constants.TILESIZE;
        float portraitY = boxY + boxHeight - portraitSize - Constants.TILESIZE;

        if (screen.currentNPC != null && screen.currentNPC.portrait != null && screen.currentNPC.dialogueHandler != null) {
            batch.draw(screen.currentNPC.portrait, portraitX, portraitY, portraitSize, portraitSize);
        }
        font.setColor(Color.BLACK);
        font.getData().setScale(1.2f);
        float textX = portraitX + portraitSize + 35;
        float textY = boxY + boxHeight - 40;
        font.draw(batch, screen.currentNPC.name, textX, textY);

        float lineTextX = textX;
        float lineTextY = textY - 20;

        font.draw(batch, line, lineTextX, lineTextY);


    }

    public void createHealthBar () {
        Texture bg = new Texture("BackgroundWood.png");
        Texture white = new Texture("ProgressWhite.png");
        Texture fill = new Texture("ProgressHealth.png");

        healthBar = new ProgressBarStack(bg, white, fill);
    }

    public void updateHealth(float current, float max) {
        float percent = current / max;
        healthBar.setHealthPercent(percent);
    }

}
