package apps.restserver.controllers;

import apps.generic.models.Account;

import javax.ws.rs.core.Response;

public interface IAuthentication {
    Response login(Account account);
    Response register(Account account);
}
