package io.hyperbola.algo;
import java.util.Comparator;
import io.hyperbola.base.Variable;
import io.hyperbola.stream.Selector;

public class RandomThreeInOneExpander extends ThreeInOneExpander {

    public RandomThreeInOneExpander(boolean policy) {
        super(policy);
    }

    @Override
    protected boolean randomSort() {
        return true;
    }

    @Override
    protected Selector<Variable> findElect() {
        return Selector.random();
    }
}
