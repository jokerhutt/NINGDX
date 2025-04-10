package cutscenes;

import entities.NPC_OldMan;
import entities.NPC_Sensei;
import jokerhut.main.MainScreen;

public class FirstOldManCutscene extends Cutscene {



    public FirstOldManCutscene(CutsceneManager manager) {
        super(manager);

    }

    @Override
    public void setup() {
        MainScreen screen = manager.screen;

        // Spawn Sensei
        manager.addAction(() -> {
            NPC_Sensei oldMan = new NPC_Sensei(8, 5, screen);
            screen.npcArray.add(oldMan);
            screen.setTempNPC("oldMan", oldMan);
        });

        // Move Sensei to battle position
        manager.addAction(() -> {
            NPC_Sensei oldMan = (NPC_Sensei) screen.getTempNPC("oldMan");
            return new MoveNPCAction(oldMan, (int) (screen.player.position.x + 2), (int) screen.player.position.y - 2, 1.5f);
        });

        // Start chasing enemies
        manager.addAction(() -> {
            NPC_Sensei sensei = (NPC_Sensei) screen.getTempNPC("oldMan");
            sensei.chasing = true;
        });

        // Wait until enemy is dead (or gone)
        manager.addAction(new CutsceneAction() {
            @Override
            public boolean update(float delta) {
                NPC_Sensei sensei = (NPC_Sensei) screen.getTempNPC("oldMan");
                sensei.update(delta); // Chase + attack

                return sensei.lockedOnto == null; // Cutscene continues once he’s done
            }
        });

        manager.addAction(() -> {
            enterCutsceneDialogue();
        });

        // Move both Sensei and Player to (8, 5)
        manager.addAction(() -> {
            NPC_Sensei sensei = (NPC_Sensei) screen.getTempNPC("oldMan");
            return new MoveNPCAction(sensei, 10, 5, 1.5f);
        });

        manager.addAction(() -> {
            return new MoveNPCAction(screen.player, 9, 5, 1.5f); // Place player nearby
        });



        // Resume game
        manager.addAction(() -> {
            screen.setPaused(false);
            screen.player.isAlive = true;
            this.used = true;
        });
    }

    public void enterCutsceneDialogue () {
        NPC_Sensei sensei = (NPC_Sensei) manager.screen.getTempNPC("oldMan");

        manager.screen.isInDialogue = true;
        manager.screen.currentNPC = sensei;

        if (!sensei.dialogueHandler.hasBeenIntroduced) {
            sensei.dialogueHandler.setDialogue("intro");
        } else {
            sensei.dialogueHandler.setDialogue("general");
        }

        sensei.dialogueHandler.startDialogue();
    }

}

