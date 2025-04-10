package entities;

import Constants.Constants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import jokerhut.main.AnimationHandler;
import jokerhut.main.Main;
import jokerhut.main.MainScreen;
import sound.SFXHandler;
import utils.MovementUtils;

public abstract class Entity {

    public Vector2 lastDirection;
    public Vector2 position;
    public Vector2 velocity;
    public float speed = 0.5f;
    public Sprite sprite;
    public Rectangle collisionRect;
    public boolean isAttacking;
    public float attackingTimer;
    public float attackingCooldown;
    public boolean isInAttackCoolDown = false;
    public Rectangle futureCollisionRect;
    public float hitboxHeight;
    public SFXHandler sfxHandler;
    public float hitboxWidth;
    public int health;
    public float actionTimer;
    public boolean isInteracting;
    public boolean isInDialogue;
    public Texture speechBubble;
    public Texture happyEmote;
    public boolean moving = false;
    public final Vector2 futurePosition = new Vector2();
    public Vector2 intendedDirection = new Vector2();
    public Vector2 knockback = new Vector2();
    public float knockbackTime = 0f;
    public MainScreen screen;
    public float invincibilityTimer = 0f;
    public boolean isInvincible = false;
    public AnimationHandler animationHandler;
    public boolean isEmoting;
    public float emoteTimer;
    public Rectangle hitboxRectangle;
    public Rectangle meleeAttackBox;
    public boolean isAlive = true;
    public float deathTimer = 0f;
    public float animationTimer = 0f;
    int maxHealth;

    public Rectangle getCollisionRect() {
        return this.collisionRect;
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

    public Vector2[] getKnockbackDirections() {
        return new Vector2[] {
            knockback.cpy(),
            new Vector2(knockback.x, 0),
            new Vector2(0, knockback.y),
            new Vector2(-knockback.x, 0),
            new Vector2(0, -knockback.y),
            new Vector2(-knockback.x, -knockback.y)
        };
    }

    public float getCenterX() {
        return this.position.x + this.sprite.getWidth() / 2f;
    }
    public float getCenterY () {
        return this.position.y + this.sprite.getHeight() / 2f;
    }


    public Entity (float x, float y, MainScreen screen) {
        this.lastDirection = new Vector2(-1, 0);
        this.velocity = new Vector2(0, 0);
        this.position = new Vector2(x, y);
        this.collisionRect = new Rectangle();
        this.futureCollisionRect = new Rectangle();
        this.screen = screen;
        sfxHandler = new SFXHandler();
        this.speechBubble = new Texture("emote20.png");
        this.happyEmote = new Texture("emote27.png");
    }

    public abstract void setIntendedAndLastDirection ();

    public void applyVelocityFromDirection () {
        MovementUtils.applyNormalizedVelocityFromDirection(this);
    }

    public boolean applySlidingMovement(Vector2 moveVec, float delta) {
        Vector2 move = moveVec.cpy().scl(delta);
        boolean moved = false;

        if (!this.willCollideAt(position.x + move.x, position.y + move.y)) {
            position.add(move);
            moved = true;
        } else {
            if (!this.willCollideAt(position.x + move.x, position.y)) {
                position.x += move.x;
                moved = true;
            }
            if (!this.willCollideAt(position.x, position.y + move.y)) {
                position.y += move.y;
                moved = true;
            }

        }
        return moved;
    }

    public boolean willCollideAt(float testX, float testY) {
        setFutureCollisionRect(testX, testY);
        return checkAllCollisions();
    }

    public abstract boolean checkAllCollisions ();

    public void setFutureCollisionRect (float futureX, float futureY) {
        collisionRect.set(
            futureX + (sprite.getWidth() - hitboxWidth) / 2f,
            futureY,
            hitboxWidth,
            hitboxHeight
        );
    }

    public void updateSpriteAnimation () {

    }

    public void takeDamage (float damage, Entity entity) {

        if (this instanceof Enemy) {
//            System.out.println(this.health);
        }

        if (!this.isInvincible && invincibilityTimer == 0) {
            sfxHandler.playSound("impact");
            this.isInvincible = true;
            this.health -= damage;
            if (this instanceof Player) {
                screen.hud.updateHealth(this.health, this.maxHealth);
            }
            screen.physicsHandler.applyKnockback(entity.getPosition(), 4f, 0.2f, this);
            if (this.health <= 0) {
                handleDeath();
                return;
            }
            System.out.println("attacked! Health is now " + health);
        }

    }

    public void handleInvincibility () {
        if (this.isInvincible && invincibilityTimer >= 8f) {
            this.isInvincible = false;
            this.invincibilityTimer = 0;
        } else if (this.isInvincible) {
            invincibilityTimer ++;
        } else {
            invincibilityTimer = 0;
        }
    }

    public void handleDeath () {
        isAlive = false;
    }







    public Vector2 getPosition () {
        return this.position;
    }

    public void update (float delta) {

    }



    public void render (SpriteBatch batch) {

    }

}
