package cutscenes;

public abstract class Cutscene {
    protected CutsceneManager manager;

    public Cutscene(CutsceneManager manager) {
        this.manager = manager;
    }

    public abstract void setup();
}
