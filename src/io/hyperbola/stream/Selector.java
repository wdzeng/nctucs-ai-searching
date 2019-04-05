package io.hyperbola.stream;
import java.util.ArrayList;
import java.util.Collection;
import io.hyperbola.algo.Node;
import io.hyperbola.base.Variable;
import static java.util.Comparator.naturalOrder;

@FunctionalInterface
public interface Selector<T> {

    /**
     * Picks the variable closest to the origin point (0, 0) for no reason. The distance from a variable to the origin
     * is defined by the sum of x- and y-coordinates of such variable's start point. If both horizontal and vertical
     * closest variables exist, picks the vertical one.
     */
    Selector<Variable> ORIGIN_CLOSEST_SELECTOR = (candidates, successor) -> candidates.stream()
                                                                                      .min(naturalOrder())
                                                                                      .get();

    /** Picks randomly. */
    static <T> Selector<T> random() {
        return (candidates, successor) -> {
            int index = (int) (Math.random() * candidates.size());
            return new ArrayList<>(candidates).get(index);
        };
    }

    T selectFrom(Collection<T> candidates, Node successor);

    default T select(Collection<T> candidates, Node successor) {
        if (candidates.isEmpty()) return null;
        else if (candidates.size() == 1) return candidates.stream().findFirst().get();
        return selectFrom(candidates, successor);
    }
}
