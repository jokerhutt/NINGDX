package hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import jokerhut.main.MainScreen;

public class PairActorGroup extends Group {

    private Texture imageTexture;
    private BitmapFont groupFont;
    public MainScreen screen;
    Label groupLabel;
    public boolean isDynamic = false;

    public PairActorGroup (MainScreen screen, String imagePath, String mainText, int dynamicValue, String finalText, char imageSize, float fontSize) {

        imageTexture = new Texture(imagePath);
        Image groupImage = new Image(new TextureRegionDrawable(new TextureRegion(imageTexture)));

        if (imageSize == 'S') {
            groupImage.setSize(32, 32);
        } else if (imageSize == 'M') {
            groupImage.setSize(64, 64);
        }
        groupImage.setPosition(0, 0);
        this.screen = screen;

        this.groupFont = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(groupFont, Color.WHITE);
        if (dynamicValue != -1) {
            isDynamic = true;
            groupLabel = new Label(String.valueOf(dynamicValue), style);
            groupLabel.setFontScale(fontSize);
            groupLabel.setPosition(groupImage.getWidth() + 10, groupImage.getHeight() / 2f - groupImage.getHeight() / 2f);

            addActor(groupImage);
            addActor(groupLabel);

            setSize(groupImage.getWidth() + groupLabel.getPrefWidth() + 10, groupImage.getHeight());
        }

    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }

    public void updateValue(int newValue, String mainText, String finalText) {
        if (isDynamic) {
            groupLabel.setText(mainText + newValue + finalText);
        }
    }


}
