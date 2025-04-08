package hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ProgressBarStack extends Stack {

    private final Image progressImage;
    float scalar = 1.5f;

    public ProgressBarStack(Texture bgTexture, Texture whiteTexture, Texture progressTexture) {
        float barWidth = 22 * scalar;
        float barHeight = 40 * scalar;
        float bgWidth = 32 * scalar;
        float bgHeight = 56 * scalar;

        // Background
        Image background = new Image(bgTexture);
        background.setSize(bgWidth, bgHeight);

// Wrap in a container and disable layout growth
        Container<Image> backgroundContainer = new Container<>(background);
        backgroundContainer.size(bgWidth, bgHeight); // force fixed size
        backgroundContainer.fill(false);             // don't auto-expand

        this.add(backgroundContainer);

        // White fill (underlay)
        Image whiteFill = new Image(whiteTexture);
        whiteFill.setSize(barWidth, barHeight);
        this.add(whiteFill);

        // Pre-sliced health regions
        progressImage = new Image(progressTexture); // Use full texture as-is
        progressImage.setSize(barWidth, 40 * scalar); // full height initially
        this.add(progressImage);


        // Now set the full Stack size to the background size
        this.setSize(bgWidth, bgHeight);
    }

    public void setHealthPercent(float percent) {
        percent = Math.max(0f, Math.min(1f, percent));

        float fullHeight = 40f * scalar;
        float newHeight = fullHeight * percent;

        progressImage.setSize(progressImage.getWidth(), newHeight);
    }
}
