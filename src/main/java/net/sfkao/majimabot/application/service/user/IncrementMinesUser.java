package net.sfkao.majimabot.application.service.user;

import org.springframework.stereotype.Service;

@Service
public interface IncrementMinesUser {

    void incrementMinas(long id, int cantidad)  ;

}
