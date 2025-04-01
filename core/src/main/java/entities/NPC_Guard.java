package entities;

import com.badlogic.gdx.graphics.Texture;
import jokerhut.main.MainScreen;

public class NPC_Guard extends NPC {

    public NPC_Guard (float x, float y, MainScreen screen) {
        super(x, y, screen);
        this.name = "guard";
        this.idlePath = "knightIdle.png";
        this.walkingPath = "knightWalk.png";
        this.portrait = new Texture("knightPortrait.png");
        this.lastDirectionY = 1f;
        setupAnimation(idlePath, walkingPath);
        setupSprite(this.idleUp);
        this.collisionRect.set(
            this.position.x + (sprite.getWidth() - hitboxWidth) / 2f,
            this.position.y,
            hitboxWidth,
            hitboxHeight
        );

    }

}
