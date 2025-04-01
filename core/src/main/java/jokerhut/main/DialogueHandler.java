package jokerhut.main;

import entities.NPC;

public class DialogueHandler {

    NPC npc;
    MainScreen screen;
    public int currentLine;

    private String[] lines = {
        "Move along now",
        "You can not pass",
        "Stop bothering me."
    };

    public DialogueHandler (NPC npc, MainScreen screen) {
        this.npc = npc;
        this.screen = screen;
        this.currentLine = -1;
    }

    public void startDialogue() {
        currentLine = 0;
    }

    public boolean advanceLine() {
        currentLine++;
        return currentLine < lines.length;
    }

    public String getCurrentLine() {
        if (currentLine >= 0 && currentLine < lines.length) {
            return lines[currentLine];
        }
        return "";
    }

}
