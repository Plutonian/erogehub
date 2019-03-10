package com.goexp.galgame.data.task.client.error;

import com.goexp.galgame.data.Config;

import java.nio.file.Path;

public class BrandTaskErrorProcessor extends AbstractTaskErrorProcessor {

    @Override
    Path getErrorPath() {
        return Config.BRAND_ERROR_FILE;
    }
}
