package debug;

import Constants.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import entities.Entity;
import entities.NPC;
import fx.EffectAnimation;
import jokerhut.main.MainScreen;

public class CollisionDebug {

    public static boolean SHOWPLAYERCOLLISION = true;
    public static boolean SHOWGAMEOBJECTCOLLISION = true;
    public static boolean SHOWSTATICOBJECTCOLLISION = true;
    public static boolean SHOWANIMALCOLLISION = true;
    public static boolean SHOWNPCCOLLISION = true;
    public static boolean SHOWDIALOGUECOLLISION = true;
    public static boolean SHOWFARMABLEDEBUG = true;
    public static boolean SHOWFARMINGRANGEDEBUG = true;
    public static boolean DRAWTILEGRID = true;
    public static boolean DRAWMELEEATTACKZONE = true;

    MainScreen screen;
    ShapeRenderer shapeRenderer;
    BitmapFont font;
    SpriteBatch textBatch;


    public CollisionDebug (MainScreen screen) {
        this.screen = screen;
        shapeRenderer = new ShapeRenderer();
        textBatch = new SpriteBatch();
    }

    public void staticObjectCollisionDebug (Array<Rectangle> rects) {
        shapeRenderer.setProjectionMatrix(screen.mainCamera.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        for (Rectangle rect: rects) {
            shapeRenderer.rect(
                rect.x,
                rect.y,
                rect.width,
                rect.height
            );

        }
        shapeRenderer.end();
    }

    public void playerCollisionDebug () {

        shapeRenderer.setProjectionMatrix(screen.mainCamera.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        shapeRenderer.rect(
            screen.player.getCollisionRect().x,
            screen.player.getCollisionRect().y,
            screen.player.getCollisionRect().width,
            screen.player.getCollisionRect().height
        );

        shapeRenderer.end();
    }



    public void playerDialogueBoxDebug () {
        shapeRenderer.setProjectionMatrix(screen.mainCamera.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        shapeRenderer.rect(
            screen.player.dialogueBox.x,
            screen.player.dialogueBox.y,
            screen.player.dialogueBox.width,
            screen.player.dialogueBox.height
        );

        shapeRenderer.end();
    }

    public void playerMeleeZoneDebug () {
        shapeRenderer.setProjectionMatrix(screen.mainCamera.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.PINK);

        shapeRenderer.rect(
            screen.player.meleeAttackBox.x,
            screen.player.meleeAttackBox.y,
            screen.player.meleeAttackBox.width,
            screen.player.meleeAttackBox.height
        );

        shapeRenderer.end();
    }

    public void drawTileGrid(int mapWidth, int mapHeight, float tileSize) {
        shapeRenderer.setProjectionMatrix(screen.mainCamera.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.DARK_GRAY);

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                shapeRenderer.rect(x, y, 1, 1); // tile is 1x1 in world units
            }
        }

        shapeRenderer.end();
    }

    public void EntityCollisionDebug (Array<? extends Entity> entityArray) {

        shapeRenderer.setProjectionMatrix(screen.mainCamera.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        if (!entityArray.isEmpty()) {
            for (Entity entity : entityArray) {
                if (entity != null) {
                    shapeRenderer.rect(
                        entity.getCollisionRect().x,
                        entity.getCollisionRect().y,
                        entity.getCollisionRect().width,
                        entity.getCollisionRect().height
                    );
                }
            }
        }

        shapeRenderer.end();

    }

}
