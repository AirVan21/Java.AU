package ru.spbau.streams;

import org.junit.Test;

import java.io.UncheckedIOException;
import java.util.*;

import static org.junit.Assert.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        final String path1 = "./data/text1.txt";
        final String path2 = "./data/text2.txt";
        final List<String> paths = new ArrayList<>();
        paths.add(path1);
        paths.add(path2);

        // first text entry
        final int washingtonWordCount = 2;
        final String washington = "Вашингтон";
        List<String> resultList = SecondPartTasks.findQuotes(paths, washington);
        assertEquals(washingtonWordCount, resultList.size());
        assertTrue(resultList.contains("В плей-офф \"Вашингтон\", как выяснилось ранее, сыграет с \"Филадельфией\"."));

        // second text entry
        final int nameWordCount = 2;
        final String name = "Смолов";
        resultList = SecondPartTasks.findQuotes(paths, name);
        assertEquals(nameWordCount, resultList.size());
        assertTrue(resultList.contains("В Екатеринбурге справедливо считают," +
                " что год в \"Урале\" сделал из Смолова человека и бомбардира."));

        // both text entry
        final int crossWordCount = 3;
        final String crossWord = "лет";
        resultList = SecondPartTasks.findQuotes(paths, crossWord);
        assertEquals(crossWordCount, resultList.size());
        assertTrue(resultList.contains("До прихода туда форвард за много лет на высшем уровне " +
                "забил едва ли больше десятка мячей,"));
    }

    @Test(expected=UncheckedIOException.class)
    public void testFindQuotesWrongFile() {
        final String wrong_path = "./data/text3.txt";
        final List<String> paths = new ArrayList<>();
        paths.add(wrong_path);

        SecondPartTasks.findQuotes(paths, "dummy");
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
        library.put("", new ArrayList<>());

        assertEquals("Pushkin", SecondPartTasks.findPrinter(library));
    }

    @Test
    public void testFindPrinterEmpty() {
        final Map<String, List<String>> library = new HashMap<>();
        assertEquals("", SecondPartTasks.findPrinter(library));
    }

    @Test
    public void testCalculateGlobalOrder() {
        final Map<String, Integer> map1 = new HashMap<>();
        final Map<String, Integer> map2 = new HashMap<>();
        final Map<String, Integer> map3 = new HashMap<>();

        map1.put("Tomato", 10);
        map1.put("Potato", 20);
        map1.put("Mango",  30);
        map1.put("Watermelon", 50);

        map2.put("Tomato", 30);
        map2.put("Mango",  30);
        map2.put("Apple", 100);
        map2.put("Pumpkin", 200);

        final List<Map<String, Integer>> orders = new ArrayList<>();
        orders.add(map1);
        orders.add(map2);
        orders.add(map3);

        final Map<String, Integer> globalOrder = SecondPartTasks.calculateGlobalOrder(orders);

        assertEquals(6, globalOrder.size());

        assertEquals(40, (int)globalOrder.get("Tomato"));
        assertEquals(20, (int)globalOrder.get("Potato"));
        assertEquals(60, (int)globalOrder.get("Mango"));
        assertEquals(100, (int)globalOrder.get("Apple"));
        assertEquals(200, (int)globalOrder.get("Pumpkin"));
        assertEquals(50, (int)globalOrder.get("Watermelon"));

        assertFalse(globalOrder.containsKey("Lemon"));
    }

    @Test
    public void testCalculateGlobalOrderEmpty() {
        final List<Map<String, Integer>> orders = new ArrayList<>();
        final Map<String, Integer> globalOrder = SecondPartTasks.calculateGlobalOrder(orders);

        assertFalse(globalOrder.containsKey(""));
        assertFalse(globalOrder.containsKey("Lemon"));

        assertTrue(globalOrder.isEmpty());
    }
}