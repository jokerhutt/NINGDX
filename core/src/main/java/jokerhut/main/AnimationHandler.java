package jokerhut.main;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import debug.CollisionDebug;
import entities.Enemy;
import entities.Entity;
import entities.NPC;
import entities.Player;
import fx.EffectAnimation;
import objects.GameObject;

public class AnimationHandler {

    Array<EffectAnimation> effectAnimationArray;



    public AnimationHandler () {

    }

    public void setupFxActionArray (Player player) {

        Array<EffectAnimation> actionsArray = new Array<>();
        actionsArray.add(new EffectAnimation(new Vector2(player.getCenterX(), player.getCenterY()), "cut"));

        effectAnimationArray = actionsArray;

    }

    public void playFxAnimation(float delta, Player player) {
        if (player.isAttacking && !player.hasTriggeredFx) {
            EffectAnimation toPlay = effectAnimationArray.get(0);
            toPlay.reset(new Vector2(player.getCenterX(), player.getCenterY()));
            player.hasTriggeredFx = true;
        }
        if (player.isAttacking) {
            effectAnimationArray.get(0).update(delta);
        } else {
            player.hasTriggeredFx = false;
        }
    }

    public void renderCurrentAnimation (Player player, SpriteBatch batch) {
        if (player.isAttacking) {
            GameObject currentInventoryItem = player.inventory.currentItem;

            if (currentInventoryItem != null && currentInventoryItem.type.equals("weapon")) {

                EffectAnimation toPlay = effectAnimationArray.get(0);

                if (!toPlay.isFinished) {
                    toPlay.render(batch, player.lastDirection);
                }

            }
        }
    }

    public void setupNPCAnimation (NPC npc, String idlePath, String walkPath) {
        Texture sheet = new Texture(idlePath);
        TextureRegion[][] split = TextureRegion.split(sheet, 16, 16);

        Texture walkingSheet = new Texture(walkPath);
        TextureRegion[][] splitWalkingSheet = TextureRegion.split(walkingSheet, 16, 16);

        npc.idleDown  = split[0][0];
        npc.walkDown  = new Animation<>(0.2f, splitWalkingSheet[0][0], splitWalkingSheet[1][0], splitWalkingSheet[2][0], splitWalkingSheet[3][0]);
        npc.idleUp    = split[0][1];
        npc.walkUp    = new Animation<>(0.2f, splitWalkingSheet[0][1], splitWalkingSheet[1][1], splitWalkingSheet[2][1], splitWalkingSheet[3][1]);
        npc.idleLeft  = split[0][2];
        npc.walkLeft  = new Animation<>(0.2f, splitWalkingSheet[0][2], splitWalkingSheet[1][2], splitWalkingSheet[2][2], splitWalkingSheet[3][2]);
        npc.idleRight = split[0][3];
        npc.walkRight = new Animation<>(0.2f, splitWalkingSheet[0][3], splitWalkingSheet[1][3], splitWalkingSheet[2][3], splitWalkingSheet[3][3] );

    }

    public void setupPlayerAnimation (Player player) {

        Texture sheet = new Texture("Idle.png");
        TextureRegion[][] split = TextureRegion.split(sheet, 16, 16);

        Texture walkingSheet = new Texture("Walk.png");
        TextureRegion[][] splitWalkingSheet = TextureRegion.split(walkingSheet, 16, 16);

        Texture attackingSheet = new Texture("Attack.png");
        TextureRegion[][] splitAttackingSheet = TextureRegion.split(attackingSheet, 16, 16);

        player.idleDown  = split[0][0];
        player.walkDown  = new Animation<>(0.2f, splitWalkingSheet[0][0], splitWalkingSheet[1][0], splitWalkingSheet[2][0], splitWalkingSheet[3][0]);
        player.idleUp    = split[0][1];
        player.walkUp    = new Animation<>(0.2f, splitWalkingSheet[0][1], splitWalkingSheet[1][1], splitWalkingSheet[2][1], splitWalkingSheet[3][1]);
        player.idleLeft  = split[0][2];
        player.walkLeft  = new Animation<>(0.2f, splitWalkingSheet[0][2], splitWalkingSheet[1][2], splitWalkingSheet[2][2], splitWalkingSheet[3][2]);
        player.idleRight = split[0][3];
        player.walkRight = new Animation<>(0.2f, splitWalkingSheet[0][3], splitWalkingSheet[1][3], splitWalkingSheet[2][3], splitWalkingSheet[3][3] );

        player.attackUp    = splitAttackingSheet[0][1];
        player.attackDown  = splitAttackingSheet[0][0];
        player.attackLeft  = splitAttackingSheet[0][2];
        player.attackRight = splitAttackingSheet[0][3];

    }

    public Animation<TextureRegion> setupDeathAnimation (Enemy enemy) {

        Texture sheet = new Texture("smokeSpriteSheet.png");
        TextureRegion[][] split = TextureRegion.split(sheet, 32, 32);

        Animation<TextureRegion> deathAnim = new Animation<>(0.5f, split[0][0], split[0][1], split[0][2], split[0][3], split[0][4], split[0][5]);
        return  deathAnim;
    }

    public void handleDeathAnimationEnemy (Enemy enemy) {



    }




    public void handleIdleAnimation (Player player) {

        if (player.lastDirection.y == 1) {
            player.sprite.setRegion(player.idleUp);
        }
        else if (player.lastDirection.x == -1) {
            player.sprite.setRegion(player.idleLeft);
        }
        else if (player.lastDirection.y == -1) {
            player.sprite.setRegion(player.idleDown);
        }
        else if (player.lastDirection.x == 1) {
            player.sprite.setRegion(player.idleRight);
        }

    }

}
