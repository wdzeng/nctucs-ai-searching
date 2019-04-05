package io.hyperbola.algo;
import java.util.List;
import io.hyperbola.base.Assignment;
import io.hyperbola.base.Variable;

/**
 * Randomly picks an unassigned variable, and then randomly sorts its domains.
 */
public class RandomExpander implements Expander {

    @Override
    public List<Assignment> assign(Node successor) {
        Variable elected = Expander.randomSelect(successor.getUnassignedVariables());
        if (elected == null) return List.of();
        return Expander.matchWordsRandomly(elected, successor.getDomainByVariable(elected));
    }
}
