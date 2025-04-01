package entities;

import com.badlogic.gdx.graphics.Texture;
import jokerhut.main.MainScreen;

public class NPC_Gilbert extends NPC {

    public NPC_Gilbert (float x, float y, MainScreen screen) {
        super(x, y, screen);
        this.name = "gilbert";
        this.type = "merchant";
        this.idlePath = "gilbertIdle.png";
        this.walkingPath = "gilbertWalk.png";
        this.portrait = new Texture("gilbertPortrait.png");
        setupAnimation(idlePath, walkingPath);
        setupSprite(idleDown);
        this.lastDirectionY = -1f;
        this.movesOnItsOwn = false;
        this.collisionRect.set(
            this.position.x + (sprite.getWidth() - hitboxWidth) / 2f,
            this.position.y,
            hitboxWidth,
            hitboxHeight
        );
    }


}
