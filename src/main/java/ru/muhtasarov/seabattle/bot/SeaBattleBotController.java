package ru.muhtasarov.seabattle.bot;

import ru.muhtasarov.seabattle.bot.typeBots.*;
import ru.muhtasarov.seabattle.core.SeaBattleBotVariant;

import java.util.HashMap;
import java.util.Map;

public final class  SeaBattleBotController
        implements  SeaBattleBot,
                    SeaBattleBotTypeCallback
{

    private final SeaBattleBotCallback callback;

    private SeaBattleBotType selectedBot;

    private Map<SeaBattleBotVariant, SeaBattleBotType> typeBots;


    public SeaBattleBotController(SeaBattleBotCallback callback) {
        this.callback = callback;
    }


    @Override
    public void initialize() {
        typeBots = new HashMap<>();
        typeBots.put(SeaBattleBotVariant.EASY, new SeaBattleBotEasy(this));
        typeBots.put(SeaBattleBotVariant.MEDIUM, new SeaBattleBotMedium(this));
        typeBots.put(SeaBattleBotVariant.HARD, new SeaBattleBotHard(this));

        for (SeaBattleBotVariant variant : typeBots.keySet()) typeBots.get(variant).initialize();

    }

}
