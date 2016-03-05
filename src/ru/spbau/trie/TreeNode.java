package ru.spbau.trie;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by airvan21 on 21.02.16.
 */
public class TreeNode {

    private int numberOfTerminalsInSubtree;
    private final Map<Character, TreeNode> links;
    private boolean isTerminal;

    public TreeNode() {
        numberOfTerminalsInSubtree = 0;
        isTerminal = false;
        links = new HashMap<Character, TreeNode>();
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public void setIsTerminal(boolean value) {
        this.isTerminal = value;
    }

    public TreeNode getLink(Character letter) {
        return links.get(letter);
    }

    public TreeNode addLink(Character letter) {
        TreeNode node = new TreeNode();
        links.put(letter, node);

        return node;
    }

    public void deleteLink(Character letter) {
        links.remove(letter);
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
}
