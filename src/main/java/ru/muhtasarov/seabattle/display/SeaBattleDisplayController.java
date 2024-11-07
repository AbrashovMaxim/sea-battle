package ru.muhtasarov.seabattle.display;

import ru.muhtasarov.seabattle.core.SeaBattleBotVariant;
import ru.muhtasarov.seabattle.display.battle.SeaBattleBattle;
import ru.muhtasarov.seabattle.display.battle.SeaBattleBattleCallback;
import ru.muhtasarov.seabattle.display.battle.SeaBattleBattleController;
import ru.muhtasarov.seabattle.display.graphics.map.SeaBattleMapBattle;
import ru.muhtasarov.seabattle.display.menu.SeaBattleMenu;
import ru.muhtasarov.seabattle.display.menu.SeaBattleMenuCallback;
import ru.muhtasarov.seabattle.display.menu.SeaBattleMenuController;
import ru.muhtasarov.seabattle.display.primaryGui.SeaBattlePrimaryGui;
import ru.muhtasarov.seabattle.display.primaryGui.SeaBattlePrimaryGuiCallback;
import ru.muhtasarov.seabattle.display.primaryGui.SeaBattlePrimaryGuiController;

public final class  SeaBattleDisplayController
        implements  SeaBattleDisplay,
                    SeaBattleBattleCallback,
                    SeaBattleMenuCallback,
                    SeaBattlePrimaryGuiCallback
{

    private final SeaBattleDisplayCallback callback;

    private SeaBattleBattle battleGui;
    private SeaBattleMenu menuGui;
    private SeaBattlePrimaryGui primaryGui;


    public SeaBattleDisplayController(SeaBattleDisplayCallback callback) {
        this.callback = callback;
    }


    @Override
    public void initialize() {
        battleGui = new SeaBattleBattleController(this);
        menuGui = new SeaBattleMenuController(this);
        primaryGui = new SeaBattlePrimaryGuiController(this);

        battleGui.initialize();
        menuGui.initialize();
        primaryGui.initialize();

        primaryGui.setGui(menuGui.getGui());
    }

    @Override
    public void callMenuStartBattle(SeaBattleBotVariant botVariant) {
        battleGui.startBattle(new SeaBattleMapBattle(), new SeaBattleMapBattle());

        primaryGui.setGui(battleGui.getGui());
    }
}
