package net.sfkao.majimabot.application.service.word;

import net.sfkao.majimabot.application.port.out.ProcessMessageDatabasePort;
import net.sfkao.majimabot.application.util.WordCleaner;
import net.sfkao.majimabot.domain.Word;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WordGetMatchesServiceTest {

    @InjectMocks
    private WordGetMatchesServiceImpl processMessageService;

    @Mock
    private ProcessMessageDatabasePort processMessageDatabasePort;

    @Mock
    private WordCleaner wordCleaner;

    @Test
    void should_get_matches() {

        //GIVEN
        when(processMessageDatabasePort.findByPalabraIn(any())).thenReturn(createPalabras());
        when(wordCleaner.clean("Test")).thenReturn("test");
        when(wordCleaner.clean("májìma")).thenReturn("majima");
        //WHEN
        List<Word> test = processMessageService.getMatches("Test   májìma");

        //THEN
        //Importante: 2 minas con misma palabra y mismo userid no deben aparecer
        assertEquals(4,test.size());
        Word word = test.get(0);
        assertEquals("test", word.getPalabra());
        assertEquals("asd", word.getId());
        assertEquals(123, word.getUserId());

        word = test.get(1);
        assertEquals("majima", word.getPalabra());
        assertEquals("qwe", word.getId());
        assertEquals(456, word.getUserId());

        word = test.get(2);
        assertEquals("test", word.getPalabra());
        assertEquals("zxc", word.getId());
        assertEquals(789, word.getUserId());

        word = test.get(3);
        assertEquals("majima", word.getPalabra());
        assertEquals("rfv", word.getId());
        assertEquals(123, word.getUserId());
    }

    private List<Word> createPalabras(){
        Word w = new Word("asd", "test", 123);
        Word w2 = new Word("qwe", "majima", 456);
        Word w3 = new Word("zxc", "test", 789);
        Word w4 = new Word("rfv", "majima", 123);
        //Meto dos veces la primera, solo deberia salir una
        return List.of(w, w, w2, w3, w4);
    }


}
