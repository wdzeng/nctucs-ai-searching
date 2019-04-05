package io.hyperbola.algo;
import io.hyperbola.base.Variable;
import io.hyperbola.stream.Selector;

public class RandomThreeInOneExpander extends ThreeInOneExpander {

    public RandomThreeInOneExpander(boolean policy) {
        super(policy);
    }

    @Override
    protected Selector<Variable> findElect() {
        return Selector.random();
    }

    @Override
    protected boolean randomSort() {
        return true;
    }
}
