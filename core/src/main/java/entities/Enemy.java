package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import jokerhut.main.MainScreen;

public class Enemy extends NPC {

    public int damage;

    public boolean isAttacking;
    public float attackCooldown;

    public Rectangle hitboxRectangle;

    public Enemy (float x, float y, MainScreen screen) {
        super(x, y, screen);
        this.name = "redSnake";
        this.type = "basicEnemy";
        this.idlePath = "redSnakeSheet.png";
        this.portrait = new Texture("redSnakePortrait.png");
        setupEnemyAnimation(idlePath);
        this.damage = 1;
        this.hitboxRectangle = new Rectangle();

        this.lastDirectionY = -1;
        setupSprite(idleDown);

        this.movesOnItsOwn = true;
        this.collisionRect.set(
            this.position.x + (sprite.getWidth() - hitboxWidth) / 2f,
            this.position.y,
            hitboxWidth,
            hitboxHeight
        );
        this.hitboxRectangle.set(
            sprite.getX(),
            sprite.getY(),
            sprite.getWidth(),
            sprite.getHeight()
        );



    }

    public void applyTheKnockBackTest () {

    }



    @Override
    public void update(float delta) {
        super.update(delta); // keeps movement logic intact

        if (isAttacking && attackCooldown > 100f) {
            isAttacking = false;
            attackCooldown = 0f;
        } else if (isAttacking) {
            attackCooldown++;
        }

        if (knockbackTime > 0f) {
            boolean moved = false;

            Vector2[] directionsToTry = new Vector2[] {
                knockback,                             // original direction
                new Vector2(knockback.x, 0),           // X only
                new Vector2(0, knockback.y),           // Y only
                new Vector2(-knockback.x, 0),          // opposite X
                new Vector2(0, -knockback.y),          // opposite Y
                new Vector2(-knockback.x, -knockback.y) // fully opposite (last resort bounce)
            };

            for (Vector2 dir : directionsToTry) {
                if (!isKnockbackCollidingOnAxis(dir, delta)) {
                    position.x += dir.x * delta;
                    position.y += dir.y * delta;
                    System.out.println("Sliding direction: " + dir);
                    moved = true;
                    break;
                }
            }

            if (!moved) {
                System.out.println("Fully blocked, no movement");
            }

            if (moved) {
                knockbackTime -= delta;
            }

            if (knockbackTime <= 0f) {
                knockbackTime = 0f;
                knockback.setZero();
            }

            collisionRect.set(
                position.x + (sprite.getWidth() - hitboxWidth) / 2f,
                position.y,
                hitboxWidth,
                hitboxHeight
            );
            hitboxRectangle.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
            sprite.setPosition(position.x, position.y);
        }

    }

    public void performAttack (Player targetPlayer) {
        if (!isAttacking && attackCooldown <= 0) {
            isAttacking = true;
            targetPlayer.handleTakenDamage(this);
        }

    }

    public void setupEnemyAnimation (String enemyPath) {
        Texture sheet = new Texture(enemyPath);
        TextureRegion[][] split = TextureRegion.split(sheet, 16, 16);

        idleDown  = split[0][0];
        walkDown  = new Animation<>(0.2f, split[0][0], split[1][0], split[2][0], split[3][0]);
        idleUp    = split[0][1];
        walkUp    = new Animation<>(0.2f, split[0][1], split[1][1], split[2][1], split[3][1]);
        idleLeft  = split[0][2];
        walkLeft  = new Animation<>(0.2f, split[0][2], split[1][2], split[2][2], split[3][2]);
        idleRight = split[0][3];
        walkRight = new Animation<>(0.2f, split[0][3], split[1][3], split[2][3], split[3][3] );



    }

}
