package ru.spbau.trie;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by airvan21 on 21.02.16.
 */
public class TreeNode {

    private int numberOfTerminalsInSubtree;
    private boolean isTerminal;
    private Map<Character, TreeNode> children;

    public TreeNode() {
        numberOfTerminalsInSubtree = 0;
        isTerminal = false;
        children = new HashMap<>();
    }

    public TreeNode(int numOfTerminals, boolean markedTerminal) {
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

    public TreeNode getChild(Character letter) {
        return children.get(letter);
    }

    public TreeNode addChild(Character letter) {
        TreeNode node = new TreeNode();
        children.put(letter, node);

        return node;
    }

    public void deleteChild(Character letter) {
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
        out.write(numberOfTerminalsInSubtree);
        out.writeBoolean(isTerminal);
        out.write(children.size());

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
        int numOfTerminals = in.read();
        boolean markTerminal = in.readBoolean();
        TreeNode node = new TreeNode(numOfTerminals, markTerminal);

        int size = in.read();
        for (int i = 0; i < size; i++) {
            char letter = in.readChar();
            node.children.put(letter, deserializeTrieNode(in));
        }

        return node;
    }
}
