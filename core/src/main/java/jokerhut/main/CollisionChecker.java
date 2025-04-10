package jokerhut.main;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import entities.Enemy;
import entities.Entity;
import entities.NPC_Sensei;
import entities.Player;

public class CollisionChecker {

    public boolean checkStaticObjectCollision (Array<Rectangle> collisionRs, Entity callingEntity) {

        boolean doesCollide = false;
        for (Rectangle rect : collisionRs) {
            if (rect.overlaps(callingEntity.collisionRect)) {
                if (callingEntity instanceof Player) {
                    System.out.println("COOLISION");
                }

                doesCollide = true;
                break;
            }
        }
        return doesCollide;

    }

    public boolean checkStaticObjectCollision (Array<Rectangle> collisionRs, Rectangle testRect) {

        boolean doesCollide = false;
        for (Rectangle rect : collisionRs) {
            if (rect.overlaps(testRect)) {
                doesCollide = true;
                break;
            }
        }
        return doesCollide;

    }

    public boolean checkEntityCollision(Array<? extends Entity> entities, Entity callingEntity) {
        boolean doesCollide = false;

        for (int i = 0; i < entities.size; i++) {
            Entity entity = entities.get(i);
            if (entity == callingEntity) {
                continue;
            }

            if (entity.collisionRect.overlaps(callingEntity.collisionRect)) {
                if (entity instanceof Enemy && callingEntity instanceof Player) {
                    ((Enemy) entity).performAttack((Player) callingEntity);
                }
//                if (callingEntity instanceof Enemy && entity instanceof NPC_Sensei) {
//                    ((NPC_Sensei) entity).performAttack((Enemy) callingEntity);
//                }
                doesCollide = true;
                break;
            }
        }

        return doesCollide;
    }

    public boolean checkEntityCollisionWithPlayer (Entity entity, Player player) {

        boolean doesCollide = false;

        if (player.collisionRect.overlaps(entity.collisionRect)) {
            if (entity instanceof Enemy) {
                ((Enemy) entity).performAttack(player);
            }
            return true;
        }

        return doesCollide;
    }

    public boolean isKnockbackCollidingOnAxis(Vector2 knockVec, float delta, Entity entity) {

        Vector2 testPos = entity.position.cpy().add(knockVec.x * delta, knockVec.y * delta);

        entity.collisionRect.set(
            testPos.x + (entity.sprite.getWidth() - entity.hitboxWidth) / 2f,
            testPos.y,
            entity.hitboxWidth,
            entity.hitboxHeight
        );

        return entity.checkAllCollisions();
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

    public void checkAttackCollision (Array<? extends Entity> targetEnemyArray, Entity callingEntity) {
        for (Entity targetEnemy : targetEnemyArray) {

            if (callingEntity.meleeAttackBox.overlaps(targetEnemy.hitboxRectangle)) {
                if (callingEntity instanceof Player && callingEntity.isAttacking) {
                    targetEnemy.takeDamage(1, callingEntity);
                } else if (callingEntity instanceof NPC_Sensei && targetEnemy instanceof Enemy) {
                    System.out.println("collidieng");
                    ((NPC_Sensei) callingEntity).performAttack((Enemy) targetEnemy);
                }
            }

        }

    }

}
