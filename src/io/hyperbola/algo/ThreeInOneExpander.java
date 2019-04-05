package io.hyperbola.algo;
import java.util.Collection;
import java.util.List;
import io.hyperbola.base.Assignment;
import io.hyperbola.base.Variable;
import io.hyperbola.stream.Filterer;
import io.hyperbola.stream.LayerFilterer;
import io.hyperbola.stream.Selector;
import static io.hyperbola.algo.DegreeHeuristicExpander.DEGREE_HEURISTIC_FILTER;
import static io.hyperbola.algo.LeastConstrainingValueExpander.lcv;
import static io.hyperbola.algo.MinimumRemainingValueExpander.MINIMUM_REMAINING_VALUE_FILTER;

/**
 * This class tries to implements three searching algorithms. It expands a node by the following steps:
 *
 * Step (1): Implementing MRV, picks the variable(s) having fewest legal values.
 *
 * Step (2): Implementing degree heuristic algorithm, if multiple variables are found in step (1), picks the one with
 * the most constraints on the remaining variables.
 *
 * Step (3): If still more than one variable selects, picks the final elect by whatever method.
 *
 * Step (4): Sorts the words implementing LCV. The one that rules out the fewest values in the remaining variables is
 * placed frontier.
 */
public abstract class ThreeInOneExpander implements Expander {

    public static final boolean DGH_MRV_LCV = true;
    public static final boolean MRV_DGH_LCV = false;
    private Filterer<Variable> first, second;

    public ThreeInOneExpander(boolean policy) {
        this.first = policy == MRV_DGH_LCV? MINIMUM_REMAINING_VALUE_FILTER: DEGREE_HEURISTIC_FILTER;
        this.second = policy == MRV_DGH_LCV? DEGREE_HEURISTIC_FILTER: MINIMUM_REMAINING_VALUE_FILTER;
    }

    @Override
    public List<Assignment> assign(AbstractNode successor) {
        Collection<Variable> unassignedVars = successor.peekUnassignedVariables();
        Variable elect = new LayerFilterer<Variable>().then(first)       // Step (1)
                                                      .then(second)      // Step (2)
                                                      .then(findElect()) // Step (3)
                                                      .select(unassignedVars, successor);
        if (elect == null) return List.of();
        // Step (4)
        List<String> wordCandidates = successor.peekDomainOf(elect);
        List<String> wordsInLcvOrder = lcv(elect, wordCandidates, successor, randomSort());
        return Expander.matchWords(elect, wordsInLcvOrder, randomSort());
    }

    protected abstract Selector<Variable> findElect();

    protected abstract boolean randomSort();
}
