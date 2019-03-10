package com.goexp.galgame.data.task.client.error;


import com.goexp.galgame.data.Config;

import java.nio.file.Path;

public class GameTaskErrorProcessor extends AbstractTaskErrorProcessor {

    @Override
    Path getErrorPath() {
        return Config.GAME_ERROR_FILE;
    }
}
