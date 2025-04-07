package entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EmoteHandler {

    Entity entity;

    public EmoteHandler (Entity entity) {
        this.entity = entity;
    }

    public void drawSpeechBubble (SpriteBatch batch) {
        if (entity.isInteracting) {
            float bubbleWidth = 0.5f;
            float bubbleHeight = 0.5f;

            float bubbleX = entity.sprite.getX() + entity.sprite.getWidth() / 2.2f - bubbleWidth / 2f;
            float bubbleY = entity.sprite.getY() + (entity.sprite.getHeight() / 1f);

            batch.draw(entity.speechBubble, bubbleX, bubbleY, bubbleWidth, bubbleHeight);
        }
        if (entity.isEmoting) {
            float bubbleWidth = 0.5f;
            float bubbleHeight = 0.5f;

            float bubbleX = entity.sprite.getX() + entity.sprite.getWidth() / 2.2f - bubbleWidth / 2f;
            float bubbleY = entity.sprite.getY() + (entity.sprite.getHeight() / 1f);

            batch.draw(entity.happyEmote, bubbleX, bubbleY, bubbleWidth, bubbleHeight);
        }
    }

    public void runEmoting () {
        if (entity.isEmoting) {
            System.out.println(entity.emoteTimer);
            if (entity.emoteTimer >= 100f) {
                entity.isEmoting = false;
                entity.emoteTimer = 0f;
            } else {
                entity.emoteTimer++;
            }
        }
    }


}
