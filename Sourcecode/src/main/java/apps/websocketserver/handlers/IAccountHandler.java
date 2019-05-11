package apps.websocketserver.handlers;

import apps.generic.models.Account;

public interface IAccountHandler {
    boolean authenticate(Account account);
    boolean isAccountExist(Account account);
}
