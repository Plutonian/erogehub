package com.goexp.galgame.gui.util;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UIUtil {

    public static List<Node> createTag(String tag) {
        return createSet(tag, label -> {
            label.getStyleClass().add("tag");
        });

    }

    public static List<Node> createTag(List<String> tag) {
        return createSet(tag, label -> {
            label.getStyleClass().add("tag");
        });

    }
    //

    public static List<Node> createSet(List<String> tag, Consumer<Node> consumer) {
        return tag.stream()
                .map(str -> {
                    var tagLabel = new Label(str);

                    if (consumer != null)
                        consumer.accept(tagLabel);

                    return tagLabel;
                }).collect(Collectors.toUnmodifiableList());

    }

    public static List<Node> createSet(String tag, Consumer<Node> consumer) {
        return Stream.of(tag.split("、"))
                .map(str -> {
                    var tagLabel = new Label(str);

                    if (consumer != null)
                        consumer.accept(tagLabel);

                    return tagLabel;
                }).collect(Collectors.toUnmodifiableList());

    }

    public static List<Node> createSetLink(String tag, Consumer<Node> consumer) {
        return Stream.of(tag.split("、"))
                .map(str -> {
                    var tagLabel = new Hyperlink(str);

                    if (consumer != null)
                        consumer.accept(tagLabel);

                    return tagLabel;
                }).collect(Collectors.toUnmodifiableList());

    }

    public static List<Node> createSetLink(List<String> tag, Consumer<Node> consumer) {
        return tag.stream()
                .map(str -> {
                    var tagLabel = new Hyperlink(str);

                    if (consumer != null)
                        consumer.accept(tagLabel);

                    return tagLabel;
                }).collect(Collectors.toUnmodifiableList());

    }
}
