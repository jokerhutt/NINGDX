package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import jokerhut.main.AnimationHandler;
import jokerhut.main.DialogueHandler;
import jokerhut.main.MainScreen;
import objects.GameObject;
import aiBehavior.AIUtils;
import utils.DirectionUtils;
import utils.MovementUtils;

public class NPC extends Entity {

    public boolean chasing = false;
    public float lastDirectionX = 0f;
    public float lastDirectionY;
    public Texture portrait;
    public float actionDuration = 0f;
    public String name;
    public String type;
    public boolean isInPurchaseScreen;
    public GameObject[] inventory;
    public DialogueHandler dialogueHandler;
    boolean movesOnItsOwn = false;
    String idlePath;
    String walkingPath;
    Vector2 lastPlayerTile = new Vector2(-1, -1);
    public int currentPathIndex = 0;
    public Array<Vector2> path = new Array<>();
    public float pathRefreshTimer;
    public float pathRefreshCooldown;
    public Entity lockedOnto = null;



    public TextureRegion idleDown, idleUp, idleLeft, idleRight;
    public Animation<TextureRegion> walkDown, walkUp, walkLeft, walkRight;

    public EmoteHandler emoteHandler;

    public NPC (float x, float y, MainScreen screen) {
        super(x, y, screen);
        this.hitboxWidth = 0.8f;
        this.animationHandler = new AnimationHandler();
        this.emoteHandler = new EmoteHandler(this);
        this.meleeAttackBox = new Rectangle();
        this.hitboxHeight = 0.5f;
        this.health = 6;
        this.emoteTimer = 0f;
    }


    public void handleActions (float delta) {

    }

    public void update (float delta) {

        if (isAlive) {
            handleInvincibility();
            if (this instanceof Enemy) {
                hitboxRectangle.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
                sprite.setPosition(position.x, position.y);
            }

            if (this.movesOnItsOwn) {
                setIntendedAndLastDirection();
                setAction(delta);
            }
            handleActions(delta);
        } else {
            handleDeathTimer(delta);
        }
    }


    public void render (SpriteBatch batch) {
        emoteHandler.drawSpeechBubble(batch);

        if (isInvincible) {
            sprite.setColor(1, 1, 1, 0.5f); // white tint, 50% opacity
        } else {
            sprite.setColor(1, 1, 1, 1f); // full opacity
        }

        sprite.draw(batch);

    }

    public void setIntendedAndLastDirection () {
        AIUtils.chooseRandomDirection(this);
    }

    public void handleDeathTimer (float delta) {
        animationTimer+= delta;
        if (!this.isAlive) {
            if (deathTimer >= 8f) {
                screen.enemyArray.removeValue((Enemy) this, true);
            } else {
                deathTimer++;
            }
        }
    }

    public void handleAttackCooldown () {
        if (isAttacking && attackingCooldown > 0.5f) {
            isAttacking = false;
            attackingCooldown = 0f;
        } else if (isAttacking) {
            attackingCooldown++;
        }
    }

    public void setAction(float delta) {

        animationTimer+= delta;
            actionTimer += delta;

            if (this instanceof Enemy) {
                AIUtils.determineNearestEntity(this, screen.player);
            } else if (this instanceof NPC_Sensei) {
                AIUtils.determineNearestEnemy(this);
            }

            if (chasing) {
                speed = 3f;
                AIUtils.handleChasing(delta, this);
            } else {
                speed = 0.5f;
                MovementUtils.applyStandardMovement(this);
            }

            if (this instanceof NPC_Sensei) {
                System.out.println("Updating attack zone");
                updateMeleeAttackZone();
                screen.collisionChecker.checkAttackCollision(screen.enemyArray, this);
            }


            if (moving || chasing) {
                if (knockback.x == 0 && knockback.y == 0) {
                    applySlidingMovement(velocity, delta);
                }
            }

            if (this instanceof Enemy) {
                hitboxRectangle.set(
                    sprite.getX(),
                    sprite.getY(),
                    sprite.getWidth(),
                    sprite.getHeight()
                );
            }

            updateSpriteAnimation();
            sprite.setPosition(position.x, position.y);
            this.collisionRect.set(
                this.position.x + (sprite.getWidth() - hitboxWidth) / 2f,
                this.position.y,
                hitboxWidth,
                hitboxHeight
            );

    }

    public void updateSpriteAnimation () {

        if (moving) {
            if (velocity.y > 0) {
                sprite.setRegion(walkUp.getKeyFrame(actionTimer, true));
            } else if (velocity.y < 0) {
                sprite.setRegion(walkDown.getKeyFrame(actionTimer, true));
            } else if (velocity.x > 0 && velocity.y == 0) {
                sprite.setRegion(walkRight.getKeyFrame(actionTimer, true));
            } else if (velocity.x < 0 && velocity.y == 0) {
                sprite.setRegion(walkLeft.getKeyFrame(actionTimer, true));
            }


    } else if (!moving) {
            if (velocity.y == 1) {
                sprite.setRegion(idleUp);
            } else if (velocity.x == -1) {
                sprite.setRegion(idleLeft);
            } else if (velocity.y == -1) {
                sprite.setRegion(idleDown);
            } else if (velocity.x == 1) {
                sprite.setRegion(idleRight);
            }
        }
    }

    public void setupSprite (TextureRegion direction) {
        sprite = new Sprite(direction);
        sprite.setSize(1f, 1f);
        sprite.setPosition(this.position.x, this.position.y);
        sprite.setRegion(direction);
    }



    public boolean checkAllCollisions () {
        if (screen.collisionChecker.checkStaticObjectCollision(screen.wallCollisionRects, this) ||
            screen.collisionChecker.checkEntityCollision(screen.npcArray, this) ||
            screen.collisionChecker.checkEntityCollision(screen.enemyArray, this) ||
            screen.collisionChecker.checkEntityCollisionWithPlayer(this, screen.player) ||
            checkEnemyBounds()
        ) {

            return true;

        } else {
            return false;
        }
    }

    public boolean checkEnemyBounds () {

        if (this instanceof Enemy) {

            if (screen.collisionChecker.checkStaticObjectCollision(screen.enemyBoundaryRects, this)) {
                return true;
            } else {
                return  false;
            }

        } else {
            return false;
        }

    }


    public void setupAnimation (String idlePath, String walkPath) {
        animationHandler.setupNPCAnimation(this, idlePath, walkPath);
    }

}
