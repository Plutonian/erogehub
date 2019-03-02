package com.goexp.galgame.data.task.download.contentprovider.game;

import com.goexp.galgame.data.task.download.contentprovider.ContentProvider;

public class LocalProvider implements ContentProvider {

    @Override
    public byte[] getContent(int id) {
        return new byte[0];
    }
}
