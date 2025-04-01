package jokerhut.main;

import entities.NPC;

public class DialogueHandler {

    NPC npc;
    MainScreen screen;
    public int currentLine;

    public String[] lines;

    public DialogueHandler (NPC npc, MainScreen screen, String[] lines) {
        this.npc = npc;
        this.screen = screen;
        this.currentLine = -1;
        this.lines = lines;
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
