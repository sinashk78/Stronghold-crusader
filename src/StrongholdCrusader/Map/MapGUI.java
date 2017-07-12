package StrongholdCrusader.Map;import StrongholdCrusader.GameObjects.Buildings.*;import StrongholdCrusader.GameObjects.GameObject;import StrongholdCrusader.GameObjects.Humans.Human;import StrongholdCrusader.GameObjects.Humans.Soldier;import StrongholdCrusader.GameObjects.Humans.Vassal;import StrongholdCrusader.GameObjects.Humans.Worker;import StrongholdCrusader.GameObjects.NatureObject;import StrongholdCrusader.GameObjects.Pair;import StrongholdCrusader.Menu;import StrongholdCrusader.Network.GameEvent;import StrongholdCrusader.ResourceManager;import StrongholdCrusader.Settings;import javafx.animation.FadeTransition;import javafx.application.Platform;import javafx.event.ActionEvent;import javafx.event.EventHandler;import javafx.geometry.Insets;import javafx.scene.Cursor;import javafx.scene.Node;import javafx.scene.Scene;import javafx.scene.control.Button;import javafx.scene.control.Label;import javafx.scene.image.Image;import javafx.scene.image.ImageView;import javafx.scene.input.KeyEvent;import javafx.scene.input.MouseButton;import javafx.scene.input.MouseEvent;import javafx.scene.input.ScrollEvent;import javafx.scene.layout.AnchorPane;import javafx.scene.layout.Background;import javafx.scene.layout.BackgroundFill;import javafx.scene.layout.CornerRadii;import javafx.scene.paint.Color;import javafx.scene.text.Font;import javafx.scene.text.Text;import javafx.stage.Screen;import javafx.util.Duration;import java.io.Serializable;import java.util.ArrayList;import static StrongholdCrusader.Map.MapGUI.GameMode.SOLDIER_SELECTED;/** * Created by Baran on 6/3/2017. */public class MapGUI implements Runnable, Serializable {    public static GameMode gameMode = GameMode.DEFAULT;    public Map map;    public Scene scene;    String playerName;    private ResourceManager resourceManager;    private Pair viewOffset;    private String navigationLR, navigationUD;    private AnchorPane anchorPane;    private AnchorPane backgroundAnchorPane, tilesAnchorPane, objectsAnchorPane, menusAnchorPane, referencesAnchorPane;    private int selectedHumanId;    private boolean isMouseClickHandled = false;    EventHandler<MouseEvent> mapMouseEventHandler = new EventHandler<MouseEvent>() {        @Override        public void handle(MouseEvent event) {            if (isMouseClickHandled) return;            int tileX = (int) (event.getX() / Settings.MAP_TILES_WIDTH);            int tileY = (int) (event.getY() / Settings.MAP_TILES_HEIGHT);            switch (gameMode) {                case BUILDING_SELECTED: {                    menusAnchorPane.getChildren().clear();                    menusAnchorPane.getChildren().add(menuOfCreateBuilding());                    gameMode = GameMode.DEFAULT;                    break;                }                case VASSAL_SELECTED: {                    if (event.getButton() == MouseButton.PRIMARY) {                        sendMoveHumanEvent(selectedHumanId, new Pair(tileX, tileY));                        menusAnchorPane.getChildren().clear();                        menusAnchorPane.getChildren().add(menuOfCreateBuilding());                        gameMode = GameMode.DEFAULT;                        scene.setCursor(Cursor.DEFAULT);                        break;                    }                    }                case SOLDIER_SELECTED:{                        if (event.getButton() == MouseButton.PRIMARY) {                            sendMoveHumanEvent(selectedHumanId, new Pair(tileX, tileY));                            menusAnchorPane.getChildren().clear();                            menusAnchorPane.getChildren().add(menuOfCreateBuilding());                            gameMode = GameMode.DEFAULT;                            scene.setCursor(Cursor.DEFAULT);                            break;                        }                        break;                    }                case WORKER_SELECTED: {                    menusAnchorPane.getChildren().clear();                    menusAnchorPane.getChildren().add(menuOfCreateBuilding());                    gameMode = GameMode.DEFAULT;                    scene.setCursor(Cursor.DEFAULT);                    break;                }                case ENEMY_SELECTED:{                    menusAnchorPane.getChildren().clear();                    menusAnchorPane.getChildren().add(menuOfCreateBuilding());                    gameMode = GameMode.DEFAULT;                    scene.setCursor(Cursor.DEFAULT);                    break;                }                case CREATING_WOOD_CUTTER: {                    createBuilding("WoodCutter", new Pair(tileX, tileY));                    break;                }                case CREATING_BARRACKS: {                    createBuilding("Barracks", new Pair(tileX, tileY));                    break;                }                case CREATING_FARM: {                    createBuilding("Farm", new Pair(tileX, tileY));                    break;                }                case CREATING_MARKET: {                    createBuilding("Market", new Pair(tileX, tileY));                    break;                }                case CREATING_PORT: {                    createBuilding("Port", new Pair(tileX, tileY));                    break;                }                case CREATING_QUARRY: {                    createBuilding("Quarry", new Pair(tileX, tileY));                    break;                }                default:                    gameMode = GameMode.DEFAULT;            }        }    };    private int numberOfMessages = 0;    public MapGUI(Map map, String playerName) {        this.map = map;        this.playerName = playerName;        resourceManager = new ResourceManager();        viewOffset = new Pair(0, 0);        resourceManager.getSound("bgMusic3").play();        navigationLR = "";        navigationUD = "";        referencesAnchorPane = new AnchorPane();        anchorPane = new AnchorPane();        backgroundAnchorPane = new AnchorPane();        scene = new Scene(anchorPane);        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {            @Override            public void handle(MouseEvent event) {                isMouseClickHandled = false;            }        });    }    private void sendMoveHumanEvent(int selectedHumanId, Pair pair) {        map.sendGameEvent(GameEvent.MOVE_HUMAN, selectedHumanId + ":" + pair.x + ":" + pair.y);    }    private void sendAttackEvent(int humadId, GameObject target) {        if (!target.ownerName.equals(playerName))            map.sendGameEvent(GameEvent.ATTACK, humadId + ":" + target.id);    }    public ResourceManager getResourceManager() {        return resourceManager;    }    public void refrencesAnchorPane() {        referencesAnchorPane.setPrefSize(Settings.REFRENCES_ANCHORPANE_WIDTH, Settings.REFRENCES_ANCHORPANE_HEIGHT);        referencesAnchorPane.setLayoutX(Screen.getPrimary().getBounds().getWidth() - Settings.REFRENCES_ANCHORPANE_WIDTH);        referencesAnchorPane.setLayoutY(0);        Text gold = new Text();        Text food = new Text();        Text wood = new Text();        gold.setLayoutX(40);        food.setLayoutX(40);        wood.setLayoutX(40);        gold.setLayoutY(70);        food.setLayoutY(130);        wood.setLayoutY(200);        wood.setFill(Color.WHITE);        wood.setFont(new Font(20));        food.setFill(Color.WHITE);        food.setFont(new Font(20));        gold.setFill(Color.WHITE);        gold.setFont(new Font(20));        ImageView imageView = new ImageView(resourceManager.getImage("Refrences"));        referencesAnchorPane.getChildren().addAll(imageView, gold, wood, food);    }    public void updateRefrences(int coin, int food, int wood) {        ((Text) referencesAnchorPane.getChildren().get(1)).setText(String.valueOf(coin));        ((Text) referencesAnchorPane.getChildren().get(3)).setText(String.valueOf(food));        ((Text) referencesAnchorPane.getChildren().get(2)).setText(String.valueOf(wood));    }    void zoom() {        scene.setOnScroll(new EventHandler<ScrollEvent>() {            @Override            public void handle(ScrollEvent event) {                if (backgroundAnchorPane.getScaleX() > 1 && event.getDeltaY() > 0)                    return;                if (backgroundAnchorPane.getScaleX() < 1.5 && event.getDeltaY() < 0)                    return;                if (viewOffset.x < -700 && event.getDeltaY() < 0) {                    if (viewOffset.y > -300) {                        backgroundAnchorPane.setScaleX(backgroundAnchorPane.getScaleX() + event.getDeltaY() / 80);                        backgroundAnchorPane.setScaleY(backgroundAnchorPane.getScaleY() + event.getDeltaY() / 80);                        viewOffset.x += 750;                        viewOffset.y -= 750;                    } else if (viewOffset.y < -1600) {                        backgroundAnchorPane.setScaleX(backgroundAnchorPane.getScaleX() + event.getDeltaY() / 80);                        backgroundAnchorPane.setScaleY(backgroundAnchorPane.getScaleY() + event.getDeltaY() / 80);                        viewOffset.x += 750;                        viewOffset.y += 750;                    } else {                        backgroundAnchorPane.setScaleX(backgroundAnchorPane.getScaleX() + event.getDeltaY() / 80);                        backgroundAnchorPane.setScaleY(backgroundAnchorPane.getScaleY() + event.getDeltaY() / 80);                        viewOffset.x += 750;                    }                } else if (viewOffset.x > -300 && event.getDeltaY() < 0) {                    if (viewOffset.y > -300) {                        backgroundAnchorPane.setScaleX(backgroundAnchorPane.getScaleX() + event.getDeltaY() / 80);                        backgroundAnchorPane.setScaleY(backgroundAnchorPane.getScaleY() + event.getDeltaY() / 80);                        viewOffset.x -= 750;                        viewOffset.y -= 750;                    } else if (viewOffset.y < -1600) {                        backgroundAnchorPane.setScaleX(backgroundAnchorPane.getScaleX() + event.getDeltaY() / 80);                        backgroundAnchorPane.setScaleY(backgroundAnchorPane.getScaleY() + event.getDeltaY() / 80);                        viewOffset.x -= 750;                        viewOffset.y += 750;                    } else {                        backgroundAnchorPane.setScaleX(backgroundAnchorPane.getScaleX() + event.getDeltaY() / 80);                        backgroundAnchorPane.setScaleY(backgroundAnchorPane.getScaleY() + event.getDeltaY() / 80);                        viewOffset.x -= 750;                    }                } else {                    backgroundAnchorPane.setScaleX(backgroundAnchorPane.getScaleX() + event.getDeltaY() / 80);                    backgroundAnchorPane.setScaleY(backgroundAnchorPane.getScaleY() + event.getDeltaY() / 80);                }            }        });    }    private void changeViewOffset() {        int mapWidth = Settings.MAP_WIDTH_RESOLUTION * Settings.MAP_TILES_WIDTH;        int mapHeight = Settings.MAP_HEIGHT_RESOLUTION * Settings.MAP_TILES_HEIGHT;        if (navigationLR.length() != 0) {            if (navigationLR.charAt(0) == 'R') {                double zoom = 1;                if (backgroundAnchorPane.getScaleX() > 1)                    zoom = 1.27;                if (viewOffset.x - Settings.MAP_NAVIGATION_SPEED > -1 * (mapWidth * zoom - Settings.MAP_NAVIGATION_SPEED - scene.getWidth())) {                    viewOffset.x -= Settings.MAP_NAVIGATION_SPEED;                }            }            if (navigationLR.charAt(0) == 'L') {                int zoom = 1;                if (backgroundAnchorPane.getScaleX() > 1)                    zoom = (int) (backgroundAnchorPane.getScaleX() * 11);                if (viewOffset.x + Settings.MAP_NAVIGATION_SPEED < Settings.MAP_NAVIGATION_SPEED * zoom) {                    viewOffset.x += Settings.MAP_NAVIGATION_SPEED;                }            }        }        if (navigationUD.length() != 0) {            if (navigationUD.charAt(0) == 'D') {                double zoom = 1;                if (backgroundAnchorPane.getScaleX() > 1)                    zoom = 1.27;                if (viewOffset.y - Settings.MAP_NAVIGATION_SPEED > -1 * (mapHeight * zoom - Settings.MAP_NAVIGATION_SPEED - scene.getHeight())) {                    viewOffset.y -= Settings.MAP_NAVIGATION_SPEED;                }            }            if (navigationUD.charAt(0) == 'U') {                int zoom = 1;                if (backgroundAnchorPane.getScaleX() > 1)                    zoom = (int) (backgroundAnchorPane.getScaleX() * 11);                if (viewOffset.y + Settings.MAP_NAVIGATION_SPEED < Settings.MAP_NAVIGATION_SPEED * zoom) {                    viewOffset.y += Settings.MAP_NAVIGATION_SPEED;                }            }        }    }    //menu for create buildings//    private AnchorPane menuOfCreateBuilding() {        AnchorPane anchorPane1 = new AnchorPane();        anchorPane1.setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(15), Insets.EMPTY)));        anchorPane1.setPadding(new Insets(30));        ImageView farm = new ImageView(resourceManager.getImage("Farm-menu"));        farm.setLayoutX(20);        ImageView quarry = new ImageView(resourceManager.getImage("Quarry-menu"));        quarry.setLayoutX(150);        ImageView market = new ImageView(resourceManager.getImage("Market-menu"));        market.setLayoutX(300);        ImageView port = new ImageView(resourceManager.getImage("Port-menu"));        port.setLayoutX(450);        ImageView barracks = new ImageView(resourceManager.getImage("Barracks-menu"));        barracks.setLayoutX(600);        ImageView woodcutter = new ImageView(resourceManager.getImage("WoodCutter-menu"));        woodcutter.setLayoutX(750);        woodcutter.setOnMouseClicked(new EventHandler<MouseEvent>() {            @Override            public void handle(MouseEvent event) {                gameMode = GameMode.CREATING_WOOD_CUTTER;                scene.setCursor(resourceManager.getCursor("WoodCutter"));            }        });        barracks.setOnMouseClicked(new EventHandler<MouseEvent>() {            @Override            public void handle(MouseEvent event) {                gameMode = GameMode.CREATING_BARRACKS;                scene.setCursor(resourceManager.getCursor("Barracks"));            }        });        farm.setOnMouseClicked(new EventHandler<MouseEvent>() {            @Override            public void handle(MouseEvent event) {                gameMode = GameMode.CREATING_FARM;                scene.setCursor(resourceManager.getCursor("Farm"));            }        });        port.setOnMouseClicked(new EventHandler<MouseEvent>() {            @Override            public void handle(MouseEvent event) {                gameMode = GameMode.CREATING_PORT;                scene.setCursor(resourceManager.getCursor("Port"));            }        });        market.setOnMouseClicked(new EventHandler<MouseEvent>() {            @Override            public void handle(MouseEvent event) {                gameMode = GameMode.CREATING_MARKET;                scene.setCursor(resourceManager.getCursor("Market"));            }        });        quarry.setOnMouseClicked(new EventHandler<MouseEvent>() {            @Override            public void handle(MouseEvent event) {                gameMode = GameMode.CREATING_QUARRY;                scene.setCursor(resourceManager.getCursor("Quarry"));            }        });        anchorPane1.getChildren().addAll(farm, quarry, market, port, barracks, woodcutter);        //AnchorPane container = new AnchorPane();        //container.getChildren().addAll(anchorPane1);        return anchorPane1;    }    //Return an AnchorPane containing tile images    public AnchorPane getMapTiles() {        AnchorPane background = new AnchorPane();        for (int i = 0; i < map.tiles.length; i++) {            for (int j = 0; j < map.tiles[i].length; j++) {                Image image = null;                if (map.tiles[i][j] instanceof Sea)                    image = resourceManager.getImage("Sea");                if (map.tiles[i][j] instanceof Plain)                    image = resourceManager.getImage("Plain1");                if (map.tiles[i][j] instanceof Mountain)                    image = resourceManager.getImage("Mountain");                ImageView imageView = new ImageView();                imageView.setImage(image);                imageView.setLayoutX(i * (image.getWidth()));                imageView.setLayoutY(j * (image.getHeight()));                background.getChildren().add(imageView);            }        }        return background;    }    public void updateObjectsAnchorPane() {        checkRemovedObjects();        //New ImageView -> create        for (GameObject object : map.objects) {            int index = getNodeIndexById(object.id);            if (index == -1) {                Image image = null;                ImageView imageView = new ImageView();                image = resourceManager.getImage(object.type);                imageView.setLayoutX(object.position.x * Settings.MAP_TILES_WIDTH);                imageView.setLayoutY(object.position.y * Settings.MAP_TILES_HEIGHT);                imageView.setId(String.valueOf(object.id));                imageView.setImage(image);                if (object instanceof Building) {                    imageView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {                        @Override                        public void handle(MouseEvent mouseEvent) {                            if (gameMode == GameMode.BUILDING_SELECTED) {                                sendAttackEvent(selectedHumanId, object);                                gameMode = GameMode.DEFAULT;                                scene.setCursor(Cursor.DEFAULT);                            } else {                                boolean owner = false;                                if (object.ownerName.equals(playerName))                                    owner = true;                                AnchorPane menu = object.clickAction(owner);                                menusAnchorPane.getChildren().clear();                                menusAnchorPane.getChildren().add(menu);                                scene.setCursor(Cursor.DEFAULT);                                gameMode = GameMode.BUILDING_SELECTED;                                isMouseClickHandled = true;                            }                        }                    });                }                if (object instanceof Soldier) {                    imageView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {                        @Override                        public void handle(MouseEvent mouseEvent) {                            if (mouseEvent.getButton() == MouseButton.PRIMARY) {                                boolean owner = false;                                if (object.ownerName.equals(playerName)) {                                    owner = true;                                    AnchorPane menu = object.clickAction(owner);                                    menusAnchorPane.getChildren().clear();                                    menusAnchorPane.getChildren().add(menu);                                    gameMode = GameMode.SOLDIER_SELECTED;                                    scene.setCursor(resourceManager.getCursor("SelectDestination"));                                    selectedHumanId = object.id;                                    isMouseClickHandled = true;                                }                                else {                                    AnchorPane menu = object.clickAction(owner);                                    menusAnchorPane.getChildren().clear();                                    menusAnchorPane.getChildren().add(menu);                                    selectedHumanId = object.id;                                    isMouseClickHandled = true;                                    gameMode = GameMode.ENEMY_SELECTED;                                }                            }                        }                    });                }                if (object instanceof Vassal) {                    imageView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {                        @Override                        public void handle(MouseEvent mouseEvent) {                            if (mouseEvent.getButton() == MouseButton.PRIMARY) {                                boolean owner = false;                                if (object.ownerName.equals(playerName)) {                                    owner = true;                                    AnchorPane menu = object.clickAction(owner);                                    menusAnchorPane.getChildren().clear();                                    menusAnchorPane.getChildren().add(menu);                                    gameMode = GameMode.VASSAL_SELECTED;                                    scene.setCursor(resourceManager.getCursor("SelectDestination"));                                    selectedHumanId = object.id;                                    isMouseClickHandled = true;                                }                                else {                                    AnchorPane menu = object.clickAction(owner);                                    menusAnchorPane.getChildren().clear();                                    menusAnchorPane.getChildren().add(menu);                                    selectedHumanId = object.id;                                    isMouseClickHandled = true;                                    gameMode = GameMode.ENEMY_SELECTED;                                }                            }                        }                    });                }                if (object instanceof Worker) {                    imageView.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {                        @Override                        public void handle(MouseEvent mouseEvent) {                            if (mouseEvent.getButton() == MouseButton.PRIMARY) {                                boolean owner = false;                                AnchorPane menu = object.clickAction(owner);                                menusAnchorPane.getChildren().clear();                                menusAnchorPane.getChildren().add(menu);                                gameMode = GameMode.WORKER_SELECTED;                                scene.setCursor(resourceManager.getCursor("SelectDestination"));                                selectedHumanId = object.id;                                isMouseClickHandled = true;                            }                        }                    });                }                objectsAnchorPane.getChildren().add(imageView);            } else { //Existing ImageView -> update positions                ImageView imageView = (ImageView) objectsAnchorPane.getChildren().get(index);                imageView.setLayoutX(object.position.x * Settings.MAP_TILES_WIDTH);                imageView.setLayoutY(object.position.y * Settings.MAP_TILES_HEIGHT);            }        }    }    private void checkRemovedObjects() {        ArrayList<Node> removeList = new ArrayList<>();        for (Node node : objectsAnchorPane.getChildren()) {            int nodeId = Integer.parseInt(node.getId());            if (!isObjectsContainsId(nodeId)) {                removeList.add(node);            }        }        for (Node node : removeList) {            objectsAnchorPane.getChildren().remove(node);        }    }    private boolean isObjectsContainsId(int id) {        for (GameObject object : map.objects) {            if (id == object.id)                return true;        }        return false;    }    public int getNodeIndexById(int id) {        for (Node node : objectsAnchorPane.getChildren())            if (node.getId().equals(String.valueOf(id)))                return objectsAnchorPane.getChildren().indexOf(node);        return -1;    }    public AnchorPane getMapMenus() {        AnchorPane menus = new AnchorPane();        menus.getChildren().add(menuOfCreateBuilding());        menus.setLayoutX(0);        menus.setLayoutY(Screen.getPrimary().getBounds().getHeight() - 170);        return menus;    }    public AnchorPane getMapNatureObjectsAnchorPane() {        AnchorPane natureObjectsAnchorPane = new AnchorPane();        for (NatureObject natureObject : map.natureObjects) {            natureObjectsAnchorPane.getChildren().add(natureObject);        }        return natureObjectsAnchorPane;    }    public void showMap() {        tilesAnchorPane = getMapTiles();        objectsAnchorPane = new AnchorPane();        refrencesAnchorPane();        updateObjectsAnchorPane();        backgroundAnchorPane.getChildren().addAll(tilesAnchorPane, objectsAnchorPane, getMapNatureObjectsAnchorPane());        AnchorPane createBuildingsMenu = menuOfCreateBuilding();        createBuildingsMenu.setLayoutY(Screen.getPrimary().getBounds().getHeight() - 170);        menusAnchorPane = new AnchorPane();        menusAnchorPane.setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(40, 40, 0, 0, false), Insets.EMPTY)));        menusAnchorPane.setLayoutX(Screen.getPrimary().getBounds().getWidth() / 2 - Settings.MENUS_ANCHORPANE_WIDTH / 2);        menusAnchorPane.setLayoutY(Screen.getPrimary().getBounds().getHeight() - Settings.MENUS_ANCHORPANE_HEIGHT);        menusAnchorPane.setPrefSize(Settings.MENUS_ANCHORPANE_WIDTH, Settings.MENUS_ANCHORPANE_HEIGHT);        menusAnchorPane.getChildren().add(menuOfCreateBuilding());        //menusAnchorPane = createBuildingsMenu;        //Offset        backgroundAnchorPane.setTranslateX(viewOffset.x);        backgroundAnchorPane.setTranslateY(viewOffset.y);        anchorPane.getChildren().addAll(backgroundAnchorPane, menusAnchorPane, referencesAnchorPane);        backgroundAnchorPane.setOnMouseClicked(mapMouseEventHandler);        //Arrow keys navigating        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {            @Override            public void handle(KeyEvent event) {                if (event.getCode().getName().equals("Left") && !navigationLR.contains("L"))                    navigationLR = "L" + navigationLR;                if (event.getCode().getName().equals("Right") && !navigationLR.contains("R"))                    navigationLR = "R" + navigationLR;                if (event.getCode().getName().equals("Up") && !navigationUD.contains("U"))                    navigationUD = "U" + navigationUD;                if (event.getCode().getName().equals("Down") && !navigationUD.contains("D"))                    navigationUD = "D" + navigationUD;            }        });        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {            @Override            public void handle(KeyEvent event) {                if (event.getCode().getName().equals("Left"))                    navigationLR = navigationLR.replace("L", "");                if (event.getCode().getName().equals("Right"))                    navigationLR = navigationLR.replace("R", "");                if (event.getCode().getName().equals("Up"))                    navigationUD = navigationUD.replace("U", "");                if (event.getCode().getName().equals("Down"))                    navigationUD = navigationUD.replace("D", "");            }        });        //Mouse Navigating        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {            @Override            public void handle(MouseEvent event) {                if (event.getScreenX() < Settings.MOUSE_MAP_NAVIGATION_MARGIN)                    navigationLR = "L" + navigationLR;                else                    navigationLR = navigationLR.replace("L", "");                if (event.getScreenX() > scene.getWidth() - Settings.MOUSE_MAP_NAVIGATION_MARGIN)                    navigationLR = "R" + navigationLR;                else                    navigationLR = navigationLR.replace("R", "");                if (event.getScreenY() < Settings.MOUSE_MAP_NAVIGATION_MARGIN)                    navigationUD = "U" + navigationUD;                else                    navigationUD = navigationUD.replace("U", "");                if (event.getScreenY() > scene.getHeight() - Settings.MOUSE_MAP_NAVIGATION_MARGIN)                    navigationUD = "D" + navigationUD;                else                    navigationUD = navigationUD.replace("D", "");            }        });        Platform.runLater(new Runnable() {            @Override            public void run() {                Menu.stage.setScene(scene);                Menu.stage.setMaximized(true);                Menu.stage.setFullScreen(true);                //Menu.stage.show();            }        });    }    //Refresh game objects position and show them    public void updateMap() {        Platform.runLater(new Runnable() {            @Override            public void run() {                backgroundAnchorPane.getChildren().remove(objectsAnchorPane);                //objectsAnchorPane = getMapObjects();                updateObjectsAnchorPane();                //Offset                backgroundAnchorPane.getChildren().add(objectsAnchorPane);                backgroundAnchorPane.setTranslateX(viewOffset.x);                backgroundAnchorPane.setTranslateY(viewOffset.y);                zoom();                Menu.stage.setScene(scene);            }        });    }    //Thread for map update in each game cycle    @Override    public void run() {        while (true) {            try {                changeViewOffset();                updateMap();                Thread.sleep(1000 / Settings.FRAME_RATE);            } catch (InterruptedException e) {                e.printStackTrace();            }        }    }    public void focusOnTile(MapTile tile) {        int x = (int) (-1 * (tile.position.x - (Screen.getPrimary().getBounds().getWidth() / Settings.MAP_TILES_WIDTH / 2)) * Settings.MAP_TILES_WIDTH);        int y = (int) (-1 * (tile.position.y - (Screen.getPrimary().getBounds().getHeight() / Settings.MAP_TILES_HEIGHT / 2)) * Settings.MAP_TILES_HEIGHT);        x -= x % Settings.MAP_NAVIGATION_SPEED;        y -= y % Settings.MAP_NAVIGATION_SPEED;        int mapWidth = Settings.MAP_WIDTH_RESOLUTION * Settings.MAP_TILES_WIDTH;        int mapHeight = Settings.MAP_HEIGHT_RESOLUTION * Settings.MAP_TILES_HEIGHT;        if (x < -1 * (mapWidth - scene.getWidth()) + 300) {            viewOffset.x = (int) (-1 * (mapWidth - scene.getWidth()) + 300);        } else if (x > 0) {            viewOffset.x = 0;        } else            viewOffset.x = x;        if (y < -1 * (mapHeight - scene.getHeight())) {            viewOffset.y = (int) (-1 * (mapHeight - scene.getHeight()));        } else if (y > 0) {            viewOffset.y = 0;        } else            viewOffset.y = y;    }    public void createBuilding(String type, Pair position) {        switch (type) {            case "WoodCutter": {                WoodCutter woodCutter = new WoodCutter();                woodCutter.position = new Pair(position.x, position.y);                map.sendGameEvent(GameEvent.WOOD_CUTTER_CREATED, position.x + ":" + position.y);                gameMode = GameMode.DEFAULT;                scene.setCursor(Cursor.DEFAULT);                break;            }            case "Farm": {                Farm farm = new Farm();                farm.position = new Pair(position.x, position.y);                map.sendGameEvent(GameEvent.FARM_CREATED, position.x + ":" + position.y);                gameMode = GameMode.DEFAULT;                scene.setCursor(Cursor.DEFAULT);                break;            }            case "Quarry": {                Quarry quarry = new Quarry();                quarry.position = new Pair(position.x, position.y);                map.sendGameEvent(GameEvent.QUARRAY_CREATED, position.x + ":" + position.y);                gameMode = GameMode.DEFAULT;                scene.setCursor(Cursor.DEFAULT);                break;            }            case "Port": {                Port port = new Port();                port.position = new Pair(position.x, position.y);                map.sendGameEvent(GameEvent.PORT_CREATED, position.x + ":" + position.y);                gameMode = GameMode.DEFAULT;                scene.setCursor(Cursor.DEFAULT);                break;            }            case "Market": {                Market market = new Market();                market.position = new Pair(position.x, position.y);                map.sendGameEvent(GameEvent.MARKET_CREATED, position.x + ":" + position.y);                gameMode = GameMode.DEFAULT;                scene.setCursor(Cursor.DEFAULT);                break;            }            case "Barracks": {                Barracks barracks = new Barracks();                barracks.position = new Pair(position.x, position.y);                map.sendGameEvent(GameEvent.BARRACKS_CREATED, position.x + ":" + position.y);                gameMode = GameMode.DEFAULT;                scene.setCursor(Cursor.DEFAULT);                break;            }        }    }    public void createHuman(String type, int buildingId) {        switch (type) {            case "Soldier": {                map.sendGameEvent(GameEvent.SOLDIER_CREATED, String.valueOf(buildingId));                scene.setCursor(Cursor.DEFAULT);                break;            }            case "Vassal": {                map.sendGameEvent(GameEvent.VASSEL_CREATED, String.valueOf(buildingId));                scene.setCursor(Cursor.DEFAULT);                break;            }            case "Worker": {                map.sendGameEvent(GameEvent.WORKER_CREATED, String.valueOf(buildingId));                scene.setCursor(Cursor.DEFAULT);                break;            }        }    }    public void removeBuildings(Building building) {        menusAnchorPane.getChildren().clear();        menusAnchorPane.getChildren().add(menuOfCreateBuilding());        map.sendGameEvent(GameEvent.DISTROY_BUILDING, String.valueOf(building.id));    }    public void changeClimbStatus(Human human, boolean status) {        map.sendGameEvent(GameEvent.CHANGE_HUMAN_CLIMB, String.valueOf(human.id) + ":" + status);    }    public void showMessage(String message) {        AnchorPane alert = new AnchorPane();        alert.setLayoutY(50 + (numberOfMessages * 100));        alert.setLayoutX(50);        alert.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(8), Insets.EMPTY)));        alert.setPadding(new Insets(5));        Label label = new Label(message);        label.setPadding(new Insets(10, 15, 10, 55));        label.setFont(new Font("IRANSans(FaNum)", 20));        Button quit = new Button("x");        quit.setPadding(new Insets(5, 10, 5, 10));        quit.setLayoutY(20);        quit.setLayoutX(10);        quit.setOnAction(new EventHandler<ActionEvent>() {            @Override            public void handle(ActionEvent event) {                alert.setDisable(true);                fadeOutNode(alert, 500);                numberOfMessages--;            }        });        fadeInNode(alert, 500);        numberOfMessages++;        Platform.runLater(new Runnable() {            @Override            public void run() {                anchorPane.getChildren().add(alert);                alert.getChildren().addAll(label, quit);            }        });        new Thread(new Runnable() {            @Override            public void run() {                try {                    Thread.sleep(5000);                    if (!alert.isDisabled()) {                        alert.setDisable(true);                        fadeOutNode(alert, 500);                        numberOfMessages--;                    }                } catch (InterruptedException e) {                    e.printStackTrace();                }            }        }).start();    }    private void fadeOutNode(Node node, double milis) {        FadeTransition fadeTransition = new FadeTransition(Duration.millis(milis), node);        fadeTransition.setFromValue(1);        fadeTransition.setToValue(0);        fadeTransition.play();    }    private void fadeInNode(Node node, double milis) {        FadeTransition fadeTransition = new FadeTransition(Duration.millis(milis), node);        fadeTransition.setFromValue(0);        fadeTransition.setToValue(1);        fadeTransition.play();    }    public void playSound(String name) {        resourceManager.getSound(name).play();    }    public enum GameMode {        DEFAULT,        CREATING_WOOD_CUTTER,        CREATING_QUARRY,        CREATING_FARM,        CREATING_PORT,        CREATING_MARKET,        CREATING_BARRACKS,        BUILDING_SELECTED,        VASSAL_SELECTED,        WORKER_SELECTED,        SOLDIER_SELECTED,        ENEMY_SELECTED    }}