package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum SearchFilmsBy {
    TITLE("title"),
    DIRECTOR("director");

    private final String value;

    SearchFilmsBy(String value) {
        this.value = value;
    }

    public static List<SearchFilmsBy> parseStr(String value) {
        String[] splittedStr = value.trim().toLowerCase().split(",");
        List<SearchFilmsBy> parsedList = new ArrayList<>();

        for (String str : splittedStr) {
            SearchFilmsBy searchFilmsBy = SearchFilmsBy.fromString(str);
            parsedList.add(searchFilmsBy);
        }

        return parsedList;
    }

    public static SearchFilmsBy fromString(String value) {
        return switch (value) {
            case "title" -> SearchFilmsBy.TITLE;
            case "director" -> SearchFilmsBy.DIRECTOR;
            default -> null;
        };
    }

}
