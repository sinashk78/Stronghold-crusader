package StrongholdCrusader;

import StrongholdCrusader.GameObjects.Buildings.*;
import StrongholdCrusader.GameObjects.GameObject;
import StrongholdCrusader.GameObjects.Pair;
import StrongholdCrusader.Map.Map;
import StrongholdCrusader.Network.Client;
import StrongholdCrusader.Network.GameEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by Baran on 6/3/2017.
 */
public class ClientPlayer {
    String username;
    public Client client;
    Map map;
    MenuGUI menuGUI;

    public ClientPlayer(String username, String serverIP, MenuGUI menuGUI) {
        this.username = username;
        client = new Client(serverIP);
        client.sendJoinRequest(username);
        map = new Map(this);
        this.menuGUI = menuGUI;

        Runnable gameEventHandleRunnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (client.hasNewEvent()) {
                        analyseGameEvent(client.getEvent());
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(gameEventHandleRunnable).start();
    }


    private void analyseGameEvent(GameEvent gameEvent) {
        switch (gameEvent.type) {
            case GameEvent.USER_JOINED_TO_NETWORK: {
                String username = gameEvent.message.substring(0, gameEvent.message.indexOf(','));
                String address = gameEvent.message.substring(gameEvent.message.indexOf(",") + 1);
                menuGUI.addPlayerToTable(username, address);
                break;
            }
            case GameEvent.START_GAME: {
                map.showMapScreen();
            }
            case GameEvent.MAP_OBJECTS: {
                JSONParser jsonParser = new JSONParser();
                System.out.println(gameEvent.message);
                try {
                    JSONObject objects = (JSONObject) jsonParser.parse(gameEvent.message);
                    JSONArray objectsArray = (JSONArray) objects.get("objects");
                    for (Object o : objectsArray) {
                        JSONObject obj = (JSONObject) o;
                        int x = new Integer(((Long) (((JSONObject) obj.get("position")).get("x"))).intValue());
                        int y = new Integer(((Long) (((JSONObject) obj.get("position")).get("y"))).intValue());
                        int id = new Integer(((Long) (obj.get("id"))).intValue());
                        int health = new Integer(((Long) (obj.get("health"))).intValue());
                        if (obj.get("type") == "WoodCutter") {
                            if (map.findGameObjectObjectById(id) == null) {
                                WoodCutter woodCutter = new WoodCutter();
                                woodCutter.position = new Pair(x, y);
                                woodCutter.id = id;
                                woodCutter.health = health;
                                map.objects.add(woodCutter);
                            }
                            else {
                                GameObject gameObject = map.findGameObjectObjectById(id);
                                gameObject.position = new Pair(x, y);
                                gameObject.health = health;
                            }
                        }
                        if (obj.get("type") == "Quarry") {
                            Quarry quarry = new Quarry();
                            quarry.position = new Pair(x, y);
                            map.objects.add(quarry);
                        }
                        if (obj.get("type") == "Market") {
                            Market market = new Market();
                            market.position = new Pair(x, y);
                            map.objects.add(market);
                        }
                        if (obj.get("type") == "Port") {
                            Port port = new Port();
                            port.position = new Pair(x, y);
                            map.objects.add(port);
                        }
                        if (obj.get("type") == "Farm") {
                            Farm farm = new Farm();
                            farm.position = new Pair(x, y);
                            map.objects.add(farm);
                        }
                        if (obj.get("type") == "Barracks") {
                            Barracks barracks = new Barracks();
                            barracks.position = new Pair(x, y);
                            map.objects.add(barracks);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
