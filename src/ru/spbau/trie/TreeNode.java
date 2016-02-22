package ru.spbau.trie;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by airvan21 on 21.02.16.
 */
public class TreeNode {

    private int numberOfLeavesInSubtree;
    private Map<Character, TreeNode> links;
    private boolean isLeaf;

    public TreeNode() {
        numberOfLeavesInSubtree = 0;
        isLeaf = false;
        links = new HashMap<Character, TreeNode>();
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean value) {
        this.isLeaf = value;
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

    public int getNumberOfLeavesInSubtree() {
        return numberOfLeavesInSubtree;
    }

    public void increaseNumberOfLeavesInSubtree() {
        numberOfLeavesInSubtree++;
    }

    public void decreaseNumberOfLeavesInSubtree() {
        numberOfLeavesInSubtree--;
    }
}
