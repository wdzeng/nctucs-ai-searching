package io.hyperbola.base;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

public final class Dictionary {

    private Map<Integer, List<String>> dict; // All words are sorted A-Z

    public Dictionary(InputStream in) throws IOException {
        List<String> wordList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String readed;
            while ((readed = br.readLine()) != null) wordList.add(readed);
        }
        buildDictionary(wordList);
    }

    public Dictionary(Collection<String> wordList) {
        buildDictionary(wordList);
    }

    private void buildDictionary(Collection<String> wordList) {
        Map<Integer, Set<String>> map = new HashMap<>();
        Set<String> s;
        for (String readed: wordList) {
            s = map.computeIfAbsent(readed.length(), k -> new HashSet<>());
            s.add(readed.toUpperCase());
        }
        dict = new HashMap<>(map.size());
        for (Map.Entry<Integer, Set<String>> e: map.entrySet()) {
            dict.put(e.getKey(), e.getValue().stream().sorted().collect(toList()));
        }
    }

    public List<String> getWordsByLength(int length) {
        List<String> s = dict.get(length);
        return s == null? List.of(): s;
    }
}
