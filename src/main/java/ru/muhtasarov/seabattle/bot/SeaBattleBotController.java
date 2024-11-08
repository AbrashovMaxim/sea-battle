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

    private Map<SeaBattleBotVariant, SeaBattleBotAbstract> typeBots;


    public SeaBattleBotController(SeaBattleBotCallback callback) {
        this.callback = callback;
    }


    @Override
    public void initialize() {
        typeBots = new HashMap<>();
        typeBots.put(SeaBattleBotVariant.EASY, new SeaBattleBotEasy());
        typeBots.put(SeaBattleBotVariant.MEDIUM, new SeaBattleBotMedium());
        typeBots.put(SeaBattleBotVariant.HARD, new SeaBattleBotHard());

        for (SeaBattleBotVariant variant : typeBots.keySet()) typeBots.get(variant).initialize();

    }


    @Override
    public SeaBattleBotAbstract getSelectedBot(SeaBattleBotVariant variant) {
        return typeBots.get(variant);
    }

}
