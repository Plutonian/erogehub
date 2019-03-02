package com.goexp.galgame.data.task.client.error;

import com.goexp.galgame.data.task.download.provider.IdsProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractTaskErrorProcessor implements IdsProvider {
    @Override
    public List<Integer> getIds() {
        try {
            if (Files.exists(getErrorPath())) {


                var ids = Files.readAllLines(getErrorPath())
                        .stream()
                        .filter(line -> line != null && line.length() > 0)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());


                Files.delete(getErrorPath());

                return ids;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return List.of();
    }

    public void recordError(String errorMes) {
        System.err.println("Error:" + errorMes);
        try {
            Files.writeString(getErrorPath(), errorMes + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    abstract Path getErrorPath();
}
