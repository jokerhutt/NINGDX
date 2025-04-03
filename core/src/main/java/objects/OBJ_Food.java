package objects;

import com.badlogic.gdx.graphics.Texture;

public class OBJ_Food extends GameObject {

    public OBJ_Food (String name) {
        this.name = name;
        setupFood();
        this.image = new Texture(this.imagePath);
        this.type = "consumable";
        this.count = 1;
    }

    public void setupFood () {

        switch (this.name) {
            case "shrimp":
                this.imagePath = "Shrimp.png";
                this.cost = 1;
                break;
            case "sushi":
                this.imagePath = "Sushi.png";
                this.cost = 2;
                break;
            case "honey":
                this.imagePath = "Honey.png";
                this.cost = 4;
                break;
            case "onigiri":
                this.imagePath = "Onigiri.png";
                this.cost = 1;
                break;
            case "tealeaf":
                this.imagePath = "TeaLeaf.png";
                this.cost = 2;
                break;
            case "fish":
                this.imagePath = "Fish.png";
                this.cost = 4;
                break;
        }

    }

}
