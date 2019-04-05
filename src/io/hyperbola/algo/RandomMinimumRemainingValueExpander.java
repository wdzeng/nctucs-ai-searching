package io.hyperbola.algo;
import java.util.Comparator;
import io.hyperbola.stream.Selector;
import io.hyperbola.base.Variable;

/**
 * This expander tries to implement MRV algorithm. It selects a variable by the following steps:
 *
 * Step (1): Selects the variable having minimum count of domains.
 *
 * Step (2): If more than one variables are picked in step (1), randomly picks one.
 *
 * Step (3): Finally, sorts all possible words randomly. Each word is matched to the elected variable to generate a
 * node.
 */
public class RandomMinimumRemainingValueExpander extends MinimumRemainingValueExpander {

    /** Step (2): Randomly picks a variable to be elected from candidates. */
    @Override
    protected Selector<Variable> findElect() {
        return Selector.random();
    }

    /** Step (3): Sorts all possible words randomly. */
    @Override
    protected Comparator<String> wordOrderPolicy() {
        return null;
    }
}
