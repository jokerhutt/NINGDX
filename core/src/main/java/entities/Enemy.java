package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import jokerhut.main.MainScreen;

public class Enemy extends NPC {

    public int damage;

    public Animation<TextureRegion> enemyDeathAnimation;

    public Enemy (float x, float y, MainScreen screen) {
        super(x, y, screen);
        this.name = "redSnake";
        this.type = "basicEnemy";
        this.idlePath = "redSnakeSheet.png";
        this.portrait = new Texture("redSnakePortrait.png");
        setupEnemyAnimation(idlePath);
        this.damage = 3;
        this.health = 10;
        this.hitboxRectangle = new Rectangle();
        this.lastDirectionY = -1;
        this.meleeAttackBox = new Rectangle();
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
        enemyDeathAnimation = animationHandler.setupDeathAnimation(this);

    }

    public void handleDeath () {
        isAlive = false;
        turnOnDeathAnimation();
    }



    public void handleActions (float delta) {
        handleAttackCooldown();
            screen.physicsHandler.handleKnockback(delta, this);
            long currTime = System.currentTimeMillis();
//        System.out.println(this.isInvincible + " at " + currTime);
    }

    public void turnOnDeathAnimation () {
        sprite.setRegion(enemyDeathAnimation.getKeyFrame(animationTimer, true));
    }



    public void performAttack (Player targetPlayer) {
        if (!isAttacking && attackingCooldown <= 0) {
            isAttacking = true;
            targetPlayer.takeDamage(this.damage, this);
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
