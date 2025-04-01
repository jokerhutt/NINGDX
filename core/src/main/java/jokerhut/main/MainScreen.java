package jokerhut.main;

import Constants.Constants;
import camera.MainCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import debug.CollisionDebug;
import entities.*;
import hud.HUD;
import sound.MusicHandler;

/** First screen of the application. Displayed after the application is created. */
public class MainScreen implements Screen {


    public TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public Array<Rectangle> wallCollisionRects;
    MapLoader mapLoader;
    SpriteBatch batch;
    public MainCamera mainCamera;
    FitViewport viewport;
    public CollisionChecker collisionChecker;
    CollisionDebug collisionDebugger;
    MusicHandler musicHandler;
    public Array<Entity> npcArray;
    public NPC currentNPC;
    public HUD hud;
    public Player player;

    @Override
    public void show() {
        // Prepare your screen here.

        map = new TmxMapLoader().load("ninjatilesmap.tmx");
        mapLoader = new MapLoader(this);
        this.musicHandler = new MusicHandler();
        mainCamera = new MainCamera(this);
        viewport = new FitViewport(Constants.screenWidthInTiles, Constants.screenHeightInTiles, mainCamera.camera);
        renderer = new OrthogonalTiledMapRenderer(map, Constants.SCALE);
        collisionDebugger = new CollisionDebug(this);
        wallCollisionRects = new Array<>();
        wallCollisionRects = mapLoader.createStaticCollisionRects("Collision");
        this.collisionChecker = new CollisionChecker();
        this.npcArray = setupNpcs();
        batch = new SpriteBatch();
        this.currentNPC = (NPC) npcArray.get(3);

        player = new Player(5, 15, this);
        musicHandler.playVillageMusic();
        hud = new HUD(new ScreenViewport(), batch, this);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainCamera.updateCamera(delta);
        player.update(delta);
        updateEntityArrays(delta);
        renderer.setView(mainCamera.camera);
        batch.setProjectionMatrix(mainCamera.camera.combined); // sync batch with camera
        renderer.render();
        batch.begin();
        player.render(batch);
        renderEntityArrays(batch);
        batch.end();
        runScreenDebugMethods();
        hud.render(delta);
        batch.begin();
        if (this.currentNPC != null) {
            hud.drawDialogue(batch);
        }
        batch.end();
    }
    public Array<Entity> setupNpcs () {
        npcArray = new Array<>();
        npcArray.add(new NPC_OldMan(7, 15, this));
        npcArray.add(new NPC_OldMan(9, 13, this));
        npcArray.add(new NPC_Guard(14, 0.5f, this));
        npcArray.add(new NPC_Guard(15, 0.5f, this));
        npcArray.add(new NPC_Gilbert(13.7f, 7f, this));

        return npcArray;
    }

    public void updateEntityArrays (float delta) {

        for (Entity npc : npcArray) {
            if (npc != null) {
                npc.update(delta);
            }
        }

    }

    public void renderEntityArrays (SpriteBatch batch) {

        for (Entity npc : npcArray) {
            if (npc != null) {
                npc.render(batch);
            }
        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hud.resize(width, height);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }

    public void runScreenDebugMethods () {

        if (CollisionDebug.SHOWSTATICOBJECTCOLLISION) {
            collisionDebugger.staticObjectCollisionDebug(wallCollisionRects);
        }
        if (CollisionDebug.SHOWPLAYERCOLLISION) {
            collisionDebugger.playerCollisionDebug();
        }
        if (CollisionDebug.SHOWNPCCOLLISION) {
            collisionDebugger.EntityCollisionDebug(npcArray);
        }
        if (CollisionDebug.SHOWDIALOGUECOLLISION) {
            collisionDebugger.playerDialogueBoxDebug();
        }

        if (CollisionDebug.DRAWTILEGRID) {
            int mapWidth = map.getProperties().get("width", Integer.class);
            int mapHeight = map.getProperties().get("height", Integer.class);
            collisionDebugger.drawTileGrid(mapWidth, mapHeight, 1f);
        }



    }
}
