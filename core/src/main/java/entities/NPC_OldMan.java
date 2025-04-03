package entities;

import com.badlogic.gdx.graphics.Texture;
import jokerhut.main.DialogueHandler;
import jokerhut.main.MainScreen;

public class NPC_OldMan extends NPC{

    public NPC_OldMan (float x, float y, MainScreen screen) {
        super(x, y, screen);
        this.name = "oldMan";
        this.idlePath = "oldManIdle.png";
        this.walkingPath = "oldManWalk.png";
        this.portrait = new Texture("oldManPortrait.png");
        setupAnimation(idlePath, walkingPath);
        setupSprite(idleDown);
        this.lastDirectionY = -1f;
        this.movesOnItsOwn = true;
        this.dialogueHandler = new DialogueHandler(this, screen);
        setupLines();
    }

    public void setupLines () {

        dialogueHandler.dialogueSets.put("intro", new String[] {
            "Eh?",
            "You look new....",
            "You should get something to eat"
        });

        dialogueHandler.dialogueSets.put("general", new String[] {
            "Visit Gilbert at the market.",
            "I have work to do, goodbye.",
        });

    }

}
