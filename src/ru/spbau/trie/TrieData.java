package ru.spbau.trie;

/**
 * Created by airvan21 on 21.02.16.
 */
public class TrieData implements Trie {

    private TreeNode root;
    private int size;

    public TrieData() {
        size = 0;
        root = new TreeNode();
    }

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }

        TreeNode current = root;
        for (Character letter : element.toCharArray()) {
            current.increaseNumberOfLeavesInSubtree();
            TreeNode next = current.getLink(letter);

            if (next == null) {
                next = current.addLink(letter);
            }
            current = next;
        }

        current.setIsLeaf(true);
        size++;

        return true;
    }

    @Override
    public boolean contains(String element) {
        TreeNode current = root;

        for (Character letter : element.toCharArray()) {
            current = current.getLink(letter);

            if (current == null) {
                // Stopped on a half-way
                return false;
            }
        }

        return current.isLeaf();
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }

        TreeNode current = root;
        for (Character letter : element.toCharArray()) {
            current.decreaseNumberOfLeavesInSubtree();
            TreeNode next = current.getLink(letter);

            if (next.getNumberOfLeavesInSubtree() == 0 || next.getNumberOfLeavesInSubtree() == 1 && !next.isLeaf()) {
                current.deleteLink(letter);
                size--;
                return true;
            }
            current = next;
        }

        current.setIsLeaf(false);
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

        for (Character letter : prefix.toCharArray()) {
            current = current.getLink(letter);
            if (current == null) {
                return 0;
            }
        }

        return current.isLeaf() ? current.getNumberOfLeavesInSubtree() + 1 : current.getNumberOfLeavesInSubtree();
    }
}
