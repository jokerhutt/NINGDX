package entities;

import com.badlogic.gdx.graphics.Texture;
import jokerhut.main.DialogueHandler;
import jokerhut.main.MainScreen;

public class NPC_Sensei extends NPC {

    public NPC_Sensei (float x, float y, MainScreen screen) {
        super(x, y, screen);
        this.name = "oldMan";
        this.idlePath = "senseiIdle.png";
        this.walkingPath = "senseiWalk.png";
        this.portrait = new Texture("oldManPortrait.png");
        setupAnimation(idlePath, walkingPath);
        setupSprite(idleDown);
        this.lastDirectionY = -1f;
        this.movesOnItsOwn = true;
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

}
