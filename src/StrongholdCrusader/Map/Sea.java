package StrongholdCrusader.Map;

import StrongholdCrusader.GameObjects.Pair;

import java.io.Serializable;

/**
 * Created by Baran on 5/29/2017.
 */
public class Sea extends MapTile implements Serializable {
    Sea(int x, int y) {
        filled = false;
        this.position = new Pair(x, y);
        this.type = "Sea";
    }
}
