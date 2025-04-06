package jokerhut.main;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import debug.CollisionDebug;
import entities.Player;
import fx.EffectAnimation;
import objects.GameObject;

public class AnimationHandler {
    Player player;
    Animation cutAnimation;
    Array<EffectAnimation> effectAnimationArray;



    public AnimationHandler (Player player) {

        this.player = player;
        effectAnimationArray = setupFxActionArray();

    }

    public Array<EffectAnimation> setupFxActionArray () {

        Array<EffectAnimation> actionsArray = new Array<>();
        actionsArray.add(new EffectAnimation(new Vector2(player.getCenterX(), player.getCenterY()), "cut"));

        return actionsArray;

    }

    public void playFxAnimation(float delta) {
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

    public void renderCurrentAnimation (SpriteBatch batch) {
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




    public void handleIdleAnimation () {

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
