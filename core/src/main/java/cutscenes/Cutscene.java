package cutscenes;

public abstract class Cutscene {
    protected CutsceneManager manager;
    public boolean used;

    public Cutscene(CutsceneManager manager) {
        this.manager = manager;
        this.used = false;
    }

    public abstract void setup();
}
