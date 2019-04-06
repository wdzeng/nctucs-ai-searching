package io.hyperbola.app;
import java.io.*;
import java.util.List;
import io.hyperbola.algo.*;
import io.hyperbola.base.Dictionary;
import io.hyperbola.base.VariableSurveyResult;
import static io.hyperbola.algo.ThreeInOneExpander.MRV_DGH_LCV;

public class FindSolutionCount {

    static PrintWriter pw;

    public static void main(String[] args) throws IOException {
        List<VariableSurveyResult> vsrList
                = RuleInflater.inflate(new FileInputStream("res/homework material/puzzle.txt"));
        Dictionary dict = new Dictionary(new FileInputStream("res/dictionary/3000 words.txt"));
        pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("res/data/Solution counts.txt")));
        for (int i = 0; i < 4; i++) {
            VariableSurveyResult vsr = vsrList.get(i);
            find(vsr, dict, i + 1);
        }
        pw.close();
    }

    private static void find(VariableSurveyResult vsr, Dictionary dict, int index) {
        BasicNode n = new BasicNode(vsr, dict, false);
        Expander e = new BasicThreeInOneExpander(MRV_DGH_LCV);
        PuzzleIterator pi = new DepthFirstPuzzleIterator(n, e);
        int nSol = 0;
        while (pi.nextSolution()) nSol++;
        pw.println("Test data #" + index + " has " + nSol + " solutions.");
        System.out.println("Test data #" + index + " has " + nSol + " solutions.");
    }
}
