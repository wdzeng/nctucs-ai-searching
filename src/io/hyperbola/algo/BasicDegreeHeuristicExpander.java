package io.hyperbola.algo;
import io.hyperbola.base.Variable;
import io.hyperbola.stream.Selector;

/**
 * This algorithm tries to implement degree heuristic searching. It expands a node by the following steps:
 *
 * Step (1): Among all unassigned variables, elects the one having most neighbors.
 *
 * Step (2): If more than one variables are selected, picks the one closest to the origin.
 *
 * Step (3): Sorts all possible words alphabetically. Each word is matched to the elected variable to generate a node.
 */
public class BasicDegreeHeuristicExpander extends DegreeHeuristicExpander {

    /**
     * Step (2): Picks the one closest to the origin point (0, 0) for no reason. The distance from a variable to the
     * origin is defined by the sum of x- and y-coordinates of such variable's start point.
     */
    protected Selector<Variable> findElect() {
        return Selector.ORIGIN_CLOSEST_SELECTOR;
    }

    /** Step (3): Sorts all possible words alphabetically. */
    @Override
    protected boolean randomSort() {
        return false;
    }
}
