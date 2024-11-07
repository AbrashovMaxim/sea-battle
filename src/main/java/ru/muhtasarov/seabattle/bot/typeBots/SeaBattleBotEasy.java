package ru.muhtasarov.seabattle.bot.typeBots;

public class SeaBattleBotEasy implements SeaBattleBotType {

    private final SeaBattleBotTypeCallback callback;


    public SeaBattleBotEasy(SeaBattleBotTypeCallback callback) {
        this.callback = callback;
    }


    @Override
    public void initialize() {

    }


}
