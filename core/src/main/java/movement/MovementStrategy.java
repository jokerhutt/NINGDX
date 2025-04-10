package movement;

import aiBehavior.AIUtils;
import entities.NPC;
import utils.MovementUtils;

public interface MovementStrategy {
    void update (NPC npc, float delta);
}





