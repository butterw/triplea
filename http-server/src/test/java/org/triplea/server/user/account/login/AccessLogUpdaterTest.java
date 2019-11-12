package org.triplea.server.user.account.login;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.triplea.domain.data.PlayerChatId;
import org.triplea.domain.data.PlayerName;
import org.triplea.domain.data.SystemId;
import org.triplea.lobby.server.db.dao.access.log.AccessLogDao;

@ExtendWith(MockitoExtension.class)
class AccessLogUpdaterTest {

  private static final LoginRecord REGISTEREDLOGIN_RECORD =
      LoginRecord.builder()
          .registered(true)
          .systemId(SystemId.of("system-id"))
          .playerChatId(PlayerChatId.newId())
          .ip("ip")
          .playerName(PlayerName.of("player-name"))
          .build();

  private static final LoginRecord ANONYMOUS_LOGIN_RECORD =
      LoginRecord.builder()
          .registered(false)
          .systemId(SystemId.of("system-id"))
          .playerChatId(PlayerChatId.newId())
          .ip("ip")
          .playerName(PlayerName.of("player-name"))
          .build();

  @Mock private AccessLogDao accessLogDao;

  private AccessLogUpdater accessLogUpdater;

  @BeforeEach
  void setup() {
    accessLogUpdater = AccessLogUpdater.builder().accessLogDao(accessLogDao).build();
  }

  @Test
  void acceptRegistered() {
    when(accessLogDao.insertRegisteredUserRecord(any(), any(), any())).thenReturn(1);

    accessLogUpdater.accept(REGISTEREDLOGIN_RECORD);

    verify(accessLogDao)
        .insertRegisteredUserRecord(
            REGISTEREDLOGIN_RECORD.getPlayerName().getValue(),
            REGISTEREDLOGIN_RECORD.getIp(),
            REGISTEREDLOGIN_RECORD.getSystemId().getValue());
  }

  @Test
  void acceptAnonymous() {
    when(accessLogDao.insertAnonymousUserRecord(any(), any(), any())).thenReturn(1);

    accessLogUpdater.accept(ANONYMOUS_LOGIN_RECORD);

    verify(accessLogDao)
        .insertAnonymousUserRecord(
            ANONYMOUS_LOGIN_RECORD.getPlayerName().getValue(),
            ANONYMOUS_LOGIN_RECORD.getIp(),
            ANONYMOUS_LOGIN_RECORD.getSystemId().getValue());
  }
}
