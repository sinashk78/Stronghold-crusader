package StrongholdCrusader.GameObjects.Buildings;

import StrongholdCrusader.GameObjects.Pair;
import StrongholdCrusader.Map.Map;
import StrongholdCrusader.Map.MapGUI;
import StrongholdCrusader.Network.GameEvent;
import StrongholdCrusader.Settings;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.io.File;

/**
 * Created by Baran on 5/29/2017.
 */
public class Market extends Building {
    public Market() {
        this.type = "Market";
        this.size = new Pair(4, 4);
    }

    public Market(MapGUI mapGUI) {
        super(mapGUI);
        this.type = "Market";
        this.size = new Pair(4, 4);
    }


    public AnchorPane anchorPane;

    @Override
    public AnchorPane clickAction(boolean owner) {
        anchorPane = new AnchorPane();
        File building = new File("Resources/images/Buildings/Market.png");
        ImageView buildingImage = new ImageView(building.toURI().toString());
        Button food = new Button("food");
        Button iron = new Button("iron");
        Button wood = new Button("wood");
        Button buy = new Button("buy(5 piece)");
        Button sell = new Button("sell(5 piece)");
        Button back = new Button("Back");
        back.setVisible(false);
        buy.setVisible(false);
        sell.setVisible(false);
        buy.setId("marketBuy");
        sell.setId("marketSell");
        food.setId("food");
        wood.setId("wood");
        iron.setId("iron");
        food.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                food.setVisible(false);
                wood.setVisible(false);
                iron.setVisible(false);
                buy.setVisible(true);
                sell.setVisible(true);
                back.setVisible(true);
                buy.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        mapGUI.map.sendGameEvent(GameEvent.BUY_RESOURCE,"food");
                    }
                });
                sell.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        mapGUI.map.sendGameEvent(GameEvent.SELL_RESOURCE,"food");
                    }
                });

            }
        });

        wood.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                wood.setVisible(false);
                food.setVisible(false);
                iron.setVisible(false);
                buy.setVisible(true);
                sell.setVisible(true);
                back.setVisible(true);
                buy.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        mapGUI.map.sendGameEvent(GameEvent.BUY_RESOURCE,"wood");
                    }
                });
                sell.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        mapGUI.map.sendGameEvent(GameEvent.SELL_RESOURCE,"wood");
                    }
                });

            }
        });

        iron.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                food.setVisible(false);
                wood.setVisible(false);
                iron.setVisible(false);
                buy.setVisible(true);
                sell.setVisible(true);
                back.setVisible(true);
                buy.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                    }
                });
                sell.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                    }
                });

            }
        });

        Button distroy = new Button("Distroy Building");
        distroy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Market.this.mapGUI.removeBuildings(Market.this);
            }
        });
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                iron.setVisible(true);
                wood.setVisible(true);
                food.setVisible(true);
                buy.setVisible(false);
                sell.setVisible(false);
                back.setVisible(false);
            }
        });

        buildingImage.setLayoutX(30);
        buildingImage.setLayoutY(30);
        distroy.setLayoutX(160);
        distroy.setLayoutY(60);
        iron.setLayoutX(400);
        wood.setLayoutX(550);
        food.setLayoutX(700);
        food.setLayoutY(60);
        wood.setLayoutY(60);
        iron.setLayoutY(60);
        buy.setLayoutX(400);
        sell.setLayoutX(550);
        sell.setLayoutY(60);
        buy.setLayoutY(60);
        back.setLayoutX(700);
        back.setLayoutY(60);
        anchorPane.getChildren().addAll(buildingImage, distroy, food, wood, iron, buy, sell, back);
        anchorPane.setId("marketMenu");
        anchorPane.setPrefSize(Settings.MENUS_ANCHORPANE_WIDTH,Settings.MENUS_ANCHORPANE_HEIGHT);
        anchorPane.getStylesheets().add("StrongholdCrusader/css/market.css");
        if (!owner){
            distroy.setVisible(false);
            back.setVisible(false);
            buy.setVisible(false);
            sell.setVisible(false);
            food.setVisible(false);
            wood.setVisible(false);
            iron.setVisible(false);
        }
        return anchorPane;
    }
}
