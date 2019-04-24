package com.goexp.galgame.gui.view.game.listview.sidebar.node;


public class CompItemNode extends DefaultItemNode {
    public String comp;

    public CompItemNode(String title, int count, String comp) {
        super(title, count);
        this.comp = comp;
    }
}