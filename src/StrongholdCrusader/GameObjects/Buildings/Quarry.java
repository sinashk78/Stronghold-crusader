package StrongholdCrusader.GameObjects.Buildings;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.util.Queue;

/**
 * Created by Baran on 5/29/2017.
 */
public class Quarry extends Building {
    public Quarry()
    {
        this.type="Quarry";
    }
    @Override
    public AnchorPane clickAction() {
        anchorPane = new AnchorPane();
        File building = new File("Resources/images/Buildings/Quarry.png");
        ImageView imageView = new ImageView(building.toURI().toString());
        Button distroy = new Button("Distroy Building");
        distroy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        imageView.setLayoutX(170);
        distroy.setLayoutX(170);
        distroy.setLayoutY(150);
        anchorPane.getChildren().addAll(imageView,distroy);
        anchorPane.setPrefSize(500,250);
        return anchorPane;
    }
}
