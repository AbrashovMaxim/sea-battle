package ru.muhtasarov.seabattle.display.graphics.map;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import ru.muhtasarov.seabattle.display.graphics.ship.SeaBattleShipBattle;
import ru.muhtasarov.seabattle.display.graphics.ship.SeaBattleShipDraw;

import java.util.HashMap;

import static ru.muhtasarov.seabattle.core.SeaBattleSettings.*;

public class SeaBattleMapEditable extends GridPane implements SeaBattleMap {

    private final SeaBattleMapCell[][] mapCell;


    public SeaBattleMapEditable(SeaBattleMapEditableCallback callback) {
        mapCell = new SeaBattleMapCell[SIZE_MAP - 1][SIZE_MAP - 1];

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
                    borderPane.getStyleClass().addAll("map-border", "map-border" + (col != 1 ? "-right-bottom" : "-right-bottom-left"));
                    this.add(borderPane, col, row);
                } else {
                    SeaBattleMapEditableCell cell = new SeaBattleMapEditableCell();
                    cell.setOnMouseClicked(event -> {
                        if (event.getButton().equals(MouseButton.SECONDARY) && !callback.callMapIsSelectedShip() && cell.isBusy()) {
                            SeaBattleShipDraw shipDraw = cell.getShip();
                            shipDraw.returnParent();
                            for (int rowI = 0; rowI < mapCell.length; rowI++) {
                                for (int colI = 0; colI < mapCell[rowI].length; colI++) {
                                    if (mapCell[rowI][colI] instanceof SeaBattleMapEditableCell editableCell) {
                                        if (shipDraw.equals(editableCell.getShip())) {
                                            editableCell.setShip(null);
                                            editableCell.getStyleClass().remove("map-cell-ship");
                                        }
                                    }
                                }
                            }
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
                                    if (
                                        !node.getStyleClass().contains("map-cell-hover") &&
                                        !node.getStyleClass().contains("map-cell-ship") &&
                                        !node.getStyleClass().contains("map-cell-strike-ship") &&
                                        !node.getStyleClass().contains("map-cell-strike-null")

                                    ) {
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


    public void clearHover() {
        for (Node node : this.getChildren()) {
            node.getStyleClass().remove("map-cell-hover");
        }
    }


    public SeaBattleMapBattle constructMap() {
        SeaBattleMapBattle battleMapBattle = new SeaBattleMapBattle();
        SeaBattleMapCell[][] cellMap = battleMapBattle.getCellMap();
        HashMap<SeaBattleShipDraw, SeaBattleShipBattle> map = new HashMap<>();
        for (int row = 0; row < mapCell.length; row++) {
            for (int col = 0; col < mapCell[row].length; col++) {
                if (mapCell[row][col] instanceof SeaBattleMapEditableCell cell) {
                    if (cell.getShip() == null) continue;

                    SeaBattleShipBattle ship;
                    if (map.containsKey(cell.getShip())) {
                        ship = map.get(cell.getShip());
                    } else {
                        ship = new SeaBattleShipBattle(cell.getShip().getCountDecks());
                        map.put(cell.getShip(), ship);
                    }
                    SeaBattleMapBattleCell battleCell = (SeaBattleMapBattleCell) cellMap[row][col];
                    battleCell.getStyleClass().add("map-cell-ship");
                    battleCell.setShip(ship);
                }
            }
        }

        return battleMapBattle;
    }


    @Override
    public SeaBattleMapCell[][] getCellMap() {
        return mapCell;
    }
}

