package entities;

import com.badlogic.gdx.graphics.Texture;
import jokerhut.main.DialogueHandler;
import jokerhut.main.MainScreen;

public class NPC_Sensei extends NPC {

    public NPC_Sensei (float x, float y, MainScreen screen) {
        super(x, y, screen);
        this.name = "sensei";
        this.idlePath = "senseiIdle.png";
        this.walkingPath = "senseiWalk.png";
        this.portrait = new Texture("senseiPortrait.png");
        setupAnimation(idlePath, walkingPath);
        setupSprite(idleDown);
        this.lastDirectionY = -1f;
        this.movesOnItsOwn = true;
        this.dialogueHandler = new DialogueHandler(this, screen);
        setupLines();
    }

    public void performAttack (Enemy targetEnemy) {
        System.out.println("attacking " + isAttacking);
        System.out.println("cooldown " + attackingCooldown);
        if (!isAttacking && attackingCooldown <= 0) {
            isAttacking = true;
            targetEnemy.takeDamage(1, this);
        }
    }

    public void handleActions (float delta) {
        handleAttackCooldown();
        screen.physicsHandler.handleKnockback(delta, this);
    }

    public void setupLines () {

        dialogueHandler.dialogueSets.put("intro", new String[] {
            "Well that was easy",
            "Come with me",
            "It's not safe here"
        });

        dialogueHandler.dialogueSets.put("general", new String[] {
            "What?",
            "Come with me.",
        });

    }

    @Override
    public void updateMeleeAttackZone () {
        float offset = 0.2f;
        float boxSize = 1f;

        float dx = 0;
        float dy = 0;

        if (lastDirection.x > 0) dx = offset;
        if (lastDirection.x < 0) dx = -offset;
        if (lastDirection.y > 0) dy = offset;
        if (lastDirection.y < 0) dy = -offset;

        meleeAttackBox.set(
            getCenterX() + dx - boxSize / 1.8f,
            (getCenterY() + dy - boxSize / 1.8f),
            boxSize + 0.2f,
            boxSize + 0.2f
        );
    }

}
