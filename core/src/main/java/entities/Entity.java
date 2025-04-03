package entities;

import Constants.Constants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Entity {

    public Vector2 lastDirection;
    public Vector2 position;
    public Vector2 velocity;
    public float speed;
    public Sprite sprite;
    public Rectangle collisionRect;
    float hitboxHeight;
    float hitboxWidth;
    public boolean isInteracting;
    public boolean isInDialogue;
    public Texture speechBubble;
    public Texture happyEmote;

    public Rectangle getCollisionRect() {
        return this.collisionRect;
    }


    public Entity (float x, float y) {
        this.lastDirection = new Vector2(-1, 0);
        this.velocity = new Vector2(0, 0);
        this.position = new Vector2(x, y);
        this.collisionRect = new Rectangle();
        this.speechBubble = new Texture("emote20.png");
        this.happyEmote = new Texture("emote27.png");
    }

    public Vector2 getPosition () {
        return this.position;
    }

    public void update (float delta) {

    }

    public void render (SpriteBatch batch) {

    }

}
