package entities;

import jokerhut.main.DialogueHandler;
import jokerhut.main.MainScreen;

public class NPC_OldMan extends NPC{

    public NPC_OldMan (float x, float y, MainScreen screen) {
        super(x, y, screen);
        this.name = "oldMan";
        this.idlePath = "oldManIdle.png";
        this.walkingPath = "oldManWalk.png";
        setupAnimation(idlePath, walkingPath);
        setupSprite(idleDown);
        this.lastDirectionY = -1f;
        this.movesOnItsOwn = true;
        this.dialogueHandler = new DialogueHandler(this, screen, setupLines());
    }

    public String[] setupLines () {

        String[] npcLines = new String[4];
        int i = 0;
        npcLines[i] = "Eh?";
        i++;
        npcLines[i] = "You look new...";
        i++;
        npcLines[i] = "You should get something to eat";
        i++;
        npcLines[i] = "Visit Gilbert at the market";
        i++;

        return npcLines;

    }

}
