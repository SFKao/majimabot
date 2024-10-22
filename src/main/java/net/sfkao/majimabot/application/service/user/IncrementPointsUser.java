package net.sfkao.majimabot.application.service.user;

import org.springframework.stereotype.Service;

@Service
public interface IncrementPointsUser {

    void incrementPuntos(long id, int cantidad) ;

}
