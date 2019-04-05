package io.hyperbola.app;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import io.hyperbola.base.Variable;
import io.hyperbola.base.VariableSurveyResult;
import static java.lang.Integer.parseInt;

public class RuleInflater {

    private static boolean dir(String s) {
        switch (s.toUpperCase()) {
        case "A":
        case "H":
            return Variable.HORIZONTAL;
        case "D":
        case "V":
            return Variable.VERTICAL;
        default:
            throw new IllegalRuleException("Invalid direction: " + s);
        }
    }

    public static List<VariableSurveyResult> inflate(InputStream in) throws IOException {
        List<VariableSurveyResult> res = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String readed;
            while ((readed = br.readLine()) != null) res.add(parse(readed));
        }
        return res;
    }

    private static VariableSurveyResult parse(String line) {
        String[] spliced = line.split("\\s+");
        if (spliced.length % 4 != 0) throw new IllegalRuleException("Invalid rule: " + line);
        Variable.Builder b = new Variable.Builder();
        for (int i = 0; i < spliced.length; i += 4) {
            b.addVariable(parseInt(spliced[i]),
                          parseInt(spliced[i + 1]),
                          parseInt(spliced[i + 2]),
                          dir(spliced[i + 3]));
        }
        return b.build();
    }

    private RuleInflater() {}
}
