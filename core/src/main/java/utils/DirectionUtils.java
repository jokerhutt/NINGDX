package utils;

import com.badlogic.gdx.math.Vector2;

public class DirectionUtils {

    public DirectionUtils () {

    }

    public static float calculateMagnitude (float dx, float dy) {

        float xSquared = dx * dx;
        float ySquared = dy * dy;

        float magnitude = (float) Math.sqrt(xSquared + ySquared);
        return magnitude;

    }


    public static Vector2 calculateDiagonalVector (float dx, float dy, float speed) {
        Vector2 scaledVector = new Vector2(dx, dy);
        float magnitude = calculateMagnitude(dx, dy);

        if (magnitude == 0) {
            scaledVector.x = 0;
            scaledVector.y = 0;
            return scaledVector;
        }

        scaledVector.x = (dx / magnitude) * speed;
        scaledVector.y = (dy / magnitude) * speed;


        return scaledVector;
    }

    public static float calculateSpriteOffset (char direction, char axis, float positionX, float positionY, float spriteHeight, float spriteWidth) {

        float handOffsetX = 0;
        float handOffsetY = 0;

        switch (direction) {
            case 'R' :
                handOffsetX = 1.3f;
                handOffsetY = 0.25f;
                break;
            case 'L' :
                handOffsetX = -0.3f;
                handOffsetY = 0.25f;
                break;
            case 'D' :
                handOffsetX = 0.2f;
                handOffsetY = -0.3f;
                break;
            case 'U' :
                handOffsetX = 0.3f;
                handOffsetY = 1.3f;
                break;
        }


        if (axis == 'X') {
            return positionX + handOffsetX - spriteWidth / 2f;
        } else {
            return positionY + handOffsetY - spriteHeight / 2f;
        }


    }

}
