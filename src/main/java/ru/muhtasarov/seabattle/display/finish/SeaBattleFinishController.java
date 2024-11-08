package ru.muhtasarov.seabattle.display.finish;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import ru.muhtasarov.seabattle.core.SeaBattleJavaFX;

public class SeaBattleFinishController implements SeaBattleFinish {

    private final SeaBattleFinishCallback callback;

    private BorderPane primaryPane;
    private Label mainLabel;


    public SeaBattleFinishController(SeaBattleFinishCallback callback) {
        this.callback = callback;
    }


    @Override
    public void initialize() {
        primaryPane = new BorderPane();
        SeaBattleJavaFX.setAnchors(primaryPane, 0.);

        VBox finishVBox = new VBox();
        finishVBox.setSpacing(5);

        Pane emptyPaneOne = new StackPane();
        VBox.setVgrow(emptyPaneOne, Priority.ALWAYS);

        mainLabel = new Label("");
        mainLabel.getStyleClass().add("name-game");
        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setCenter(mainLabel);
        VBox.setVgrow(mainBorderPane, Priority.NEVER);

        Button startGameButton = new Button("Начать новую игру");
        startGameButton.getStyleClass().add("gui-button");
        startGameButton.setOnAction(event -> callback.callFinishReturnMainMenu());
        BorderPane startGameBorderPane = new BorderPane();
        startGameBorderPane.setCenter(startGameButton);
        VBox.setVgrow(startGameBorderPane, Priority.NEVER);

        Pane emptyPaneTwo = new StackPane();
        VBox.setVgrow(emptyPaneTwo, Priority.ALWAYS);

        finishVBox.getChildren().addAll(emptyPaneOne, mainBorderPane, startGameBorderPane, emptyPaneTwo);

        primaryPane.setCenter(finishVBox);
    }


    @Override
    public void setText(String text) {
        mainLabel.setText(text);
    }


    @Override
    public Node getGui() {
        return primaryPane;
    }
}
