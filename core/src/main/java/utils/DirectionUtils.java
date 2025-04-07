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

    public static Vector2 subtractVectors (Vector2 vOne, Vector2 vTwo) {
        return new Vector2(vOne.x - vTwo.x, vOne.y - vTwo.y);
    }

    public static float findDistance (Vector2 destination, Vector2 origin) {
        Vector2 distance = subtractVectors(destination, origin);
        return calculateMagnitude(distance.x, distance.y);

    }




}
