package apps.restserver.context;

import apps.generic.models.Account;

public interface IAuthenticationContext {
    boolean insertAccount(Account account);
    Account selectAccount(String email, String password);
    boolean selectAccountByEmail(String email);
}
