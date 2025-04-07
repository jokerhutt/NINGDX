package entities;

import Constants.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import jokerhut.main.AnimationHandler;
import jokerhut.main.CollisionChecker;
import jokerhut.main.KeyHandler;
import jokerhut.main.MainScreen;
import utils.DirectionUtils;
import utils.MovementUtils;

import java.util.Map;

public class Player extends Entity{

    public TextureRegion idleDown, idleUp, idleLeft, idleRight;
    public TextureRegion attackDown, attackUp, attackLeft, attackRight;
    public Animation<TextureRegion> walkDown, walkUp, walkLeft, walkRight;
    public AnimationHandler animationHandler;
    public boolean hasTriggeredFx = false;
    public float animationTimer = 0f;

    Texture stickInHandTexture = new Texture("stickInHand.png");
    Texture lanceInHandTexture = new Texture("lanceInHand.png");
    Texture swordInHandTexture = new Texture("swordInHand.png");
    TextureRegion stickInHandRegion;
    TextureRegion lanceInHandRegion;
    TextureRegion swordInHandRegion;
    public boolean isAttacking;
    public float attackingTimer;
    public float attackingCooldown;
    public boolean isInAttackCoolDown = false;

    private final Map<String, Vector2> weaponAnchors = Map.of(
        "right", new Vector2(1.35f, 0.25f),
        "left", new Vector2(-0.35f, 0.25f),
        "up", new Vector2(0.3f, 1.35f),
        "down", new Vector2(0.2f, -0.35f)
    );

    Sprite weaponSprite;



    public int coins;
    public Inventory inventory;

    public Rectangle dialogueBox;
    public Rectangle meleeAttackBox;

    public KeyHandler playerKeyHandler;

    public Player (float x, float y, MainScreen screen) {
        super(x, y, screen);

        sprite = new Sprite();
        sprite.setSize(1f, 1f);
        sprite.setPosition(this.position.x, this.position.y);

        animationHandler = new AnimationHandler();
        animationHandler.setupPlayerAnimation(this);
        animationHandler.setupFxActionArray(this);
        stickInHandRegion = new TextureRegion(stickInHandTexture);
        lanceInHandRegion = new TextureRegion(lanceInHandTexture);
        swordInHandRegion = new TextureRegion(swordInHandTexture);

        this.health = 6;
        this.attackingTimer = 0f;
        this.attackingCooldown = 0f;
        this.isAttacking = false;
        sprite.setRegion(idleDown);
        this.dialogueBox = new Rectangle();
        this.meleeAttackBox = new Rectangle();
        weaponSprite = new Sprite(stickInHandTexture);
        weaponSprite.setSize(9f / Constants.TILESIZE, 36 / Constants.TILESIZE);
        weaponSprite.setOriginCenter(); // Important: so it rotates around the middle

        this.playerKeyHandler = new KeyHandler(this, screen);
        this.hitboxWidth = 0.8f;
        this.hitboxHeight = 0.5f;
        this.speed = 6f;
        this.coins = 10;
        this.inventory = new Inventory(this);


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

    public void handleMovement (float delta) {

        animationTimer += delta;
        velocity.set(0, 0);
        moving = false;
        intendedDirection.set(0, 0);

        //DIRECTION BASED METHODS
        setIntendedAndLastDirection();
        applyVelocityFromDirection();
        moving = MovementUtils.checkIfMoving(this);

        //COLLISION BOX & SPRITE POSITION UPDATE METHODS
        updateCollisionBoxes();

        //ACTION BASED METHODS
        playerKeyHandler.handleAttacking();

        //APPLYING MOVEMENT METHODS
        if (moving) {
            applySlidingMovement(velocity, delta);
        }

        //ANIMATION METHODS
        updateSpriteAnimation();
        playerKeyHandler.enterDialogue();
        sprite.setPosition(position.x, position.y);

    }

    /// START
    ///
    ///
    @Override
    public void setIntendedAndLastDirection () {
        handleInput();
    }

    @Override
    public boolean checkAllCollisions () {
        if (screen.collisionChecker.checkStaticObjectCollision(screen.wallCollisionRects, this) ||
            screen.collisionChecker.checkEntityCollision(screen.npcArray, this) ||
            screen.collisionChecker.checkEntityCollision(screen.enemyArray, this)
        ) {
            return true;
        } else {
            return false;
        }
    }

    public void updateCollisionBoxes () {
        updateDialogueCollisionZone();
        screen.collisionChecker.checkEntityDialogueCollision(screen.npcArray, this);
        updateMeleeAttackZone();
        screen.collisionChecker.checkAttackCollision(screen.enemyArray, this);
    }

    public void updateSpriteAnimation () {

        if (moving && !isAttacking) {

            if (velocity.y > 0) {
                sprite.setRegion(walkUp.getKeyFrame(animationTimer, true));
            } else if (velocity.y < 0) {
                sprite.setRegion(walkDown.getKeyFrame(animationTimer, true));
            } else if (velocity.x > 0 && velocity.y == 0) {
                sprite.setRegion(walkRight.getKeyFrame(animationTimer, true));
            } else if (velocity.x < 0 && velocity.y == 0) {
                sprite.setRegion(walkLeft.getKeyFrame(animationTimer, true));
            }

        } else if (!moving && !isAttacking) {
            if (velocity.y == 1) {
                sprite.setRegion(idleUp);
            }
            else if (velocity.x == -1) {
                sprite.setRegion(idleLeft);
            }
            else if (velocity.y == -1) {
                sprite.setRegion(idleDown);
            }
            else if (velocity.x == 1) {
                sprite.setRegion(idleRight);
            }
        } else if (isAttacking) {
            if (lastDirection.y > 0) {
                sprite.setRegion(attackUp);
            } else if (lastDirection.y < 0) {
                sprite.setRegion(attackDown);
            } else if (lastDirection.x > 0) {
                sprite.setRegion(attackRight);
            } else if (lastDirection.x < 0) {
                sprite.setRegion(attackLeft);
            }
        }

    }

    public void handleInput () {

        playerKeyHandler.handleSettingCardinalMovement();
        playerKeyHandler.handleSettingDiagonalMovement();

    }


    /// END


    public void updateDialogueCollisionZone () {
        float offset = 1f;
        float boxSize = 1.5f;

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

    public void updateMeleeAttackZone () {
        float offset = 0.8f;
        float boxSize = 0.8f;

        float dx = 0;
        float dy = 0;

        if (lastDirection.x > 0) dx = offset;
        if (lastDirection.x < 0) dx = -offset;
        if (lastDirection.y > 0) dy = offset;
        if (lastDirection.y < 0) dy = -offset;

        meleeAttackBox.set(
            getCenterX() + dx - boxSize / 1.8f,
            (getCenterY() + dy - boxSize / 1.8f),
            boxSize,
            boxSize
        );
    }

    public void handleTakenDamage (Enemy enemyEntity) {

        System.out.println("took damage");
        health -= enemyEntity.damage;
        System.out.println("Health is now: " + health);

    }

    public void updateWeaponSprite() {
        if (inventory.currentItem == null) return;

        switch (inventory.currentItem.name) {
            case "stick":
                weaponSprite.setRegion(stickInHandRegion);
                weaponSprite.setSize(9f / Constants.TILESIZE, 36 / Constants.TILESIZE);
                break;
            case "lance":
                weaponSprite.setRegion(lanceInHandRegion);
                weaponSprite.setSize(18f / Constants.TILESIZE, 48 / Constants.TILESIZE);
                break;
            case "sword":
                weaponSprite.setRegion(swordInHandRegion);
                weaponSprite.setSize(18f / Constants.TILESIZE, 33 / Constants.TILESIZE);
                break;
        }

        weaponSprite.setOriginCenter();

        String dir = "down";
        if (lastDirection.x == 1) dir = "right";
        else if (lastDirection.x == -1) dir = "left";
        else if (lastDirection.y == 1) dir = "up";

        // Get the anchor point
        Vector2 anchor = weaponAnchors.get(dir);

        // Set rotation
        switch (dir) {
            case "right":
                weaponSprite.setRotation(90f);
                break;
            case "left":
                weaponSprite.setRotation(270f);
                break;
            case "up":
                weaponSprite.setRotation(180f);
                break;
            case "down":
                weaponSprite.setRotation(0f);
                break;
        }

        weaponSprite.setPosition(
            position.x + anchor.x - weaponSprite.getWidth() / 2f,
            position.y + anchor.y - weaponSprite.getHeight() / 2f
        );

    }






    public void update(float delta) {

        handleMovement(delta);
    }

    public void render(SpriteBatch batch) {
        if (isAttacking) {
            updateWeaponSprite();
            weaponSprite.draw(batch);
        }
        sprite.draw(batch);
    }


}
