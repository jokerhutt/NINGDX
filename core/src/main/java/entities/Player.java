package entities;

import Constants.Constants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import jokerhut.main.AnimationHandler;
import jokerhut.main.CollisionChecker;
import jokerhut.main.KeyHandler;
import jokerhut.main.MainScreen;

public class Player extends Entity{

    public TextureRegion idleDown, idleUp, idleLeft, idleRight;
    public Animation<TextureRegion> walkDown, walkUp, walkLeft, walkRight;
    AnimationHandler playerAnimationHandler;
    public float animationTimer = 0f;
    public boolean moving;

    public Rectangle dialogueBox;

    public final Vector2 futurePosition = new Vector2();

    public KeyHandler playerKeyHandler;
    MainScreen screen;

    public Player (float x, float y, MainScreen screen) {
        super(x, y);
        this.screen = screen;
        setupPlayerAnimation();
        playerAnimationHandler = new AnimationHandler(this);
        sprite = new Sprite(idleDown);
        sprite.setSize(1f, 1f);
        sprite.setPosition(this.position.x, this.position.y);
        sprite.setRegion(idleDown);
        this.dialogueBox = new Rectangle();
        this.moving = false;
        this.playerKeyHandler = new KeyHandler(this, screen);
        this.hitboxWidth = 0.8f;
        this.hitboxHeight = 0.5f;
        this.speed = 6f;


    }

    public float getCenterX() {
        return this.position.x + this.sprite.getWidth() / 2f;
    }
    public float getCenterY () {
        return this.position.y + this.sprite.getHeight() / 2f;
    }

    public void setMoving (boolean moving) {
        this.moving = moving;
    }

    public void setVelocity (Vector2 velocity) {
        this.velocity = velocity;
    }

    public void setVelocityY (float velocityY) {
        this.velocity.y = velocityY;
    }

    public void setVelocityX (float velocityX) {
        this.velocity.x = velocityX;
    }

    public float getSpeed () {
        return this.speed;
    }

    public void setupPlayerAnimation () {
        Texture sheet = new Texture("Idle.png");
        TextureRegion[][] split = TextureRegion.split(sheet, 16, 16);

        Texture walkingSheet = new Texture("Walk.png");
        TextureRegion[][] splitWalkingSheet = TextureRegion.split(walkingSheet, 16, 16);

        idleDown  = split[0][0];
        walkDown  = new Animation<>(0.2f, splitWalkingSheet[0][0], splitWalkingSheet[1][0], splitWalkingSheet[2][0], splitWalkingSheet[3][0]);
        idleUp    = split[0][1];
        walkUp    = new Animation<>(0.2f, splitWalkingSheet[0][1], splitWalkingSheet[1][1], splitWalkingSheet[2][1], splitWalkingSheet[3][1]);
        idleLeft  = split[0][2];
        walkLeft  = new Animation<>(0.2f, splitWalkingSheet[0][2], splitWalkingSheet[1][2], splitWalkingSheet[2][2], splitWalkingSheet[3][2]);
        idleRight = split[0][3];
        walkRight = new Animation<>(0.2f, splitWalkingSheet[0][3], splitWalkingSheet[1][3], splitWalkingSheet[2][3], splitWalkingSheet[3][3] );

    }

    public void handleMovement (float delta) {

        animationTimer += delta;
        velocity.set(0, 0);
        moving = false;

        //Only Sets Direction/Velocity!!
        if (!playerKeyHandler.handleDiagonalMovement()) {
            playerKeyHandler.handlePlayerCardinalMovement();
        }


        if (!moving) {
            animationTimer = 0f;
            playerAnimationHandler.handleIdleAnimation();
        }

        handleCollisionAndUpdatePlayer(delta);

        playerKeyHandler.checkXAndY(this);


    }

    public void handleCollisionAndUpdatePlayer (float delta) {

        if (isCollidingOnAxis('x', delta)) {
            position.x = futurePosition.x;
        }
        if (isCollidingOnAxis('y', delta)) {
            position.y = futurePosition.y;
        }

        updateDialogueCollisionZone();
        sprite.setPosition(position.x, position.y);
        screen.collisionChecker.checkEntityDialogueCollision(screen.npcArray, this);
        playerKeyHandler.enterDialogue();

    }

    public boolean isCollidingOnAxis (char axis, float delta) {

        if (axis == 'x') {
            futurePosition.set(position.x + velocity.x * delta, position.y);
            collisionRect.set(
                futurePosition.x + (sprite.getWidth() - hitboxWidth) / 2f,
                futurePosition.y,
                hitboxWidth,
                hitboxHeight
            );

            if (this.checkAllCollisions()) {
                return true;
            } else {
                return false;
            }
        }
        if (axis == 'y') {
            futurePosition.set(position.x, position.y + velocity.y * delta);
            collisionRect.set(
                futurePosition.x + (sprite.getWidth() - hitboxWidth) / 2f,
                futurePosition.y,
                hitboxWidth,
                hitboxHeight
            );

            if (this.checkAllCollisions())
            {
                return true;
            } else {
                return false;
            }
        }

        return false;

    }

    public void updateDialogueCollisionZone () {
        float offset = 1f;
        float boxSize = 0.8f;

        float dx = 0;
        float dy = 0;

        if (lastDirection.x > 0) dx = offset;
        if (lastDirection.x < 0) dx = -offset;
        if (lastDirection.y > 0) dy = offset;
        if (lastDirection.y < 0) dy = -offset;

        dialogueBox.set(
            getCenterX() + dx - boxSize / 2f,
            getCenterY() + dy - boxSize / 2f,
            boxSize,
            boxSize
        );
    }

    public boolean checkAllCollisions () {
        if (!screen.collisionChecker.checkStaticObjectCollision(screen.wallCollisionRects, this) &&
            !screen.collisionChecker.checkEntityCollision(screen.npcArray, this)) {
            return true;
        } else {
            return false;
        }
    }






    public void update(float delta) {
        handleMovement(delta);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }


}
