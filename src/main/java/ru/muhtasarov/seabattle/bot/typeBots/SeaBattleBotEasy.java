package ru.muhtasarov.seabattle.bot.typeBots;

import ru.muhtasarov.seabattle.core.SeaBattleBotSettings.SeaBattleBotStrike;
import ru.muhtasarov.seabattle.display.graphics.map.SeaBattleMapBattle;
import java.util.Random;

import static ru.muhtasarov.seabattle.core.SeaBattleSettings.SIZE_MAP;

public class SeaBattleBotEasy extends SeaBattleBotAbstract {

    private static final Random random = new Random();


    public SeaBattleBotEasy() {
        super();
    }


    @Override
    public void initialize() {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                map[row][col] = SeaBattleBotStrike.EMPTY;
            }
        }
        countStrikeShip = 0;
    }

    @Override
    public boolean battle(SeaBattleMapBattle userMap) {
        while (true) {
            int x = random.nextInt(SIZE_MAP - 1);
            int y = random.nextInt(SIZE_MAP - 1);

            switch (checkStrike(x, y, userMap)) {
                case WIN -> { return true; }
                case NULL -> { return false; }
                default -> {}
            }
        }
    }


}
