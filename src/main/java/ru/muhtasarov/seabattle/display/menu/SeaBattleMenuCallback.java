package ru.muhtasarov.seabattle.display.menu;

import ru.muhtasarov.seabattle.core.SeaBattleBotVariant;
import ru.muhtasarov.seabattle.display.graphics.map.SeaBattleMapBattle;

public interface SeaBattleMenuCallback {

    void callMenuStartBattle(SeaBattleBotVariant botVariant, SeaBattleMapBattle mapBattle);

}
