package entities;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import jokerhut.main.DialogueHandler;
import jokerhut.main.MainScreen;
import objects.GameObject;
import objects.OBJ_Food;

public class NPC_Gilbert extends NPC {


    public NPC_Gilbert (float x, float y, MainScreen screen) {
        super(x, y, screen);
        this.name = "gilbert";
        this.type = "merchant";
        this.idlePath = "gilbertIdle.png";
        this.walkingPath = "gilbertWalk.png";
        this.portrait = new Texture("gilbertPortrait.png");
        setupAnimation(idlePath, walkingPath);
        setupSprite(idleDown);
        this.lastDirectionY = -1f;
        this.movesOnItsOwn = false;
        this.collisionRect.set(
            this.position.x + (sprite.getWidth() - hitboxWidth) / 2f,
            this.position.y,
            hitboxWidth,
            hitboxHeight
        );
        this.inventory = new GameObject[12];
        addItemsForSale();
        this.dialogueHandler = new DialogueHandler(this, screen);
        setupLines();
    }

    public void setupLines () {

        dialogueHandler.dialogueSets.put("intro", new String[] {
            "Hello there!",
            "I am Gilbert the merchant.",
            "Come visit my shop anytime."
        });

        dialogueHandler.dialogueSets.put("general", new String[] {
            "The weather's nice today.",
            "Heard any good rumors lately?",
            "Be careful out there."
        });

    }

    public void addItemsForSale () {

        this.inventory[0] = new OBJ_Food("shrimp");
        this.inventory[1] = new OBJ_Food("sushi");
        this.inventory[2] = new OBJ_Food("honey");
        this.inventory[3] = new OBJ_Food("onigiri");
        this.inventory[4] = new OBJ_Food("tealeaf");
        this.inventory[5] = new OBJ_Food("fish");

    }




}
