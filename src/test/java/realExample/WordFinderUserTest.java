package realExample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WordFinderUserTest {

    private static final String FILE_ANY = "file://any";
    private static final String WORD = "";
    private WordFinderUser wordFinderUser;
    private WordFinder wordFinder = Mockito.mock(WordFinder.class);
    private final static String FIRST_SENTENCE = "Doing Test";
    private final static String SECOND_SENTENCE = "Lets do it";

    @BeforeEach
    void beforeEach() {
        wordFinderUser = new WordFinderUser(wordFinder);
    }

    @Test
    void doWordNull() {
        when(wordFinder.getSentences(any())).thenReturn(null);
        assertDoesNotThrow(()->wordFinderUser.doWord("file://any", WORD));
    }

    @Test
    void doWordEmtySet() {
        when(wordFinder.getSentences(any())).thenReturn(new HashSet<>());
        try {
            wordFinderUser.doWord(FILE_ANY, "");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        verify(wordFinder, times(0)).checkIfWordSentence(any(), any());
        verify(wordFinder, times(0)).writeSentence(any());
    }

    @Test
    void doWordIfTrue() {
        when(wordFinder.getSentences(any())).thenReturn(new HashSet<>(Arrays.asList(FIRST_SENTENCE, SECOND_SENTENCE)));
        when(wordFinder.checkIfWordSentence(FIRST_SENTENCE, WORD)).thenReturn(true);
        when(wordFinder.checkIfWordSentence(SECOND_SENTENCE, WORD)).thenReturn(false);
        try {
            wordFinderUser.doWord(FILE_ANY, WORD);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        verify(wordFinder, times(1)).writeSentence(FIRST_SENTENCE);
        verify(wordFinder, times(0)).writeSentence(SECOND_SENTENCE);
    }

    @Test
    void doWordURLForming() {
        final ArgumentCaptor<URL> argument = ArgumentCaptor.forClass(URL.class);
        try {
            wordFinderUser.doWord(FILE_ANY, WORD);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        verify(wordFinder).getSentences(argument.capture());
        assertEquals(FILE_ANY, argument.getValue().toString());
    }

    @Test
    void doWordBadURL() {
        assertThrows(MalformedURLException.class, ()->wordFinderUser.doWord("", ""));
    }
}
