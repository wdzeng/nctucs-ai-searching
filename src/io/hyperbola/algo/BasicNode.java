package io.hyperbola.algo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.hyperbola.base.Assignment;
import io.hyperbola.base.Dictionary;
import io.hyperbola.base.Variable;
import io.hyperbola.base.VariableSurveyResult;

/**
 * This class is an simple implementation of {@link Node}. Forward checking is supported and it is just an option.
 */
public class BasicNode extends AbstractNode {

    private final Assignment assignment;
    private final List<Assignment> prevAssignments;
    private final BasicNode successor;
    private final Map<Variable, List<String>> unassigned;
    private final Map<Variable, List<Variable>> unassignedNeighbors;
    private final int wBoard, hBoard;
    private final boolean forwardCheck;

    /**
     * Creates a root node. Dictionary must be given so that the variable-domain map can be built. Variable survey
     * result must be given so the node knows the size of board so that it can build it.
     * @param varSet              variables and board dimensions data
     * @param dictionary          revised dictionary to be used
     * @param requireForwardCheck set to true to enables forward checking in its offspring nodes
     */
    public BasicNode(VariableSurveyResult varSet, Dictionary dictionary, boolean requireForwardCheck) {
        assignment = null;
        successor = null;
        wBoard = varSet.boardWidth;
        hBoard = varSet.boardHeight;
        prevAssignments = List.of();
        unassigned = createRootVarDomainMap(varSet, dictionary);
        unassignedNeighbors = createRootVarNeighborsMap(varSet);
        forwardCheck = requireForwardCheck;
    }

    /**
     * Creates a non-root node. If the forward checking is enabled, an {@link EmptyDomainException} might be thrown.
     * @param parent         successor node
     * @param lastAssignment successor performs such assignment and then generates this node
     */
    private BasicNode(BasicNode parent, Assignment lastAssignment) {
        Map<Variable, List<String>> unassignedVarDomainMap = new HashMap<>(parent.unassigned);
        Map<Variable, List<Variable>> unassignedVarNeighborsMap = new HashMap<>(parent.unassignedNeighbors);
        updateVarDomainAndVarNeighborsMap(lastAssignment,
                                          unassignedVarDomainMap,
                                          unassignedVarNeighborsMap,
                                          parent.forwardCheck);
        successor = parent;
        assignment = lastAssignment;
        unassigned = unassignedVarDomainMap;
        unassignedNeighbors = unassignedVarNeighborsMap;
        prevAssignments = appendList(successor.prevAssignments, successor.assignment);
        wBoard = successor.wBoard;
        hBoard = successor.hBoard;
        forwardCheck = successor.forwardCheck;
    }

    @Override
    public BasicNode expand(Assignment assignment) {
        return new BasicNode(this, assignment);
    }

    @Override
    public BasicNode getSuccessor() {
        return successor;
    }

    @Override
    public Map<Variable, List<String>> getUnassignedVariableDomainMap() {
        return unassigned;
    }

    @Override
    public Map<Variable, List<Variable>> getUnassignedVariableNeighborsMap() {
        return unassignedNeighbors;
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
    public Assignment getAssignment() {
        return assignment;
    }
}
