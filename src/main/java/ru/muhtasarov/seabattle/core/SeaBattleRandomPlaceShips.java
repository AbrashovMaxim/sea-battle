package ru.muhtasarov.seabattle.core;

import ru.muhtasarov.seabattle.display.graphics.map.SeaBattleMap;
import ru.muhtasarov.seabattle.display.graphics.map.SeaBattleMapCell;
import ru.muhtasarov.seabattle.display.graphics.ship.SeaBattleShip;

import java.util.List;
import java.util.Random;

import static ru.muhtasarov.seabattle.core.SeaBattleSettings.SIZE_MAP;

public class SeaBattleRandomPlaceShips {

    private static final Random random = new Random();
    private static SeaBattleMapCell[][] map;


    public static void placeShips(SeaBattleMap battleMap, List<SeaBattleShip> shipList) {
        map = battleMap.getCellMap();
        // Количество и длины кораблей
        int[] shipSizes = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

        for (SeaBattleShip ship : shipList) {
            boolean placed = false;
            while (!placed) {
                placed = placeShip(ship);
            }
        }
    }

    private static boolean placeShip(SeaBattleShip ship) {
        // Случайно выбираем направление: true - горизонтально, false - вертикально
        boolean horizontal = random.nextBoolean();
        int x = random.nextInt(SIZE_MAP - 1);
        int y = random.nextInt(SIZE_MAP - 1);

        // Проверяем, что корабль помещается в пределах поля
        if (horizontal) {
            if (x + ship.getCountDecks() > SIZE_MAP - 1) return false;
        } else {
            if (y + ship.getCountDecks() > SIZE_MAP - 1) return false;
        }

        // Проверяем, что ячейки свободны и нет кораблей вокруг
        for (int i = 0; i < ship.getCountDecks(); i++) {
            int xi = horizontal ? x + i : x;
            int yi = horizontal ? y : y + i;
            if (!isCellAvailable(xi, yi)) return false;
        }

        // Размещаем корабль
        for (int i = 0; i < ship.getCountDecks(); i++) {
            int xi = horizontal ? x + i : x;
            int yi = horizontal ? y : y + i;
            map[xi][yi].setShip(ship);
        }

        return true;
    }

    private static boolean isCellAvailable(int x, int y) {
        // Проверяем, что ячейка свободна и вокруг нет других кораблей
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nx = x + i;
                int ny = y + j;
                if (nx >= 0 && nx < SIZE_MAP - 1 && ny >= 0 && ny < SIZE_MAP - 1) {
                    if (map[nx][ny].isBusy()) return false;
                }
            }
        }
        return true;
    }
}
