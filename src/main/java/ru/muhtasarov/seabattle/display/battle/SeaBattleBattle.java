package ru.muhtasarov.seabattle.display.battle;

import javafx.scene.Node;
import ru.muhtasarov.seabattle.display.graphics.map.SeaBattleMapBattle;

public interface SeaBattleBattle {

    void initialize();

    Node getGui();

    void startBattle(SeaBattleMapBattle userMap, SeaBattleMapBattle botMap);

}
