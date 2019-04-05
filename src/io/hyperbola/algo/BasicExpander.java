package io.hyperbola.algo;
import java.util.List;
import io.hyperbola.base.Assignment;
import io.hyperbola.base.Variable;
import io.hyperbola.stream.Selector;

public class BasicExpander implements Expander {

    @Override
    public List<Assignment> assign(AbstractNode successor) {
        Variable elect = Selector.ORIGIN_CLOSEST_SELECTOR.select(successor.peekUnassignedVariables(), successor);
        if (elect == null) return List.of();
        return Expander.matchWords(elect,
                                   successor.peekDomainOf(elect),
                                   false);
    }
}
