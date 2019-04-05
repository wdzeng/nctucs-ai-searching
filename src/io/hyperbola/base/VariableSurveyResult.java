package io.hyperbola.base;
import java.util.Collection;
import java.util.List;

public final class VariableSurveyResult {

    public final int boardWidth, boardHeight;
    public final Collection<Variable> variables;

    public VariableSurveyResult(Collection<? extends Variable> variables, int boardWidth, int boardHeight) {
        if (variables.isEmpty()) throw new IllegalArgumentException();
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.variables = List.copyOf(variables);
    }
}
