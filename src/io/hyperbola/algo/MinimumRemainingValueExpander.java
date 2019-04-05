package io.hyperbola.algo;
import java.util.*;
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

    /** A filter than always filter variables having minimum count of domains. */
    public static final Filterer<Variable> MINIMUM_REMAINING_VALUE_FILTER = (candidates, successor) -> {
        List<Variable> elects = new ArrayList<>();
        // First gets the domains of candidates
        Map<Variable, Set<String>> varDomainMap = new HashMap<>(successor.getUnassignedVariableDomainMap());
        for (Variable v: candidates) {
            if (!candidates.contains(v)) varDomainMap.remove(v);
        }
        // Then finds those having smallest domain
        int nVal = Integer.MAX_VALUE, n;
        for (Map.Entry<Variable, Set<String>> entry: varDomainMap.entrySet()) {
            n = entry.getValue().size();
            if (n > nVal) continue;
            if (n < nVal) {
                nVal = n;
                elects.clear();
            }
            elects.add(entry.getKey());
        }
        return elects;
    };

    @Override
    public final List<Assignment> assign(Node successor) {
        Collection<Variable> unassignedVars = successor.getUnassignedVariables();
        Variable elect = new LayerFilterer<Variable>().then(MINIMUM_REMAINING_VALUE_FILTER)          // Step (1)
                                                      .then(findElect())                             // Step (2)
                                                      .select(unassignedVars, successor);
        if (elect == null) return List.of();
        return Expander.matchWords(elect, successor.getDomainByVariable(elect), wordOrderPolicy());  // Step (3)
    }

    /** Step (2): If more than one variables are picked in step (1), elect the final one by some method. */
    protected abstract Selector<Variable> findElect();

    /** Step (3): Sorts all possible words in some order. */
    protected abstract Comparator<String> wordOrderPolicy();
}
