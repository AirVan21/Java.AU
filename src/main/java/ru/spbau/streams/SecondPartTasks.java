package ru.spbau.streams;

import org.apache.commons.lang3.tuple.Pair;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths
                .stream()
                .flatMap(path -> {
                        try {
                            return Files.lines(Paths.get(path));
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                })
                .filter(line -> line.contains(sequence))
                .collect(toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        final int testSize  = 1_000_000;

        final Random rand = new Random();
        final Supplier<Double> random = () -> rand.nextDouble() - 1;

        final double hit = Stream
                .generate(() -> Pair.of(random.get(), random.get()))
                .limit(testSize)
                .filter(point -> Math.pow(point.getKey(), 2) + Math.pow(point.getValue(), 2) <= 1)
                .count();

        return hit / testSize;
    }


    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions
                .entrySet()
                .stream()
                .map(item ->  Pair.of(item.getKey(), item
                        .getValue()
                        .stream()
                        .mapToInt(String::length)
                        .sum()))
                .max(Comparator.comparingInt(Pair::getValue))
                .orElse(Pair.of(null, null))
                .getKey();

    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders
                .stream()
                .flatMap(order -> order
                        .entrySet()
                        .stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
    }
}
