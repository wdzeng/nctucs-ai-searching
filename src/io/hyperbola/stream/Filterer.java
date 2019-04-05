package io.hyperbola.stream;
import java.util.Collection;
import java.util.List;
import io.hyperbola.algo.Node;

@FunctionalInterface
public interface Filterer<T> {

    List<T> filterFrom(Collection<T> candidates, Node successor);

    default List<T> filter(Collection<T> candidates, Node successor) {
        if (candidates.isEmpty()) return List.of();
        return filterFrom(candidates, successor);
    }
}
