package io.hyperbola.algo;
import io.hyperbola.base.Variable;
import io.hyperbola.stream.Selector;

/**
 * This algorithm tries to implement degree heuristic searching. It expands a node by the following steps:
 *
 * Step (1): Among all unassigned variables, elects the one having most neighbors.
 *
 * Step (2): If more than one variables are selected, randomly picks one.
 *
 * Step (3): Sorts all possible words randomly. Each word is matched to the elected variable to generate a node.
 */
public class RandomDegreeHeuristicExpander extends DegreeHeuristicExpander {

    /** Step (2): From candidates randomly picks a variable to be the elected. */
    @Override
    protected Selector<Variable> findElect() {
        return Selector.random();
    }

    @Override
    protected boolean randomSort() {
        return true;
    }
}
