package StrongholdCrusader.Map;import StrongholdCrusader.GameObjects.Buildings.*;import StrongholdCrusader.GameObjects.GameObject;import StrongholdCrusader.GameObjects.Humans.Human;import StrongholdCrusader.GameObjects.Pair;import StrongholdCrusader.Menu;import StrongholdCrusader.Network.GameEvent;import StrongholdCrusader.ResourceManager;import StrongholdCrusader.Settings;import javafx.animation.FadeTransition;import javafx.application.Platform;import javafx.event.ActionEvent;import javafx.event.EventHandler;import javafx.geometry.Insets;import javafx.scene.Cursor;import javafx.scene.Node;import javafx.scene.Scene;import javafx.scene.control.Button;import javafx.scene.control.Label;import javafx.scene.image.Image;import javafx.scene.image.ImageView;import javafx.scene.input.KeyEvent;import javafx.scene.input.MouseEvent;import javafx.scene.input.ScrollEvent;import javafx.scene.layout.AnchorPane;import javafx.scene.layout.Background;import javafx.scene.layout.BackgroundFill;import javafx.scene.layout.CornerRadii;import javafx.scene.paint.Color;import javafx.scene.text.Font;import javafx.stage.Screen;import javafx.util.Duration;import java.io.Serializable;/** * Created by Baran on 6/3/2017. */public class MapGUI implements Runnable, Serializable {    public static final int DEFUALT_MODE = 0;    public static final int CREATING_WOOD_CUTTER_MODE = 1;    public static final int CREATING_QUARRY_MODE = 2;    public static final int CREATING_FARM_MODE = 3;    public static final int CREATING_PORT_MODE = 4;    public static final int CREATING_MARKET_MODE = 5;    public static final int CREATING_BARRACKS_MODE = 6;    public static int gameMode = MapGUI.DEFUALT_MODE;    private ResourceManager resourceManager;    private Pair viewOffset;    private Map map;    private String navigationLR, navigationUD;    private AnchorPane anchorPane;    private AnchorPane backgroundAnchorPane, tilesAnchorPane, objectsAnchorPane, menusAnchorPane;    private Scene scene;    private int numberOfMessages = 0;    EventHandler<MouseEvent> mapMouseEventHandler = new EventHandler<MouseEvent>() {        @Override        public void handle(MouseEvent event) {            int tileX = (int) (event.getX() / Settings.MAP_TILES_WIDTH);            int tileY = (int) (event.getY() / Settings.MAP_TILES_HEIGHT);            switch (gameMode) {                case MapGUI.CREATING_WOOD_CUTTER_MODE: {                    createBuilding("WoodCutter", new Pair(tileX, tileY));                    break;                }                case MapGUI.CREATING_BARRACKS_MODE: {                    createBuilding("Barracks", new Pair(tileX, tileY));                    break;                }                case MapGUI.CREATING_FARM_MODE: {                    createBuilding("Farm", new Pair(tileX, tileY));                    break;                }                case MapGUI.CREATING_MARKET_MODE: {                    createBuilding("Market", new Pair(tileX, tileY));                    break;                }                case MapGUI.CREATING_PORT_MODE: {                    createBuilding("Port", new Pair(tileX, tileY));                    break;                }                case MapGUI.CREATING_QUARRY_MODE: {                    createBuilding("Quarry", new Pair(tileX, tileY));                    break;                }                default:                    gameMode = MapGUI.DEFUALT_MODE;            }        }    };    public MapGUI(Map map) {        this.map = map;        resourceManager = new ResourceManager();        viewOffset = new Pair(0, 0);        navigationLR = "";        navigationUD = "";        anchorPane = new AnchorPane();        backgroundAnchorPane = new AnchorPane();        scene = new Scene(anchorPane);    }    void zoom() {        scene.setOnScroll(new EventHandler<ScrollEvent>() {            @Override            public void handle(ScrollEvent event) {                if (backgroundAnchorPane.getScaleX() > 1 && event.getDeltaY() > 0)                    return;                if (backgroundAnchorPane.getScaleX() < 1.5 && event.getDeltaY() < 0)                    return;                if (viewOffset.x < -700 && event.getDeltaY() < 0) {                    if (viewOffset.y > -300) {                        backgroundAnchorPane.setScaleX(backgroundAnchorPane.getScaleX() + event.getDeltaY() / 80);                        backgroundAnchorPane.setScaleY(backgroundAnchorPane.getScaleY() + event.getDeltaY() / 80);                        viewOffset.x += 750;                        viewOffset.y -= 750;                    } else if (viewOffset.y < -1600) {                        backgroundAnchorPane.setScaleX(backgroundAnchorPane.getScaleX() + event.getDeltaY() / 80);                        backgroundAnchorPane.setScaleY(backgroundAnchorPane.getScaleY() + event.getDeltaY() / 80);                        viewOffset.x += 750;                        viewOffset.y += 750;                    } else {                        backgroundAnchorPane.setScaleX(backgroundAnchorPane.getScaleX() + event.getDeltaY() / 80);                        backgroundAnchorPane.setScaleY(backgroundAnchorPane.getScaleY() + event.getDeltaY() / 80);                        viewOffset.x += 750;                    }                } else if (viewOffset.x > -300 && event.getDeltaY() < 0) {                    if (viewOffset.y > -300) {                        backgroundAnchorPane.setScaleX(backgroundAnchorPane.getScaleX() + event.getDeltaY() / 80);                        backgroundAnchorPane.setScaleY(backgroundAnchorPane.getScaleY() + event.getDeltaY() / 80);                        viewOffset.x -= 750;                        viewOffset.y -= 750;                    } else if (viewOffset.y < -1600) {                        backgroundAnchorPane.setScaleX(backgroundAnchorPane.getScaleX() + event.getDeltaY() / 80);                        backgroundAnchorPane.setScaleY(backgroundAnchorPane.getScaleY() + event.getDeltaY() / 80);                        viewOffset.x -= 750;                        viewOffset.y += 750;                    } else {                        backgroundAnchorPane.setScaleX(backgroundAnchorPane.getScaleX() + event.getDeltaY() / 80);                        backgroundAnchorPane.setScaleY(backgroundAnchorPane.getScaleY() + event.getDeltaY() / 80);                        viewOffset.x -= 750;                    }                } else {                    backgroundAnchorPane.setScaleX(backgroundAnchorPane.getScaleX() + event.getDeltaY() / 80);                    backgroundAnchorPane.setScaleY(backgroundAnchorPane.getScaleY() + event.getDeltaY() / 80);                }            }        });    }    private void changeViewOffset() {        int mapWidth = (int) (Settings.MAP_WIDTH_RESOLUTION * (int) Settings.MAP_TILES_WIDTH);        int mapHeight = (int) (Settings.MAP_HEIGHT_RESOLUTION * (int) Settings.MAP_TILES_HEIGHT);        if (navigationLR.length() != 0) {            if (navigationLR.charAt(0) == 'R') {                double zoom = 1;                if (backgroundAnchorPane.getScaleX() > 1)                    zoom = 1.27;                if (viewOffset.x - Settings.MAP_NAVIGATION_SPEED > -1 * (mapWidth * zoom - Settings.MAP_NAVIGATION_SPEED - scene.getWidth())) {                    viewOffset.x -= Settings.MAP_NAVIGATION_SPEED;                }            }            if (navigationLR.charAt(0) == 'L') {                int zoom = 1;                if (backgroundAnchorPane.getScaleX() > 1)                    zoom = (int) (backgroundAnchorPane.getScaleX() * 11);                if (viewOffset.x + Settings.MAP_NAVIGATION_SPEED < (int) (Settings.MAP_NAVIGATION_SPEED * zoom)) {                    viewOffset.x += Settings.MAP_NAVIGATION_SPEED;                }            }        }        if (navigationUD.length() != 0) {            if (navigationUD.charAt(0) == 'D') {                double zoom = 1;                if (backgroundAnchorPane.getScaleX() > 1)                    zoom = 1.27;                if (viewOffset.y - Settings.MAP_NAVIGATION_SPEED > -1 * (mapHeight * zoom - Settings.MAP_NAVIGATION_SPEED - scene.getHeight())) {                    viewOffset.y -= Settings.MAP_NAVIGATION_SPEED;                }            }            if (navigationUD.charAt(0) == 'U') {                int zoom = 1;                if (backgroundAnchorPane.getScaleX() > 1)                    zoom = (int) (backgroundAnchorPane.getScaleX() * 11);                if (viewOffset.y + Settings.MAP_NAVIGATION_SPEED < Settings.MAP_NAVIGATION_SPEED * zoom) {                    viewOffset.y += Settings.MAP_NAVIGATION_SPEED;                }            }        }    }    //menu for create buildings//    private AnchorPane menuOfCreateBuilding() {        AnchorPane anchorPane1 = new AnchorPane();        anchorPane1.setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(15), Insets.EMPTY)));        anchorPane1.setPadding(new Insets(30));        ImageView farm = new ImageView(resourceManager.getImage("Farm-menu"));        farm.setLayoutX(20);        ImageView quarry = new ImageView(resourceManager.getImage("Quarry-menu"));        quarry.setLayoutX(150);        ImageView market = new ImageView(resourceManager.getImage("Market-menu"));        market.setLayoutX(300);        ImageView port = new ImageView(resourceManager.getImage("Port-menu"));        port.setLayoutX(450);        ImageView barracks = new ImageView(resourceManager.getImage("Barracks-menu"));        barracks.setLayoutX(600);        ImageView woodcutter = new ImageView(resourceManager.getImage("WoodCutter-menu"));        woodcutter.setLayoutX(750);        woodcutter.setOnMouseClicked(new EventHandler<MouseEvent>() {            @Override            public void handle(MouseEvent event) {                gameMode = MapGUI.CREATING_WOOD_CUTTER_MODE;                scene.setCursor(resourceManager.getCursor("WoodCutter"));            }        });        barracks.setOnMouseClicked(new EventHandler<MouseEvent>() {            @Override            public void handle(MouseEvent event) {                gameMode = MapGUI.CREATING_BARRACKS_MODE;                scene.setCursor(resourceManager.getCursor("Barracks"));            }        });        farm.setOnMouseClicked(new EventHandler<MouseEvent>() {            @Override            public void handle(MouseEvent event) {                gameMode = MapGUI.CREATING_FARM_MODE;                scene.setCursor(resourceManager.getCursor("Farm"));            }        });        port.setOnMouseClicked(new EventHandler<MouseEvent>() {            @Override            public void handle(MouseEvent event) {                gameMode = MapGUI.CREATING_PORT_MODE;                scene.setCursor(resourceManager.getCursor("Port"));            }        });        market.setOnMouseClicked(new EventHandler<MouseEvent>() {            @Override            public void handle(MouseEvent event) {                gameMode = MapGUI.CREATING_MARKET_MODE;                scene.setCursor(resourceManager.getCursor("Market"));            }        });        quarry.setOnMouseClicked(new EventHandler<MouseEvent>() {            @Override            public void handle(MouseEvent event) {                gameMode = MapGUI.CREATING_QUARRY_MODE;                scene.setCursor(resourceManager.getCursor("Quarry"));            }        });        anchorPane1.getChildren().addAll(farm, quarry, market, port, barracks, woodcutter);        anchorPane1.setLayoutX(Screen.getPrimary().getBounds().getWidth() / 2 - anchorPane1.getBoundsInParent().getWidth() / 2);        anchorPane1.setLayoutY(Screen.getPrimary().getBounds().getHeight() - 150);        return anchorPane1;    }    //Return an AnchorPane containing tile images    public AnchorPane getMapTiles() {        AnchorPane background = new AnchorPane();        for (int i = 0; i < map.tiles.length; i++) {            for (int j = 0; j < map.tiles[i].length; j++) {                Image image = null;                if (map.tiles[i][j] instanceof Sea)                    image = resourceManager.getImage("Sea");                if (map.tiles[i][j] instanceof Plain)                    image = resourceManager.getImage("Plain1");                if (map.tiles[i][j] instanceof Mountain)                    image = resourceManager.getImage("Mountain");                ImageView imageView = new ImageView();                imageView.setImage(image);                imageView.setLayoutX(i * (image.getWidth()));                imageView.setLayoutY(j * (image.getHeight()));                background.getChildren().add(imageView);            }        }        //background.setOnMouseClicked(mapMouseEventHandler);        return background;    }    //Return an AnchorPane containing game object images    public AnchorPane getMapObjects() {        AnchorPane objects = new AnchorPane();        for (GameObject object : map.objects) {            Image image = null;            ImageView imageView = new ImageView();            if (object instanceof Building) {                image = resourceManager.getImage(object.type);            }            if (object instanceof Human)                image = resourceManager.getImage(object.type);            imageView.setImage(image);            imageView.setLayoutX(object.position.x * Settings.MAP_TILES_WIDTH);            imageView.setLayoutY(object.position.y * Settings.MAP_TILES_HEIGHT);            objects.getChildren().add(imageView);        }        return objects;    }    public AnchorPane getMapMenus() {        AnchorPane menus = new AnchorPane();        Button create = new Button("+");        create.setPrefSize(40, 40);        create.setFont(new Font(20));        create.setLayoutX(10);        create.setLayoutY(10);        create.setOnAction(new EventHandler<ActionEvent>() {            @Override            public void handle(ActionEvent event) {                create.setVisible(false);                anchorPane.getChildren().add(menuOfCreateBuilding());            }        });        menus.getChildren().addAll(create);        menus.setLayoutX(10);        menus.setLayoutY(Screen.getPrimary().getBounds().getHeight() - 70);        return menus;    }    public void showMap() {        tilesAnchorPane = getMapTiles();        objectsAnchorPane = getMapObjects();        backgroundAnchorPane.getChildren().addAll(tilesAnchorPane, objectsAnchorPane);        menusAnchorPane = getMapMenus();        //Offset        backgroundAnchorPane.setTranslateX(viewOffset.x);        backgroundAnchorPane.setTranslateY(viewOffset.y);        anchorPane.getChildren().addAll(backgroundAnchorPane, menusAnchorPane);        backgroundAnchorPane.setOnMouseClicked(mapMouseEventHandler);        //Arrow keys navigating        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {            @Override            public void handle(KeyEvent event) {                if (event.getCode().getName().equals("Left") && !navigationLR.contains("L"))                    navigationLR = "L" + navigationLR;                if (event.getCode().getName().equals("Right") && !navigationLR.contains("R"))                    navigationLR = "R" + navigationLR;                if (event.getCode().getName().equals("Up") && !navigationUD.contains("U"))                    navigationUD = "U" + navigationUD;                if (event.getCode().getName().equals("Down") && !navigationUD.contains("D"))                    navigationUD = "D" + navigationUD;            }        });        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {            @Override            public void handle(KeyEvent event) {                if (event.getCode().getName().equals("Left"))                    navigationLR = navigationLR.replace("L", "");                if (event.getCode().getName().equals("Right"))                    navigationLR = navigationLR.replace("R", "");                if (event.getCode().getName().equals("Up"))                    navigationUD = navigationUD.replace("U", "");                if (event.getCode().getName().equals("Down"))                    navigationUD = navigationUD.replace("D", "");            }        });        //Mouse Navigating        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {            @Override            public void handle(MouseEvent event) {                if (event.getScreenX() < Settings.MOUSE_MAP_NAVIGATION_MARGIN)                    navigationLR = "L" + navigationLR;                else                    navigationLR = navigationLR.replace("L", "");                if (event.getScreenX() > scene.getWidth() - Settings.MOUSE_MAP_NAVIGATION_MARGIN)                    navigationLR = "R" + navigationLR;                else                    navigationLR = navigationLR.replace("R", "");                if (event.getScreenY() < Settings.MOUSE_MAP_NAVIGATION_MARGIN)                    navigationUD = "U" + navigationUD;                else                    navigationUD = navigationUD.replace("U", "");                if (event.getScreenY() > scene.getHeight() - Settings.MOUSE_MAP_NAVIGATION_MARGIN)                    navigationUD = "D" + navigationUD;                else                    navigationUD = navigationUD.replace("D", "");            }        });        Platform.runLater(new Runnable() {            @Override            public void run() {                Menu.stage.setScene(scene);                Menu.stage.setMaximized(true);                Menu.stage.setFullScreen(true);                //Menu.stage.show();            }        });    }    //Refresh game objects position and show them    public void updateMap() {        Platform.runLater(new Runnable() {            @Override            public void run() {                backgroundAnchorPane.getChildren().remove(objectsAnchorPane);                objectsAnchorPane = getMapObjects();                //Offset                backgroundAnchorPane.getChildren().add(objectsAnchorPane);                backgroundAnchorPane.setTranslateX(viewOffset.x);                backgroundAnchorPane.setTranslateY(viewOffset.y);                zoom();                System.out.println(viewOffset.x + ":" + viewOffset.y);                Menu.stage.setScene(scene);            }        });    }    //Thread for map update in each game cycle    @Override    public void run() {        while (true) {            try {                changeViewOffset();                updateMap();                Thread.sleep(1000 / Settings.FRAME_RATE);            } catch (InterruptedException e) {                e.printStackTrace();            }        }    }    public void focusOnGameObject(GameObject gameObject) {        viewOffset.x = -1 * (int) (gameObject.position.x - scene.getWidth());        viewOffset.y = -1 * (int) (gameObject.position.y - scene.getHeight());    }    private void createBuilding(String type, Pair position) {        switch (type) {            case "WoodCutter": {                WoodCutter woodCutter = new WoodCutter();                woodCutter.position = new Pair(position.x, position.y);                map.sendGameEvent(GameEvent.WOOD_CUTTER_CREATED, position.x + ":" + position.y);                gameMode = MapGUI.DEFUALT_MODE;                scene.setCursor(Cursor.DEFAULT);                break;            }            case "Farm": {                Farm farm = new Farm();                farm.position = new Pair(position.x, position.y);                map.sendGameEvent(GameEvent.FARM_CREATED, position.x + ":" + position.y);                gameMode = MapGUI.DEFUALT_MODE;                scene.setCursor(Cursor.DEFAULT);                break;            }            case "Quarry": {                Quarry quarry = new Quarry();                quarry.position = new Pair(position.x, position.y);                map.sendGameEvent(GameEvent.QUARRAY_CREATED, position.x + ":" + position.y);                gameMode = MapGUI.DEFUALT_MODE;                scene.setCursor(Cursor.DEFAULT);                break;            }            case "Port": {                Port port = new Port();                port.position = new Pair(position.x, position.y);                map.sendGameEvent(GameEvent.PORT_CREATED, position.x + ":" + position.y);                gameMode = MapGUI.DEFUALT_MODE;                scene.setCursor(Cursor.DEFAULT);                break;            }            case "Market": {                Market market = new Market();                market.position = new Pair(position.x, position.y);                map.sendGameEvent(GameEvent.MARKET_CREATED, position.x + ":" + position.y);                //map.objects.add(market);                gameMode = MapGUI.DEFUALT_MODE;                scene.setCursor(Cursor.DEFAULT);                break;            }            case "Barracks": {                Barracks barracks = new Barracks();                barracks.position = new Pair(position.x, position.y);                map.sendGameEvent(GameEvent.BARRACKS_CREATED, position.x + ":" + position.y);                //map.objects.add(barracks);                gameMode = MapGUI.DEFUALT_MODE;                scene.setCursor(Cursor.DEFAULT);                break;            }        }    }    public void showMessage(String message) {        AnchorPane alert = new AnchorPane();        alert.setLayoutY(50 + (numberOfMessages * 100));        alert.setLayoutX(50);        alert.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(8), Insets.EMPTY)));        alert.setPadding(new Insets(5));        anchorPane.getChildren().add(alert);        Label label = new Label(message);        label.setPadding(new Insets(10, 15, 10, 55));        label.setFont(new Font("IRANSans(FaNum)", 20));        alert.getChildren().add(label);        Button quit = new Button("x");        quit.setPadding(new Insets(5, 10, 5, 10));        quit.setLayoutY(20);        quit.setLayoutX(10);        alert.getChildren().add(quit);        quit.setOnAction(new EventHandler<ActionEvent>() {            @Override            public void handle(ActionEvent event) {                alert.setDisable(true);                fadeOutNode(alert, 500);                numberOfMessages--;            }        });        fadeInNode(alert, 500);        numberOfMessages++;        new Thread(new Runnable() {            @Override            public void run() {                try {                    Thread.sleep(5000);                    if (!alert.isDisabled()) {                        alert.setDisable(true);                        fadeOutNode(alert, 500);                        numberOfMessages--;                    }                } catch (InterruptedException e) {                    e.printStackTrace();                }            }        }).start();    }    private void fadeOutNode(Node node, double milis) {        FadeTransition fadeTransition = new FadeTransition(Duration.millis(milis), node);        fadeTransition.setFromValue(1);        fadeTransition.setToValue(0);        fadeTransition.play();    }    private void fadeInNode(Node node, double milis) {        FadeTransition fadeTransition = new FadeTransition(Duration.millis(milis), node);        fadeTransition.setFromValue(0);        fadeTransition.setToValue(1);        fadeTransition.play();    }}