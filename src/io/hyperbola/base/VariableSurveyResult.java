package io.hyperbola.base;
import java.util.List;
import static java.util.List.copyOf;

public final class VariableSurveyResult {

    public final int boardWidth, boardHeight;
    public final List<Variable> variables;

    public VariableSurveyResult(List<Variable> variables, int boardWidth, int boardHeight) {
        if (variables.isEmpty()) throw new IllegalArgumentException();
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.variables = copyOf(variables);
    }
}
