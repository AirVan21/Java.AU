package ru.spbau.trie;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by airvan21 on 22.02.16.
 */
public class TrieDataTest {

    private TrieData trie;

    @Before
    public void beforeTest() {
        trie = new TrieData();
    }

    @Test
    public void addTest() {
        assertEquals(0, trie.size());

        assertTrue(trie.add("test"));
        assertEquals(1, trie.size());

        assertFalse(trie.add("test"));
        assertEquals(1, trie.size());

        assertTrue(trie.add("element"));
        assertEquals(2, trie.size());

        assertFalse(trie.contains(""));
        assertTrue(trie.add(""));
        assertEquals(3, trie.size());

        assertTrue(trie.contains("test"));
        assertTrue(trie.contains("element"));
        assertTrue(trie.contains(""));
    }

    @Test
    public void containsTest() {
        trie.add("test");
        assertEquals(1, trie.size());
        assertTrue(trie.contains("test"));

        trie.add("testt");
        assertEquals(2, trie.size());
        assertTrue(trie.contains("test"));
        assertTrue(trie.contains("testt"));

        assertTrue(trie.add(""));
        assertEquals(3, trie.size());
        assertTrue(trie.contains(""));

        trie.add("element");
        assertEquals(4, trie.size());
        assertTrue(trie.contains("element"));

        assertFalse(trie.contains("elem"));
        assertFalse(trie.contains("ent"));
        assertFalse(trie.contains("tes"));
    }

    @Test
    public void removeTest() {
        assertFalse(trie.remove(""));
        assertEquals(0, trie.size());

        trie.add("test");
        trie.add("element");
        trie.add("AU");

        assertEquals(3, trie.size());

        assertTrue(trie.remove("element"));
        assertEquals(2, trie.size());

        assertTrue(trie.remove("test"));
        assertEquals(1, trie.size());

        assertFalse(trie.remove("te"));
        assertFalse(trie.remove("A"));

        assertTrue(trie.remove("AU"));
        assertEquals(0, trie.size());

        trie.add("a");
        trie.add("aa");
        trie.add("aaa");
        assertEquals(3, trie.size());

        assertTrue(trie.remove("aa"));
        assertTrue(trie.contains("a"));
        assertTrue(trie.contains("aaa"));
        assertEquals(2, trie.size());

        trie.add("");
        assertEquals(3, trie.size());
        assertTrue(trie.remove(""));
        assertEquals(2, trie.size());

        assertTrue(trie.remove("a"));
        assertTrue(trie.remove("aaa"));
        assertEquals(0, trie.size());
    }

    @Test
    public void sizeTest() {
        assertEquals(0, trie.size());

        trie.add("a");
        trie.add("aa");
        trie.add("aaa");
        assertEquals(3, trie.size());

        trie.remove("a");
        assertEquals(2, trie.size());

        trie.remove("aa");
        assertEquals(1, trie.size());

        trie.remove("aaa");
        assertEquals(0, trie.size());
    }

    @Test
    public void howManyStartsWithPrefixTest() {
        trie.add("a");
        trie.add("aa");
        trie.add("aaa");

        assertEquals(3, trie.howManyStartsWithPrefix("a"));
        assertEquals(2, trie.howManyStartsWithPrefix("aa"));
        assertEquals(1, trie.howManyStartsWithPrefix("aaa"));

        trie.add("");
        assertEquals(4, trie.howManyStartsWithPrefix(""));

        trie.remove("aa");
        assertEquals(3, trie.howManyStartsWithPrefix(""));

        trie.add("ab");
        assertEquals(3, trie.howManyStartsWithPrefix("a"));

        trie.remove("ab");
        trie.remove("aaa");
        trie.remove("a");

        assertEquals(0, trie.howManyStartsWithPrefix("a"));
        assertEquals(1, trie.howManyStartsWithPrefix(""));

        trie.remove("");
        assertEquals(0, trie.howManyStartsWithPrefix(""));
    }
}