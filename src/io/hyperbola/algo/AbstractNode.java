package io.hyperbola.algo;
import java.util.*;
import io.hyperbola.base.Dictionary;
import io.hyperbola.base.*;
import static io.hyperbola.base.Variable.HORIZONTAL;
import static java.util.stream.Collectors.toSet;

/**
 * This class is an implementation of the {@link Node} interface.
 */
public abstract class AbstractNode implements Node {

    /**
     * Appends an item to the end of the list, returning the resulting list. The origin list remains unchanged.
     * @param former the list to be appended
     * @param latter the item to be added to the list
     */
    protected static <T> List<T> appendList(List<T> former, T latter) {
        if (latter == null) return former;
        List<T> merged = new ArrayList<>(former.size() + 1);
        merged.addAll(former);
        merged.add(latter);
        return List.copyOf(merged);
    }

    /**
     * Given all variables and the revised dictionary, creating an initial variable-domain map.
     * @param varSet     this survey result contains all variables
     * @param dictionary revised dictionary used to build the variable-domain map
     * @return an initial variable-domain map
     */
    protected static Map<Variable, Set<String>> createRootMap(VariableSurveyResult varSet, Dictionary dictionary) {
        Map<Variable, Set<String>> initMap = new HashMap<>(varSet.variables.size());
        for (Variable v: varSet.variables) {
            initMap.put(v, dictionary.getWordsByLength(v.length()));
        }
        return initMap;
    }

    /**
     * Given the variable-domain map and an assignment, updating the map.
     * @param map                   variable-domain map
     * @param requireNonEmptyDomain set to true to ensure the domains in the resulting map are all non-empty.
     * @throws EmptyDomainException when requireNonEmptyDomain is set to true and an empty domain is found
     */
    protected static void updateVarDomainMap(Assignment assignment,
                                             Map<Variable, Set<String>> map,
                                             boolean requireNonEmptyDomain) {
        assert map.containsKey(assignment.variable);
        map.remove(assignment.variable);
        Set<String> domain;
        IntersectJudger j;
        for (Variable u: assignment.variable.getNeighbors()) {
            domain = map.get(u);
            if (domain == null) continue;  // Already assigned variable
            j = new IntersectJudger(u, assignment.variable, assignment.word);
            domain = domain.stream()
                           .filter(j::judge)
                           .collect(toSet());
            if (requireNonEmptyDomain && domain.isEmpty()) {
                throw new EmptyDomainException();
            }
            map.put(u, Set.copyOf(domain));
        }
    }

    @Override
    public abstract AbstractNode expand(Assignment assignment);

    @Override
    public final List<Assignment> getAllAssignments() {
        LinkedList<Assignment> records = new LinkedList<>();
        Node n = this;
        while (n != null) {
            records.add(0, n.getAssignment());
            n = n.getSuccessor();
        }
        return new ArrayList<>(records);
    }

    @Override
    public final Board getBoard() {
        Board b = new Board(getBoardWidth(), getBoardHeight());
        if (getSuccessor() == null) return b;
        Node n = this;
        Assignment a;
        Variable v;
        while (n != null) {
            a = n.getAssignment();
            if (a == null) break;
            v = a.variable;
            if (v.getDirection() == HORIZONTAL) b.fillHorizontal(v.getX(), v.getY(), a.word);
            else b.fillVertical(v.getX(), v.getY(), a.word);
            n = n.getSuccessor();
        }
        return b;
    }

    @Override
    public final boolean isSolution() {
        return getUnassignedVariableDomainMap().isEmpty();
    }

    /**
     * Queries the height of the board
     */
    protected abstract int getBoardHeight();

    /**
     * Queries the width of the board
     */
    protected abstract int getBoardWidth();
}
