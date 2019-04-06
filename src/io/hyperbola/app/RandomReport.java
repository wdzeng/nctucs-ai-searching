package io.hyperbola.app;
import java.io.*;
import java.util.List;
import io.hyperbola.algo.*;
import io.hyperbola.base.Dictionary;
import io.hyperbola.base.VariableSurveyResult;
import static io.hyperbola.algo.ThreeInOneExpander.DGH_MRV_LCV;
import static io.hyperbola.algo.ThreeInOneExpander.MRV_DGH_LCV;
import static java.text.NumberFormat.getNumberInstance;

public class RandomReport {

    private static final Expander oBsc = new BasicExpander();
    private static final Expander oDgh = new BasicDegreeHeuristicExpander();
    private static final Expander oDghMrvLcv = new BasicThreeInOneExpander(DGH_MRV_LCV);
    private static final Expander oLcv = new BasicLeastConstrainingValueExpander(true);
    private static final Expander oMrv = new BasicMinimumRemainingValueExpander();
    private static final Expander oMrvDghLcv = new BasicThreeInOneExpander(MRV_DGH_LCV);
    private static final Expander rBsc = new RandomExpander();
    private static final Expander rDgh = new RandomDegreeHeuristicExpander();
    private static final Expander rDghMrvLcv = new RandomThreeInOneExpander(DGH_MRV_LCV);
    private static final Expander rLcv = new RandomLeastConstrainingValueExpander(true);
    private static final Expander rMrv = new RandomMinimumRemainingValueExpander();
    private static final Expander rMrvDghLcv = new RandomThreeInOneExpander(MRV_DGH_LCV);
    private static PrintWriter pw;

    private static void bar() {
        println("-".repeat(106));
    }

    public static void main(String[] args) throws IOException {
        List<VariableSurveyResult> vsrList
                = RuleInflater.inflate(new FileInputStream("res/homework material/puzzle.txt"));
        Dictionary dict = new Dictionary(new FileInputStream("res/dictionary/3000 words.txt"));
        for (int i = 0; i < 3; i++) {
            VariableSurveyResult vsr = vsrList.get(i);
            String title = "Test Data " + (i + 1) + " (Random)";
            String path = "res/data/" + title + ".txt";
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path)));
            println("# " + title);
            task(dict, vsr);
            println("");
            pw.close();
        }
    }

    private static void println(String s) {
        System.out.println(s);
        pw.println(s);
    }

    private static void task(Dictionary dict, VariableSurveyResult vsr) {

        AbstractNode node;
        String testName;

        println("");
        println(String.format("%-31s%12s%18s%18s%9s%18s",
                              "Searching algorithm",
                              "Solutions",
                              "Time (ms)",
                              "Steps",
                              "S/T",
                              "Max stack size"));

        bar();
        node = new BasicNode(vsr, dict, false);
        testName = "";
        warm(node, oMrvDghLcv); // JVM warn up
        warm(node, oDghMrvLcv);

        test("MRV-DGH-LCV " + testName, node, oMrvDghLcv, 'S');
        test("MRV-DGH-LCV " + testName, node, rMrvDghLcv, 'R');
        println("");
        test("DGH-MRC-LCV " + testName, node, oDghMrvLcv, 'S');
        test("DGH-MRC-LCV " + testName, node, rDghMrvLcv, 'R');
        println("");
        test("MRV " + testName, node, oMrv, 'S');
        test("MRV " + testName, node, rMrv, 'R');
        println("");
        test("DGH " + testName, node, oDgh, 'S');
        test("DGH " + testName, node, rDgh, 'R');
        println("");
        test("Basic " + testName, node, oBsc, 'S');
        test("Basic " + testName, node, rBsc, 'R');

        bar();
        node = new BasicNode(vsr, dict, true);
        testName = "Forward check";
        test("MRV-DGH-LCV " + testName, node, oMrvDghLcv, 'S');
        test("MRV-DGH-LCV " + testName, node, rMrvDghLcv, 'R');
        println("");
        test("DGH-MRC-LCV " + testName, node, oDghMrvLcv, 'S');
        test("DGH-MRC-LCV " + testName, node, rDghMrvLcv, 'R');
        println("");
        test("MRV " + testName, node, oMrv, 'S');
        test("MRV " + testName, node, rMrv, 'R');
        println("");
        test("DGH " + testName, node, oDgh, 'S');
        test("DGH " + testName, node, rDgh, 'R');
        println("");
        test("LCV " + testName, node, oLcv, 'S');
        test("LCV " + testName, node, rLcv, 'R');
        println("");
        test("Basic " + testName, node, oBsc, 'S');
        test("Basic " + testName, node, rBsc, 'R');

        bar();
        node = new Ac3Node(vsr, dict);
        testName = "AC-3";
        test("MRV-DGH-LCV " + testName, node, oMrvDghLcv, 'S');
        test("MRV-DGH-LCV " + testName, node, rMrvDghLcv, 'R');
        println("");
        test("DGH-MRC-LCV " + testName, node, oDghMrvLcv, 'S');
        test("DGH-MRC-LCV " + testName, node, rDghMrvLcv, 'R');
        println("");
        test("MRV " + testName, node, oMrv, 'S');
        test("MRV " + testName, node, rMrv, 'R');
        println("");
        test("DGH " + testName, node, oDgh, 'S');
        test("DGH " + testName, node, rDgh, 'R');
        println("");
        test("LCV " + testName, node, oLcv, 'S');
        test("LCV " + testName, node, rLcv, 'R');
        println("");
        test("Basic " + testName, node, oBsc, 'S');
        test("Basic " + testName, node, rBsc, 'R');
    }

    private static void test(String title, AbstractNode root, Expander e, char c) {
        int nSol = 0, nStep = 0, maxStackSize = 0;
        long time = 0;
        int t = c == 'S'? 5: 87;
        for (int i = 1; i <= t; i++) {
            PuzzleIterator s = new DepthFirstPuzzleIterator(root, e);
            int solCount = 0;
            long start = System.currentTimeMillis();
            while (s.nextSolution() && solCount < 16023) solCount++;
            long end = System.currentTimeMillis();
            end -= start;
            String output = String.format("%-28s%3s%12s%18s%18s%9s%18s",
                                          title,
                                          "" + c + String.format("%02d", i),
                                          getNumberInstance().format(solCount),
                                          getNumberInstance().format(end),
                                          getNumberInstance().format(s.step()),
                                          end == 0? "Infinity" : getNumberInstance().format(s.step() / end),
                                          getNumberInstance().format(s.maxStackSize()));
            println(output);
            nSol += solCount;
            nStep += s.step();
            maxStackSize += s.maxStackSize();
            time += end;
        }
        String output = String.format("%-28s%3s%12s%18s%18s%9s%18s",
                                      title,
                                      "AVG",
                                      getNumberInstance().format(nSol / t),
                                      getNumberInstance().format(time / t),
                                      getNumberInstance().format(nStep / t),
                                      time == 0? "infinity": getNumberInstance().format(nStep / time),
                                      getNumberInstance().format(maxStackSize / t));
        println(output);
    }

    private static void warm(AbstractNode root, Expander e) {
        PuzzleIterator s = new DepthFirstPuzzleIterator(root, e);
        int solCount = 0;
        while (s.nextSolution() && solCount < 1500000) solCount++;
    }
}
