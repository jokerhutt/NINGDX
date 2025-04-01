package jokerhut.main;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import entities.Entity;
import entities.Player;

public class CollisionChecker {

    public boolean checkStaticObjectCollision (Array<Rectangle> collisionRs, Entity callingEntity) {

        boolean doesCollide = false;
        for (Rectangle rect : collisionRs) {
            if (rect.overlaps(callingEntity.collisionRect)) {
                doesCollide = true;
                break;
            }
        }
        return doesCollide;

    }

    public boolean checkEntityCollision (Array<Entity> entities, Entity callingEntity) {

        boolean doesCollide = false;
        for (Entity entity : entities) {
            if (entity.collisionRect.overlaps(callingEntity.collisionRect)) {
                doesCollide = true;
                break;
            }
        }
        return doesCollide;
    }

    public boolean checkEntityCollisionWithPlayer (Entity entity, Entity player) {

        boolean doesCollide = false;

        if (player.collisionRect.overlaps(entity.collisionRect)) {
            return true;
        }

        return doesCollide;
    }

    public void checkEntityDialogueCollision (Array<Entity> entities, Player player) {
        for (Entity entity : entities) {
            if (entity != null) {
                if (entity.collisionRect.overlaps(player.dialogueBox)) {
                    entity.isInteracting = true;
                    break;
                } else {
                    entity.isInteracting = false;
                }
            }
        }
    }

}
