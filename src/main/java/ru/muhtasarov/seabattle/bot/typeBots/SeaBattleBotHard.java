package ru.muhtasarov.seabattle.bot.typeBots;

import ru.muhtasarov.seabattle.core.SeaBattleBotSettings;
import ru.muhtasarov.seabattle.core.SeaBattleCoord;
import ru.muhtasarov.seabattle.display.graphics.map.SeaBattleMapBattle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static ru.muhtasarov.seabattle.core.SeaBattleSettings.SIZE_MAP;

public class SeaBattleBotHard extends SeaBattleBotAbstract {

    private static final Random random = new Random();

    private SeaBattleCoord commonCoordStrike;
    private SeaBattleCoord coordStrike;

    private List<SeaBattleCoord> findLinkorList;
    private List<SeaBattleCoord> findCreiserList;
    private List<SeaBattleCoord> coordMapList;

    private int shag;
    private int checkedOrientation; // 0 - HORIZONTAL | 1 - VERTICAL
    private int[] checkedPath;


    public SeaBattleBotHard() {
        super();
    }


    @Override
    public void initialize() {
        coordMapList = new ArrayList<>();

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                map[row][col] = SeaBattleBotSettings.SeaBattleBotStrike.EMPTY;
                coordMapList.add(new SeaBattleCoord(col, row));
            }
        }

        findLinkorList = new ArrayList<>();
        findCreiserList = new ArrayList<>();

        int startX = 3;
        for (int row = 0; row < SIZE_MAP - 1; row++) {
            if (startX == -1) startX = 3;
            for (int col = startX; col < SIZE_MAP - 1; col += 4) {
                findLinkorList.add(new SeaBattleCoord(col, row));
            }
            startX--;
        }

        startX = 1;
        for (int row = 0; row < SIZE_MAP - 1; row++) {
            if (startX == -1) startX = 3;
            for (int col = startX; col < SIZE_MAP - 1; col += 4) {
                findCreiserList.add(new SeaBattleCoord(col, row));
            }
            startX--;
        }


        Collections.shuffle(coordMapList);
        Collections.shuffle(findLinkorList);
        Collections.shuffle(findCreiserList);

        shag = 0;
        countStrikeShip = 0;
    }

    @Override
    public boolean battle(SeaBattleMapBattle userMap) {
        SeaBattleBotSettings.threadSleep(500);
        while (true) {
            int x;
            int y;
            if (coordStrike == null) {
                if (!findLinkorList.isEmpty()) {
                    x = (int) findLinkorList.get(0).getX();
                    y = (int) findLinkorList.get(0).getY();
                    findLinkorList.remove(0);
                } else if (!findCreiserList.isEmpty()) {
                    x = (int) findCreiserList.get(0).getX();
                    y = (int) findCreiserList.get(0).getY();
                    findCreiserList.remove(0);
                } else {
                    x = (int) coordMapList.get(0).getX();
                    y = (int) coordMapList.get(0).getY();
                    coordMapList.remove(0);
                }
                checkedOrientation = -1;
                switch (checkStrike(x, y, userMap)) {
                    case WIN -> {
                        return true;
                    }
                    case STRIKE -> {
                        commonCoordStrike = new SeaBattleCoord(x, y);
                        coordStrike = new SeaBattleCoord(x, y);
                        shag = 0;
                    }
                    case DESTROY -> shag = 0;
                    case NULL -> {
                        return false;
                    }
                    default -> {}
                }
                SeaBattleBotSettings.threadSleep(500);
            } else {
                boolean checkedShags = true;
                int checkError = 0;
                while (checkedShags) {
                    if (checkError == 15) System.exit(228);
                    switch (shag) {
                        case 0 -> {
                            if (checkedOrientation == -1) {
                                checkedOrientation = random.nextInt(2);
                                checkedPath = new int[]{random.nextInt(2), -1};
                            } else {
                                if (checkedPath[1] == -1) {
                                    checkedPath[0] = checkedPath[0] == 0 ? 1 : 0;
                                    checkedPath[1] = 0;
                                } else {
                                    checkedOrientation = checkedOrientation == 0 ? 1 : 0;
                                    checkedPath = new int[]{random.nextInt(2), -1};
                                }
                            }

                            x = (int) coordStrike.getX();
                            y = (int) coordStrike.getY();

                            if (checkedOrientation == 0) {
                                if (checkedPath[0] == 0) {
                                    x = Math.max(0, x - 1);
                                } else {
                                    x = Math.min(SIZE_MAP - 2, x + 1);
                                }
                            } else {
                                if (checkedPath[0] == 0) {
                                    y = Math.max(0, y - 1);
                                } else {
                                    y = Math.min(SIZE_MAP - 2, y + 1);
                                }
                            }

                            if (x == (int) coordStrike.getX() && y == (int) coordStrike.getY()) continue;

                            switch (checkStrike(x, y, userMap)) {
                                case WIN -> { return true; }
                                case STRIKE -> {
                                    shag = 1;
                                    coordStrike = new SeaBattleCoord(x, y);
                                    SeaBattleBotSettings.threadSleep(500);
                                }
                                case DESTROY -> {
                                    coordStrike = null;
                                    checkedShags = false;
                                    checkedOrientation = -1;
                                    SeaBattleBotSettings.threadSleep(500);
                                }
                                case NULL -> {
                                    shag = 0;
                                    return false;
                                }
                                default -> {}
                            }
                        }
                        case 1 -> {
                            x = (int) coordStrike.getX();
                            y = (int) coordStrike.getY();

                            if (checkedOrientation == 0) {
                                if (checkedPath[0] == 0) {
                                    x = Math.max(0, x - 1);
                                } else {
                                    x = Math.min(SIZE_MAP - 2, x + 1);
                                }
                            } else {
                                if (checkedPath[0] == 0) {
                                    y = Math.max(0, y - 1);
                                } else {
                                    y = Math.min(SIZE_MAP - 2, y + 1);
                                }
                            }

                            if (x == (int) coordStrike.getX() && y == (int) coordStrike.getY()) {
                                checkedPath[0] = checkedPath[0] == 0 ? 1 : 0;
                                coordStrike = new SeaBattleCoord(commonCoordStrike.getX(), commonCoordStrike.getY());
                                continue;
                            }

                            switch (checkStrike(x, y, userMap)) {
                                case WIN -> { return true; }
                                case STRIKE -> {
                                    coordStrike = new SeaBattleCoord(x, y);
                                    SeaBattleBotSettings.threadSleep(500);
                                }
                                case DESTROY -> {
                                    shag = 0;
                                    coordStrike = null;
                                    checkedShags = false;
                                    checkedOrientation = -1;
                                    SeaBattleBotSettings.threadSleep(500);
                                }
                                case NULL -> {
                                    checkedPath[0] = checkedPath[0] == 0 ? 1 : 0;
                                    coordStrike = new SeaBattleCoord(commonCoordStrike.getX(), commonCoordStrike.getY());
                                    return false;
                                }
                                case ERROR -> {
                                    checkedPath[0] = checkedPath[0] == 0 ? 1 : 0;
                                    coordStrike = new SeaBattleCoord(commonCoordStrike.getX(), commonCoordStrike.getY());
                                }
                                default -> {}
                            }
                        }
                    }
                    checkError++;
                }
            }
        }
    }


}
