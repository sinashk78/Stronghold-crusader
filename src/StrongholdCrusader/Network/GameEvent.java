package StrongholdCrusader.Network;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by Baran on 6/1/2017.
 */
public class GameEvent {

    public static final int JOIN_TO_GAME = 1;
    public static final int USER_JOINED_TO_NETWORK = 2;
    public static final int DUPLICATE_USERNAME = 3;
    public static final int USER_SUCCESSFULLY_CREATED = 4;
    public static final int ALL_PLAYERS = 5;
    public static final int START_GAME = 6;
    public static final int WOOD_CUTTER_CREATED = 7;
    public static final int BARRACKS_CREATED = 8;
    public static final int MARKET_CREATED = 9;
    public static final int PORT_CREATED = 10;
    public static final int QUARRAY_CREATED = 11;
    public static final int FARM_CREATED = 12;
    public static final int MAP_OBJECTS = 13;
    public static final int MAP_ID = 14;
    public static final int SHOW_ALERT = 15;
    public static final int SOLDIER_CREATED = 16;
    public static final int VASSEL_CREATED = 17;
    public static final int WORKER_CREATED = 18;
    public static final int MOVE_HUMAN = 19;
    public static final int DISTROY_BUILDING = 20;
    public static final int FOCUS_ON_BUILDING = 21;
    public static final int RESOURCES = 22;
    public static final int CHANGE_HUMAN_CLIMB = 23;
    public static final int BUY_RESOURCE = 24;
    public static final int SELL_RESOURCE = 25;
    public static final int PLAY_SOUND = 26;
    public int type;
    public String message;
    public GameEvent(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public GameEvent() {
    }

    public static GameEvent parseFromString(String string) {
        JSONParser jsonParser = new JSONParser();
        GameEvent gameEvent = null;
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(string);
            int type = new Integer(((Long) jsonObject.get("type")).intValue());
            String message = (String) jsonObject.get("message");
            gameEvent = new GameEvent(type, message);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gameEvent;
    }

    public void show() {
        System.out.println("type: " + type + " , message: " + message);
    }

    public String getJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", this.type);
        jsonObject.put("message", this.message);
        return jsonObject.toJSONString();
    }

    public enum EventType {
        JOIN_TO_GAME,
        USER_JOINED_TO_NETWORK,
        DUPLICATE_USERNAME,
        USER_SUCCESSFULLY_CREATED,
        ALL_PLAYERS,
        START_GAME,
        WOOD_CUTTER_CREATED,
        BARRACKS_CREATED,
        MARKET_CREATED,
        PORT_CREATED,
        QUARRAY_CREATED,
        FARM_CREATED,
        MAP_OBJECTS,
        MAP_ID
    } //TODO
}
