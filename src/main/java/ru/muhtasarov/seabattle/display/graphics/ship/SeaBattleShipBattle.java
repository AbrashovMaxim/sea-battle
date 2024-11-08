package ru.muhtasarov.seabattle.display.graphics.ship;

public class SeaBattleShipBattle implements SeaBattleShip {

    private final int countDecks;
    private int countStrikes;


    public SeaBattleShipBattle(int countDecks) {
        this.countDecks = countDecks;
        this.countStrikes = 0;
    }


    @Override
    public int getCountDecks() {
        return countDecks;
    }

    public int getCountStrikes() {
        return countStrikes;
    }

    public void setCountStrikes(int countStrikes) {
        this.countStrikes = countStrikes;
    }
}
