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
import cutscenes.CutsceneManager;
import debug.CollisionDebug;
import entities.*;
import hud.HUD;
import sound.MusicHandler;

import java.util.HashMap;
import java.util.Map;

/** First screen of the application. Displayed after the application is created. */
public class MainScreen implements Screen {


    public TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    public Array<Rectangle> wallCollisionRects;
    public Array<Rectangle> enemyBoundaryRects;
    MapLoader mapLoader;
    SpriteBatch batch;
    public MainCamera mainCamera;
    FitViewport viewport;
    public CollisionChecker collisionChecker;
    public CollisionDebug collisionDebugger;
    MusicHandler musicHandler;
    public Array<Entity> npcArray;
    public Array<Enemy> enemyArray;
    public PhysicsHandler physicsHandler;
    public NPC currentNPC;
    public CutsceneManager cutsceneManager;
    public boolean isInDialogue = false;
    public boolean isViewingInventory;
    public boolean paused = false;

    public boolean[][] walkableGrid;

    private final Map<String, NPC> tempNpcMap = new HashMap<>();

    public void setTempNPC(String key, NPC npc) {
        tempNpcMap.put(key, npc);
    }

    public SpriteBatch getBatch () {
        return  this.batch;
    }

    public NPC getTempNPC(String key) {
        return tempNpcMap.get(key);
    }

    public void clearTempNPCs() {
        tempNpcMap.clear();
    }


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
        enemyBoundaryRects = new Array<>();
        enemyBoundaryRects = mapLoader.createStaticCollisionRects("EnemyBoundary");
        this.collisionChecker = new CollisionChecker();
        this.npcArray = setupNpcs();
        this.enemyArray = setupEnemies();
        batch = new SpriteBatch();
        this.physicsHandler = new PhysicsHandler(this);
        player = new Player(5, 15, this);
        musicHandler.playVillageMusic();
        hud = new HUD(new ScreenViewport(), batch, this);
        this.walkableGrid = mapLoader.generateWalkableGrid();
        this.cutsceneManager = new CutsceneManager(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainCamera.updateCamera(delta);
        if (!isInDialogue && !paused) {
            player.update(delta);
            updateEntityArrays(delta);
            player.playerKeyHandler.toggleInventory();
        } else if (isInDialogue){
            isViewingInventory = false;
            player.playerKeyHandler.checkUpdateDialogue();
        } else {
            cutsceneManager.update(delta);
        }
        updateEnemyArrays(delta);
        player.animationHandler.playFxAnimation(delta, player);
        renderer.setView(mainCamera.camera);
        batch.setProjectionMatrix(mainCamera.camera.combined); // sync batch with camera
        renderer.render();
        batch.begin();
        renderEntityArrays(batch);
        renderEnemyArrays(batch);
        player.render(batch);
        if (player.isAttacking) {
            player.animationHandler.renderCurrentAnimation(player, batch);
        }
        batch.end();
        runScreenDebugMethods();
        hud.render(delta);
        batch.begin();
        if (this.currentNPC != null && this.currentNPC.dialogueHandler != null && player.isInDialogue) {
            hud.drawDialogue(currentNPC.dialogueHandler.getCurrentLine(), batch);
        }
        batch.end();
    }



    public Array<Entity> setupNpcs () {
        npcArray = new Array<>();
        npcArray.add(new NPC_OldMan(7, 15, this));
        npcArray.add(new NPC_OldMan(9, 13, this));
        npcArray.add(new NPC_OldMan(10.8f, 2.5f, this));
        npcArray.add(new NPC_OldMan(17, 3, this));
        npcArray.add(new NPC_Guard(14, 0.5f, this));
        npcArray.add(new NPC_Guard(15, 0.5f, this));
        npcArray.add(new NPC_Gilbert(13.7f, 7f, this));

        return npcArray;
    }

    public Array<Enemy> setupEnemies () {
        enemyArray = new Array<>();
        enemyArray.add(new Enemy(3, 7, this));


        return enemyArray;
    }

    public void setPaused (boolean paused) {
        this.paused = paused;
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

    public void updateEnemyArrays (float delta) {

        for (Enemy enemy : enemyArray) {
            if (enemy != null) {
                enemy.update(delta);
            }
        }

    }

    public void renderEnemyArrays (SpriteBatch batch) {

        for (Enemy enemy : enemyArray) {
            if (enemy != null) {
                enemy.render(batch);
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
        if (CollisionDebug.SHOWNPCCOLLISION) {
            collisionDebugger.EntityCollisionDebug(enemyArray);
        }
        if (CollisionDebug.SHOWDIALOGUECOLLISION) {
            collisionDebugger.playerDialogueBoxDebug();
        }

        if (CollisionDebug.DRAWTILEGRID) {
            int mapWidth = map.getProperties().get("width", Integer.class);
            int mapHeight = map.getProperties().get("height", Integer.class);
            collisionDebugger.drawTileGrid(mapWidth, mapHeight, 1f);
        }
        collisionDebugger.playerMeleeZoneDebug();

        for (Entity npc : npcArray) {
            collisionDebugger.entityMeleeZoneDebug(npc);
        }



    }




}
