package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Getter
public enum SearchFilmsBy {
    TITLE("title"),
    DIRECTOR("director");

    private final String value;

    SearchFilmsBy(String value) {
        this.value = value;
    }

    public static <T extends Throwable> List<SearchFilmsBy> parseStrOrThrow(String value,
                                                                            Supplier<T> exceptionSupplier) throws T {
        String[] splittedStr = value.trim().toLowerCase().split(",");
        List<SearchFilmsBy> parsedList = new ArrayList<>();

        for (String str : splittedStr) {
            parsedList.add(SearchFilmsBy.fromString(str)
                    .orElseThrow(exceptionSupplier));
        }

        return parsedList;
    }

    public static Optional<SearchFilmsBy> fromString(String value) {
        return switch (value) {
            case "title" -> Optional.of(TITLE);
            case "director" -> Optional.of(DIRECTOR);
            default -> Optional.empty();
        };
    }

}
