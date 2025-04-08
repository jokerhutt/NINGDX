package cutscenes;

import com.badlogic.gdx.math.Vector2;
import entities.Entity;
import entities.NPC;

public class MoveNPCAction implements CutsceneAction {
    private final NPC npc;
    private final Vector2 targetTile;
    private final float tileSize = 1f; // adjust if your tiles are not 1x1
    private final float speed;

    public MoveNPCAction(NPC npc, int tileX, int tileY, float speed) {
        this.npc = npc;
        this.targetTile = new Vector2(tileX * tileSize, tileY * tileSize);
        this.speed = speed;
        npc.speed = speed;
    }

    @Override
    public boolean update(float delta) {
        npc.actionTimer += delta;
        Vector2 center = new Vector2(npc.getCenterX(), npc.getCenterY());

        Vector2 direction = new Vector2(targetTile).sub(center);
        float distance = direction.len();

        if (distance < 0.05f) {
            npc.velocity.setZero();
            npc.moving = false;
            npc.position.set(targetTile);
            npc.updateSpriteAnimation();
            npc.updateMeleeAttackZone();
            npc.collisionRect.set(
                npc.position.x + (npc.sprite.getWidth() - npc.hitboxWidth) / 2f,
                npc.position.y,
                npc.hitboxWidth,
                npc.hitboxHeight
            );
            npc.sprite.setPosition(npc.position.x, npc.position.y);
            return true;
        }

        npc.intendedDirection.set(direction.nor());
        npc.applyVelocityFromDirection();
        npc.applySlidingMovement(npc.velocity, delta);
        npc.updateSpriteAnimation();
        npc.updateMeleeAttackZone();
        npc.collisionRect.set(
            npc.position.x + (npc.sprite.getWidth() - npc.hitboxWidth) / 2f,
            npc.position.y,
            npc.hitboxWidth,
            npc.hitboxHeight
        );
        npc.moving = true;
        npc.updateSpriteAnimation();
        npc.sprite.setPosition(npc.position.x, npc.position.y);

        return false;
    }
}
