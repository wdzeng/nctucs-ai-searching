package io.hyperbola.algo;
import java.util.Comparator;
import java.util.List;
import io.hyperbola.base.Assignment;
import io.hyperbola.base.Variable;
import io.hyperbola.stream.Selector;

public class BasicExpander implements Expander {

    @Override
    public List<Assignment> assign(Node successor) {
        Variable elect = Selector.ORIGIN_CLOSEST_SELECTOR.select(successor.getUnassignedVariables(), successor);
        if (elect == null) return List.of();
        return Expander.matchWords(elect,
                                   successor.getDomainByVariable(elect),
                                   Comparator.naturalOrder());
    }
}
