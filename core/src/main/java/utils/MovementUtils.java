package utils;

import com.badlogic.gdx.math.MathUtils;
import entities.Entity;
import entities.NPC;
import entities.NPC_OldMan;

public class MovementUtils {

    public static boolean checkIfMoving (Entity entity) {
        if (entity.velocity.x != 0 || entity.velocity.y != 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void applyNormalizedVelocityFromDirection (Entity entity) {
        if (entity instanceof NPC_OldMan) {
        }
        if (entity.intendedDirection.x != 0 || entity.intendedDirection.y != 0) {
            entity.velocity.set(entity.intendedDirection).nor().scl(entity.speed);
        } else {
            entity.velocity.set(0, 0);
        }
    }

    public static void applyStandardMovement (NPC npc) {
        if (npc.actionTimer >= npc.actionDuration) {
            npc.actionTimer = 0;
            npc.moving = !npc.moving;

            if (npc.moving) {
                npc.setIntendedAndLastDirection();
                npc.applyVelocityFromDirection();
                npc.actionDuration = MathUtils.random(1f, 4f);
            } else {
                npc.velocity.set(0, 0); // stop
                npc.actionDuration = MathUtils.random(1f, 2f);
            }
        }
    }


}
