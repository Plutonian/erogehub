package com.goexp.galgame.common.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class CV {
    public String name;
    public String nameStr;


    public int star;
    public String otherName;

    public static Map<String, CV> getMap(List<CV> cvList) {
        return cvList.stream()
                .flatMap(cv -> {
                    return Arrays.stream(cv.nameStr.split("[=＝]"))
                            .map(name -> {
                                var c = new CV();
                                c.name = cv.name;
                                c.otherName = name.trim().toLowerCase();

                                return c;

                            });
                })
                .collect(Collectors.toUnmodifiableMap(cv -> cv.otherName, cv -> cv));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CV.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("nameStr='" + nameStr + "'")
                .add("star=" + star)
                .add("otherName='" + otherName + "'")
                .toString();
    }
}