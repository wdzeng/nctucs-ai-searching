package io.hyperbola.algo;
import io.hyperbola.base.Variable;
import io.hyperbola.stream.Selector;
import static io.hyperbola.stream.Selector.ORIGIN_CLOSEST_SELECTOR;

/**
 * This expander tries to implement MRV algorithm. It selects a variable by the following steps:
 *
 * Step (1): Selects the variable having minimum count of domains.
 *
 * Step (2): If more than one variables are picked in step (1), picks the one closest to the origin point.
 *
 * Step (3): Finally, sorts all possible words alphabetically. Each word is matched to the elected variable to generate
 * a node.
 */
public class BasicMinimumRemainingValueExpander extends MinimumRemainingValueExpander {

    /** Step (2): If more than one candidates are found, picks the one closest to the origin point. */
    @Override
    protected Selector<Variable> findElect() {
        return ORIGIN_CLOSEST_SELECTOR;
    }

    /** Step (3): Sorts all possible words alphabetically. */
    @Override
    protected boolean randomSort() {
        return false;
    }
}
