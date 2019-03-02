package com.goexp.galgame.common.model;

import java.util.*;
import java.util.stream.Collectors;

public class CV {
    public String name;
    public String nameStr;


    public int star;
    public String otherName;


    @Override
    public String toString() {
        return "CV{" +
                "name='" + name + '\'' +
                ", nameStr='" + nameStr + '\'' +
                ", star=" + star +
                ", otherName='" + otherName + '\'' +
                '}';
    }

    public static Map<String, CV> getMap(List<CV> cvList) {
        return cvList.stream()
                .map(cv -> {
                    return Arrays.stream(cv.nameStr.split("[=＝]"))
                            .map(name -> {
                                var c = new CV();
                                c.name=cv.name;
                                c.otherName = name.trim().toLowerCase();

                                return c;

                            });
                })
                .flatMap(s -> s)
                .collect(Collectors.toMap((CV e) -> e.otherName, s -> s));
    }
}