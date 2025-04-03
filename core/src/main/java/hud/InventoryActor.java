    package hud;

    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.TextureRegion;
    import com.badlogic.gdx.scenes.scene2d.ui.Image;
    import com.badlogic.gdx.scenes.scene2d.ui.Table;
    import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
    import com.badlogic.gdx.utils.Scaling;
    import jokerhut.main.MainScreen;
    import objects.GameObject;


    public class InventoryActor extends Table {

        MainScreen screen;
        private Texture squareTexture;
        private final Texture choiceBoxTexture;
        public BoxWithText textBox;

        public InventoryActor(MainScreen screen) {
            this.squareTexture = new Texture("brownSquare.png");
            this.choiceBoxTexture = new Texture("choiceBox.png");
            this.screen = screen;
            TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(squareTexture));
            TextureRegionDrawable choiceDrawable = new TextureRegionDrawable(new TextureRegion(choiceBoxTexture));

            this.center().right().pad(20);
            this.setDebug(true); // Show layout boxes while testing

            textBox = new BoxWithText("choiceBox.png", "YOUR INVENTORY", 2f);
            this.add(textBox).size(320, 100).colspan(3).center(); // <-- the key fix

            this.row().padTop(10); // Add spacing above the choice box
            // Add 6 squares in a 3-column layout (2 rows)
            for (int i = 0; i < 12; i++) {
                GameObject item = (i < screen.player.inventory.inventoryArray.length) ? screen.player.inventory.inventoryArray[i] : null;

                InventorySlotUI slot = new InventorySlotUI(item);
                this.add(slot).size(128, 128).pad(4);

                if ((i + 1) % 3 == 0) {
                    this.row();
                }
            }

        }

        @Override
        public void act(float delta) {
            super.act(delta);
        }

        public void refreshInventory () {
            this.clear();

            textBox = new BoxWithText("choiceBox.png", "YOUR INVENTORY", 2f);
            this.add(textBox).size(320, 100).colspan(3).center();
            this.row().padTop(10);

            for (int i = 0; i < 12; i++) {
                GameObject item = null;
                if (i < screen.player.inventory.inventoryArray.length) {
                    item = screen.player.inventory.inventoryArray[i];
                }

                InventorySlotUI slot = new InventorySlotUI(item);
                this.add(slot).size(128, 128).pad(4);

                if ((i + 1) % 3 == 0) {
                    this.row();
                }
            }
        }



    }
