package io.hyperbola.algo;
import io.hyperbola.base.Variable;
import io.hyperbola.stream.Selector;

public class BasicThreeInOneExpander extends ThreeInOneExpander {

    public BasicThreeInOneExpander(boolean policy) {
        super(policy);
    }

    @Override
    protected Selector<Variable> findElect() {
        return Selector.ORIGIN_CLOSEST_SELECTOR;
    }

    @Override
    protected boolean randomSort() {
        return false;
    }
}
