package me.ellebelle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Repository<T> {

    private final ArrayList<T> items = new ArrayList<>();

    // Metod som tar emot ett objekt av samma typ som Repository har fått.
    public void add(T item) {
        items.add(item);
    }

    // Metod som returnerar en lista med objket av samma typ som Repository har fått.
    public List<T> findAll() {
        return items;
    }

    // Metod som returnerar de objekt som uppfyller ett vist villkor. Returnerar en lista med träffarna.
    public List<T> findWhere(Predicate<T> condition) {
        List<T> matches = new ArrayList<>();
        for (T item : items) {
            if (condition.test(item)) {
                matches.add(item);
            }
        }
        return matches;
    }

}

