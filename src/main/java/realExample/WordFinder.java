package realExample;

import java.net.URL;
import java.util.HashSet;

public interface WordFinder {

    HashSet<String> getSentences(URL resource);
    boolean checkIfWordSentence(String sentence, String word);
    void writeSentence(String sentence);
}
