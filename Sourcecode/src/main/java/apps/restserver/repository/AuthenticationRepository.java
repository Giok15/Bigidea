package apps.restserver.repository;

import apps.generic.models.Account;
import apps.restserver.context.IAuthenticationContext;

public class AuthenticationRepository {
    private IAuthenticationContext authenticationInterface;

    public AuthenticationRepository(IAuthenticationContext context)
    {
        authenticationInterface = context;
    }

    public Account login(String email, String password)
    {
      return authenticationInterface.selectAccount(email, password);
    }

    public boolean register(Account account)
    {
        return authenticationInterface.insertAccount(account);
    }

    public Boolean checkAccountExistByEmail(String email)
    {
        return authenticationInterface.selectAccountByEmail(email);
    }
}
