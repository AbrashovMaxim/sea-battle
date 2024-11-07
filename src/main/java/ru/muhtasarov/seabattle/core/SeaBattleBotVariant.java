package ru.muhtasarov.seabattle.core;

public enum SeaBattleBotVariant {

    EASY("Легкий", new short[]{83, 232, 70}),
    MEDIUM("Средний", new short[]{232, 151, 70}),
    HARD("Сложный", new short[]{232, 70, 70});


    private final String name;
    private final short[] color;


    SeaBattleBotVariant(String name, short[] color) {
        this.name = name;
        this.color = color;
    }


    public String getName() {
        return name;
    }
    public short[] getColor() {
        return color;
    }

    @Override
    public String toString() {
        return name;
    }
}
