package entities;

import com.badlogic.gdx.graphics.Texture;
import jokerhut.main.DialogueHandler;
import jokerhut.main.MainScreen;

public class NPC_Guard extends NPC {

    public NPC_Guard (float x, float y, MainScreen screen) {
        super(x, y, screen);
        this.name = "guard";
        this.idlePath = "knightIdle.png";
        this.walkingPath = "knightWalk.png";
        this.portrait = new Texture("knightPortrait.png");
        this.lastDirectionY = 1f;
        setupAnimation(idlePath, walkingPath);
        setupSprite(this.idleUp);
        this.collisionRect.set(
            this.position.x + (sprite.getWidth() - hitboxWidth) / 2f,
            this.position.y,
            hitboxWidth,
            hitboxHeight
        );
        this.dialogueHandler = new DialogueHandler(this, screen);
        setupLines();

    }

    public void setupLines () {

        String[] npcLines = new String[4];
        int i = 0;
        npcLines[i] = "You can not pass";
        i++;
        npcLines[i] = "Move along.";
        i++;
        npcLines[i] = "Stop bothering me...";
        i++;
        npcLines[i] = "You are not permitted to enter";
        i++;

        dialogueHandler.dialogueSets.put("intro", new String[] {
            "You can not pass",
            "Move along.",
            "Stop bothering me..."
        });

        dialogueHandler.dialogueSets.put("general", new String[] {
            "You are not permitted to enter.",
            "top bothering me...",
        });

    }

}
