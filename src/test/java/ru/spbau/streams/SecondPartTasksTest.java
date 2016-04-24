package ru.spbau.streams;

import org.junit.Test;

import java.io.UncheckedIOException;
import java.util.*;

import static org.junit.Assert.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        final String path1 = "src/test/resources/text1.txt";
        final String path2 = "src/test/resources/text2.txt";
        final List<String> paths = Arrays.asList(path1, path2);

        // first text entry
        final int washingtonWordCount = 2;
        final String washington = "Вашингтон";
        List<String> resultList = SecondPartTasks.findQuotes(paths, washington);
        assertEquals(washingtonWordCount, resultList.size());
        resultList
                .forEach(quote -> assertTrue(quote.contains(washington)));

        // second text entry
        final int nameWordCount = 2;
        final String name = "Смолов";
        resultList = SecondPartTasks.findQuotes(paths, name);
        assertEquals(nameWordCount, resultList.size());
        resultList
                .forEach(quote -> assertTrue(quote.contains(name)));

        // both text entry
        final int crossWordCount = 3;
        final String crossWord = "лет";
        resultList = SecondPartTasks.findQuotes(paths, crossWord);
        assertEquals(crossWordCount, resultList.size());
        resultList
                .forEach(quote -> assertTrue(quote.contains(crossWord)));
    }

    @Test(expected=UncheckedIOException.class)
    public void testFindQuotesWrongFile() {
        final String wrongPath = "src/test/resources/text3.txt";
        SecondPartTasks.findQuotes(Collections.singletonList(wrongPath), "dummy");
    }

    @Test
    public void testPiDividedBy4() {
        assertEquals(Math.PI / 4, SecondPartTasks.piDividedBy4(), 0.001);
    }

    @Test
    public void testFindPrinter() {
        final List<String> works1 = Arrays.asList("Kapitanskaya dochka", "Boris Godunov", "Dubrovsky");
        final List<String> works2 = Arrays.asList("Dark Avenues", "Flowers of the Field");
        final Map<String, List<String>> library = new HashMap<>();

        library.put("Pushkin", works1);
        library.put("Bunin", works2);
        library.put("", Collections.emptyList());

        assertEquals("Pushkin", SecondPartTasks.findPrinter(library));
        assertNull(SecondPartTasks.findPrinter(Collections.emptyMap()));
    }

    @Test
    public void testCalculateGlobalOrder() {
        final Map<String, Integer> map1 = new HashMap<>();
        final Map<String, Integer> map2 = new HashMap<>();
        final Map<String, Integer> map3 = Collections.emptyMap();

        map1.put("Tomato", 10);
        map1.put("Potato", 20);
        map1.put("Mango",  30);
        map1.put("Watermelon", 50);

        map2.put("Tomato", 30);
        map2.put("Mango",  30);
        map2.put("Apple", 100);
        map2.put("Pumpkin", 200);

        final List<Map<String, Integer>> orders = Arrays.asList(map1, map2, map3);
        final Map<String, Integer> globalOrder = SecondPartTasks.calculateGlobalOrder(orders);

        assertEquals(6, globalOrder.size());

        assertEquals(40,  (int)globalOrder.get("Tomato"));
        assertEquals(20,  (int)globalOrder.get("Potato"));
        assertEquals(60,  (int)globalOrder.get("Mango"));
        assertEquals(100, (int)globalOrder.get("Apple"));
        assertEquals(200, (int)globalOrder.get("Pumpkin"));
        assertEquals(50,  (int)globalOrder.get("Watermelon"));
    }

    @Test
    public void testCalculateGlobalOrderEmpty() {
        final Map<String, Integer> globalOrder = SecondPartTasks.calculateGlobalOrder(Collections.emptyList());
        assertTrue(globalOrder.isEmpty());
    }
}