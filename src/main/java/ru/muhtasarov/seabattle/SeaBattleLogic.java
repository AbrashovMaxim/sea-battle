package ru.muhtasarov.seabattle;

import ru.muhtasarov.seabattle.bot.SeaBattleBot;
import ru.muhtasarov.seabattle.bot.SeaBattleBotCallback;
import ru.muhtasarov.seabattle.bot.typeBots.SeaBattleBotAbstract;
import ru.muhtasarov.seabattle.core.SeaBattleBotVariant;
import ru.muhtasarov.seabattle.display.SeaBattleDisplay;
import ru.muhtasarov.seabattle.display.SeaBattleDisplayCallback;

public final class  SeaBattleLogic
        implements  SeaBattleBotCallback,
                    SeaBattleDisplayCallback
{

    private SeaBattleBot bot;
    private SeaBattleDisplay display;


    public SeaBattleLogic() { }


    public void setBot(SeaBattleBot bot) {
        this.bot = bot;
    }

    public void setDisplay(SeaBattleDisplay display) {
        this.display = display;
    }


    public void initialize() {
        bot.initialize();

        display.initialize();
    }

    @Override
    public SeaBattleBotAbstract callDisplayGetBot(SeaBattleBotVariant variant) {
        return bot.getSelectedBot(variant);
    }
}
