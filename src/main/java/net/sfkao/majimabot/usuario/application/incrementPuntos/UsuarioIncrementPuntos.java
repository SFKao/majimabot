package net.sfkao.majimabot.usuario.application.incrementPuntos;

import net.sfkao.majimabot.exceptions.UsuarioNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface UsuarioIncrementPuntos {

    void incrementPuntos(long id, int cantidad) ;

}
