package net.sfkao.majimabot.usuario.application.incrementMinas;

import net.sfkao.majimabot.exceptions.UsuarioNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface UsuarioIncrementMinas {

    void incrementMinas(long id, int cantidad)  ;

}
