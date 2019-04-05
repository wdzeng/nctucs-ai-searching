package io.hyperbola.algo;
import java.util.*;
import io.hyperbola.base.Assignment;
import io.hyperbola.base.Variable;
import static java.util.stream.Collectors.toList;

/**
 * The task for an expander is to determine the offspring of a node and the order must be put into consideration. That
 * is, everything an expander has to do is to decide the next assignments of a node.
 */
@FunctionalInterface
public interface Expander {

    /**
     * Generates offspring of a given node.
     */
    default List<? extends Node> expand(Node node) {
        List<Assignment> asgList = assign(node);
        List<Node> expanded = new ArrayList<>();
        for (Assignment a: asgList) {
            try {expanded.add(node.expand(a));}
            catch (EmptyDomainException ignored) {}
        }
        return expanded;
    }

    /**
     * Determines the next assignments and returns the ordered results.
     * @param successor node about to be expanded
     * @return a list of assignment, or an empty list if this node is not expandable.
     */
    List<Assignment> assign(Node successor);

    /**
     * Converts words to assignments by given elected variable. Words are sorted randomly.
     * @param assignedVar variable to be assigned
     * @param words       revised dictionary
     */
    static List<Assignment> matchWordsRandomly(Variable assignedVar, Collection<String> words) {
        List<String> copy = new ArrayList<>(words);
        Collections.shuffle(copy, new Random(System.currentTimeMillis())); // Shuffle
        return copy.stream()
                   .map(str -> new Assignment(str, assignedVar))
                   .collect(toList());
    }

    /**
     * Converts words to assignments by given elected variable. Words ares sorted by given comparator.
     * @param assignedVar    variable to be assigned
     * @param words          revised dictionary
     * @param wordComparator sorting policy
     */
    static List<Assignment> matchWords(Variable assignedVar,
                                       Collection<String> words,
                                       Comparator<String> wordComparator) {
        if (assignedVar == null) return List.of();
        if (wordComparator == null) return matchWordsRandomly(assignedVar, words);
        return words.stream()
                    .sorted(wordComparator)
                    .map(str -> new Assignment(str, assignedVar))
                    .collect(toList());
    }

    /**
     * Converts words to assignments by given elected variable.
     * @param assignedVar variable to be assigned
     * @param words       revised dictionary
     */
    static List<Assignment> matchWords(Variable assignedVar, Collection<String> words) {
        return words.stream()
                    .map(str -> new Assignment(str, assignedVar))
                    .collect(toList());
    }

    /** Randomly selects a variable from a collection. */
    static Variable randomSelect(Collection<Variable> candidates) {
        int index = (int) (Math.random() * candidates.size());
        return List.copyOf(candidates).get(index);
    }
}
