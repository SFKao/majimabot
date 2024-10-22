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
public class User {

    @EqualsAndHashCode.Include
    private long id;

    private long puntos;

    private long minasExplotadas;

    public void incrementarPuntos(long puntosExtra) {
        this.puntos += puntosExtra;
    }

    public void incrementarMinasExplotadas(long minasExtra) {
        this.minasExplotadas += minasExtra;
    }

}
