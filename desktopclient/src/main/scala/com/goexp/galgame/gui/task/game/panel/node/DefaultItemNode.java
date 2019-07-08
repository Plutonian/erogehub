package com.goexp.galgame.gui.task.game.panel.node;

public abstract class DefaultItemNode {
    public String title;
    public int count;

    public DefaultItemNode(String title, int count) {
        this.title = title;
        this.count = count;
    }
}
