package ru.muhtasarov.seabattle.display.graphics.map;

import javafx.scene.layout.Pane;
import ru.muhtasarov.seabattle.display.graphics.ship.SeaBattleShip;
import ru.muhtasarov.seabattle.display.graphics.ship.SeaBattleShipDraw;

public class SeaBattleMapEditableCell extends Pane implements SeaBattleMapCell {

    private SeaBattleShipDraw ship;


    public SeaBattleMapEditableCell() {}


    @Override
    public boolean isBusy() {
        return ship != null;
    }


    @Override
    public SeaBattleShipDraw getShip() {
        return ship;
    }

    @Override
    public void setShip(SeaBattleShip ship) {
        this.ship = (SeaBattleShipDraw) ship;
    }
}
