package com.goexp.galgame.gui.task.game.panel.node;


public class CompItemNode extends DefaultItemNode {
    public final String comp;

    public CompItemNode(String title, int count, String comp) {
        super(title, count);
        this.comp = comp;
    }
}