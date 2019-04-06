package io.hyperbola.algo;
import java.util.*;
import io.hyperbola.base.Dictionary;
import io.hyperbola.base.*;
import static io.hyperbola.base.Variable.HORIZONTAL;
import static java.util.stream.Collectors.toList;

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
        return merged;
    }

    /**
     * Given all variables and the dictionary, creating an initial variable-domain map.
     * @param varSet     this survey data contains all variables
     * @param dictionary dictionary used to build the variable-domain map
     * @return an initial variable-domain map
     */
    protected static Map<Variable, List<String>> createRootVarDomainMap(VariableSurveyResult varSet,
                                                                        Dictionary dictionary) {
        Map<Variable, List<String>> initMap = new HashMap<>(varSet.variables.size());
        for (Variable v: varSet.variables) initMap.put(v, dictionary.getWordsByLength(v.length()));
        return initMap;
    }

    protected static Map<Variable, List<Variable>> createRootVarNeighborsMap(VariableSurveyResult varSet) {
        Collection<Variable> vars = varSet.variables;
        Map<Variable, List<Variable>> map = new HashMap<>(vars.size());
        for (Variable v: vars) map.put(v, v.getNeighbors());
        return map;
    }

    /**
     * Given the variable-domain map and an assignment, updating the map.
     * @param domainMap             variable-domain map
     * @param requireNonEmptyDomain set to true to ensure the domains in the resulting map are all non-empty.
     * @throws EmptyDomainException when requireNonEmptyDomain is set to true and an empty domain is found
     */
    protected static void updateVarDomainAndVarNeighborsMap(Assignment assignment,
                                                            Map<Variable, List<String>> domainMap,
                                                            Map<Variable, List<Variable>> neighborsMap,
                                                            boolean requireNonEmptyDomain) {
        Variable assignedVar = assignment.variable;
        assert domainMap.containsKey(assignedVar);
        assert neighborsMap.containsKey(assignedVar);

        List<String> domain;
        List<Variable> updatedNeighborList;
        IntersectJudger j;
        for (Variable n: neighborsMap.get(assignedVar)) {

            // Update the domain
            domain = domainMap.get(n);
            j = new IntersectJudger(n, assignedVar, assignment.word);
            domain = domain.stream()
                           .filter(j::judge)
                           .collect(toList());
            if (requireNonEmptyDomain && domain.isEmpty()) {
                throw new EmptyDomainException();
            }
            domainMap.put(n, domain);

            // Update the neighbors
            updatedNeighborList = new ArrayList<>(neighborsMap.get(n));
            assert updatedNeighborList.contains(assignedVar);
            updatedNeighborList.remove(assignedVar);
            neighborsMap.put(n, updatedNeighborList);
        }

        domainMap.remove(assignedVar);
        neighborsMap.remove(assignedVar);
    }

    protected static void updateVarNeighborsMap(Assignment assignment, Map<Variable, List<Variable>> map) {
        Variable assigned = assignment.variable;
        for (Variable n: assigned.getNeighbors()) {
            map.get(n).remove(assigned);
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
        return peekUnassignedVariableDomainMap().isEmpty();
    }

    /**
     * Queries the height of the board
     */
    protected abstract int getBoardHeight();

    /**
     * Queries the width of the board
     */
    protected abstract int getBoardWidth();

    protected final int peekCountOfUnassignedNeighborsOf(Variable variable) {
        return peekUnassignedNeighborsOf(variable).size();
    }

    /**
     * Queries the domain of a given unassigned variable.
     * @return the domain; or null if the variable is already assigned.
     */
    protected final List<String> peekDomainOf(Variable variable) {
        assert peekUnassignedVariableDomainMap().containsKey(variable);
        return peekUnassignedVariableDomainMap().get(variable);
    }

    protected final int peekDomainSizeOf(Variable variable) {
        return peekDomainOf(variable).size();
    }

    protected final List<Variable> peekUnassignedNeighborsOf(Variable variable) {
        return peekUnassignedVariableNeighborsMap().get(variable);
    }

    /**
     * Queries the variable-domain map.
     */
    protected abstract Map<Variable, List<String>> peekUnassignedVariableDomainMap();

    /**
     * Queries the variable-neighbors map.
     */
    protected abstract Map<Variable, List<Variable>> peekUnassignedVariableNeighborsMap();

    /**
     * Queries all unassigned variables.
     */
    protected final Set<Variable> peekUnassignedVariables() {
        return peekUnassignedVariableDomainMap().keySet();
    }
}
