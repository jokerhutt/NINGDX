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
    public AnimationHandler playerAnimationHandler;
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
    public boolean moving;
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

    public final Vector2 futurePosition = new Vector2();
    public Vector2 intendedDirection = new Vector2();

    public KeyHandler playerKeyHandler;
    MainScreen screen;

    public Player (float x, float y, MainScreen screen) {
        super(x, y);
        this.screen = screen;
        setupPlayerAnimation();
        stickInHandRegion = new TextureRegion(stickInHandTexture);
        lanceInHandRegion = new TextureRegion(lanceInHandTexture);
        swordInHandRegion = new TextureRegion(swordInHandTexture);
        sprite = new Sprite(idleDown);
        sprite.setSize(1f, 1f);

        this.health = 6;
        this.attackingTimer = 0f;
        this.attackingCooldown = 0f;
        this.isAttacking = false;
        sprite.setPosition(this.position.x, this.position.y);
        sprite.setRegion(idleDown);
        playerAnimationHandler = new AnimationHandler(this);
        this.dialogueBox = new Rectangle();
        this.meleeAttackBox = new Rectangle();
        this.moving = false;
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


    public void setupPlayerAnimation () {

        Texture sheet = new Texture("Idle.png");
        TextureRegion[][] split = TextureRegion.split(sheet, 16, 16);

        Texture walkingSheet = new Texture("Walk.png");
        TextureRegion[][] splitWalkingSheet = TextureRegion.split(walkingSheet, 16, 16);

        Texture attackingSheet = new Texture("Attack.png");
        TextureRegion[][] splitAttackingSheet = TextureRegion.split(attackingSheet, 16, 16);

        idleDown  = split[0][0];
        walkDown  = new Animation<>(0.2f, splitWalkingSheet[0][0], splitWalkingSheet[1][0], splitWalkingSheet[2][0], splitWalkingSheet[3][0]);
        idleUp    = split[0][1];
        walkUp    = new Animation<>(0.2f, splitWalkingSheet[0][1], splitWalkingSheet[1][1], splitWalkingSheet[2][1], splitWalkingSheet[3][1]);
        idleLeft  = split[0][2];
        walkLeft  = new Animation<>(0.2f, splitWalkingSheet[0][2], splitWalkingSheet[1][2], splitWalkingSheet[2][2], splitWalkingSheet[3][2]);
        idleRight = split[0][3];
        walkRight = new Animation<>(0.2f, splitWalkingSheet[0][3], splitWalkingSheet[1][3], splitWalkingSheet[2][3], splitWalkingSheet[3][3] );

        attackUp    = splitAttackingSheet[0][1];
        attackDown  = splitAttackingSheet[0][0];
        attackLeft  = splitAttackingSheet[0][2];
        attackRight = splitAttackingSheet[0][3];

    }

    public void handleMovement (float delta) {

        animationTimer += delta;
        velocity.set(0, 0);
        moving = false;
        intendedDirection.set(0, 0);

        //DIRECTION BASED METHODS
        handleInput();
        applyNormalizedVelocityFromDirection();

        //ACTION BASED METHODS
        playerKeyHandler.handleAttacking(delta);

        //APPLYING MOVEMENT METHODS
        if (moving) {
            applySlidingMovement(velocity, delta);
        }

        //ANIMATION METHODS
        updateSpriteAnimation();

        //COLLISION BOX & SPRITE POSITION UPDATE METHODS
        updateCollisionBoxes();

        playerKeyHandler.enterDialogue();

        sprite.setPosition(position.x, position.y);

//        animationTimer += delta;
//        velocity.set(0, 0);
//        moving = false;
//
//        //Only Sets Direction/Velocity!!
//        if (!playerKeyHandler.handleDiagonalMovement()) {
//            playerKeyHandler.handlePlayerCardinalMovement();
//        }
//
//
//        if (!moving && !isAttacking) {
//            animationTimer = 0f;
//            playerAnimationHandler.handleIdleAnimation();
//        }
//
//        playerKeyHandler.handleAttacking(delta);
//
//        handleCollisionAndUpdatePlayer(delta);
//
//        playerKeyHandler.checkXAndY(this);



    }

    /// START
    ///

    public void updateCollisionBoxes () {
        updateDialogueCollisionZone();
        screen.collisionChecker.checkEntityDialogueCollision(screen.npcArray, this);
        updateMeleeAttackZone();
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

    public void applyNormalizedVelocityFromDirection () {
        if (intendedDirection.x != 0 || intendedDirection.y != 0) {
            velocity.set(intendedDirection).nor().scl(speed);
        } else {
            velocity.set(0, 0);
        }
        moving = MovementUtils.checkIfMoving(this);
    }

    public boolean applySlidingMovement(Vector2 moveVec, float delta) {
        Vector2 move = moveVec.cpy().scl(delta);
        boolean moved = false;

        if (!willCollideAt(position.x + move.x, position.y + move.y)) {
            position.add(move);
            moved = true;
        } else {
            if (!willCollideAt(position.x + move.x, position.y)) {
                position.x += move.x;
                moved = true;
            }
            if (!willCollideAt(position.x, position.y + move.y)) {
                position.y += move.y;
                moved = true;
            }
        }

        return moved;
    }

    public boolean willCollideAt(float testX, float testY) {
        setFutureCollisionRect(testX, testY);
        for (Rectangle wall : screen.wallCollisionRects) {
            if (collisionRect.overlaps(wall)) {
                return true;
            }
        }
        return false;
    }

    public void setFutureCollisionRect (float futureX, float futureY) {
        collisionRect.set(
            futureX + (sprite.getWidth() - hitboxWidth) / 2f,
            futureY,
            hitboxWidth,
            hitboxHeight
        );
    }


    /// END

    public void handleCollisionAndUpdatePlayer (float delta) {

        updateMeleeAttackZone();
        screen.collisionChecker.checkAttackCollision(screen.enemyArray, this);


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

    public boolean checkAllCollisions () {
        if (!screen.collisionChecker.checkStaticObjectCollision(screen.wallCollisionRects, this) &&
            !screen.collisionChecker.checkEntityCollision(screen.npcArray, this) &&
            !screen.collisionChecker.checkEntityCollision(screen.enemyArray, this)
        ) {
            return true;
        } else {
            return false;
        }
    }

    public void handleTakenDamage (Enemy enemyEntity) {

        System.out.println("took damage");
        health -= enemyEntity.damage;
        System.out.println("Health is now: " + health);

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
