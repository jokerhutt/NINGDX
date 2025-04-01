package hud;

import Constants.Constants;
import com.badlogic.gdx.Gdx;
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
    protected Texture soundIcon;
    MainScreen screen;
    private BitmapFont font = new BitmapFont();
    Texture dialogueBoxTexture = new Texture("DialogBoxFaceset.png");

    public HUD(Viewport viewport, SpriteBatch batch, MainScreen screen) {
        stage = new Stage(new ScreenViewport(), batch);
        this.screen = screen;

        Gdx.input.setInputProcessor(stage);
    }

    public void render(float delta) {
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void drawDialogue(SpriteBatch batch) {
        float screenWidth = Gdx.graphics.getWidth();

        float slotSize = 64f;
        float padding = 4f;
        float inventoryWidth = 4 * slotSize + 3 * padding * 2;
        float boxHeight = 2 * slotSize + padding * 2;
        float boxWidth = screenWidth - inventoryWidth - 40;
        float boxX = inventoryWidth + 30;
        float boxY = 10;

        // 1. Draw the dialogue box first (behind the portrait)
        batch.draw(dialogueBoxTexture, boxX, boxY, boxWidth, boxHeight);

        // 2. Draw the portrait on top, slightly more to the right
        float portraitSize = 72f;
        float portraitX = boxX + Constants.TILESIZE; // shift into the dialogue box slightly
        float portraitY = boxY + boxHeight - portraitSize - Constants.TILESIZE;

        if (screen.currentNPC != null && screen.currentNPC.portrait != null) {
            batch.draw(screen.currentNPC.portrait, portraitX, portraitY, portraitSize, portraitSize);
        }
    }

//    public void refreshInventory () {
//        inventoryTable.clear();
//        for (int row = 0; row < 2; row++) {
//            for (int col = 0; col < 4; col++) {
//                int index = row * 4 + col;
//
//                GameObject currItem = screen.player.inventory[index];
//
//                Stack slot = new Stack();
//
//                if (screen.player.getSelectedItemIndex() == index) {
//                    slot.add(new Image(selectedSlotDrawable));
//                } else {
//                    slot.add(new Image(slotDrawable));
//                }
//
//                if (currItem != null && currItem.sprite != null) {
//                    TextureRegionDrawable itemDrawable = new TextureRegionDrawable(new TextureRegionDrawable(currItem.sprite));
//                    slot.add(new Image(itemDrawable));
//                }
//
//                inventoryTable.add(slot).size(64, 64).pad(4);
//
//            }
//            inventoryTable.row();
//        }
//    }
}
