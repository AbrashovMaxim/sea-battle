package ru.muhtasarov.seabattle.core;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class SeaBattleJavaFX {

    public static void setAnchors(Node node, Double value) {
        setAnchors(node, value, value, value, value);
    }

    public static void setAnchors(Node node, Double topAnchorValue, Double rightAnchorValue, Double bottomAnchorValue, Double leftAnchorValue) {
        AnchorPane.setTopAnchor(node, topAnchorValue);
        AnchorPane.setRightAnchor(node, rightAnchorValue);
        AnchorPane.setBottomAnchor(node, bottomAnchorValue);
        AnchorPane.setLeftAnchor(node, leftAnchorValue);
    }

}
