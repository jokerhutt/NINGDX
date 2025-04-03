package objects;

import com.badlogic.gdx.graphics.Texture;

public class OBJ_Weapon extends GameObject{

    public OBJ_Weapon (String name) {
        this.name = name;
        setupWeapon();
        this.image = new Texture(this.imagePath);
        this.type = "weapon";
        setupWeapon();
    }

    public void setupWeapon () {

        switch (this.name) {
            case "stick":
                this.imagePath = "stickWeapon.png";
                this.cost = 5;
                this.count = 1;
                this.countable = false;
                break;
        }

    }

}
