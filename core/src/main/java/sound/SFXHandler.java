package sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SFXHandler {

    Sound swingSound;
    Sound hitSound;

    public SFXHandler () {
        swingSound = Gdx.audio.newSound(Gdx.files.internal("swingweapon.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("impact4.wav"));
    }

    public void playSound (String type) {

        switch (type) {

            case "swingweapon" :
                swingSound.play();
                break;
            case "impact" :
                hitSound.play();
                break;

        }

    }

}
