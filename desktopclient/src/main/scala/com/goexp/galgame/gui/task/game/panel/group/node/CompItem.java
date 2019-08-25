package com.goexp.galgame.gui.task.game.panel.group.node;


public class CompItem extends DefaultItem {
    public final String comp;

    public CompItem(String title, int count, String comp) {
        super(title, count);
        this.comp = comp;
    }
}