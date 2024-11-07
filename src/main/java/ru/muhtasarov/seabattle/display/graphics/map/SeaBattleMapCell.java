package ru.muhtasarov.seabattle.display.graphics.map;

import ru.muhtasarov.seabattle.display.graphics.ship.SeaBattleShip;

public interface SeaBattleMapCell {

    boolean isBusy();
    SeaBattleShip getShip();
    void setShip(SeaBattleShip ship);

}
