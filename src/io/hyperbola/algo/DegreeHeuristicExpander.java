package io.hyperbola.algo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import io.hyperbola.base.Assignment;
import io.hyperbola.base.Variable;
import io.hyperbola.stream.Filterer;
import io.hyperbola.stream.LayerFilterer;
import io.hyperbola.stream.Selector;

/**
 * This algorithm tries to implement degree heuristic searching. It expands a node by the following steps.
 *
 * Step (1): Among all unassigned variables, elects the one having most neighbors.
 *
 * Step (2): If more than one variables are selected, by whatever method picks the final one elect.
 *
 * Step (3): Sorts all possible words in some order. Each word is matched to the elected variable to generate a node.
 */
public abstract class DegreeHeuristicExpander implements Expander {

    /** A filter that always filter variables having most unassigned neighbors. */
    public static final Filterer<Variable> DEGREE_HEURISTIC_FILTER = (candidates, successor) -> {
        int max = -1, nUnassignedNeighbor;
        Collection<Variable> unassignedVars = successor.getUnassignedVariables();
        List<Variable> passed = new ArrayList<>();
        for (Variable v: candidates) {
            nUnassignedNeighbor = getUnassignedNeighborCount(v, unassignedVars);
            if (nUnassignedNeighbor < max) {
                continue;
            }
            if (nUnassignedNeighbor > max) {
                max = nUnassignedNeighbor;
                passed.clear();
            }
            passed.add(v);
        }
        return passed;
    };

    /**
     * Queries the count of unassigned neighbor of a given variable.
     * @param checkedVar     variable to be checked
     * @param unassignedVars all unassigned variables in the puzzle
     * @return the count of unassigned neighbor of a given variable
     */
    private static int getUnassignedNeighborCount(Variable checkedVar, Collection<Variable> unassignedVars) {
        return (int) checkedVar.getNeighbors()
                               .stream()
                               .filter(unassignedVars::contains)
                               .count();
    }

    @Override
    public final List<Assignment> assign(Node successor) {

        Collection<Variable> unassignedVars = successor.getUnassignedVariables();
        Variable elect = new LayerFilterer<Variable>().then(DEGREE_HEURISTIC_FILTER)      // Step (1)
                                                      .then(findElect())                  // Step (2)
                                                      .select(unassignedVars, successor);
        if (elect == null) return List.of();
        return Expander.matchWords(elect, successor.getDomainByVariable(elect), wordOrderPolicy());    // Step (3)
    }

    /** Step (2): Determines the only elected variable within candidates. */
    protected abstract Selector<Variable> findElect();

    /** Step (3): Sorts all possible words in some order. */
    protected abstract Comparator<String> wordOrderPolicy();
}
