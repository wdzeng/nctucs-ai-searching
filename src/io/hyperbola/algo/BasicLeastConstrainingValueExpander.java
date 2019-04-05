package io.hyperbola.algo;
import java.util.Comparator;
import io.hyperbola.base.Variable;
import io.hyperbola.stream.Selector;

/**
 * This algorithm tries to implement the least constraining value searching. It expands a node by the following steps:
 *
 * Step (1): Picks the variable that is closest to the origin.
 *
 * Step (2): Find all neighbors of such variable. If nothing found, all words in domain of the elect has identical
 * priority and hence no need to sort them. In this case, orders these words alphabetically them assigns them to the
 * elect and then returns.
 *
 * Step (3): Iterating the neighbors and words in the domain of the elect. Ordering words by LCV algorithm (the one that
 * rules out the fewest values in the remaining variables is put frontier). Then assigns these words to the elect.
 */
public class BasicLeastConstrainingValueExpander extends LeastConstrainingValueExpander {

    public BasicLeastConstrainingValueExpander() {
        this(true);
    }

    public BasicLeastConstrainingValueExpander(boolean forwardCheck) {
        super(forwardCheck);
    }

    /** Step (1): Picks the variable that is closest to the origin. */
    @Override
    protected Selector<Variable> findElect() {
        return Selector.ORIGIN_CLOSEST_SELECTOR;
    }

    @Override
    protected boolean randomSort() {
        return false;
    }
}
