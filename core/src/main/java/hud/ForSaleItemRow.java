package hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import objects.GameObject;

public class ForSaleItemRow extends Table {

    TextureRegionDrawable imageDrawable;
    TextureRegionDrawable backgroundDrawable;
    GameObject item;

    public ForSaleItemRow (String backgroundPath, GameObject item, BitmapFont font) {

        this.item = item;
        imageDrawable = new TextureRegionDrawable(new TextureRegion(item.image));
        item.image.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        Texture backGroundBox = new Texture(backgroundPath);
        backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backGroundBox));


        this.setBackground(backgroundDrawable);
        this.pad(8, 30, 8, 30);
        this.setDebug(true);

        // Image
        Image itemImage = new Image(imageDrawable);
        this.add(itemImage).size(48).padRight(10);

        // Name
        Label nameLabel = new Label(item.name, new Label.LabelStyle(font, Color.WHITE));
        nameLabel.setFontScale(1.2f);
        nameLabel.setColor(Color.BLACK);
        this.add(nameLabel).expandX().left();

        // Cost
        Label costLabel = new Label(item.cost + " G", new Label.LabelStyle(font, Color.GOLD));
        costLabel.setFontScale(1.2f);
        this.add(costLabel).right().padRight(10);

    }

}
