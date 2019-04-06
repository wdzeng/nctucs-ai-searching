package io.hyperbola.algo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.hyperbola.base.Assignment;
import io.hyperbola.base.Dictionary;
import io.hyperbola.base.Variable;
import io.hyperbola.base.VariableSurveyResult;
import static java.util.Map.copyOf;

/**
 * This class tries to implement the AC-3 searching algorithm. That is, if any unassigned variable is found to be with a
 * domain containing only one item, immediately fills this variable with that item and then continues to generate the
 * next node. If any unassigned variable is found to be with an empty domain, prohibits such node to be generated.
 */
public class Ac3Node extends AbstractNode {

    private final Assignment assignment;                              // unmodifiable
    private final List<Assignment> prevAssignments;                   // unmodifiable
    private final Ac3Node successor;
    private final Map<Variable, List<String>> unassigned;             // unmodifiable
    private final Map<Variable, List<Variable>> unassignedNeighbors;  // unmodifiable
    private final int wBoard, hBoard;

    /**
     * Creates a root node using AC-3. Dictionary must be given so that the variable-domain map can be built. Variable
     * survey data must be given so the node knows the size of board so that it can build it.
     * @param varSet     variables and board dimensions data
     * @param dictionary dictionary to be used
     */
    public Ac3Node(VariableSurveyResult varSet, Dictionary dictionary) {
        assignment = null;
        successor = null;
        prevAssignments = List.of();
        unassigned = createRootVarDomainMap(varSet, dictionary);
        unassignedNeighbors = createRootVarNeighborsMap(varSet);
        wBoard = varSet.boardWidth;
        hBoard = varSet.boardHeight;
    }

    /**
     * Creates a non-root node.
     * @param parent         node which is expanded
     * @param lastAssignment the parent node performs this assignment and then generates this node
     * @throws EmptyDomainException if such assignment resulting to some variable having empty domain
     */
    private Ac3Node(Ac3Node parent, Assignment lastAssignment) {

        Map<Variable, List<String>> unassignedVarDomainMap = new HashMap<>(parent.unassigned);
        Map<Variable, List<Variable>> unassignedVarNeighborsMap = new HashMap<>(parent.unassignedNeighbors);

        // Only neighbors of last assigned variable should be checked
        outer:
        while (true) {
            // Updates the domain and neighbors map
            updateVarDomainAndVarNeighborsMap(lastAssignment,
                                              unassignedVarDomainMap,
                                              unassignedVarNeighborsMap,
                                              true);
            // Checks if any variable with one-item-domain exists
            List<Variable> unassignedVars = new ArrayList<>(unassignedVarDomainMap.keySet());
            unassignedVars.sort(null); // must be iterated in natural order
            for (Variable v: unassignedVars) {
                List<String> domain = unassignedVarDomainMap.get(v);
                if (domain.size() != 1) continue;
                // Here we found
                // Builds a medium node
                parent = new Ac3Node(parent, lastAssignment, unassignedVarDomainMap, unassignedVarNeighborsMap);
                // Updates the assignment so that the variable-domain map will be updated in the next loop
                String theOnlyWord = domain.stream().findFirst().get();
                lastAssignment = new Assignment(theOnlyWord, v);
                // Continues the loop. Again checks if any variable with one-item-domain exists
                continue outer;
            }
            // No such variable exists, done!
            break;
        }

        successor = parent;
        unassigned = unassignedVarDomainMap;
        unassignedNeighbors = unassignedVarNeighborsMap;
        assignment = lastAssignment;
        prevAssignments = appendList(successor.prevAssignments, successor.assignment);
        wBoard = successor.wBoard;
        hBoard = successor.hBoard;
    }

    /**
     * Given every data, built a node without any check.
     * @param parent                 node expanded
     * @param lastAssignment         parent node performs this assignment and then generates this node
     * @param unassignedVarDomainMap the updated variable-domain map
     */
    private Ac3Node(Ac3Node parent,
                    Assignment lastAssignment,
                    Map<Variable, List<String>> unassignedVarDomainMap,
                    Map<Variable, List<Variable>> unassignedVarNeighborsMap) {
        successor = parent;
        unassigned = copyOf(unassignedVarDomainMap);              // Should make a copy
        unassignedNeighbors = copyOf(unassignedVarNeighborsMap);  // Should make a copy
        assignment = lastAssignment;
        prevAssignments = appendList(successor.prevAssignments, successor.assignment);
        wBoard = parent.wBoard;
        hBoard = parent.hBoard;
    }

    @Override
    public Ac3Node expand(Assignment assignment) {
        return new Ac3Node(this, assignment);
    }

    @Override
    protected int getBoardHeight() {
        return hBoard;
    }

    @Override
    protected int getBoardWidth() {
        return wBoard;
    }

    @Override
    protected Map<Variable, List<String>> peekUnassignedVariableDomainMap() {
        return unassigned;
    }

    @Override
    protected Map<Variable, List<Variable>> peekUnassignedVariableNeighborsMap() {
        return unassignedNeighbors;
    }

    @Override
    public Assignment getAssignment() {
        return assignment;
    }

    @Override
    public Ac3Node getSuccessor() {
        return successor;
    }
}
