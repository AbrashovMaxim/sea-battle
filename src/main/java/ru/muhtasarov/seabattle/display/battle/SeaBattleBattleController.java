package ru.muhtasarov.seabattle.display.battle;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import ru.muhtasarov.seabattle.core.SeaBattleJavaFX;
import ru.muhtasarov.seabattle.display.graphics.map.SeaBattleMapBattle;

public final class SeaBattleBattleController implements SeaBattleBattle {

    private final SeaBattleBattleCallback callback;

    private BorderPane primaryPane;

    private VBox primaryBox;
    private Label hodGameLabel;

    private HBox hBoxMaps;

    private SeaBattleMapBattle userMap;
    private SeaBattleMapBattle botMap;


    public SeaBattleBattleController(SeaBattleBattleCallback callback) {
        this.callback = callback;
    }


    @Override
    public void initialize() {
        primaryPane = new BorderPane();
        SeaBattleJavaFX.setAnchors(primaryPane, 0.);

        primaryBox = new VBox();
        primaryBox.setAlignment(Pos.CENTER);
        primaryBox.setSpacing(10);

        Pane emptyPaneOne = new StackPane();
        VBox.setVgrow(emptyPaneOne, Priority.ALWAYS);

        Label nameGameLabel = new Label("Морской бой");
        nameGameLabel.getStyleClass().add("name-game");
        BorderPane nameBorderPane = new BorderPane();
        nameBorderPane.setCenter(nameGameLabel);
        VBox.setVgrow(nameBorderPane, Priority.NEVER);

        hodGameLabel = new Label("Ход: ВЫ");
        hodGameLabel.getStyleClass().add("podname-game");
        BorderPane hodBorderPane = new BorderPane();
        hodBorderPane.setCenter(hodGameLabel);
        VBox.setVgrow(hodBorderPane, Priority.NEVER);

        hBoxMaps = new HBox();
        hBoxMaps.setAlignment(Pos.CENTER);
        hBoxMaps.setSpacing(30);

        Button backDifficultButton = new Button("Выйти");
        backDifficultButton.getStyleClass().add("gui-button");
        backDifficultButton.setOnAction(event -> callback.callBattleFinish(false));
        BorderPane backDifficultBorderPane = new BorderPane();
        backDifficultBorderPane.setCenter(backDifficultButton);
        VBox.setVgrow(backDifficultBorderPane, Priority.NEVER);

        Pane emptyPaneTwo = new StackPane();
        VBox.setVgrow(emptyPaneTwo, Priority.ALWAYS);

        primaryBox.getChildren().addAll(emptyPaneOne, nameGameLabel, hodGameLabel, hBoxMaps, backDifficultBorderPane, emptyPaneTwo);


        primaryPane.setCenter(primaryBox);
    }


    @Override
    public Node getGui() {
        return primaryPane;
    }


    @Override
    public void startBattle(SeaBattleMapBattle userMap, SeaBattleMapBattle botMap) {
        VBox userBox = new VBox();
        userBox.setSpacing(5);
        VBox botBox = new VBox();
        botBox.setSpacing(5);

        Label userLabel = new Label("Вы");
        userLabel.setAlignment(Pos.CENTER);
        userLabel.getStyleClass().add("podname-game");
        BorderPane userBorderPane = new BorderPane(userLabel);
        this.userMap = userMap;
        userBox.getChildren().addAll(userBorderPane, this.userMap);

        Label botLabel = new Label("Бот");
        botLabel.setAlignment(Pos.CENTER);
        botLabel.getStyleClass().add("podname-game");
        BorderPane botBorderPane = new BorderPane(botLabel);
        this.botMap = botMap;
        botBox.getChildren().addAll(botBorderPane, this.botMap);

        hBoxMaps.getChildren().clear();
        hBoxMaps.getChildren().addAll(userBox, botBox);

        botMap.setIsCanStrike(true);
    }

    @Override
    public void setHod(boolean isUser) {
        if (isUser) {
            Platform.runLater(() -> {
                botMap.setIsCanStrike(true);
                hodGameLabel.setText("Ход: ВЫ");
            });
        } else {
            botMap.setIsCanStrike(false);
            hodGameLabel.setText("Ход: БОТ");
        }
    }


}
