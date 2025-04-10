package misc;

import aiBehavior.AIUtils;
import entities.NPC;
import utils.MovementUtils;

public interface MovementStrategy {
    void update (NPC npc, float delta);
}

class MovementStrategies {
    public static final MovementStrategy IDLE = new IdleWanderStrategy();
    public static final MovementStrategy CHASE = new ChaseEnemyStrategy();
}

//THESE CLASSES FUNCTION AS LIKE METHOD WRAPPERS

class ChaseEnemyStrategy implements MovementStrategy {
    @Override
    public void update(NPC npc, float delta) {
        AIUtils.determineNearestEnemy(npc);
        AIUtils.handleChasing(delta, npc);
    }
}

class IdleWanderStrategy implements MovementStrategy {
    @Override
    public void update(NPC npc, float delta) {
        MovementUtils.applyStandardMovement(npc);
    }
}


