package ru.muhtasarov.seabattle.display;

import ru.muhtasarov.seabattle.bot.typeBots.SeaBattleBotAbstract;
import ru.muhtasarov.seabattle.core.SeaBattleBotVariant;

public interface SeaBattleDisplayCallback {

    SeaBattleBotAbstract callDisplayGetBot(SeaBattleBotVariant bot);

}
