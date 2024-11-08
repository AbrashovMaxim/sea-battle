package ru.muhtasarov.seabattle.core;

public class SeaBattleBotSettings {

    public enum SeaBattleBotStrike {
        NULL, STRIKE, EMPTY
    }

    public enum SeaBattleBotStrikeType {
        STRIKE, WIN, NULL, ERROR, DESTROY
    }


    public static void threadSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {}
    }
}
