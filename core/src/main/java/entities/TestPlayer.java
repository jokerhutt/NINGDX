//package entities;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.math.Vector2;
//import jokerhut.main.MainScreen;
//import utils.MovementUtils;
//
//public class TestPlayer extends Entity{
//
//    public Vector2 intendedDirection;
//    public Vector2 lastDirection;
//    public Vector2 futurePosition;
//
//    public boolean isMoving;
//    MainScreen screen;
//    Vector2 knockback;
//    float knockbackTime;
//    boolean isBeingKnockedBack;
//
//
//
//    public TestPlayer (float x, float y, MainScreen screen) {
//        super(x, y);
//        this.screen = screen;
//        this.velocity = new Vector2(0, 0);
//        this.position = new Vector2(x, y);
//        this.intendedDirection = new Vector2(0, 0);
//        this.lastDirection = new Vector2(0, 0);
//        this.knockback = new Vector2(0, 0);
//        this.futurePosition = new Vector2(0, 0);
//        this.isBeingKnockedBack = true;
//        this.isMoving = false;
//        sprite = new Sprite();
//        sprite.setSize(1f, 1f);
//        sprite.setPosition(this.position.x, this.position.y);
//
//
//    }
//
//    public void setFutureCollisionRect (float futureX, float futureY) {
//        collisionRect.set(
//            futureX + (sprite.getWidth() - hitboxWidth) / 2f,
//            futureY,
//            hitboxWidth,
//            hitboxHeight
//        );
//    }
//
//    public void update (float delta) {
//        handleMovement(delta);
//    }
//
//    public void handleMovement (float delta) {
//
//        handleInput();
//        applyVelocityFromIntendedDirection();
//
//        if (isBeingKnockedBack) {
//            handleKnockback(delta);           // 3a. Handle knockback movement
//        } else {
//            applySlidingMovement(velocity, delta); // 3b. Regular movement with sliding
//        }
//
//        updateSpriteAnimation();
//        sprite.setPosition(position.x, position.y);
//
//
//    }
//
//
//
//    public void updateSpriteAnimation () {
//
//        isMoving = MovementUtils.checkIfMoving(this);
//
//        if (isMoving) {
//
//            if (velocity.y > 0) {
//                sprite.setRegion(walkUp.getKeyFrame(animationTimer, true));
//            } else if (velocity.y < 0) {
//                sprite.setRegion(walkDown.getKeyFrame(animationTimer, true));
//            } else if (velocity.x > 0) {
//                sprite.setRegion(walkRight.getKeyFrame(animationTimer, true));
//            } else if (velocity.x < 0) {
//                sprite.setRegion(walkLeft.getKeyFrame(animationTimer, true));
//            }
//
//        } else {
//            if (velocity.y == 1) {
//                sprite.setRegion(idleUp);
//            }
//            else if (velocity.x == -1) {
//                sprite.setRegion(idleLeft);
//            }
//            else if (velocity.y == -1) {
//                sprite.setRegion(idleDown);
//            }
//            else if (velocity.x == 1) {
//                sprite.setRegion(idleRight);
//            }
//        }
//
//    }
//
//    public boolean applySlidingMovement(Vector2 moveVec, float delta) {
//        Vector2 move = moveVec.cpy().scl(delta);
//        boolean moved = false;
//
//        if (!willCollideAt(position.x + move.x, position.y + move.y)) {
//            position.add(move);
//            moved = true;
//        } else {
//            if (!willCollideAt(position.x + move.x, position.y)) {
//                position.x += move.x;
//                moved = true;
//            }
//            if (!willCollideAt(position.x, position.y + move.y)) {
//                position.y += move.y;
//                moved = true;
//            }
//        }
//
//        return moved;
//    }
//
//    public void handleInput () {
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
//            intendedDirection.set(0, 1);
//        } else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
//            intendedDirection.set(-1, 0);
//        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
//            intendedDirection.set(0, -1);
//        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
//            intendedDirection.set(1, 0);
//        }
//
//    }
//
//    public void applyVelocityFromIntendedDirection () {
//        velocity.set(intendedDirection);
//    }
//
//    public void handleKnockback(float delta) {
//
//        boolean moved = applySlidingMovement(knockback, delta);
//
//        if (moved) {
//            knockbackTime -= delta;
//        }
//
//        if (knockbackTime <= 0f) {
//            knockback.set(0, 0);
//            knockbackTime = 0f;
//            isBeingKnockedBack = false;
//        }
//    }
//
//    public boolean willCollideAt(float testX, float testY) {
//        setFutureCollisionRect(testX, testY);
//        for (Rectangle wall : screen.wallCollisionRects) {
//            if (collisionRect.overlaps(wall)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void applyKnockBack (Entity attacker, float strength) {
//        Vector2 knockbackDirection = this.position.cpy().sub(attacker.position).nor();
//        knockback.set(knockbackDirection.scl(strength));
//    }
//
//}
