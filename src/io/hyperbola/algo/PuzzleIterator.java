package io.hyperbola.algo;
import io.hyperbola.base.Board;

/**
 * A puzzle iterator hold a stack and determines where the expanded nodes are inserted into the stack.
 */
public interface PuzzleIterator {

    int FAILURE = 0;
    int SOLUTION = 1;
    int UNKNOWN = -1;
    int NONE = -2;

    /**
     * Queries the current board status.
     */
    Board currentBoard();

    /** Queries if there is still next step. */
    boolean hasNext();

    /**
     * Queries the max size of stack in the searching history.
     */
    int maxStackSize();

    /**
     * Goes to next step. If the iterator has reached the end, nothing happens.
     * @return a flag indicating if new state is a solution, a failure or unknown
     * @throws IllegalStateException if the searching ends
     */
    int next();

    /**
     * Finds the next failure.
     * @return true if a failure is found; or else false if no failure is found before the searching ends.
     */
    default boolean nextFailure() {
        while (hasNext()) {
            if (next() == FAILURE) return true;
        }
        return false;
    }

    /**
     * Finds the next solution or failure.
     * @return a flag indicating a solution or failure or if searching reaches the end and nothing found.
     */
    default int nextResult() {
        int i;
        while (hasNext()) {
            i = next();
            if (i == FAILURE || i == SOLUTION) return i;
        }
        return NONE;
    }

    /**
     * Finds the next solution.
     * @return true if a solution is found; false if no solutions found when the searching ends
     */
    default boolean nextSolution() {
        while (hasNext()) {
            if (next() == SOLUTION) return true;
        }
        return false;
    }

    /**
     * Queries the current size of stack.
     */
    int stackSize();

    /**
     * Queries the current step.
     */
    long step();
}
