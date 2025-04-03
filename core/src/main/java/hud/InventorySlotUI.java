package hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import objects.GameObject;

public class InventorySlotUI extends Stack {
    Image bg;
    private Image itemImage;
    Texture backGroundTexture;

    public InventorySlotUI(GameObject inventoryItem) {

        Texture backGroundTexture = new Texture("brownSquare.png");

        bg = new Image(new TextureRegionDrawable(new TextureRegion(backGroundTexture)));
        this.add(bg);
        if (inventoryItem != null) {
            itemImage = new Image(new TextureRegionDrawable(new TextureRegion(inventoryItem.image)));
            itemImage.setScaling(Scaling.stretch);

            Container<Image> container = new Container<>(itemImage);
            if (inventoryItem.type == "weapon" && inventoryItem.name == "stick") {
                container.size(9, 48);  // this is respected!
            } else {
                container.size(48, 48);
            }
            container.fill();        // stretch inside
            this.add(container);
        }

        if (inventoryItem != null && inventoryItem.countable && inventoryItem.count >= 1) {
            Label.LabelStyle style = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
            Label countLabel = new Label(String.valueOf(inventoryItem.count), style);
            countLabel.setFontScale(1.2f);

            // Position in bottom right using a Container
            Container<Label> countContainer = new Container<>(countLabel);
            countContainer.bottom().right().padBottom(30).padRight(30); // ⬅️ positions it at bottom-right with slight padding

            this.add(countContainer);
        }
    }

    public void updateItem(GameObject newInventoryItem) {
        if (itemImage != null) {
            this.removeActor(itemImage);
        }
        if (newInventoryItem != null) {
            itemImage = new Image(new TextureRegionDrawable(new TextureRegion(newInventoryItem.image)));;
            itemImage.setScaling(Scaling.stretch);
            itemImage.setSize(48, 48);
            this.add(itemImage);
        }
    }
}
