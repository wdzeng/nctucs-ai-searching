package io.hyperbola.app;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import io.hyperbola.algo.*;
import io.hyperbola.base.Dictionary;
import io.hyperbola.base.VariableSurveyResult;
import static io.hyperbola.algo.ThreeInOneExpander.MRV_DGH_LCV;

public class FindResult {

    public static void main(String[] args) throws IOException {
        List<VariableSurveyResult> vsrList
                = RuleInflater.inflate(new FileInputStream("res/homework material/puzzle.txt"));
        Dictionary dict = new Dictionary(new FileInputStream("res/dictionary/3000 words.txt"));
        for (int i = 0; i < 4; i++) {
            VariableSurveyResult vsr = vsrList.get(i);

            BasicNode n = new BasicNode(vsr, dict, false);
            Expander e = new BasicExpander();
            PuzzleIterator pi = new DepthFirstPuzzleIterator(n, e);
            pi.nextSolution();
            System.out.println(pi.currentBoard().toHtml());

            n = new BasicNode(vsr, dict, false);
            Expander er = new RandomThreeInOneExpander(MRV_DGH_LCV);
            for (int z = 0; z < 3; z++) {
                pi = new DepthFirstPuzzleIterator(n, er);
                pi.nextSolution();
                System.out.println(pi.currentBoard().toHtml());
            }
        }
    }
}
