package utils;

import entities.Entity;
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
            System.out.println(entity.intendedDirection.x + ", " + entity.intendedDirection.y);
        }
        if (entity.intendedDirection.x != 0 || entity.intendedDirection.y != 0) {
            entity.velocity.set(entity.intendedDirection).nor().scl(entity.speed);
        } else {
            entity.velocity.set(0, 0);
        }
    }


}
