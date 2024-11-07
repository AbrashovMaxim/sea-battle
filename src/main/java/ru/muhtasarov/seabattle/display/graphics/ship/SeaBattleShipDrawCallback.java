package ru.muhtasarov.seabattle.display.graphics.ship;

public interface SeaBattleShipDrawCallback {

    void callShipDrawSetSelectedShip(SeaBattleShipDraw selectedShip);
    void callShipDrawRemoveSelectedShip(SeaBattleShipDraw selectedShip);

}
