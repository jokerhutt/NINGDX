package utils;

import com.badlogic.gdx.math.MathUtils;
import entities.NPC;
import entities.NPC_OldMan;

public class AIUtils {

    public AIUtils () {

    }

    public static void chooseRandomDirection(NPC npc) {
        int i = MathUtils.random(0, 3);
        if (npc instanceof NPC_OldMan) {
            System.out.println("Random direction index: " + i);
        }
        switch (i) {
            case 0:
                npc.intendedDirection.set(1, 0);
                npc.lastDirection.set(1, 0);
                break;
            case 1:
                npc.intendedDirection.set(-1, 0);
                npc.lastDirection.set(-1, 0);

                break;
            case 2:
                npc.intendedDirection.set(0, 1);
                npc.lastDirection.set(0, 1);

                break;
            case 3:
                npc.intendedDirection.set(0, -1);
                npc.lastDirection.set(0, -1);
                break;
        }
        if (npc instanceof NPC_OldMan) {
            System.out.println("Set intendedDirection to: " + npc.intendedDirection);
        }
    }

}
