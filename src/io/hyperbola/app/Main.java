package io.hyperbola.app;
import java.io.*;
import java.text.NumberFormat;
import java.util.List;
import io.hyperbola.algo.*;
import io.hyperbola.base.Dictionary;
import io.hyperbola.base.VariableSurveyResult;
import static io.hyperbola.algo.ThreeInOneExpander.DGH_MRV_LCV;
import static io.hyperbola.algo.ThreeInOneExpander.MRV_DGH_LCV;

public class Main {

    private static PrintWriter pw;

    private static void bar() {
        println("-".repeat(106));
    }

    public static void main(String[] args) throws IOException {
        int[] dss = {200, 500, 1000, 1800, 3000};
        List<VariableSurveyResult> vsrList
                = RuleInflater.inflate(new FileInputStream("res/homework material/puzzle.txt"));
        for (int i = 1; i < 3; i++) {
            VariableSurveyResult vsr = vsrList.get(i);
            String title = "Test Data " + (i + 1) + " (Standard)";
            String path = "res/results/" + title + ".txt";
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path)));
            println("# " + title);
            println("");
            for (int s: dss) {
                Dictionary dict = new Dictionary(new FileInputStream("res/dictionary/" + s + " words.txt"));
                task(dict, vsr, "## " + s + " Words");
                println("");
            }
            pw.close();
        }
    }

    private static void println(String s) {
        System.out.println(s);
        pw.println(s);
    }

    private static void task(Dictionary dict, VariableSurveyResult vsr, String title) {

        AbstractNode node;
        String testName;
        Expander oDgh = new BasicDegreeHeuristicExpander();
        Expander oMrv = new BasicMinimumRemainingValueExpander();
        Expander oLcv = new BasicLeastConstrainingValueExpander(true);
        Expander oDghMrvLcv = new BasicThreeInOneExpander(DGH_MRV_LCV);
        Expander oMrvDghLcv = new BasicThreeInOneExpander(MRV_DGH_LCV);
        Expander oBsc = new BasicExpander();

        println(title);
        println("");
        println(String.format("%-31s%12s%18s%18s%9s%18s",
                              "Searching algorithm",
                              "Solutions",
                              "Time (ms)",
                              "Steps",
                              "Spms",
                              "Max stack size"));
        bar();

        node = new BasicNode(vsr, dict, false);
        testName = "";
        test("MRV-DGH-LCV " + testName, node, oMrvDghLcv);
        println("");
        test("DGH-MRC-LCV " + testName, node, oDghMrvLcv);
        println("");
        test("MRV " + testName, node, oMrv);
        println("");
        test("DGH " + testName, node, oDgh);
        println("");
        test("LCV " + testName, node, oLcv);
        println("");
        test("Basic " + testName, node, oBsc);

        bar();
        node = new BasicNode(vsr, dict, true);
        testName = "Forward check";
        test("MRV-DGH-LCV " + testName, node, oMrvDghLcv);
        println("");
        test("DGH-MRC-LCV " + testName, node, oDghMrvLcv);
        println("");
        test("MRV " + testName, node, oMrv);
        println("");
        test("DGH " + testName, node, oDgh);
        println("");
        test("LCV " + testName, node, oLcv);
        println("");
        test("Basic " + testName, node, oBsc);

        bar();
        node = new Ac3Node(vsr, dict);
        testName = "AC-3";
        test("MRV-DGH-LCV " + testName, node, oMrvDghLcv);
        println("");
        test("DGH-MRC-LCV " + testName, node, oDghMrvLcv);
        println("");
        test("MRV " + testName, node, oMrv);
        println("");
        test("DGH " + testName, node, oDgh);
        println("");
        test("LCV " + testName, node, oLcv);
        println("");
        test("Basic " + testName, node, oBsc);
    }

    private static void test(String title, AbstractNode root, Expander e) {
        int nSol = 0, nStep = 0, maxStackSize = 0;
        long time = 0;
        for (int i = 1; i <= 5; i++) {
            PuzzleIterator s = new DepthFirstPuzzleIterator(root, e);
            int solCount = 0;
            long start = System.currentTimeMillis();
            while (s.nextSolution()) solCount++;
            long end = System.currentTimeMillis();
            end -=start;
            String output = String.format("%-28s%3s%12s%18s%18s%9s%18s",
                                          title,
                                          "# " + i,
                                          NumberFormat.getNumberInstance().format(solCount),
                                          NumberFormat.getNumberInstance().format(end),
                                          NumberFormat.getNumberInstance().format(s.step()),
                                          NumberFormat.getNumberInstance().format(end/s.step()),
                                          NumberFormat.getNumberInstance().format(s.maxStackSize()));
            println(output);
            nSol += solCount;
            nStep += s.step();
            maxStackSize += s.maxStackSize();
            time += (end - start);
        }
        String output = String.format("%-28s%3s%12s%18s%18s%9s%18s",
                                      title,
                                      "AVG",
                                      NumberFormat.getNumberInstance().format(nSol / 5),
                                      NumberFormat.getNumberInstance().format(time / 5),
                                      NumberFormat.getNumberInstance().format(nStep / 5),
                                      NumberFormat.getNumberInstance().format(time/nStep),
                                      NumberFormat.getNumberInstance().format(maxStackSize / 5));
        println(output);
    }
}
