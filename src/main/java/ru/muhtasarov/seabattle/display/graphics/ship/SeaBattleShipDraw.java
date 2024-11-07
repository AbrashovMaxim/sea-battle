package ru.muhtasarov.seabattle.display.graphics.ship;

import javafx.scene.layout.*;

import static ru.muhtasarov.seabattle.core.SeaBattleSettings.SIZE_DECK;

public class SeaBattleShipDraw extends GridPane implements SeaBattleShip {

    private final SeaBattleShipDrawCallback callback;

    private final Pane parent;
    private final int countDecks;


    public SeaBattleShipDraw(SeaBattleShipDrawCallback callback, int countDecks, Pane parent) {
        this.callback = callback;
        this.countDecks = countDecks;
        this.parent = parent;

        this.getColumnConstraints().add(new ColumnConstraints(SIZE_DECK));
        for (int i = 0; i < countDecks; i++) {
            this.getRowConstraints().add(new RowConstraints(SIZE_DECK));
        }

        for (int i = 0; i < countDecks; i++) {
            Pane pane = new StackPane();
            this.add(pane, 0, i);
        }

        this.getStyleClass().add("map-cell-ship");

        this.setOnMouseClicked(event -> {
            if (this.getStyleClass().contains("map-cell-ship-selected")) {
                this.getStyleClass().remove("map-cell-ship-selected");
                callback.callShipDrawRemoveSelectedShip(this);
            } else {
                this.getStyleClass().add("map-cell-ship-selected");
                callback.callShipDrawSetSelectedShip(this);
            }
        });

        parent.getChildren().add(this);
    }


    @Override
    public int getCountDecks() {
        return countDecks;
    }


    public void returnParent() {
        if (!parent.getChildren().contains(this)) parent.getChildren().add(this);
    }
}
