package net.sfkao.majimabot.application.port.in;

import net.sfkao.majimabot.domain.MessageEvent;
import net.sfkao.majimabot.domain.exception.UserNotFoundException;

public interface ProcessMessagePort {
    MessageEvent processMessage(MessageEvent event) throws UserNotFoundException;
}
