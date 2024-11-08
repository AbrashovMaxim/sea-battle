package ru.muhtasarov.seabattle.display.menu;

import javafx.beans.InvalidationListener;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import ru.muhtasarov.seabattle.core.SeaBattleBotVariant;
import ru.muhtasarov.seabattle.core.SeaBattleCoord;
import ru.muhtasarov.seabattle.core.SeaBattleJavaFX;
import ru.muhtasarov.seabattle.core.SeaBattleRandomPlaceShips;
import ru.muhtasarov.seabattle.display.graphics.map.*;
import ru.muhtasarov.seabattle.display.graphics.ship.SeaBattleShip;
import ru.muhtasarov.seabattle.display.graphics.ship.SeaBattleShipDraw;
import ru.muhtasarov.seabattle.display.graphics.ship.SeaBattleShipDrawCallback;

import java.util.ArrayList;
import java.util.List;

import static ru.muhtasarov.seabattle.core.SeaBattleSettings.SIZE_MAP;

public final class  SeaBattleMenuController
        implements  SeaBattleMenu,
                    SeaBattleShipDrawCallback,
                    SeaBattleMapEditableCallback
{

    private final SeaBattleMenuCallback callback;

    private BorderPane primaryPane;
    private AnchorPane primaryAnchorPane;

    private VBox startMenuVBox;
    private VBox startGameVBox;
    private VBox shipPlacementVBox;

    private SeaBattleBotVariant botVariant;
    private SeaBattleMapEditable shipsPlaceMap;
    private List<SeaBattleShipDraw> shipDrawList;

    private VBox shipsLeftPart;
    private VBox shipsRightPart;

    private BorderPane nextPlacementBorderPane;

    private SeaBattleShipDraw selectedShip;
    private boolean isRotate;


    public SeaBattleMenuController(SeaBattleMenuCallback callback) {
        this.callback = callback;
    }


    @Override
    public void initialize() {
        primaryPane = new BorderPane();
        SeaBattleJavaFX.setAnchors(primaryPane, 0.);

        startMenuVBox = new VBox();
        startMenuVBox.setSpacing(5);
        SeaBattleJavaFX.setAnchors(startMenuVBox, 0.);
        startGameVBox = new VBox();
        startGameVBox.setSpacing(5);
        SeaBattleJavaFX.setAnchors(startGameVBox, 0.);
        shipPlacementVBox = new VBox();
        shipPlacementVBox.setSpacing(5);
        SeaBattleJavaFX.setAnchors(shipPlacementVBox, 0.);

        primaryAnchorPane = new AnchorPane(startMenuVBox);

        initializeBaseMenu();
        initializeDifficultyMenu();
        initializeShipPlaceMenu();

        primaryPane.setCenter(primaryAnchorPane);

    }


    @Override
    public void reset() {
        shipsLeftPart.getChildren().clear();
        shipsRightPart.getChildren().clear();

        shipDrawList.forEach(SeaBattleShipDraw::returnParent);

        SeaBattleMapCell[][] cellList = shipsPlaceMap.getCellMap();
        for (int row = 0; row < cellList.length; row++) {
            for (int col = 0; col < cellList[row].length; col++) {
                if (cellList[row][col] instanceof SeaBattleMapEditableCell cell) {
                    cell.setShip(null);
                    cell.getStyleClass().remove("map-cell-ship");
                }
            }
        }

        shipPlacementVBox.getChildren().remove(nextPlacementBorderPane);

        botVariant = null;

        primaryAnchorPane.getChildren().setAll(startMenuVBox);
    }


    @Override
    public Node getGui() {
        return primaryPane;
    }


    @Override
    public void callShipDrawSetSelectedShip(SeaBattleShipDraw selectedShip) {
        if (this.selectedShip != null) {
            this.selectedShip.getStyleClass().remove("map-cell-ship-selected");
        }
        this.selectedShip = selectedShip;
        isRotate = false;
    }

    @Override
    public void callShipDrawRemoveSelectedShip(SeaBattleShipDraw selectedShip) {
        if (this.selectedShip.equals(selectedShip)) {
            this.selectedShip = null;
        }
        isRotate = false;
    }

    /**
     * Инициализация базового меню
     */
    private void initializeBaseMenu() {
        Pane emptyPaneOne = new StackPane();
        VBox.setVgrow(emptyPaneOne, Priority.ALWAYS);

        Label nameGameLabel = new Label("Морской бой");
        nameGameLabel.getStyleClass().add("name-game");
        BorderPane nameBorderPane = new BorderPane();
        nameBorderPane.setCenter(nameGameLabel);
        VBox.setVgrow(nameBorderPane, Priority.NEVER);

        Label byGameLabel = new Label("Данил Мухтасаров ПЕ-22Б");
        byGameLabel.getStyleClass().add("by-game");
        BorderPane byGameBorderPane = new BorderPane();
        byGameBorderPane.setCenter(byGameLabel);
        VBox.setVgrow(byGameBorderPane, Priority.NEVER);

        Button startGameButton = new Button("Начать новую игру");
        startGameButton.getStyleClass().add("gui-button");
        startGameButton.setOnAction(event -> primaryAnchorPane.getChildren().setAll(startGameVBox));
        BorderPane startGameBorderPane = new BorderPane();
        startGameBorderPane.setCenter(startGameButton);
        VBox.setVgrow(startGameBorderPane, Priority.NEVER);

        Pane emptyPaneTwo = new StackPane();
        VBox.setVgrow(emptyPaneTwo, Priority.ALWAYS);

        startMenuVBox.getChildren().addAll(emptyPaneOne, nameBorderPane, byGameBorderPane, startGameBorderPane, emptyPaneTwo);
    }

    /**
     * Инициализация выбора сложности
     */
    private void initializeDifficultyMenu() {
        Pane emptyPaneThree = new StackPane();
        VBox.setVgrow(emptyPaneThree, Priority.ALWAYS);

        Label selectDifficultLabel = new Label("Выберите сложность");
        selectDifficultLabel.getStyleClass().add("name-game");
        BorderPane selectDifficultBorderPane = new BorderPane();
        selectDifficultBorderPane.setCenter(selectDifficultLabel);
        VBox.setVgrow(selectDifficultBorderPane, Priority.NEVER);

        HBox difficultButtonsHBox = new HBox();
        difficultButtonsHBox.setAlignment(Pos.CENTER);
        difficultButtonsHBox.setSpacing(5);
        for (SeaBattleBotVariant variant : SeaBattleBotVariant.values()) {
            String color = variant.getColor()[0] + ", " + variant.getColor()[1] + ", " + variant.getColor()[2];
            Button button = new Button(variant.getName());
            button.getStyleClass().add("gui-button");
            button.setStyle("-fx-border-color: rgb(" + color + "); -fx-text-fill: rgb(" + color + ");");
            button.setOnAction(event -> {
                botVariant = variant;
                primaryAnchorPane.getChildren().setAll(shipPlacementVBox);
            });

            difficultButtonsHBox.getChildren().add(button);
        }
        BorderPane difficultBoxBorderPane = new BorderPane();
        difficultBoxBorderPane.setCenter(difficultButtonsHBox);
        VBox.setVgrow(difficultBoxBorderPane, Priority.NEVER);

        Button backDifficultButton = new Button("Назад");
        backDifficultButton.getStyleClass().add("gui-button");
        backDifficultButton.setOnAction(event -> primaryAnchorPane.getChildren().setAll(startMenuVBox));
        BorderPane backDifficultBorderPane = new BorderPane();
        backDifficultBorderPane.setCenter(backDifficultButton);
        VBox.setVgrow(backDifficultBorderPane, Priority.NEVER);

        Pane emptyPaneFour = new StackPane();
        VBox.setVgrow(emptyPaneFour, Priority.ALWAYS);

        startGameVBox.getChildren().addAll(emptyPaneThree, selectDifficultBorderPane, difficultBoxBorderPane, backDifficultBorderPane, emptyPaneFour);
    }

    /**
     * Инициализация растановки кораблей
     */
    private void initializeShipPlaceMenu() {
        Pane emptyPaneFive = new StackPane();
        VBox.setVgrow(emptyPaneFive, Priority.ALWAYS);

        Label shipPlacementLabel = new Label("Расположите корабли");
        shipPlacementLabel.getStyleClass().add("name-game");
        BorderPane shipPlacementBorderPane = new BorderPane();
        shipPlacementBorderPane.setCenter(shipPlacementLabel);
        VBox.setVgrow(shipPlacementBorderPane, Priority.NEVER);

        HBox shipPlacementHBox = new HBox();
        shipPlacementHBox.setAlignment(Pos.CENTER);
        shipPlacementHBox.setSpacing(5);
        VBox.setVgrow(shipPlacementHBox, Priority.NEVER);

        isRotate = false;
        shipsPlaceMap = new SeaBattleMapEditable(this);
        shipsPlaceMap.setOnMouseMoved(event -> {
            if (selectedShip != null) {
                callShipDrawCheckLineUnderMapCells(new SeaBattleCoord(event.getX(), event.getY()), selectedShip.getCountDecks());
            }
        });
        shipsPlaceMap.setOnMouseClicked(event -> {
            if (selectedShip == null) return;

            if (event.getButton().equals(MouseButton.MIDDLE)) {
                isRotate = !isRotate;
                callShipDrawCheckLineUnderMapCells(new SeaBattleCoord(event.getX(), event.getY()), selectedShip.getCountDecks());
            }
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                callShipPlaceShipOnMap(selectedShip);
            }
        });

        shipsLeftPart = new VBox();
        shipsLeftPart.setSpacing(5);
        shipsLeftPart.setMinWidth(25);

        shipsRightPart = new VBox();
        shipsRightPart.setSpacing(5);
        shipsRightPart.setMinWidth(25);

        shipDrawList = new ArrayList<>(List.of(
                new SeaBattleShipDraw(this, 1, shipsRightPart),
                new SeaBattleShipDraw(this, 1, shipsLeftPart),
                new SeaBattleShipDraw(this, 1, shipsLeftPart),
                new SeaBattleShipDraw(this, 1, shipsLeftPart),
                new SeaBattleShipDraw(this, 2, shipsLeftPart),
                new SeaBattleShipDraw(this, 2, shipsLeftPart),
                new SeaBattleShipDraw(this, 2, shipsRightPart),
                new SeaBattleShipDraw(this, 3, shipsLeftPart),
                new SeaBattleShipDraw(this, 3, shipsRightPart),
                new SeaBattleShipDraw(this, 4, shipsRightPart)
        ));

        shipPlacementHBox.getChildren().addAll(shipsLeftPart, shipsPlaceMap, shipsRightPart);

        Button randomPlacementButton = new Button("Расположить автоматически");
        randomPlacementButton.getStyleClass().add("gui-button");
        randomPlacementButton.setOnAction(event -> {
            selectedShip = null;
            for (SeaBattleShip ship : shipDrawList) {
                SeaBattleShipDraw shipDraw = (SeaBattleShipDraw) ship;
                shipDraw.returnParent();
                shipDraw.getStyleClass().remove("map-cell-ship-selected");
            }
            SeaBattleMapCell[][] cellsArrayFromMap = shipsPlaceMap.getCellMap();
            for (int row = 0; row < cellsArrayFromMap.length; row++) {
                for (int col = 0; col < cellsArrayFromMap[row].length; col++) {
                    if (cellsArrayFromMap[row][col] instanceof SeaBattleMapEditableCell cell) {
                        cell.getStyleClass().remove("map-cell-ship");
                        cell.setShip(null);
                    }
                }
            }
            List<SeaBattleShip> shipList = shipDrawList.stream().map(ship -> (SeaBattleShip) ship).toList();
            SeaBattleRandomPlaceShips.placeShips(shipsPlaceMap, shipList);
            cellsArrayFromMap = shipsPlaceMap.getCellMap();
            for (int row = 0; row < cellsArrayFromMap.length; row++) {
                for (int col = 0; col < cellsArrayFromMap[row].length; col++) {
                    if (cellsArrayFromMap[row][col] instanceof SeaBattleMapEditableCell cell) {
                        if (cell.isBusy()) {
                            cell.getStyleClass().add("map-cell-ship");
                            shipsLeftPart.getChildren().remove(cell.getShip());
                            shipsRightPart.getChildren().remove(cell.getShip());
                        }
                    }
                }
            }
        });
        BorderPane randomPlacementBorderPane = new BorderPane();
        randomPlacementBorderPane.setCenter(randomPlacementButton);
        VBox.setVgrow(randomPlacementBorderPane, Priority.NEVER);

        Button nextPlacementButton = new Button("Начать игру");
        nextPlacementButton.getStyleClass().addAll("gui-button", "gui-button-green");
        nextPlacementButton.setOnAction(event -> {
            callback.callMenuStartBattle(botVariant, shipsPlaceMap.constructMap());
            primaryAnchorPane.getChildren().setAll(startGameVBox);
        });
        nextPlacementBorderPane = new BorderPane();
        nextPlacementBorderPane.setCenter(nextPlacementButton);
        VBox.setVgrow(nextPlacementBorderPane, Priority.NEVER);

        shipsRightPart.getChildren().addListener((InvalidationListener) observable -> {
            if (shipsLeftPart.getChildren().isEmpty() && shipsRightPart.getChildren().isEmpty()) {
                if (!shipPlacementVBox.getChildren().contains(nextPlacementBorderPane)) {
                    shipPlacementVBox.getChildren().add(shipPlacementVBox.getChildren().size() - 2, nextPlacementBorderPane);
                }
            } else {
                shipPlacementVBox.getChildren().remove(nextPlacementBorderPane);
            }
        });

        shipsLeftPart.getChildren().addListener((InvalidationListener) observable -> {
            if (shipsLeftPart.getChildren().isEmpty() && shipsRightPart.getChildren().isEmpty()) {
                if (!shipPlacementVBox.getChildren().contains(nextPlacementBorderPane)) {
                    shipPlacementVBox.getChildren().add(shipPlacementVBox.getChildren().size() - 2, nextPlacementBorderPane);
                }
            } else {
                shipPlacementVBox.getChildren().remove(nextPlacementBorderPane);
            }
        });

        Button backPlacementButton = new Button("Назад");
        backPlacementButton.getStyleClass().add("gui-button");
        backPlacementButton.setOnAction(event -> primaryAnchorPane.getChildren().setAll(startGameVBox));
        BorderPane backPlacementBorderPane = new BorderPane();
        backPlacementBorderPane.setCenter(backPlacementButton);
        VBox.setVgrow(backPlacementBorderPane, Priority.NEVER);
        VBox.setMargin(backPlacementBorderPane, new Insets(5));

        Pane emptyPaneSix = new StackPane();
        VBox.setVgrow(emptyPaneSix, Priority.ALWAYS);

        shipPlacementVBox.getChildren().addAll(emptyPaneFive, shipPlacementBorderPane, shipPlacementHBox, randomPlacementBorderPane, backPlacementBorderPane, emptyPaneSix);
    }

    private void callShipDrawCheckLineUnderMapCells(SeaBattleCoord mouseCoord, int countDecks) {
        shipsPlaceMap.clearHover();

        // Создаем линию по ( 25 * ( кол-во палуб / 2 ) ) пикселей влево и вправо ( если повернули корабль ), иначе вверх и вниз
        double value = ((double) countDecks / 2) * 25;

        double startX = isRotate ? mouseCoord.getX() - value : mouseCoord.getX();
        double startY = isRotate ? mouseCoord.getY() : mouseCoord.getY() - value;

        double endX = isRotate ? mouseCoord.getX() + value : mouseCoord.getX();
        double endY = isRotate ? mouseCoord.getY() : mouseCoord.getY() + value;

        List<SeaBattleMapEditableCell> cellsDrawArray = new ArrayList<>();
        SeaBattleMapCell[][] cellsArrayFromMap = shipsPlaceMap.getCellMap();

        // Проходимся по всем ячейкам и проверяем, находится ли наша линия на этих ячейках и свободны ли они
        for (int row = 0; row < cellsArrayFromMap.length; row++) {
            for (int col = 0; col < cellsArrayFromMap[row].length; col++) {
                if (cellsArrayFromMap[row][col] instanceof SeaBattleMapEditableCell cell) {
                    Bounds cellBoundsInParent = cell.getBoundsInParent();

                    if (doesLineIntersectRectangle(startX, startY, endX, endY, cellBoundsInParent.getMinX() + 2, cellBoundsInParent.getMinY() + 2, cellBoundsInParent.getMaxX() - 2, cellBoundsInParent.getMaxY() - 2)) {
                        if (cell.isBusy()) return;
                        if (cellsDrawArray.size() != countDecks) cellsDrawArray.add(cell);
                    }
                }
            }
        }

        if (cellsDrawArray.size() != countDecks) return;


        // Получаем минимальную ячейку и максимальную
        int minRow = Math.max(0, GridPane.getRowIndex(cellsDrawArray.getFirst()) - 2);
        int minCol = Math.max(0, GridPane.getColumnIndex(cellsDrawArray.getFirst()) - 2);
        int maxRow = Math.min(SIZE_MAP - 1, GridPane.getRowIndex(cellsDrawArray.getLast()) + 1);
        int maxCol = Math.min(SIZE_MAP - 1, GridPane.getColumnIndex(cellsDrawArray.getLast()) + 1);

        for (int row = minRow; row < maxRow; row++) {
            for (int col = minCol; col < maxCol; col++) {
                if (cellsArrayFromMap[row][col] instanceof SeaBattleMapEditableCell cell) {
                    if (cell.isBusy()) return;
                }
            }
        }

        // Проходимся по всем ячейкам и делаем им HOVER ( то есть, что эти ячейки выбраны для корабля )
        for (SeaBattleMapEditableCell cell : cellsDrawArray) {
            if (!cell.getStyleClass().contains("map-cell-hover")) cell.getStyleClass().add("map-cell-hover");
        }
    }

    /**
     * Накладывает корабль на карту
     * @param shipDraw Корабль
     */
    public void callShipPlaceShipOnMap(SeaBattleShipDraw shipDraw) {
        List<SeaBattleMapEditableCell> cellsDrawArray = new ArrayList<>();
        SeaBattleMapCell[][] cellsArrayFromMap = shipsPlaceMap.getCellMap();

        // Проходимся по всем ячейкам, и те, у которых HOVER - накладываем туда наш корабль
        for (int row = 0; row < cellsArrayFromMap.length; row++) {
            for (int col = 0; col < cellsArrayFromMap[row].length; col++) {
                if (cellsArrayFromMap[row][col] instanceof SeaBattleMapEditableCell cell) {
                    if (cell.getStyleClass().contains("map-cell-hover")) {
                        if (cellsDrawArray.size() != shipDraw.getCountDecks()) cellsDrawArray.add(cell);
                    }
                }
            }
        }

        // Если ячеек не столько, сколько у корябля палуб - то не накладываем
        if (cellsDrawArray.size() != shipDraw.getCountDecks()) return;

        // Удаляем корабль из боковых панелей
        shipsLeftPart.getChildren().remove(shipDraw);
        shipsRightPart.getChildren().remove(shipDraw);

        // Проходимся по списку и накладываем корабль
        for (SeaBattleMapEditableCell cell : cellsDrawArray) {
            cell.getStyleClass().remove("map-cell-hover");
            cell.getStyleClass().add("map-cell-ship");
            cell.setShip(selectedShip);
        }

        // Убираем выбор текущего корабля
        if (selectedShip.equals(shipDraw)) {
            selectedShip.getStyleClass().remove("map-cell-ship-selected");
            selectedShip = null;
        }
    }

    /**
     * Проверяет, находится ли линия внутри прямоугольника или пересекает ли его
     * @param lineStartX Начало линии по X
     * @param lineStartY Начало линии по Y
     * @param lineEndX Конец линии по X
     * @param lineEndY Конец линии по Y
     * @param rectMinX Вверхняя левая граница прямоугольника по X
     * @param rectMinY Вверхняя левая граница прямоугольника по Y
     * @param rectMaxX Нижняя правая граница прямоугольника по X
     * @param rectMaxY Нижняя правая граница прямоугольника по Y
     * @return Если линия находится внутри или пересекает прямоугольник - True. Иначе - False
     */
    private boolean doesLineIntersectRectangle(double lineStartX, double lineStartY, double lineEndX, double lineEndY, double rectMinX, double rectMinY, double rectMaxX, double rectMaxY) {
        return intersects(lineStartX, lineStartY, lineEndX, lineEndY, rectMinX, rectMinY, rectMaxX, rectMinY) || // верхняя грань
                intersects(lineStartX, lineStartY, lineEndX, lineEndY, rectMaxX, rectMinY, rectMaxX, rectMaxY) || // правая грань
                intersects(lineStartX, lineStartY, lineEndX, lineEndY, rectMaxX, rectMaxY, rectMinX, rectMaxY) || // нижняя грань
                intersects(lineStartX, lineStartY, lineEndX, lineEndY, rectMinX, rectMaxY, rectMinX, rectMinY) || // левая грань
                (lineStartX >= rectMinX && lineStartX <= rectMaxX && lineStartY >= rectMinY && lineStartY <= rectMaxY) || // начальная точка внутри
                (lineEndX >= rectMinX && lineEndX <= rectMaxX && lineEndY >= rectMinY && lineEndY <= rectMaxY);   // конечная точка внутри
    }


    private boolean intersects(double x1, double y1, double x2, double y2,
                                      double x3, double y3, double x4, double y4) {
        double d1 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
        double d2 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3);
        double d3 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
        double d4 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1);

        return ((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) &&
                ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0)) ||
                (d1 == 0 && x1 >= Math.min(x3, x4) && x1 <= Math.max(x3, x4) &&
                        y1 >= Math.min(y3, y4) && y1 <= Math.max(y3, y4)) ||
                (d2 == 0 && x2 >= Math.min(x3, x4) && x2 <= Math.max(x3, x4) &&
                        y2 >= Math.min(y3, y4) && y2 <= Math.max(y3, y4)) ||
                (d3 == 0 && x3 >= Math.min(x1, x2) && x3 <= Math.max(x1, x2) &&
                        y3 >= Math.min(y1, y2) && y3 <= Math.max(y1, y2)) ||
                (d4 == 0 && x4 >= Math.min(x1, x2) && x4 <= Math.max(x1, x2) &&
                        y4 >= Math.min(y1, y2) && y4 <= Math.max(y1, y2));
    }

    @Override
    public boolean callMapIsSelectedShip() {
        return selectedShip != null;
    }
}
