package GenericTest;

import apps.generic.models.Account;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AccountTest {

    @Test
    public void accountWithNameEmailPassword(){

        Account account = new Account("Test", "test@test.nl", "@Welkom1");
        assertEquals("Test",account.getName() );
        assertEquals("@Welkom1", account.getPassword());
        assertEquals("test@test.nl", account.getEmail() );
    }

    @Test
    public void accountWithNameEmail(){

        Account account = new Account( "test@test.nl", "@Welkom1");
        assertNull(account.getName());
        assertEquals("@Welkom1", account.getPassword());
        assertEquals("test@test.nl", account.getEmail());
    }

    @Test
    public void account(){
        Account account = new Account();
        assertNull(account.getEmail());
        assertNull(account.getPassword());
        assertNull(account.getName());
    }
}
