package jokerhut.main;

import entities.NPC;

import java.util.HashMap;

public class DialogueHandler {

    NPC npc;
    MainScreen screen;
    public int currentLine;
    public HashMap<String, String[]> dialogueSets = new HashMap<>();
    private String[] currentDialogue;

    public boolean hasBeenIntroduced;

    public String[] lines;

    public DialogueHandler (NPC npc, MainScreen screen) {
        this.npc = npc;
        this.screen = screen;
        this.currentLine = -1;
    }

    public void loadDialogueSet(String key, String[] lines) {
        dialogueSets.put(key, lines);
    }

    public void setDialogue(String key) {
        if (dialogueSets.containsKey(key)) {
            this.currentDialogue = dialogueSets.get(key);
            this.currentLine = -1;
        }
    }

    public void startDialogue() {
        currentLine = 0;
    }

    public boolean advanceLine() {
        currentLine++;
        return currentDialogue != null && currentLine < currentDialogue.length;
    }

    public String getCurrentLine() {
        if (currentDialogue != null && currentLine >= 0 && currentLine < currentDialogue.length) {
            return currentDialogue[currentLine];
        }
        return "";
    }

    public boolean isDone () {
        return currentDialogue == null || currentLine >= currentDialogue.length;
    }

}
