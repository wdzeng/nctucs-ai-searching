package io.hyperbola.algo;
import java.util.Comparator;
import io.hyperbola.base.Variable;
import io.hyperbola.stream.Selector;

public class RandomThreeInOneExpander extends ThreeInOneExpander {

    public RandomThreeInOneExpander(boolean policy) {
        super(policy);
    }

    @Override
    protected Comparator<String> wordOrderPolicy() {
        return null;
    }

    @Override
    protected Selector<Variable> findElect() {
        return Selector.random();
    }
}
