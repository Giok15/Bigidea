package apps.restserver.controllers;

import apps.generic.models.Account;
import apps.generic.constants.ErrorConstants;
import apps.restserver.context.MssqlAuthenticationImpl;
import apps.restserver.repository.AuthenticationRepository;
import apps.generic.utils.AccountUtil;
import apps.generic.utils.LoggingUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import java.util.logging.Level;

@Path("/authentication")
public class Authentication implements IAuthentication {

    private AuthenticationRepository authenticationRepo = new AuthenticationRepository(new MssqlAuthenticationImpl());

    /**
     * user login
     *
     * @param account - generic account model
     * @return Response - javax.ws.rs.core
     */
    @POST
    @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    @Override
    public Response login(Account account) {
        ResponseBuilder response = Response.status(Status.OK).entity("");
        try {
            Account result = authenticationRepo.login(account.getEmail(), account.getPassword());

            if (result != null) {
                result.setPassword(account.getPassword());
                response.entity(result);
            }
            else
            {
                response.status(Status.BAD_REQUEST).entity(ErrorConstants.ERROR_ACCOUNTUNKNOWN);
            }

        } catch (Exception e) {
           LoggingUtil.log(Authentication.class.getName(), Level.SEVERE, e);
            response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return response.build();
    }

    /**
     *  register an user
     *
     * @param account - generic account model
     * @return Response - javax.ws.rs.core
     */
    @POST
    @Path("/register")
    @Consumes("application/json")
    @Produces("application/json")
    @Override
    public Response register(Account account) {
        ResponseBuilder response = Response.status(Status.BAD_REQUEST);
        try {
            String result = isValid(account);
            if (result.equals("")) {
                if (authenticationRepo.register(account))
                    response.status(Status.CREATED);
            } else {
                response.entity(result);
            }
        } catch (Exception e) {
            LoggingUtil.log(Authentication.class.getName(), Level.SEVERE, e);
            response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return response.build();
    }


    /**
     * check if account is valid for creating
     *
     * @param account  - generic account model
     * @return result - error result
     */
    private String isValid(Account account) {
        String result = "";
        if (!AccountUtil.isValidMail(account.getEmail())) {
            result = ErrorConstants.ERROR_VALIDMAIL;
        } else if (!AccountUtil.isValidPassword(account.getPassword())) {
            result = ErrorConstants.ERROR_PASS;
        } else if (authenticationRepo.checkAccountExistByEmail(account.getEmail())) {
            result = ErrorConstants.ERROR_EMAILUSED;
        }
        return result;
    }
}