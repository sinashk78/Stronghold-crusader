package StrongholdCrusader.GameObjects.Buildings;

import StrongholdCrusader.GameObjects.Pair;
import StrongholdCrusader.Map.MapGUI;
import StrongholdCrusader.ResourceManager;
import StrongholdCrusader.Settings;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * Created by Baran on 5/29/2017.
 */
public class WoodCutter extends Building {

    public AnchorPane anchorPane;
    private Button destroy;

    public WoodCutter() {
        this.type = "WoodCutter";
        this.size = new Pair(4, 4);
        this.health = Settings.WOOD_CUTTER_INITIAL_HEALTH;
    }

    public WoodCutter(MapGUI mapGUI) {
        super(mapGUI);
        setImage(ResourceManager.getImage("WoodCutter"));
        this.type = "WoodCutter";
        this.size = new Pair(4, 4);
        this.health = Settings.WOOD_CUTTER_INITIAL_HEALTH;
    }

    @Override
    public void initializeAnchorPane() {
        anchorPane = new AnchorPane();
        ImageView imageView = new ImageView(ResourceManager.getImage("WoodCutter"));
        destroy = new Button("Destroy Building");
        destroy.setGraphic(imageView);
        imageView.setLayoutX(60);
        imageView.setLayoutY(20);
        destroy.setLayoutX(50);
        destroy.setLayoutY(10);
        ProgressBar healthBar = new ProgressBar((double) this.health / Settings.WOOD_CUTTER_INITIAL_HEALTH);
        healthBar.setLayoutX(Settings.MENUS_ANCHOR_PANE_WIDTH - 100);
        healthBar.setLayoutY(20);
        healthBar.setPrefSize(100, 20);
        healthBar.setStyle("-fx-accent: #96ff4c;");
        anchorPane.getChildren().addAll(imageView, destroy, healthBar);
        anchorPane.setId("building");
        anchorPane.getStylesheets().add("StrongholdCrusader/css/building.css");
        anchorPane.setPrefSize(Settings.MENUS_ANCHOR_PANE_WIDTH, Settings.MENUS_ANCHOR_PANE_HEIGHT);
    }

    public AnchorPane objectsMenuAnchorPane(boolean owner) {
        initializeAnchorPane();
        transition(destroy);
        destroy.setOnAction(event -> WoodCutter.this.mapGUI.removeBuildings(WoodCutter.this));

        if (!owner) {
            destroy.setVisible(false);
        }
        return anchorPane;
    }

    public void transition(Node button) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(100), button);
        transition.setAutoReverse(true);
        button.setOnMouseEntered(event -> {
            transition.setFromX(1);
            transition.setToX(1.1);
            transition.setFromY(1);
            transition.setToY(1.1);
            transition.play();
        });
        button.setOnMouseExited(event -> {
            transition.setFromX(1.1);
            transition.setToX(1);
            transition.setFromY(1.1);
            transition.setToY(1);
            transition.play();
        });
    }
}

