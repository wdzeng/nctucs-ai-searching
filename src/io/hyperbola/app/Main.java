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
        println("-".repeat(94));
    }

    public static void main(String[] args) throws IOException {
        int[] dicts = {200, 500, 1000, 1800, 3000};
        for (int i = 2; i <= 4; i++) {
            for (int dict: dicts) {
                task(dict, "Test data " + i + " (Standard)/dict" + dict + ".txt", i, "Dict " + dict);
            }
        }
    }

    private static void task(int dictCount, String outputPath, int varIndex, String title) throws IOException {
        List<VariableSurveyResult> resList = RuleInflater.inflate(new FileInputStream("res/puzzle.txt"));
        VariableSurveyResult res = resList.get(varIndex);

        Node node;
        String testName;
        String path = "res/" + outputPath;
        File p = new File(path);
        p.getParentFile().mkdirs();
        Dictionary dict = new Dictionary(new FileInputStream("res/dict" + dictCount + ".txt"));
        Expander oDgh = new BasicDegreeHeuristicExpander();
        Expander oMrv = new BasicMinimumRemainingValueExpander();
        Expander oLcv = new BasicLeastConstrainingValueExpander(true);
        Expander oDghMrvLcv = new BasicThreeInOneExpander(DGH_MRV_LCV);
        Expander oMrvDghLcv = new BasicThreeInOneExpander(MRV_DGH_LCV);
        Expander oBsc = new BasicExpander();

        pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("res/" + outputPath)));
        println(title);
        println("");
        println(String.format("%-28s%12s%18s%18s%18s",
                              "Searching algorithm",
                              "Solutions",
                              "Time (ms)",
                              "Steps",
                              "Max stack size"));
        bar();

        node = new BasicNode(res, dict, false);
        testName = "Basic";
        test("MRV-DGH-LCV " + testName, node, oMrvDghLcv);
        test("DGH-MRC-LCV " + testName, node, oDghMrvLcv);
        test("MRV " + testName, node, oMrv);
        test("DGH " + testName, node, oDgh);
        test("LCV " + testName, node, oLcv);
        test("Basic " + testName, node, oBsc);

        bar();
        node = new BasicNode(res, dict, true);
        testName = "Forward check";
        test("MRV-DGH-LCV " + testName, node, oMrvDghLcv);
        test("DGH-MRC-LCV " + testName, node, oDghMrvLcv);
        test("MRV " + testName, node, oMrv);
        test("DGH " + testName, node, oDgh);
        test("LCV " + testName, node, oLcv);
        test("Basic " + testName, node, oBsc);

        bar();
        node = new Ac3Node(res, dict);
        testName = "AC-3";
        test("MRV-DGH-LCV " + testName, node, oMrvDghLcv);
        test("DGH-MRC-LCV " + testName, node, oDghMrvLcv);
        test("MRV " + testName, node, oMrv);
        test("DGH " + testName, node, oDgh);
        test("LCV " + testName, node, oLcv);
        test("Basic " + testName, node, oBsc);

        System.out.println("\n\n");
        pw.close();
    }

    private static void println(String s) {
        System.out.println(s);
        pw.println(s);
    }

    private static void test(String title, Node root, Expander e) {
        PuzzleIterator s = new DepthFirstPuzzleIterator(root, e);
        int solCount = 0;
        long start = System.currentTimeMillis();
        while (s.nextSolution()) solCount++;
        long end = System.currentTimeMillis();
        String output = String.format("%-28s%12s%18s%18s%18s",
                                      title,
                                      NumberFormat.getNumberInstance().format(solCount),
                                      NumberFormat.getNumberInstance().format(end - start),
                                      NumberFormat.getNumberInstance().format(s.step()),
                                      NumberFormat.getNumberInstance().format(s.maxStackSize()));
        println(output);
    }
}
