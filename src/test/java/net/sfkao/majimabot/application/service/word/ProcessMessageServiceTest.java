package net.sfkao.majimabot.application.service.word;

import net.sfkao.majimabot.application.port.out.WordBlackListDatabasePort;
import net.sfkao.majimabot.application.service.ProcessMessageService;
import net.sfkao.majimabot.domain.MessageEvent;
import net.sfkao.majimabot.domain.Word;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProcessMessageServiceTest {

    @Mock
    private WordGetMatchesService wordGetMatchesService;

    @Mock
    private WordBlackListDatabasePort wordBlackListDatabasePort;

    @InjectMocks
    private ProcessMessageService processMessageService;

    @Test
    void hasMatches(){

        //GIVEN
        List<Word> matches = getMatches();
        when(wordGetMatchesService.getMatches(any())).thenReturn(matches);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(500000);
        MockedStatic<Calendar> calendarMockedStatic = mockStatic(Calendar.class);
        when(Calendar.getInstance()).thenReturn(calendar);

        //WHEN
        MessageEvent response = processMessageService.processMessage(getEvent());

        //THEN
        assertNull(response.getMessageResponse());
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(500000);
        calendar.add(Calendar.MINUTE, 3);
        assertEquals(response.getBanUntil().toEpochMilli(), calendar.toInstant().toEpochMilli());

        verify(wordBlackListDatabasePort, times(1)).incrementMines(response.getIdUsuario(), matches.size());
        verify(wordBlackListDatabasePort, times(1)).incrementPoints(123, 1);
        verify(wordBlackListDatabasePort, times(1)).incrementPoints(456, 2);

        calendarMockedStatic.close();

    }

    List<Word> getMatches(){
        return List.of(
                new Word("qwe", "test", 123),
                new Word("asd", "test", 456),
                new Word("zxc", "hola", 456)
        );
    }

    MessageEvent getEvent(){
        return new MessageEvent("hola buenas tardes test", 741, null, null, null);
    }


}
