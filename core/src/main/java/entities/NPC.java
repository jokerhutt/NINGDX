package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import jokerhut.main.DialogueHandler;
import jokerhut.main.MainScreen;
import objects.GameObject;

public class NPC extends Entity {

    public float invincibilityTimer = 0f;
    public boolean isInvincible = true;

    MainScreen screen;
    public float lastDirectionX = 0f;
    public float lastDirectionY;
    public Texture portrait;
    float actionTimer;
    float actionDuration;
    boolean isMoving;
    public String name;
    public String type;
    public boolean isInPurchaseScreen;
    public GameObject[] inventory;
    public DialogueHandler dialogueHandler;
    boolean movesOnItsOwn = false;
    public boolean isEmoting;
    public float emoteTimer;
    public final Vector2 futurePosition = new Vector2();

    public Vector2 knockback = new Vector2();
    public float knockbackTime = 0f;

    String idlePath;
    String walkingPath;

    public TextureRegion idleDown, idleUp, idleLeft, idleRight;
    public Animation<TextureRegion> walkDown, walkUp, walkLeft, walkRight;

    public NPC (float x, float y, MainScreen screen) {
        super(x, y);
        this.screen = screen;
        this.hitboxWidth = 0.8f;
        this.hitboxHeight = 0.5f;
        this.speed = 0.5f;
        this.health = 6;
        this.emoteTimer = 0f;
    }

    public void setupSprite (TextureRegion direction) {
        sprite = new Sprite(direction);
        sprite.setSize(1f, 1f);
        sprite.setPosition(this.position.x, this.position.y);
        sprite.setRegion(direction);
    }

    public void takeDamage (float damage, Entity entity) {

        if (!this.isInvincible && invincibilityTimer == 0) {
            this.isInvincible = true;
            this.health -= damage;
            applyKnockback(entity.getPosition(), 4f, 0.2f);
            System.out.println("attacked! Health is now " + health);
        } else if (this.isInvincible && invincibilityTimer >= 5f) {
            this.isInvincible = false;
            this.invincibilityTimer = 0;
        } else if (this.isInvincible) {
            invincibilityTimer ++;
        }

    }

    public boolean isKnockbackCollidingOnAxis(Vector2 knockVec, float delta) {
        // Copy position so we don't affect the real one
        Vector2 testPos = position.cpy().add(knockVec.x * delta, knockVec.y * delta);

        collisionRect.set(
            testPos.x + (sprite.getWidth() - hitboxWidth) / 2f,
            testPos.y,
            hitboxWidth,
            hitboxHeight
        );

        return checkAllCollisionsForNPC();
    }

    public void applyKnockback(Vector2 from, float strength, float duration) {
        Vector2 direction = new Vector2(position).sub(from).nor();
        direction.scl(1f);

        knockback.set(direction.scl(strength));
        knockbackTime = duration;
        System.out.println("Knockback from: " + from + " to " + position);
        System.out.println("Knockback direction: " + knockback);
    }

    public void drawSpeechBubble (SpriteBatch batch) {
        if (isInteracting) {
            float bubbleWidth = 0.5f;
            float bubbleHeight = 0.5f;

            float bubbleX = sprite.getX() + sprite.getWidth() / 2.2f - bubbleWidth / 2f;
            float bubbleY = sprite.getY() + (sprite.getHeight() / 1f);

            batch.draw(this.speechBubble, bubbleX, bubbleY, bubbleWidth, bubbleHeight);
        }
        if (isEmoting) {
            float bubbleWidth = 0.5f;
            float bubbleHeight = 0.5f;

            float bubbleX = sprite.getX() + sprite.getWidth() / 2.2f - bubbleWidth / 2f;
            float bubbleY = sprite.getY() + (sprite.getHeight() / 1f);

            batch.draw(this.happyEmote, bubbleX, bubbleY, bubbleWidth, bubbleHeight);
        }
    }

    public void update (float delta) {
        if (this.movesOnItsOwn) {
            setAction(delta);
        }
    }

    public void runEmoting () {
        if (isEmoting) {
            System.out.println(emoteTimer);
            if (emoteTimer >= 100f) {
                isEmoting = false;
                emoteTimer = 0f;
            } else {
                emoteTimer++;
            }
        }
    }

    public void render (SpriteBatch batch) {
        drawSpeechBubble(batch);
        sprite.draw(batch);
    }

    public void chooseRandomDirection() {
        int i = MathUtils.random(0, 3);
        switch (i) {
            case 0:
                velocity.set(1, 0);
                break;
            case 1:
                velocity.set(-1, 0);
                break;
            case 2:
                velocity.set(0, 1);
                break;
            case 3:
                velocity.set(0, -1);
                break;
        }
    }

    public void setAction(float delta) {
        actionTimer += delta;

        if (actionTimer >= actionDuration) {
            actionTimer = 0;
            isMoving = !isMoving;

            if (isMoving) {
                chooseRandomDirection();
                actionDuration = MathUtils.random(1f, 4f);
            } else {
                velocity.set(0, 0); // stop
                actionDuration = MathUtils.random(1f, 2f);
            }
        }

        if (isMoving) {
            if (!screen.collisionChecker.checkStaticObjectCollision(screen.wallCollisionRects, this) &&
                !screen.collisionChecker.checkEntityCollisionWithPlayer(this, screen.player)
            )
            {

                futurePosition.set(
                    position.x + velocity.x * speed * delta,
                    position.y + velocity.y * speed * delta
                );


                collisionRect.set(
                    futurePosition.x + (sprite.getWidth() - hitboxWidth) / 2f,
                    futurePosition.y,
                    hitboxWidth,
                    hitboxHeight
                );

                if (this instanceof Enemy) {
                    ((Enemy) this).hitboxRectangle.set(
                        sprite.getX(),
                        sprite.getY(),
                        sprite.getWidth(),
                        sprite.getHeight()
                    );
                }

                if (!checkAllCollisionsForNPC()) {
                    position.set(futurePosition);
                } else {
                    velocity.setZero();
                }

                if (velocity.y > 0) {
                    this.sprite.setRegion(walkUp.getKeyFrame(actionTimer, true));
                } else if (velocity.y < 0) {
                    this.sprite.setRegion(walkDown.getKeyFrame(actionTimer, true));
                } else if (velocity.x > 0) {
                    this.sprite.setRegion(walkRight.getKeyFrame(actionTimer, true));
                } else if (velocity.x < 0) {
                    this.sprite.setRegion(walkLeft.getKeyFrame(actionTimer, true));
                }

                position.add(velocity.x * speed * delta, velocity.y * speed * delta);
                this.collisionRect.set(
                    this.position.x + (sprite.getWidth() - hitboxWidth) / 2f,
                    this.position.y,
                    hitboxWidth,
                    hitboxHeight
                );

            }
        } else {
            if (lastDirectionX > 0) {
                sprite.setRegion(idleUp);
            } else if (lastDirectionX < 0) {
                sprite.setRegion(idleDown);
            } else if (lastDirectionY > 0) {
                sprite.setRegion(idleRight);
            } else if (lastDirectionY < 0) {
                sprite.setRegion(idleLeft);
            }
        }

        sprite.setPosition(position.x, position.y);
        if (velocity.x != 0) {
            lastDirectionX = velocity.x;
        }
        if (velocity.y != 0) {
            lastDirectionY = velocity.y;
        }
    }

    public boolean checkAllCollisionsForNPC () {
        if (!screen.collisionChecker.checkStaticObjectCollision(screen.wallCollisionRects, this) &&
            !screen.collisionChecker.checkEntityCollisionWithPlayer(this, screen.player) &&
            !screen.collisionChecker.checkEntityCollision(screen.npcArray, this)
        ) {
            return false;
        } else {
            return true;
        }
    }

    public void setupAnimation (String idlePath, String walkPath) {
        Texture sheet = new Texture(idlePath);
        TextureRegion[][] split = TextureRegion.split(sheet, 16, 16);

        Texture walkingSheet = new Texture(walkPath);
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

}
