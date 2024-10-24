package net.sfkao.majimabot.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageEvent {

    private String content;
    private long idUsuario;

    private String messageResponse;
    private Instant banUntil;
    private List<Word> matches;

}
