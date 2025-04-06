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
            if (!player.isAttacking) {
                player.sprite.setRegion(player.walkUp.getKeyFrame(player.animationTimer, true));

            }
            player.setMoving(true);
            player.lastDirection.set(0, 1);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.setVelocityX(player.getSpeed() * -1);;
            if (!player.isAttacking) {
                player.sprite.setRegion(player.walkLeft.getKeyFrame(player.animationTimer, true));

            }
            player.setMoving(true);
            player.lastDirection.set(-1, 0);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.setVelocityY(player.getSpeed() * -1);
            if (!player.isAttacking) {
                player.sprite.setRegion(player.walkDown.getKeyFrame(player.animationTimer, true));

            }
            player.setMoving(true);
            player.lastDirection.set(0, -1);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.setVelocityX(player.getSpeed());;
            if (!player.isAttacking) {
                player.sprite.setRegion(player.walkRight.getKeyFrame(player.animationTimer, true));

            }
            player.setMoving(true);
            player.lastDirection.set(1, 0);
        }

    }

    public boolean handleSettingCardinalMovement () {

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.intendedDirection.set(0, 1);
            player.lastDirection.set(0, 1);
            return true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.intendedDirection.set(-1, 0);
            player.lastDirection.set(-1, 0);
            return true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.intendedDirection.set(0, -1);
            player.lastDirection.set(0, -1);
            return true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.intendedDirection.set(1, 0);
            player.lastDirection.set(1, 0);
            return true;
        }


        return false;

    }

    public boolean handleSettingDiagonalMovement () {

        if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.intendedDirection.set(-1, 1);
            player.lastDirection.set(0, 1);
            return true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.intendedDirection.set(1, 1);
            player.lastDirection.set(0, 1);
            return true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.intendedDirection.set(-1, -1);
            player.lastDirection.set(0, -1);
            return true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.intendedDirection.set(1, -1);
            player.lastDirection.set(0, -1);
            return true;
        }

        return false;

    }

    public void handleAttacking ( float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (!player.isAttacking && !player.isInAttackCoolDown
                && player.inventory.currentItem != null
                && player.inventory.currentItem.type.equals("weapon")) {
                player.isAttacking = true;
            }
        }
        if (player.isAttacking) {
            player.attackingTimer++;
            if (player.attackingTimer >= 10) {
                player.isAttacking = false;
                player.attackingTimer = 0;
                player.isInAttackCoolDown = true;
            }
        }

        if (player.isInAttackCoolDown) {
            player.attackingCooldown++;
            if (player.attackingCooldown >= 10) {
                player.isInAttackCoolDown = false;
                player.attackingCooldown = 0;
            }
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
                        if (!screen.currentNPC.dialogueHandler.hasBeenIntroduced) {
                            screen.currentNPC.dialogueHandler.setDialogue("intro");
                        } else {
                            screen.currentNPC.dialogueHandler.setDialogue("general");
                        }
                        screen.currentNPC.dialogueHandler.startDialogue();
                        break;
                    }
                }
            }
        }
    }

    public void toggleInventory () {
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            screen.isViewingInventory = !screen.isViewingInventory;
        }
    }

    public void checkUpdateDialogue () {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            System.out.println("Advancing");
            if (!screen.currentNPC.dialogueHandler.advanceLine()) {
                if (!screen.currentNPC.dialogueHandler.hasBeenIntroduced) {
                    screen.currentNPC.dialogueHandler.hasBeenIntroduced = true;
                }
                if (screen.currentNPC.type == "merchant" && !screen.currentNPC.isInPurchaseScreen) {
                    screen.currentNPC.isInPurchaseScreen = true;
                } else if (screen.isInDialogue && screen.currentNPC.isInPurchaseScreen) {
                    screen.currentNPC.isInPurchaseScreen = false;
                    screen.isInDialogue = false;
                    screen.currentNPC.dialogueHandler.currentLine = -1;
                    screen.currentNPC = null;
                } else {
                    screen.isInDialogue = false;
                    screen.currentNPC.dialogueHandler.currentLine = -1;
                    screen.currentNPC = null;
                }
            }
        }
    }



}
