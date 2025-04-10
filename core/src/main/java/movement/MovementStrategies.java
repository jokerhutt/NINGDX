package movement;

import aiBehavior.AIUtils;
import entities.NPC;
import utils.MovementUtils;

public class MovementStrategies {

    //Lambda time B)
    //THESE CLASSES FUNCTION AS LIKE METHOD WRAPPERS
    public static final MovementStrategy IDLE = (NPC npc, float delta) -> {
        MovementUtils.applyStandardMovement(npc);
    };

    public static final MovementStrategy CHASE = (NPC npc, float delta) -> {
        AIUtils.determineNearestEnemy(npc);
        AIUtils.handleChasing(delta, npc);
    };

    public static final MovementStrategy NONE = (NPC npc, float delta) -> {

    };

}
