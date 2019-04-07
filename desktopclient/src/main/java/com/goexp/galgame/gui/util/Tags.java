package com.goexp.galgame.gui.util;

import javafx.scene.Node;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Tags {

    public static List<Node> toNodes(List<String> tag) {
        Objects.requireNonNull(tag);

        return toNodes(tag, str -> {
            var tagLabel = new Label(str);
            tagLabel.getStyleClass().add("tag");

            return tagLabel;
        });
    }

    public static List<Node> toNodes(List<String> tag, Function<String, Node> mapper) {
        Objects.requireNonNull(tag);
        Objects.requireNonNull(mapper);

        return tag.stream()
                .filter(str -> !str.isEmpty())
                .map(mapper)
                .collect(Collectors.toUnmodifiableList());
    }
}
