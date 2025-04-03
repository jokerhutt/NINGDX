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

public class CoinActor extends Group {

    final Label coinLabel;
    MainScreen screen;
    public Texture coinTexture = new Texture("MoneyBag.png");
    private BitmapFont coinFont = new BitmapFont();


    public CoinActor(MainScreen screen, char size) {
        Image coinImage = new Image(new TextureRegionDrawable(new TextureRegion(coinTexture)));
        if (size == 'S') {
            coinImage.setSize(32, 32);
        } else if (size == 'M') {
            coinImage.setSize(64, 64);
        }
        coinImage.setPosition(0, 0);
        this.screen = screen;
        Label.LabelStyle style = new Label.LabelStyle(coinFont, Color.WHITE);
        coinLabel = new Label(String.valueOf(screen.player.coins), style);
        coinLabel.setFontScale(2f);
        coinLabel.setPosition(coinImage.getWidth() + 10, coinImage.getHeight() / 2f - coinLabel.getHeight() / 2f);

        addActor(coinImage);
        addActor(coinLabel);

        setSize(coinImage.getWidth() + coinLabel.getPrefWidth() + 10, coinImage.getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        coinLabel.setText(String.valueOf(screen.player.coins));
    }
}
