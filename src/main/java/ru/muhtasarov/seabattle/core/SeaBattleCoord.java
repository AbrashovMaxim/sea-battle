package ru.muhtasarov.seabattle.core;

public class SeaBattleCoord {

    private double x;
    private double y;


    public SeaBattleCoord(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


    public void setCoord(double x, double y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public String toString() {
        return x + " " + y;
    }
}
