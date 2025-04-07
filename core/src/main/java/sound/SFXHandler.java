package sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SFXHandler {

    Sound swingSound;

    public SFXHandler () {
        swingSound = Gdx.audio.newSound(Gdx.files.internal("swingweapon.wav"));
    }

    public void playSound (String type) {

        switch (type) {

            case "swingweapon" :
                swingSound.play();
                break;

        }

    }

}
