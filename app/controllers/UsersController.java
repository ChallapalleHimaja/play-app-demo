package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.security.Authenticator;
import controllers.security.IsAdmin;
import daos.UserDao;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Optional;

public class UsersController extends Controller {

    private final static Logger.ALogger LOGGER = Logger.of(UsersController.class);

    private UserDao userDao;

    @Inject
    public UsersController(UserDao userDao) {
        this.userDao = userDao;
    }

    public Result registerUser() {

        final JsonNode json = request().body().asJson();
        final User user = Json.fromJson(json, User.class);

        if (null == user.getUsername() ||
                null == user.getPassword() ||
                null == user.getEmail()) {
            return badRequest("Missing mandatory parameters");
        }

        if (null != userDao.findUserByName(user.getUsername())) {
            return badRequest("User taken");
        }

        user.setState(User.State.VERIFIED);
        user.setRole(User.Role.USER);

        userDao.create(user);

        final JsonNode result = Json.toJson(user);

        return ok(result);
    }

    public Result signInUser() {

        final JsonNode json = request().body().asJson();
        final User user = Json.fromJson(json, User.class);

        if (null == user.getUsername() ||
                null == user.getPassword()) {
            return badRequest("Missing mandatory parameters");
        }

        final User existingUser = userDao.findUserByName(user.getUsername());

        if (null == existingUser) {
            return unauthorized("Wrong username");
        }

        if (!existingUser.getPassword().equals(user.getPassword())) {
            return unauthorized("Wrong password");
        }

        if (existingUser.getState() != User.State.VERIFIED) {
            return unauthorized("Account not verified");
        }

        existingUser.setAccessToken(generateAccessToken());

        userDao.update(existingUser);

        final JsonNode result = Json.toJson(existingUser);

        return ok(result);
    }

    private String generateAccessToken() {

        // TODO Generate a random string of 20 (or more characters)

        return "ABC1234";
    }

    @Authenticator
    public Result signOutUser() {

        final User user = (User) ctx().args.get("user");

        user.setAccessToken(null);

        userDao.update(user);

        return ok();
    }

    @Authenticator
    @IsAdmin
    public Result getCurrentUser() {

        final User user = (User) ctx().args.get("user");
        
        final JsonNode result = Json.toJson(user);

        return ok(result);
    }


}
