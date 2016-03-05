package ru.spbau.trie;

import java.io.*;

/**
 * Created by airvan21 on 21.02.16.
 */
public class TrieImpl implements Trie, StreamSerializable {

    private TreeNode root;
    private int size;

    public TrieImpl() {
        size = 0;
        root = new TreeNode();
    }

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }

        TreeNode current = root;
        for (char letter : element.toCharArray()) {
            current.increaseNumberOfTerminalsInSubtree();
            TreeNode next = current.getChild(letter);

            if (next == null) {
                next = current.addChild(letter);
            }
            current = next;
        }

        current.setIsTerminal(true);
        size++;

        return true;
    }

    @Override
    public boolean contains(String element) {
        TreeNode current = root;

        for (char letter : element.toCharArray()) {
            current = current.getChild(letter);

            if (current == null) {
                // Stopped on a half-way
                return false;
            }
        }

        return current.isTerminal();
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }

        TreeNode current = root;
        for (char letter : element.toCharArray()) {
            current.decreaseNumberOfTerminalsInSubtree();
            TreeNode next = current.getChild(letter);

            if (next.getNumberOfTerminalsInSubtree() == 0 || next.getNumberOfTerminalsInSubtree() == 1 && !next.isTerminal()) {
                current.deleteChild(letter);
                size--;
                return true;
            }
            current = next;
        }

        current.setIsTerminal(false);
        size--;

        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        TreeNode current = root;

        for (char letter : prefix.toCharArray()) {
            current = current.getChild(letter);
            if (current == null) {
                return 0;
            }
        }

        return current.isTerminal()
                ? current.getNumberOfTerminalsInSubtree() + 1
                : current.getNumberOfTerminalsInSubtree();
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        DataOutputStream dout = new DataOutputStream(out);
        out.write(size);
        root.serialize(dout);
    }

    @Override
    public void deserialize(InputStream in) throws IOException {
        DataInputStream din = new DataInputStream(in);
        size = in.read();
        root.deserialize(din);
    }
}
