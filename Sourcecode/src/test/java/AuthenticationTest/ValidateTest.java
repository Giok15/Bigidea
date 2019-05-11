package AuthenticationTest;

import apps.generic.models.Account;
import apps.generic.utils.AccountUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ValidateTest {

    Account account;
    Account accountFalse;

    @Before
    public void setup(){
        account = new Account("test2", "test@test.nl", "@Welkom1");
        accountFalse = new Account("test2", "noEmail", "noPassword");
    }

    @Test
    public void validateEmail(){
        assertTrue(AccountUtil.isValidMail(account.getEmail()));
        assertFalse(AccountUtil.isValidMail(accountFalse.getEmail()));
    }

    @Test
    public void validPassword(){
        assertTrue(AccountUtil.isValidPassword(account.getPassword()));
        assertFalse(AccountUtil.isValidPassword(accountFalse.getPassword()));
    }
}
