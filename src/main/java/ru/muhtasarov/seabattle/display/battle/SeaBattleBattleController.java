package ru.muhtasarov.seabattle.display.battle;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.muhtasarov.seabattle.core.SeaBattleJavaFX;
import ru.muhtasarov.seabattle.display.graphics.map.SeaBattleMapBattle;

public final class SeaBattleBattleController implements SeaBattleBattle {

    private final SeaBattleBattleCallback callback;

    private BorderPane primaryPane;

    private HBox hBoxMaps;

    private SeaBattleMapBattle userMap;
    private SeaBattleMapBattle botMap;

    private Label timerLabel;


    public SeaBattleBattleController(SeaBattleBattleCallback callback) {
        this.callback = callback;
    }


    @Override
    public void initialize() {
        primaryPane = new BorderPane();
        SeaBattleJavaFX.setAnchors(primaryPane, 0.);

        hBoxMaps = new HBox();

        timerLabel = new Label("00:00");

        primaryPane.setCenter(hBoxMaps);
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
        this.userMap = userMap;
        userBox.getChildren().addAll(userLabel, this.userMap);

        Label botLabel = new Label("Бот");
        this.botMap = botMap;
        botBox.getChildren().addAll(botLabel, this.botMap);

        hBoxMaps.getChildren().clear();
        hBoxMaps.getChildren().addAll(userBox, timerLabel, botBox);
    }


}
