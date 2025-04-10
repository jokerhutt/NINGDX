package movement;

import entities.NPC;

public class MovementDirector {
    public static void updateStrategy(NPC npc) {
        if (npc.isAttacking) {
            npc.setMovementStrategy(MovementStrategies.NONE);
        } else if (npc.chasing) {
            npc.setMovementStrategy(MovementStrategies.CHASE);
        } else {
            npc.setMovementStrategy(MovementStrategies.IDLE);
        }
    }


}
