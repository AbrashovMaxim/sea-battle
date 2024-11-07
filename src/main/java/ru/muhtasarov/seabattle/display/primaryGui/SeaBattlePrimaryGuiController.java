package ru.muhtasarov.seabattle.display.primaryGui;

import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.muhtasarov.seabattle.core.SeaBattleJavaFX;

public final class SeaBattlePrimaryGuiController implements SeaBattlePrimaryGui {

    private final SeaBattlePrimaryGuiCallback callback;

    private AnchorPane primaryPane;


    public SeaBattlePrimaryGuiController(SeaBattlePrimaryGuiCallback callback) {
        this.callback = callback;
    }


    @Override
    public void initialize() {
        primaryPane = new AnchorPane();
        primaryPane.addEventFilter(EventType.ROOT, event -> {

        });

        Scene scene = new Scene(primaryPane);
        scene.getStylesheets().add(this.getClass().getResource("/css/style.css").toExternalForm());

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setMinHeight(200);
        stage.setMinWidth(400);
        stage.show();
    }

    @Override
    public void setGui(Node node) {
        primaryPane.getChildren().setAll(node);
    }
}
