package ru.muhtasarov.seabattle;

public class SeaBattleLaunch {

    public static void main(String[] args) {
        try {
            System.out.println("\n\t\tSEA-BATTLE - START!!!\n");
            SeaBattleApplication.launch(SeaBattleApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\n\t\tSEA-BATTLE - START FAILED!!!\n");
        }
    }

}
