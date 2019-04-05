package io.hyperbola.algo;
import java.util.*;
import io.hyperbola.base.Assignment;
import io.hyperbola.base.Variable;
import io.hyperbola.stream.Selector;
import static java.util.stream.Collectors.toList;

/**
 * This algorithm tries to implement the least constraining value searching. It expands a node by the following steps:
 *
 * Step (1): By whatever method picks a variable as an elect. If an elect is not found, this node cannot be expanded.
 *
 * Step (2): Find all neighbors of such variable. If no neighbors found, all words in domain of the elect has identical
 * priority and hence no need to sort them. In this case, orders these words alphabetically and assigns them to the
 * elect. Then returns.
 *
 * Step (3): Iterating the neighbors and words in the domain of the elect. Ordering words by LCV algorithm (the one that
 * rules out the fewest values in the remaining variables is put frontier). Then assigns these words to the elect.
 */
public abstract class LeastConstrainingValueExpander implements Expander {

    /**
     * Given the elected variable, sorts words in the order of LCV algorithm without forward checkout. Noted that
     * sometimes not all words are found in the returned list if such assignment empties another variable's domain.
     * @param elect          elected variable
     * @param wordCandidates words should be sorted
     * @param successor      successor node
     * @param comparator     if multiple words have same level then sorts these words by this comparator; sets to null
     *                       to perform randomly sort
     */
    public static List<String> lcv(Variable elect,
                                   Collection<String> wordCandidates,
                                   Node successor,
                                   Comparator<String> comparator) {
        return lcv(elect, wordCandidates, successor, comparator, false);
    }

    /**
     * Given the elected variable, sorts words in the order of LCV algorithm. Noted that sometimes not all words are
     * found in the returned list if such assignment empties another variable's domain.
     * @param elect          elected variable
     * @param wordCandidates words should be sorted
     * @param successor      successor node
     * @param comparator     if multiple words have same level then sorts these words by this comparator; sets to null
     *                       to perform randomly sort
     * @param forwardCheck   true to enable forward checking
     */
    public static List<String> lcv(Variable elect,
                                   Collection<String> wordCandidates,
                                   Node successor,
                                   Comparator<String> comparator,
                                   boolean forwardCheck) {

        // Step (2): Find all neighbors
        Collection<Variable> unassigned = successor.getUnassignedVariables();
        Collection<Variable> neighbors = elect.getNeighbors()
                                              .stream()
                                              .filter(unassigned::contains)
                                              .collect(toList());
        if (neighbors.isEmpty()) {
            // This assignment is not concerning others, just returns standard assignments.
            return sortWords(wordCandidates, comparator);
        }

        // Step (3): Sorts all the words in the lcv order.
        // Step (3-1): Builds a count-to-words map
        Map<Integer, List<String>> nValMap = new HashMap<>(wordCandidates.size());
        int count;
        IntersectJudger j;
        next:
        for (String word: wordCandidates) {
            count = 0;
            for (Variable n: neighbors) {
                j = new IntersectJudger(n, elect, word);
                // Finds the size of single unassigned variable's domain
                int passed = (int) successor.getDomainByVariable(n).stream().filter(j::judge).count();
                if (forwardCheck && passed == 0) {
                    // If this word contributes to some another unassigned variable having empty domain, this word
                    // must lead to failure; hence this word is not put into consideration and such node should not
                    // be generated.
                    continue next;
                }
                count += passed;
            }
            List<String> list = nValMap.computeIfAbsent(count, k -> new ArrayList<>());
            list.add(word);
        }
        // Step (3-2): Then converts this map to a list or ordered words
        List<Integer> counts = new ArrayList<>(nValMap.keySet());
        counts.sort(null);
        List<String> words = new ArrayList<>(wordCandidates.size());
        for (int c: counts) {
            List<String> picked = nValMap.get(c);
            sortWords(picked, comparator);
            words.addAll(picked);
        }
        return words;
    }

    /**
     * Given the elected variable, sorts words in the order of LCV algorithm and performs forward checking. Noted that
     * sometimes not all words are found in the returned list if such assignment empties another variable's domain.
     * @param elect          elected variable
     * @param wordCandidates words should be sorted
     * @param successor      successor node
     * @param comparator     if multiple words have same level then sorts these words by this comparator; sets to null
     *                       to perform randomly sort
     */
    public static List<String> lcvForwardCheck(Variable elect,
                                               Collection<String> wordCandidates,
                                               Node successor,
                                               Comparator<String> comparator) {
        return lcv(elect, wordCandidates, successor, comparator, true);
    }

    private static List<String> sortWords(Collection<String> words, Comparator<String> comp) {
        List<String> list = new ArrayList<>(words);
        if (comp == null) {
            Collections.shuffle(list, new Random(System.currentTimeMillis()));
        }
        else {
            list.sort(comp);
        }
        return list;
    }

    private boolean forwardCheck;

    public LeastConstrainingValueExpander(boolean forwardCheck) {
        this.forwardCheck = forwardCheck;
    }

    @Override
    public List<Assignment> assign(Node successor) {
        Collection<Variable> unassignedVars = successor.getUnassignedVariables();
        // Step (1)
        Variable elect = findElect().select(unassignedVars, successor);
        if (elect == null) {
            // If for whatever reason an elect cannot be selected, this node is not expandable.
            // Hence returns an empty list.
            return List.of();
        }
        // Step (2) & (3)
        List<String> wordsInLcv = lcv(elect,
                                      successor.getDomainByVariable(elect),
                                      successor,
                                      wordOrderPolicy(),
                                      forwardCheck);
        return Expander.matchWords(elect, wordsInLcv);
    }

    /** Step (1): Picks a variable as the elect. */
    protected abstract Selector<Variable> findElect();

    protected abstract Comparator<String> wordOrderPolicy();
}
