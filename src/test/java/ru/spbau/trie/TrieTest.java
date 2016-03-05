package ru.spbau.trie;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TrieTest {

    @Test
    public void testSimple() {
        Trie trie = instance();

        assertTrue(trie.add("abc"));
        assertTrue(trie.contains("abc"));
        assertEquals(1, trie.size());
        assertEquals(1, trie.howManyStartsWithPrefix("abc"));
    }

    @Test
    public void testSimpleSerialization() throws IOException {
        Trie trie = instance();

        assertTrue(trie.add("abc"));
        assertTrue(trie.add("cde"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ((StreamSerializable) trie).serialize(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Trie newTrie = instance();
        ((StreamSerializable) newTrie).deserialize(inputStream);

        assertTrue(newTrie.contains("abc"));
        assertTrue(newTrie.contains("cde"));
    }

    @Test
    public void testSerialization() throws IOException {
        Trie trie = instance();

        assertTrue(trie.add(""));
        assertTrue(trie.add("a"));
        assertTrue(trie.add("aa"));
        assertTrue(trie.add("aaa"));
        assertTrue(trie.add("ab"));
        assertTrue(trie.add("abb"));
        assertTrue(trie.add("bbb"));

        assertEquals(7, trie.size());
        assertEquals(5, trie.howManyStartsWithPrefix("a"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ((StreamSerializable) trie).serialize(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Trie newTrie = instance();
        ((StreamSerializable) newTrie).deserialize(inputStream);

        assertEquals(7, newTrie.size());
        assertEquals(5, newTrie.howManyStartsWithPrefix("a"));

        assertTrue(newTrie.contains(""));
        assertTrue(newTrie.contains("a"));
        assertTrue(newTrie.contains("aa"));
        assertTrue(newTrie.contains("aaa"));
        assertTrue(newTrie.contains("ab"));
        assertTrue(newTrie.contains("abb"));
        assertTrue(newTrie.contains("bbb"));

        assertFalse(newTrie.contains("bb"));
        assertFalse(newTrie.contains("ba"));
    }

    @Test(expected=IOException.class)
    public void testSimpleSerializationFails() throws IOException {
        Trie trie = instance();

        assertTrue(trie.add("abc"));
        assertTrue(trie.add("cde"));

        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Fail");
            }
        };

        ((StreamSerializable) trie).serialize(outputStream);
    }

    public static Trie instance() {
        try {
            return (Trie) Class.forName("ru.spbau.trie.TrieImpl").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
