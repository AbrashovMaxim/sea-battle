package ru.muhtasarov.seabattle.bot;

import ru.muhtasarov.seabattle.bot.typeBots.SeaBattleBotAbstract;
import ru.muhtasarov.seabattle.core.SeaBattleBotVariant;

public interface SeaBattleBot {

    void initialize();

    SeaBattleBotAbstract getSelectedBot(SeaBattleBotVariant variant);

}
