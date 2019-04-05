package io.hyperbola.algo;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import io.hyperbola.base.Board;

/**
 * A puzzle iterator implementing DFS.
 */
public class DepthFirstPuzzleIterator implements PuzzleIterator {

    private final Expander ea;
    private final LinkedList<Node> stack = new LinkedList<>();
    private Node currentNode = null;
    private int maxSize = 0;
    private long step = 0;

    public DepthFirstPuzzleIterator(Node root, Expander algorithm) {
        this.ea = algorithm;
        stack.add(Objects.requireNonNull(root));
    }

    @Override
    public Board currentBoard() {
        return currentNode == null? null: currentNode.getBoard();
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public int maxStackSize() {
        return maxSize;
    }

    @Override
    public int next() {
        Node n;
        try { n = stack.pop(); }
        // If the stack is empty, the searching is ended.
        catch (NoSuchElementException e) {
            throw new IllegalStateException();
        }

        step++;
        currentNode = n;
        List<? extends Node> expanded = ea.expand(currentNode);
        if (expanded.isEmpty()) {
            return currentNode.isSolution()? SOLUTION: FAILURE;
        }

        stack.addAll(0, expanded); // DFS
        maxSize = Math.max(maxSize, stackSize());
        return UNKNOWN;
    }

    @Override
    public int stackSize() {
        return stack.size();
    }

    @Override
    public long step() {
        return step;
    }
}