package hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

public class BoxWithText extends Stack {

    private Texture imageTexture;

    public BoxWithText(String backgroundPath, String text, float fontScale) {
        imageTexture = new Texture(backgroundPath);
        Image bg = new Image(new TextureRegionDrawable(new TextureRegion(imageTexture)));
        bg.setScaling(Scaling.stretch);
        this.add(bg);

        Label label = new Label(text, new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        label.setFontScale(fontScale);

        Table inner = new Table();
        inner.setFillParent(true);
        inner.center();
        inner.add(label);

        this.add(inner);
    }
}
