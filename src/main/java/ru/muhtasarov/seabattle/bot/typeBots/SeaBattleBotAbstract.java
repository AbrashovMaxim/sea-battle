package ru.muhtasarov.seabattle.bot.typeBots;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import ru.muhtasarov.seabattle.core.SeaBattleBotSettings.*;
import ru.muhtasarov.seabattle.display.graphics.map.SeaBattleMapBattle;
import ru.muhtasarov.seabattle.display.graphics.map.SeaBattleMapBattleCell;
import ru.muhtasarov.seabattle.display.graphics.map.SeaBattleMapCell;
import ru.muhtasarov.seabattle.display.graphics.ship.SeaBattleShipBattle;

import java.util.ArrayList;
import java.util.List;

import static ru.muhtasarov.seabattle.core.SeaBattleSettings.SIZE_MAP;

public abstract class SeaBattleBotAbstract {

    protected final SeaBattleBotStrike[][] map;
    protected int countStrikeShip;


    public SeaBattleBotAbstract() {
        this.map = new SeaBattleBotStrike[SIZE_MAP - 1][SIZE_MAP - 1];
    }


    public abstract void initialize();

    public abstract boolean battle(SeaBattleMapBattle userMap);


    protected final SeaBattleBotStrikeType checkStrike(int x, int y, SeaBattleMapBattle userMap) {
        if (map[y][x].equals(SeaBattleBotStrike.EMPTY)) {
            SeaBattleMapCell[][] mapCell = userMap.getCellMap();
            if (mapCell[y][x] instanceof SeaBattleMapBattleCell battleCell) {
                battleCell.setStrike(true);
                if (battleCell.getShip() != null) {
                    if (battleCell.getShip() instanceof SeaBattleShipBattle shipBattle) {
                        Platform.runLater(() -> battleCell.getStyleClass().add("map-cell-strike-ship"));
                        map[y][x] = SeaBattleBotStrike.STRIKE;
                        if (shipBattle.getCountStrikes() + 1 == shipBattle.getCountDecks()) {
                            List<SeaBattleMapBattleCell> battleCellList = new ArrayList<>();

                            for (int row = 0; row < mapCell.length; row ++) {
                                for (int col = 0; col < mapCell[row].length; col++) {
                                    if (mapCell[row][col].getShip() != null && mapCell[row][col].getShip().equals(shipBattle)) {
                                        battleCellList.add((SeaBattleMapBattleCell) mapCell[row][col]);
                                    }
                                }
                            }
                            int minRow = Math.max(0, GridPane.getRowIndex(battleCellList.getFirst()) - 2);
                            int minCol = Math.max(0, GridPane.getColumnIndex(battleCellList.getFirst()) - 2);
                            int maxRow = Math.min(SIZE_MAP - 1, GridPane.getRowIndex(battleCellList.getLast()) + 1);
                            int maxCol = Math.min(SIZE_MAP - 1, GridPane.getColumnIndex(battleCellList.getLast()) + 1);

                            for (int row = minRow; row < maxRow; row++) {
                                for (int col = minCol; col < maxCol; col++) {
                                    if (mapCell[row][col] instanceof SeaBattleMapBattleCell cellBattle) {
                                        int finalRow = row;
                                        int finalCol = col;
                                        Platform.runLater(() -> {
                                            if (!battleCellList.contains(cellBattle) && !cellBattle.getStyleClass().contains("map-cell-strike-null")) {
                                                cellBattle.getStyleClass().add("map-cell-strike-null");
                                                map[finalRow][finalCol] = SeaBattleBotStrike.NULL;
                                            }
                                            cellBattle.setStrike(true);
                                        });
                                    }
                                }
                            }
                            countStrikeShip++;
                            if (countStrikeShip == 10) {
                                return SeaBattleBotStrikeType.WIN;
                            } else {
                                return SeaBattleBotStrikeType.DESTROY;
                            }
                        } else {
                            shipBattle.setCountStrikes(shipBattle.getCountStrikes() + 1);
                            return SeaBattleBotStrikeType.STRIKE;
                        }
                    }
                } else {
                    map[y][x] = SeaBattleBotStrike.NULL;
                    Platform.runLater(() -> {
                        battleCell.getStyleClass().add("map-cell-strike-null");
                    });
                    return SeaBattleBotStrikeType.NULL;
                }

            }
        }

        return SeaBattleBotStrikeType.ERROR;
    }

}
