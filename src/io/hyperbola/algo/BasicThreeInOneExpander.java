package io.hyperbola.algo;
import java.util.Comparator;
import io.hyperbola.base.Variable;
import io.hyperbola.stream.Selector;

public class BasicThreeInOneExpander extends ThreeInOneExpander {

    public BasicThreeInOneExpander(boolean policy) {
        super(policy);
    }

    @Override
    protected Comparator<String> wordOrderPolicy() {
        return Comparator.naturalOrder();
    }

    @Override
    protected Selector<Variable> findElect() {
        return Selector.ORIGIN_CLOSEST_SELECTOR;
    }
}
