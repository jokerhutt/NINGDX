package cutscenes;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import entities.NPC_Sensei;
import jokerhut.main.MainScreen;

import java.util.LinkedList;
import java.util.function.Supplier;

public class CutsceneManager {

    public final MainScreen screen;
    private boolean isPlaying = false;

    private final Queue<CutsceneAction> actions = new LinkedList<>();
    private CutsceneAction currentAction = null;

    public final Map<String, Cutscene> cutsceneMap = new HashMap<>();

    public CutsceneManager(MainScreen screen) {
        this.screen = screen;
        cutsceneMap.put("first_old_man", new FirstOldManCutscene(this));
        // Register your cutscenes
//        cutsceneMap.put("player_death", new PlayerDeathCutscene(this));
    }



    public void play(String id) {
        Cutscene cutscene = cutsceneMap.get(id);
        if (cutscene != null) {
            startCutscene();
            cutscene.setup();
        }
    }

    public void startCutscene() {
        isPlaying = true;
        screen.setPaused(true);
    }

    public void update(float delta) {
        if (!isPlaying) return;

        // Handle current action
        if (currentAction == null && !actions.isEmpty()) {
            currentAction = actions.poll();
        }

        if (currentAction != null) {
            boolean done = currentAction.update(delta);
            if (done) {
                currentAction = null;
            }
        }

        // Finish cutscene when all actions complete
        if (currentAction == null && actions.isEmpty()) {
            endCutscene();
        }
    }

    public void endCutscene() {
        isPlaying = false;
        screen.setPaused(false);
        actions.clear();
        currentAction = null;
    }

    public void addAction(CutsceneAction action) {
        actions.add(action);
    }

    // For simple code blocks that just run once
    public void addAction(Runnable runnable) {
        actions.add(new CutsceneAction() {
            boolean ran = false;

            @Override
            public boolean update(float delta) {
                if (!ran) {
                    runnable.run();
                    ran = true;
                }
                return true;
            }
        });
    }

    // For actions that return a CutsceneAction (like MoveNPCAction)
    public void addAction(Supplier<CutsceneAction> supplier) {
        actions.add(new CutsceneAction() {
            CutsceneAction realAction = null;

            @Override
            public boolean update(float delta) {
                if (realAction == null) {
                    realAction = supplier.get();
                }
                return realAction.update(delta);
            }
        });
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
