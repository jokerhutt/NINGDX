package cutscenes;

import com.badlogic.gdx.math.Vector2;
import entities.Entity;
import entities.NPC;

public class MoveNPCAction implements CutsceneAction {
    private final Entity entity;
    private final Vector2 targetTile;
    private final float tileSize = 1f; // adjust if your tiles are not 1x1
    private final float speed;

    public MoveNPCAction(Entity npc, int tileX, int tileY, float speed) {
        this.entity = npc;
        this.targetTile = new Vector2(tileX * tileSize, tileY * tileSize);
        this.speed = speed;
        entity.speed = speed;
    }

    @Override
    public boolean update(float delta) {
        entity.actionTimer += delta;
        entity.animationTimer += delta;
        Vector2 center = new Vector2(entity.getCenterX(), entity.getCenterY());

        Vector2 direction = new Vector2(targetTile).sub(center);
        float distance = direction.len();

        if (distance < 0.05f) {
            entity.velocity.setZero();
            entity.moving = false;
            entity.position.set(targetTile);
            entity.updateSpriteAnimation();
            entity.updateMeleeAttackZone();
            entity.collisionRect.set(
                entity.position.x + (entity.sprite.getWidth() - entity.hitboxWidth) / 2f,
                entity.position.y,
                entity.hitboxWidth,
                entity.hitboxHeight
            );
            entity.sprite.setPosition(entity.position.x, entity.position.y);
            return true;
        }

        entity.intendedDirection.set(direction.nor());
        entity.applyVelocityFromDirection();
        entity.applySlidingMovement(entity.velocity, delta);
        entity.updateMeleeAttackZone();
        entity.collisionRect.set(
            entity.position.x + (entity.sprite.getWidth() - entity.hitboxWidth) / 2f,
            entity.position.y,
            entity.hitboxWidth,
            entity.hitboxHeight
        );
        entity.moving = true;
        entity.updateSpriteAnimation();
        entity.sprite.setPosition(entity.position.x, entity.position.y);

        return false;
    }
}
