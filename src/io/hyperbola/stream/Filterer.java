package io.hyperbola.stream;
import java.util.Collection;
import java.util.List;
import io.hyperbola.algo.AbstractNode;

@FunctionalInterface
public interface Filterer<T> {

    default List<T> filter(Collection<T> candidates, AbstractNode successor) {
        if (candidates.isEmpty()) return List.of();
        return filterFrom(candidates, successor);
    }

    List<T> filterFrom(Collection<T> candidates, AbstractNode successor);
}
