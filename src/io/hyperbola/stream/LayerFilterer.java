package io.hyperbola.stream;
import java.util.Collection;
import java.util.List;
import io.hyperbola.algo.AbstractNode;

public class LayerFilterer<T> implements Filterer<T> {

    private Filterer<T> filterer;
    private Filterer<T> prev;

    public LayerFilterer() {
        this.prev = null;
        this.filterer = null;
    }

    LayerFilterer(Filterer<T> filterer, LayerFilterer<T> prev) {
        this.prev = prev;
        this.filterer = filterer;
    }

    @Override
    public List<T> filterFrom(Collection<T> candidates, AbstractNode successor) {
        if (prev != null) candidates = prev.filter(candidates, successor);
        return filterer.filter(candidates, successor);
    }

    public Selector<T> then(Selector<T> selector) {
        return (candidates, successor) -> selector.select(this.filter(candidates, successor), successor);
    }

    public LayerFilterer<T> then(Filterer<T> filterer) {
        if (this.filterer == null) {
            this.filterer = filterer;
            return this;
        }
        return new LayerFilterer<>(filterer, this);
    }
}
