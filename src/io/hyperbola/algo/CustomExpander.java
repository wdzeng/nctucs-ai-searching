package io.hyperbola.algo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import io.hyperbola.base.Assignment;
import io.hyperbola.base.Variable;
import io.hyperbola.stream.Filterer;
import io.hyperbola.stream.Selector;

public class CustomExpander implements Expander {

    public static class Builder {

        List<Filterer<Variable>> filterers = new ArrayList<>();
        Selector<Variable> selector;
        Comparator<String> wordComparator = null;

        public Builder() {}

        public Builder then(Filterer<Variable> filterer) {
            filterers.add(filterer);
            return this;
        }

        public Builder2 then(Selector<Variable> selector) {
            this.selector = selector;
            return new Builder2(this);
        }

        public Builder wordOrderPolicy(Comparator<String> wordComparator) {
            this.wordComparator = wordComparator;
            return this;
        }
    }

    public static class Builder2 {

        Builder b;

        private Builder2(Builder builder) {
            b = builder;
        }

        public Builder2 wordOrderPolicy(Comparator<String> wordComparator) {
            b.wordComparator = wordComparator;
            return this;
        }

        public CustomExpander build() {
            Selector<Variable> finalSelector = (candidates, successor) -> {
                Collection<Variable> vars = candidates;
                for (Filterer<Variable> f: b.filterers) vars = f.filter(vars, successor);
                return b.selector.select(vars, successor);
            };
            return new CustomExpander(finalSelector, b.wordComparator);
        }
    }

    private Selector<Variable> varSelector;
    private Comparator<String> wordComparator;

    public CustomExpander(Selector<Variable> variableSelector, Comparator<String> wordComparator) {
        this.varSelector = variableSelector;
        this.wordComparator = wordComparator;
    }

    @Override
    public List<Assignment> assign(Node successor) {
        Variable elect = varSelector.select(successor.getUnassignedVariables(), successor);
        if (elect == null) return List.of();
        Collection<String> domain = successor.getDomainByVariable(elect);
        return Expander.matchWords(elect, domain, wordComparator);
    }
}
