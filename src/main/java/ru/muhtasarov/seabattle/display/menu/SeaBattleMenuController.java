package ru.muhtasarov.seabattle.display.menu;

import javafx.geometry.Bounds;
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

    private VBox shipsLeftPart;
    private VBox shipsRightPart;

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

        shipsRightPart = new VBox();
        shipsRightPart.setSpacing(5);

        SeaBattleShipDraw oneShip1 = new SeaBattleShipDraw(this, 1, shipsRightPart);
        SeaBattleShipDraw oneShip2 = new SeaBattleShipDraw(this, 1, shipsLeftPart);
        SeaBattleShipDraw oneShip3 = new SeaBattleShipDraw(this, 1, shipsLeftPart);
        SeaBattleShipDraw oneShip4 = new SeaBattleShipDraw(this, 1, shipsLeftPart);

        SeaBattleShipDraw twoShip1 = new SeaBattleShipDraw(this, 2, shipsLeftPart);
        SeaBattleShipDraw twoShip2 = new SeaBattleShipDraw(this, 2, shipsLeftPart);
        SeaBattleShipDraw twoShip3 = new SeaBattleShipDraw(this, 2, shipsRightPart);

        SeaBattleShipDraw threeShip1 = new SeaBattleShipDraw(this, 3, shipsLeftPart);
        SeaBattleShipDraw threeShip2 = new SeaBattleShipDraw(this, 3, shipsRightPart);

        SeaBattleShipDraw fourShip1 = new SeaBattleShipDraw(this, 4, shipsRightPart);


        shipPlacementHBox.getChildren().addAll(shipsLeftPart, shipsPlaceMap, shipsRightPart);

        Button randomPlacementButton = new Button("Расположить автоматически");
        randomPlacementButton.getStyleClass().add("gui-button");
        randomPlacementButton.setOnAction(event -> {
            selectedShip = null;
            List<SeaBattleShip> ships = new ArrayList<>(List.of(fourShip1, threeShip1, threeShip2, twoShip1, twoShip2, twoShip3, oneShip1, oneShip2, oneShip3, oneShip4));
            for (SeaBattleShip ship : ships) {
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
            SeaBattleRandomPlaceShips.placeShips(shipsPlaceMap, ships);
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

        Button backPlacementButton = new Button("Назад");
        backPlacementButton.getStyleClass().add("gui-button");
        backPlacementButton.setOnAction(event -> primaryAnchorPane.getChildren().setAll(startGameVBox));
        BorderPane backPlacementBorderPane = new BorderPane();
        backPlacementBorderPane.setCenter(backPlacementButton);
        VBox.setVgrow(backPlacementBorderPane, Priority.NEVER);

        Pane emptyPaneSix = new StackPane();
        VBox.setVgrow(emptyPaneSix, Priority.ALWAYS);

        shipPlacementVBox.getChildren().addAll(emptyPaneFive, shipPlacementBorderPane, shipPlacementHBox, randomPlacementBorderPane, backPlacementBorderPane, emptyPaneSix);
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

    private void callShipDrawCheckLineUnderMapCells(SeaBattleCoord mouseCoord, int countDecks) {
        shipsPlaceMap.clearHover();

        double value = ((double) countDecks / 2) * 25;

        double startX = isRotate ? mouseCoord.getX() - value : mouseCoord.getX();
        double startY = isRotate ? mouseCoord.getY() : mouseCoord.getY() - value;

        double endX = isRotate ? mouseCoord.getX() + value : mouseCoord.getX();
        double endY = isRotate ? mouseCoord.getY() : mouseCoord.getY() + value;

        List<SeaBattleMapEditableCell> cellsDrawArray = new ArrayList<>();
        SeaBattleMapCell[][] cellsArrayFromMap = shipsPlaceMap.getCellMap();
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

        int minRow = Math.max(0, GridPane.getRowIndex(cellsDrawArray.getFirst()) - 2);
        int minCol = Math.max(0, GridPane.getColumnIndex(cellsDrawArray.getFirst()) - 2);
        int maxRow = Math.min(SIZE_MAP - 1, GridPane.getRowIndex(cellsDrawArray.getLast()));
        int maxCol = Math.min(SIZE_MAP - 1, GridPane.getColumnIndex(cellsDrawArray.getLast()));

        for (int row = minRow; row < maxRow; row++) {
            for (int col = minCol; col < maxCol; col++) {
                if (cellsArrayFromMap[row][col] instanceof SeaBattleMapEditableCell cell) {
                    if (cell.isBusy()) return;
                }
            }
        }

        for (SeaBattleMapEditableCell cell : cellsDrawArray) {
            if (!cell.getStyleClass().contains("map-cell-hover")) cell.getStyleClass().add("map-cell-hover");
        }
    }

    public void callShipPlaceShipOnMap(SeaBattleShipDraw shipDraw) {
        List<SeaBattleMapEditableCell> cellsDrawArray = new ArrayList<>();
        SeaBattleMapCell[][] cellsArrayFromMap = shipsPlaceMap.getCellMap();
        for (int row = 0; row < cellsArrayFromMap.length; row++) {
            for (int col = 0; col < cellsArrayFromMap[row].length; col++) {
                if (cellsArrayFromMap[row][col] instanceof SeaBattleMapEditableCell cell) {
                    if (cell.getStyleClass().contains("map-cell-hover")) {
                        if (cellsDrawArray.size() != shipDraw.getCountDecks()) cellsDrawArray.add(cell);
                    }
                }
            }
        }

        if (cellsDrawArray.size() != shipDraw.getCountDecks()) return;

        shipsLeftPart.getChildren().remove(shipDraw);
        shipsRightPart.getChildren().remove(shipDraw);

        for (SeaBattleMapEditableCell cell : cellsDrawArray) {
            cell.getStyleClass().remove("map-cell-hover");
            cell.getStyleClass().add("map-cell-ship");
            cell.setShip(selectedShip);
        }

        if (selectedShip.equals(shipDraw)) {
            selectedShip.getStyleClass().remove("map-cell-ship-selected");
            selectedShip = null;
        }
    }

    private boolean doesLineIntersectRectangle(double x1, double y1, double x2, double y2, double rx0, double ry0, double rx1, double ry1) {
        return intersects(x1, y1, x2, y2, rx0, ry0, rx1, ry0) || // верхняя грань
                intersects(x1, y1, x2, y2, rx1, ry0, rx1, ry1) || // правая грань
                intersects(x1, y1, x2, y2, rx1, ry1, rx0, ry1) || // нижняя грань
                intersects(x1, y1, x2, y2, rx0, ry1, rx0, ry0) || // левая грань
                (x1 >= rx0 && x1 <= rx1 && y1 >= ry0 && y1 <= ry1) || // начальная точка внутри
                (x2 >= rx0 && x2 <= rx1 && y2 >= ry0 && y2 <= ry1);   // конечная точка внутри
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
