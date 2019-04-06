package io.hyperbola.algo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import io.hyperbola.base.Assignment;
import io.hyperbola.base.Variable;
import io.hyperbola.stream.Filterer;
import io.hyperbola.stream.LayerFilterer;
import io.hyperbola.stream.Selector;

/**
 * This expander tries to implement MRV algorithm. It selects a variable by the following steps:
 *
 * Step (1): Selects the variable having minimum count of domains.
 *
 * Step (2): If more than one variables are picked in step (1), elect the final one by some method.
 *
 * Step (3): Finally, sorts all possible words in some order. Each word is matched to the elected variable to generate a
 * node.
 */
public abstract class MinimumRemainingValueExpander implements Expander {

    /** A filter that always selects variables having minimum count of domains. */
    public static final Filterer<Variable> MINIMUM_REMAINING_VALUE_FILTER = (candidates, successor) -> {
        List<Variable> passed = new ArrayList<>();
        int minDomainSize = Integer.MAX_VALUE, domainSize;
        for (Variable v: candidates) {
            domainSize = successor.peekDomainSizeOf(v);
            if (domainSize > minDomainSize) continue;
            if (domainSize < minDomainSize) {
                minDomainSize = domainSize;
                passed.clear();
            }
            passed.add(v);
        }
        return passed;
    };

    @Override
    public final List<Assignment> assign(AbstractNode successor) {
        Collection<Variable> unassignedVars = successor.peekUnassignedVariables();
        Variable elect = new LayerFilterer<Variable>().then(MINIMUM_REMAINING_VALUE_FILTER)          // Step (1)
                                                      .then(findElect())                             // Step (2)
                                                      .select(unassignedVars, successor);
        if (elect == null) return List.of();
        return Expander.matchWords(elect, successor.peekDomainOf(elect), randomSort());  // Step (3)
    }

    /** Step (2): If more than one variables are picked in step (1), elect the final one by some method. */
    protected abstract Selector<Variable> findElect();

    /** Step (3): Sorts all possible words in some order. */
    protected abstract boolean randomSort();
}
