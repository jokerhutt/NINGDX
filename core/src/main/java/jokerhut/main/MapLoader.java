package jokerhut.main;

import Constants.Constants;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class MapLoader {

    MainScreen screen;

    public MapLoader (MainScreen screen) {
        this.screen = screen;
    }

        public Array<Rectangle> createStaticCollisionRects (String layerName) {

            Array<Rectangle> newCollisionRects = new Array<>();
            MapLayer collisionLayer = screen.map.getLayers().get(layerName);
            addRectsToArray(collisionLayer, newCollisionRects);

            return newCollisionRects;

        }


        public void addRectsToArray (MapLayer collisionLayer, Array<Rectangle> newCollisionRects) {
            if (collisionLayer != null) {
                MapObjects objects = collisionLayer.getObjects();
                for (RectangleMapObject obj : objects.getByType(RectangleMapObject.class)) {

                    Rectangle r = obj.getRectangle();

                    Rectangle scaled = new Rectangle(
                        r.x * Constants.SCALE,
                        r.y * Constants.SCALE,
                        r.width * Constants.SCALE,
                        r.height * Constants.SCALE // no scaling here
                    );

                    newCollisionRects.add(scaled);
                }
            }
        }

}
