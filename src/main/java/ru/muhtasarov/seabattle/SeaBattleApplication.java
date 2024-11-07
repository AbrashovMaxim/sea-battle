package ru.muhtasarov.seabattle;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.muhtasarov.seabattle.bot.SeaBattleBot;
import ru.muhtasarov.seabattle.bot.SeaBattleBotController;
import ru.muhtasarov.seabattle.display.SeaBattleDisplay;
import ru.muhtasarov.seabattle.display.SeaBattleDisplayController;

public class SeaBattleApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SeaBattleLogic logic = new SeaBattleLogic();

        SeaBattleBot bot = new SeaBattleBotController(logic);
        SeaBattleDisplay display = new SeaBattleDisplayController(logic);

        logic.setBot(bot);
        logic.setDisplay(display);

        logic.initialize();
    }
}
