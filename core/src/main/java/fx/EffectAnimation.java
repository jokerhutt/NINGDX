package fx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class EffectAnimation {

    public Vector2 position;
    public Animation<TextureRegion> animation;

    Texture cutSheet = new Texture("slashCurved.png");

    public float stateTime = 0f;
    public boolean isFinished = false;

    private boolean flippedX = false;
    private boolean flippedY = false;

    public EffectAnimation (Vector2 position, String type) {

        this.position = position;
        setupEffectAnimation(type);

    }

    public void reset(Vector2 newPosition) {
        this.position.set(newPosition);
        this.stateTime = 0f;
        this.isFinished = false;
    }

    public void setupEffectAnimation (String type) {

        Texture sheet;
        sheet = cutSheet;
        switch (type) {

            case "cut" :
                sheet = cutSheet;
                break;
        }

        TextureRegion[][] animationSheet = TextureRegion.split(sheet, 32, 32);

        this.animation = new Animation<>(0.05f, animationSheet[0][0], animationSheet[0][1], animationSheet[0][2], animationSheet[0][3]);

    }



    public void update(float delta) {
        stateTime += delta;
        if (animation.isAnimationFinished(stateTime)) {
            isFinished = true;
        }
    }

    public void render(SpriteBatch batch, Vector2 lastDirection) {

        TextureRegion frame = animation.getKeyFrame(stateTime, false);


        boolean flippedX = false;
        boolean flippedY = false;

        // Flip X if facing left
        if (lastDirection.x < 0 && !frame.isFlipX()) {
            frame.flip(true, false);
            flippedX = true;
        }

        // Flip Y if facing up
        if (lastDirection.y > 0 && !frame.isFlipY()) {
            frame.flip(false, true);
            flippedY = true;
        }

        if (lastDirection.y > 0) {
            batch.draw(
                frame,
                position.x, position.y + 0.1f,  // draw at position
                0, 0,              // origin of rotation (center of sprite)
                1f, 1f,                  // width & height
                1f, 1f,                  // scale
                45f                     // rotation in degrees
            );
        } else if (lastDirection.y < 0) {
            batch.draw(
                frame,
                position.x - 0.6f, position.y - 0.15f,  // draw at position
                0, 0,              // origin of rotation (center of sprite)
                1f, 1f,                  // width & height
                1f, 1f,                  // scale
                270f                     // rotation in degrees
            );
        } else if (lastDirection.x > 0 && lastDirection.y == 0) {
            batch.draw(frame, position.x + 0.4f, position.y - 0.6f, 1f, 1f);
        } else if (lastDirection.x < 0 && lastDirection.y == 0) {
            batch.draw(frame, position.x - 1.4f, position.y - 0.6f, 1f, 1f);
        }

//        if (lastDirection.x == 0) {
//
//            if (lastDirection.y >= 1) {
//                batch.draw(
//                    frame,
//                    position.x, position.y + 0.1f,  // draw at position
//                    0, 0,              // origin of rotation (center of sprite)
//                    1f, 1f,                  // width & height
//                    1f, 1f,                  // scale
//                    45f                     // rotation in degrees
//                );
//            } else {
//                //DOWN
//                batch.draw(
//                    frame,
//                    position.x - 0.6f, position.y - 0.15f,  // draw at position
//                    0, 0,              // origin of rotation (center of sprite)
//                    1f, 1f,                  // width & height
//                    1f, 1f,                  // scale
//                    270f                     // rotation in degrees
//                );
//            }
//
//        } else {
//
//            if (lastDirection.x > 0 && lastDirection.y == 0) {
//                //RIGHT
//                batch.draw(frame, position.x + 0.4f, position.y - 0.6f, 1f, 1f);
//            } else if (lastDirection.y == 0) {
//                //LEFT
//                batch.draw(frame, position.x - 1.4f, position.y - 0.6f, 1f, 1f);
//            }
//
//        }

        if (flippedX) frame.flip(true, false);
        if (flippedY) frame.flip(false, true);

    }



}
