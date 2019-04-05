package io.hyperbola.base;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public final class Dictionary {

    private Map<Integer, Set<String>> dict;

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
        for (Map.Entry<Integer, Set<String>> e: map.entrySet()) {
            e.setValue(Set.copyOf(e.getValue()));
        }
        dict = Map.copyOf(map);
    }

    public Set<String> getWordsByLength(int length) {
        Set<String> s = dict.get(length);
        return s == null? Set.of(): s;
    }
}
