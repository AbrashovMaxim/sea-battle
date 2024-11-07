package ru.muhtasarov.seabattle.display.graphics.map;

import javafx.scene.layout.Pane;
import ru.muhtasarov.seabattle.display.graphics.ship.SeaBattleShip;
import ru.muhtasarov.seabattle.display.graphics.ship.SeaBattleShipBattle;

public class SeaBattleMapBattleCell extends Pane implements SeaBattleMapCell {

    private SeaBattleShipBattle ship;


    public SeaBattleMapBattleCell() { }


    @Override
    public boolean isBusy() {
        return ship != null;
    }

    @Override
    public SeaBattleShip getShip() {
        return ship;
    }

    @Override
    public void setShip(SeaBattleShip ship) {
        this.ship = (SeaBattleShipBattle) ship;
    }
}
