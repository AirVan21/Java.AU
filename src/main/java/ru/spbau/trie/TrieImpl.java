package ru.spbau.trie;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by airvan21 on 21.02.16.
 */
public class TrieImpl implements Trie, StreamSerializable {

    final private TreeNode root;

    public TrieImpl() {
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
                return true;
            }
            current = next;
        }

        current.setIsTerminal(false);

        return true;
    }

    @Override
    public int size() {
        return howManyStartsWithPrefix("");
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
        root.serialize(dout);
        dout.flush();
    }

    @Override
    public void deserialize(InputStream in) throws IOException {
        DataInputStream din = new DataInputStream(in);
        root.deserialize(din);
    }

    private class TreeNode {

        private int numberOfTerminalsInSubtree;
        private boolean isTerminal;
        private Map<Character, TreeNode> children;

        public TreeNode() {
            numberOfTerminalsInSubtree = 0;
            isTerminal = false;
            children = new HashMap<>();
        }

        private TreeNode(int numOfTerminals, boolean markedTerminal) {
            numberOfTerminalsInSubtree = numOfTerminals;
            isTerminal = markedTerminal;
            children = new HashMap<>();
        }

        public boolean isTerminal() {
            return isTerminal;
        }

        public void setIsTerminal(boolean value) {
            this.isTerminal = value;
        }

        public TreeNode getChild(char letter) {
            return children.get(letter);
        }

        public TreeNode addChild(char letter) {
            TreeNode node = new TreeNode();
            children.put(letter, node);

            return node;
        }

        public void deleteChild(char letter) {
            children.remove(letter);
        }

        public int getNumberOfTerminalsInSubtree() {
            return numberOfTerminalsInSubtree;
        }

        public void increaseNumberOfTerminalsInSubtree() {
            numberOfTerminalsInSubtree++;
        }

        public void decreaseNumberOfTerminalsInSubtree() {
            numberOfTerminalsInSubtree--;
        }

        public void serialize(DataOutputStream out) throws IOException {
            out.writeInt(numberOfTerminalsInSubtree);
            out.writeBoolean(isTerminal);
            out.writeInt(children.size());

            for (Map.Entry<Character, TreeNode> node : children.entrySet()) {
                out.writeChar(node.getKey());
                node.getValue().serialize(out);
            }
        }

        public void deserialize(DataInputStream in) throws IOException {
            TreeNode restoredNode = deserializeTrieNode(in);

            numberOfTerminalsInSubtree = restoredNode.numberOfTerminalsInSubtree;
            isTerminal = restoredNode.isTerminal;
            children = restoredNode.children;
        }

        private TreeNode deserializeTrieNode(DataInputStream in) throws IOException {
            int numOfTerminals = in.readInt();
            boolean markTerminal = in.readBoolean();
            TreeNode node = new TreeNode(numOfTerminals, markTerminal);

            int size = in.readInt();
            for (int i = 0; i < size; i++) {
                char letter = in.readChar();
                node.children.put(letter, deserializeTrieNode(in));
            }

            return node;
        }
    }
}

