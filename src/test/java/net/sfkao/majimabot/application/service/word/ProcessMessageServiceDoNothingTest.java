package net.sfkao.majimabot.application.service.word;

import net.sfkao.majimabot.application.port.out.WordBlackListDatabasePort;
import net.sfkao.majimabot.application.service.ProcessMessageService;
import net.sfkao.majimabot.domain.MessageEvent;
import net.sfkao.majimabot.domain.Word;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProcessMessageServiceDoNothingTest {

    @Mock
    private WordGetMatchesService wordGetMatchesService;

    @Mock
    private WordBlackListDatabasePort wordBlackListDatabasePort;

    @Mock
    private RandomGenerator random;

    @InjectMocks
    private ProcessMessageService processMessageService;

    @Test
    void shiny(){

        //GIVEN
        List<Word> matches = getMatches();
        when(wordGetMatchesService.getMatches(any())).thenReturn(matches);
        when(random.nextInt(4096)).thenReturn(0);

        //WHEN
        MessageEvent response = processMessageService.processMessage(getEvent());

        //THEN
        assertNull(response.getMessageResponse());
        assertNull(response.getMatches());
        assertNull(response.getBanUntil());

        verify(wordBlackListDatabasePort, times(0)).incrementMines(anyLong(), anyInt());
        verify(wordBlackListDatabasePort, times(0)).incrementPoints(anyLong(), anyInt());
        verify(wordBlackListDatabasePort, times(0)).incrementPoints(anyLong(), anyInt());


    }

    List<Word> getMatches(){
        return List.of();
    }

    MessageEvent getEvent(){
        return new MessageEvent("hola buenas tardes test", 741, null, null, null);
    }


}
