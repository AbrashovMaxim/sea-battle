package ru.muhtasarov.seabattle.display;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import ru.muhtasarov.seabattle.bot.typeBots.SeaBattleBotAbstract;
import ru.muhtasarov.seabattle.core.SeaBattleBotVariant;
import ru.muhtasarov.seabattle.core.SeaBattleRandomPlaceShips;
import ru.muhtasarov.seabattle.display.battle.SeaBattleBattle;
import ru.muhtasarov.seabattle.display.battle.SeaBattleBattleCallback;
import ru.muhtasarov.seabattle.display.battle.SeaBattleBattleController;
import ru.muhtasarov.seabattle.display.finish.SeaBattleFinish;
import ru.muhtasarov.seabattle.display.finish.SeaBattleFinishCallback;
import ru.muhtasarov.seabattle.display.finish.SeaBattleFinishController;
import ru.muhtasarov.seabattle.display.graphics.map.SeaBattleMapBattle;
import ru.muhtasarov.seabattle.display.graphics.map.SeaBattleMapBattleCell;
import ru.muhtasarov.seabattle.display.graphics.map.SeaBattleMapCell;
import ru.muhtasarov.seabattle.display.graphics.ship.SeaBattleShip;
import ru.muhtasarov.seabattle.display.graphics.ship.SeaBattleShipBattle;
import ru.muhtasarov.seabattle.display.menu.SeaBattleMenu;
import ru.muhtasarov.seabattle.display.menu.SeaBattleMenuCallback;
import ru.muhtasarov.seabattle.display.menu.SeaBattleMenuController;
import ru.muhtasarov.seabattle.display.primaryGui.SeaBattlePrimaryGui;
import ru.muhtasarov.seabattle.display.primaryGui.SeaBattlePrimaryGuiCallback;
import ru.muhtasarov.seabattle.display.primaryGui.SeaBattlePrimaryGuiController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static ru.muhtasarov.seabattle.core.SeaBattleSettings.SIZE_MAP;

public final class  SeaBattleDisplayController
        implements  SeaBattleDisplay,
                    SeaBattleBattleCallback,
                    SeaBattleMenuCallback,
                    SeaBattleFinishCallback,
                    SeaBattlePrimaryGuiCallback
{

    private final SeaBattleDisplayCallback callback;

    private Thread threadStrikeBot;
    private AtomicBoolean isBotStrike;

    private SeaBattleBattle battleGui;
    private SeaBattleMenu menuGui;
    private SeaBattleFinish finishGui;
    private SeaBattlePrimaryGui primaryGui;

    private int countStrikeShip;


    public SeaBattleDisplayController(SeaBattleDisplayCallback callback) {
        this.callback = callback;
    }


    @Override
    public void initialize() {
        battleGui = new SeaBattleBattleController(this);
        menuGui = new SeaBattleMenuController(this);
        finishGui = new SeaBattleFinishController(this);
        primaryGui = new SeaBattlePrimaryGuiController(this);

        battleGui.initialize();
        menuGui.initialize();
        finishGui.initialize();
        primaryGui.initialize();

        primaryGui.setGui(menuGui.getGui());

        isBotStrike = new AtomicBoolean(false);
    }

    @Override
    public void callMenuStartBattle(SeaBattleBotVariant botVariant, SeaBattleMapBattle userMap) {
        SeaBattleBotAbstract bot = callback.callDisplayGetBot(botVariant);
        bot.initialize();

        if (threadStrikeBot != null && threadStrikeBot.isAlive()) {
            threadStrikeBot.interrupt();
        }
        threadStrikeBot = new Thread(() -> {
            try {
                while (true) {
                    if (isBotStrike.get()) {
                        boolean isWinBot = bot.battle(userMap);
                        if (isWinBot) {
                            finishGui.setText("ВЫ ПРОИГРАЛИ!");
                            Platform.runLater(() -> primaryGui.setGui(finishGui.getGui()));
                            return;
                        }
                        battleGui.setHod(true);
                        isBotStrike.set(false);
                    } else {
                        Thread.sleep(500);
                    }
                }
            } catch (InterruptedException e) {}
        });
        threadStrikeBot.setName("threadStrikeBot");
        threadStrikeBot.start();

        countStrikeShip = 0;

        List<SeaBattleShip> shipList = new ArrayList<>(List.of(
                new SeaBattleShipBattle(1),
                new SeaBattleShipBattle(1),
                new SeaBattleShipBattle(1),
                new SeaBattleShipBattle(1),
                new SeaBattleShipBattle(2),
                new SeaBattleShipBattle(2),
                new SeaBattleShipBattle(2),
                new SeaBattleShipBattle(3),
                new SeaBattleShipBattle(3),
                new SeaBattleShipBattle(4)
        ));

        SeaBattleMapBattle botMap = new SeaBattleMapBattle();
        SeaBattleRandomPlaceShips.placeShips(botMap, shipList);
        botMap.addConsumerClick(cell -> {
            if (cell instanceof SeaBattleMapBattleCell battleCell) {
                if (!battleCell.isStrike()) {
                    battleCell.setStrike(true);
                    if (battleCell.getShip() != null) {
                        battleCell.getStyleClass().add("map-cell-strike-ship");
                        if (battleCell.getShip() instanceof SeaBattleShipBattle shipBattle) {
                            if (shipBattle.getCountStrikes() + 1 == shipBattle.getCountDecks()) {
                                List<SeaBattleMapBattleCell> battleCellList = new ArrayList<>();
                                SeaBattleMapCell[][] mapCell = botMap.getCellMap();
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
                                            if (!battleCellList.contains(cellBattle) && !cellBattle.getStyleClass().contains("map-cell-strike-null")) {
                                                cellBattle.getStyleClass().add("map-cell-strike-null");
                                            }
                                            cellBattle.setStrike(true);
                                        }
                                    }
                                }
                                countStrikeShip += 1;

                                if (countStrikeShip == 10) {
                                    finishGui.setText("ВЫ ВЫЙГРАЛИ!");
                                    primaryGui.setGui(finishGui.getGui());
                                }
                            } else {
                                shipBattle.setCountStrikes(shipBattle.getCountStrikes() + 1);
                            }
                        }
                    } else {
                        battleCell.getStyleClass().add("map-cell-strike-null");
                        battleGui.setHod(false);
                        isBotStrike.set(true);
                    }
                }
            }

        });

        battleGui.startBattle(userMap, botMap);

        primaryGui.setGui(battleGui.getGui());
    }

    @Override
    public void callFinishReturnMainMenu() {
        menuGui.reset();
        primaryGui.setGui(menuGui.getGui());
    }

    @Override
    public void callBattleFinish(boolean isWin) {
        threadStrikeBot.interrupt();
        finishGui.setText(isWin ? "ВЫ ВЫЙГРАЛИ!" : "ВЫ ПРОИГРАЛИ!");
        primaryGui.setGui(finishGui.getGui());
    }

    @Override
    public void callPrimaryReleased() {
        threadStrikeBot.interrupt();
    }
}
