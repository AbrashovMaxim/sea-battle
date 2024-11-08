package ru.muhtasarov.seabattle.display.graphics.map;

import javafx.scene.layout.Pane;
import ru.muhtasarov.seabattle.display.graphics.ship.SeaBattleShip;
import ru.muhtasarov.seabattle.display.graphics.ship.SeaBattleShipBattle;

public class SeaBattleMapBattleCell extends Pane implements SeaBattleMapCell {

    private SeaBattleShipBattle ship;
    private boolean isStrike;


    public SeaBattleMapBattleCell() {
        isStrike = false;
    }


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

    public boolean isStrike() {
        return isStrike;
    }

    public void setStrike(boolean strike) {
        isStrike = strike;
    }
}
