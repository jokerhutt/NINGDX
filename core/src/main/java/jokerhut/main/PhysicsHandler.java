package jokerhut.main;

import com.badlogic.gdx.math.Vector2;
import entities.Entity;

public class PhysicsHandler {

    MainScreen screen;

    public PhysicsHandler (MainScreen screen) {

        this.screen = screen;

    }

    public void applyKnockback(Vector2 from, float strength, float duration, Entity entity) {
        Vector2 direction = new Vector2(entity.position).sub(from).nor();
        direction.scl(1f);

        entity.knockback.set(direction.scl(strength));
        entity.knockbackTime = duration;
        System.out.println("Knockback from: " + from + " to " + entity.position);
        System.out.println("Knockback direction: " + entity.knockback);
    }

    public void handleKnockback (float delta, Entity entity) {
        if (entity.knockbackTime > 0f) {
            boolean moved = false;

            Vector2[] directionsToTry = entity.getKnockbackDirections();

            for (Vector2 dir : directionsToTry) {
                if (!screen.collisionChecker.isKnockbackCollidingOnAxis(dir, delta, entity)) {
                    entity.position.x += dir.x * delta;
                    entity.position.y += dir.y * delta;
                    System.out.println("Sliding direction: " + dir);
                    moved = true;
                    break;
                }
            }

            if (moved) {
                entity.knockbackTime -= delta;
            }

            if (entity.knockbackTime <= 0f) {
                entity.knockbackTime = 0f;
                entity.knockback.setZero();
            }

            entity.collisionRect.set(
                entity.position.x + (entity.sprite.getWidth() - entity.hitboxWidth) / 2f,
                entity.position.y,
                entity.hitboxWidth,
                entity.hitboxHeight
            );
            entity.hitboxRectangle.set(entity.sprite.getX(), entity.sprite.getY(), entity.sprite.getWidth(), entity.sprite.getHeight());
            entity.sprite.setPosition(entity.position.x, entity.position.y);
        }
    }

}
