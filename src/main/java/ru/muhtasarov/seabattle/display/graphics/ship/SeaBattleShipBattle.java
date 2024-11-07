package ru.muhtasarov.seabattle.display.graphics.ship;

public class SeaBattleShipBattle implements SeaBattleShip {

    private final int countDecks;


    public SeaBattleShipBattle(int countDecks) {
        this.countDecks = countDecks;
    }


    @Override
    public int getCountDecks() {
        return countDecks;
    }
}
