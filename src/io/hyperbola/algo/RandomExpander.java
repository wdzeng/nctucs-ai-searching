package io.hyperbola.algo;
import java.util.Collection;
import java.util.List;
import io.hyperbola.base.Assignment;
import io.hyperbola.base.Variable;

/**
 * Randomly picks an unassigned variable, and then randomly sorts its domains.
 */
public class RandomExpander implements Expander {

    @Override
    public List<Assignment> assign(AbstractNode successor) {
        Collection<Variable> unassigned = successor.peekUnassignedVariables();
        if (unassigned.isEmpty()) return List.of();
        Variable elected = Expander.randomSelect(unassigned);
        if (elected == null) return List.of();
        return Expander.matchWordsRandomly(elected, successor.peekDomainOf(elected));
    }
}
