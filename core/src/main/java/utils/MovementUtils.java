package utils;

import entities.Entity;

public class MovementUtils {

    public static boolean checkIfMoving (Entity entity) {
        if (entity.velocity.x != 0 || entity.velocity.y != 0) {
            return true;
        } else {
            return false;
        }
    }


}
