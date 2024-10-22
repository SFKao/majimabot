package net.sfkao.majimabot.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Word {

    @EqualsAndHashCode.Include
    private String palabra;

    private long userId;

    public void asignarUsuario(long userId) {
        this.userId = userId;
    }
}
