package StrongholdCrusader.GameObjects.Humans;

import StrongholdCrusader.Map.Map;
import StrongholdCrusader.Map.MapGUI;
import StrongholdCrusader.Map.MapTile;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by Baran on 5/29/2017.
 */
public class Soldier extends Human {
    public Soldier()     {
        this.type="Soldier";
        this.speed = 1;
        this.zone=7;
    }

    public Soldier(MapGUI mapGUI)     {
        super(mapGUI);
        this.type="Soldier";
        this.speed = 1;
    }
    public AnchorPane anchorPane;
    @Override
    public AnchorPane clickAction() {

        anchorPane = new AnchorPane();
        File file = new File("Resources/images/Humans/Soldier.png");
        ImageView imageView = new ImageView(file.toURI().toString());
        Button button = new Button("Change to Vassal");
        button.setLayoutX(button.getLayoutX()+70);
        button.setLayoutY(30);
        anchorPane.getChildren().addAll(imageView,button);
        anchorPane.setPrefHeight(100);
        anchorPane.setPrefWidth(300);
        return anchorPane;
    }
    @Override
    public void useResources() {
        ///TODO
    }

}
