package ru.muhtasarov.seabattle.display.graphics.map;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static ru.muhtasarov.seabattle.core.SeaBattleSettings.*;

public class SeaBattleMapBattle extends GridPane implements SeaBattleMap {

    private final SeaBattleMapCell[][] mapCell;
    private final List<Consumer<Node>> consumerClickList;

    private final AtomicBoolean isCanStrike;


    public SeaBattleMapBattle() {
        consumerClickList = new ArrayList<>();
        mapCell = new SeaBattleMapCell[SIZE_MAP - 1][SIZE_MAP - 1];
        isCanStrike = new AtomicBoolean(false);

        for (int i = 0; i < SIZE_MAP; i++) {
            this.getColumnConstraints().add(new ColumnConstraints(25));
            this.getRowConstraints().add(new RowConstraints(25));
        }

        for (int row = 0; row < SIZE_MAP; row++) { // строка
            for (int col = 0; col < SIZE_MAP; col++) { // колонка
                if (row == 0 && col == 0) continue;

                if (col == 0) {
                    BorderPane borderPane = new BorderPane(new Label(NAME_ROWS_MAP[row - 1]));
                    borderPane.getStyleClass().addAll("map-border", "map-border" + (row == 1 ? "-top" : "") + "-right-bottom");
                    this.add(borderPane, col, row);
                } else if (row == 0) {
                    BorderPane borderPane = new BorderPane(new Label(NAME_COLUMNS_MAP[col - 1]));
                    borderPane.getStyleClass().addAll("map-border", "map-border" + (col != 1 ? "-right-bottom" : "-right-bottom-left") );
                    this.add(borderPane, col, row);
                } else {
                    SeaBattleMapBattleCell cell = new SeaBattleMapBattleCell();
                    cell.setOnMouseClicked(event -> {
                        if (isCanStrike.get()) {
                            consumerClickList.forEach(clickEvent -> clickEvent.accept(cell));
                        }
                    });
                    cell.hoverProperty().addListener((obs, oldV, newV) -> {
                        if (newV) {
                            int cellRowIndex = GridPane.getRowIndex(cell);
                            int cellColIndex = GridPane.getColumnIndex(cell);
                            for (Node node : this.getChildren()) {
                                if (node.equals(cell)) continue;
                                int rowIndex = GridPane.getRowIndex(node);
                                int colIndex = GridPane.getColumnIndex(node);
                                if (rowIndex == cellRowIndex || colIndex == cellColIndex) {
                                    if (!node.getStyleClass().contains("map-cell-hover")) {
                                        node.getStyleClass().add("map-cell-hover");
                                    }
                                }
                            }
                        } else {
                            for (Node node : this.getChildren()) {
                                node.getStyleClass().remove("map-cell-hover");
                            }
                        }
                    });
                    cell.getStyleClass().add("map-border-cell");
                    if (row == 10 || col == 10) {
                        cell.getStyleClass().add("map-border-cell" + (col == 10 ? "-right" : "") + (row == 10 ? "-bottom" : ""));
                    }
                    mapCell[row - 1][col - 1] = cell;

                    this.add(cell, col, row);
                }
            }
        }

    }


    public void addConsumerClick(Consumer<Node> consumer) {
        this.consumerClickList.add(consumer);
    }


    public void clearHover() {
        for (Node node : this.getChildren()) {
            node.getStyleClass().remove("map-cell-hover");
        }
    }


    public boolean getIsCanStrike() {
        return isCanStrike.get();
    }

    public void setIsCanStrike(boolean isCanStrike) {
        this.isCanStrike.set(isCanStrike);
    }


    @Override
    public SeaBattleMapCell[][] getCellMap() {
        return mapCell;
    }
}
