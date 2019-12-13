package org.vanautrui.octofinsights.controllers.auth;

import spark.Request;

public final class AuthUtils {

    public static void authenticate_user(final Request req, final int user_id, final String username){
        req.session(true);
        req.session().attribute("authenticated", "true");
        req.session().attribute("username", username+"");
        req.session().attribute("user_id", user_id+"");
    }
}
