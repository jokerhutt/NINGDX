package jokerhut.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import entities.Entity;
import entities.NPC;
import entities.Player;
import utils.DirectionUtils;

public class KeyHandler {

    Player player;
    MainScreen screen;

    public KeyHandler (Player player, MainScreen screen) {
        this.player = player;
        this.screen = screen;
    }

    public void handlePlayerCardinalMovement () {

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.setVelocityY(player.getSpeed());;
            player.sprite.setRegion(player.walkUp.getKeyFrame(player.animationTimer, true));
            player.setMoving(true);
            player.lastDirection.set(0, 1);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.setVelocityX(player.getSpeed() * -1);;
            player.sprite.setRegion(player.walkLeft.getKeyFrame(player.animationTimer, true));
            player.setMoving(true);
            player.lastDirection.set(-1, 0);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.setVelocityY(player.getSpeed() * -1);
            player.sprite.setRegion(player.walkDown.getKeyFrame(player.animationTimer, true));
            player.setMoving(true);
            player.lastDirection.set(0, -1);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.setVelocityX(player.getSpeed());;
            player.sprite.setRegion(player.walkRight.getKeyFrame(player.animationTimer, true));
            player.setMoving(true);
            player.lastDirection.set(1, 0);
        }

    }

    public boolean handleDiagonalMovement () {

        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.setVelocity(DirectionUtils.calculateDiagonalVector(-1, 1, player.getSpeed()));
            player.sprite.setRegion(player.walkUp.getKeyFrame(player.animationTimer, true));
            player.setMoving(true);
            player.lastDirection.set(0, 1);
            return true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.setVelocity(DirectionUtils.calculateDiagonalVector(1, 1, player.getSpeed()));
            player.sprite.setRegion(player.walkUp.getKeyFrame(player.animationTimer, true));
            player.setMoving(true);
            player.lastDirection.set(0, 1);
            return true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.setVelocity(DirectionUtils.calculateDiagonalVector(-1, -1, player.getSpeed()));
            player.sprite.setRegion(player.walkDown.getKeyFrame(player.animationTimer, true));
            player.setMoving(true);
            player.lastDirection.set(0, -1);
            return true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.setVelocity(DirectionUtils.calculateDiagonalVector(1, -1, player.getSpeed()));
            player.sprite.setRegion(player.walkDown.getKeyFrame(player.animationTimer, true));
            player.setMoving(true);
            player.lastDirection.set(0, -1);
            return true;
        }

        return false;

    }

    public void checkXAndY (Player player) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            System.out.println("X: " + player.position.x + " Y: " + player.position.y);
        }
    }

    public void enterDialogue () {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (!screen.isInDialogue) {
                for (Entity entity : screen.npcArray) {
                    if (entity != null && player.dialogueBox.overlaps(entity.collisionRect)) {
                        screen.isInDialogue = true;
                        screen.currentNPC = (NPC) entity;
                        screen.currentNPC.dialogueHandler.startDialogue();
                        break;
                    }
                }
            }
        }
    }

    public void checkUpdateDialogue () {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            System.out.println("Advancing");
            if (!screen.currentNPC.dialogueHandler.advanceLine()) {
                screen.isInDialogue = false;
                screen.currentNPC.dialogueHandler.currentLine = -1;
                screen.currentNPC = null;
            }
        }
    }



}
