package org.vanautrui.octofinsights.controllers.auth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import j2html.tags.ContainerTag;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.vanautrui.octofinsights.db_utils.DBUtils;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectToGETResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.Map;
import java.util.Optional;

import static j2html.TagCreator.*;
import static org.vanautrui.octofinsights.generated.tables.Users.USERS;

public class LoginWithOAuthViaGoogleController extends VaquitaController {

    static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    //static HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

    private static GoogleClientSecrets getClientSecrets()throws Exception{
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(LoginWithOAuthViaGoogleController.class.getResourceAsStream("client_id.json")));


        return clientSecrets;
    }

    private static Credential authorize() throws Exception{
        GoogleClientSecrets clientSecrets = getClientSecrets();

        //TODO
        /*

        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            System.out.println(
                    "Enter Client ID and Secret from https://code.google.com/apis/console/?api=drive "
                            + "into drive-cmdline-sample/src/main/resources/client_secrets.json");
            System.exit(1);
        }
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singleton(DriveScopes.DRIVE_FILE)).setDataStoreFactory(dataStoreFactory)
                .build();
        // authorize

        return new AuthorizationCodeFlow();

         */
        return null;
    }

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPRequest vaquitaHTTPRequest) throws Exception {



        ContainerTag page =
                html(
                        HeadUtil.makeHead(),
                        body(
                                div(
                                        attrs(".container"),
                                        "should redirect to google login"
                                )
                        )
                );
        GoogleClientSecrets clientSecrets = getClientSecrets();

        //return new VaquitaHTMLResponse(200,page.render());
        String base="https://accounts.google.com/o/oauth2/v2/auth";
        String location=base+"?";
        base+="client_id="+clientSecrets.getDetails().getClientId();
        base+="&scope="+"https://www.googleapis.com/auth/calendar.events.readonly";
        base+="&access_type=online";
        URL url = new URL(base);
        return new VaquitaRedirectToGETResponse(url.toString(),vaquitaHTTPRequest);
    }

    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest) throws Exception {
        //verify login credentials and set cookie

        Map<String,String> parameters= vaquitaHTTPEntityEnclosingRequest.getPostParameters();

        String username = URLDecoder.decode(parameters.get("username"));
        String password = URLDecoder.decode(parameters.get("password"));

        Connection conn = DBUtils.makeDBConnection();

        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        Result<Record1<Integer>> does_it_login= create
                .select(USERS.ID)
                .from(USERS)
                .where(
                        USERS.USERNAME.eq(username)
                        .and(USERS.PASSWORD.eq(password))
                )
                .fetch();
        Optional<Integer> id = Optional.empty();

        if(does_it_login.size()>0){
            for(Record r : does_it_login){
                id=Optional.of(r.getValue(USERS.ID));
            }
        }

        conn.close();

        if(vaquitaHTTPEntityEnclosingRequest.session().isPresent()){

            if(id.isPresent()) {

                vaquitaHTTPEntityEnclosingRequest.session().get().put("authenticated", "true");
                vaquitaHTTPEntityEnclosingRequest.session().get().put("username", parameters.get("username"));
                vaquitaHTTPEntityEnclosingRequest.session().get().put("user_id", id.get().toString());

                return new VaquitaRedirectToGETResponse("/",vaquitaHTTPEntityEnclosingRequest);
            }else {
                return new VaquitaTextResponse(500,"user seems not to exist");
            }
        }else{
            return new VaquitaTextResponse(500,"something went wrong with the sessions");
        }
    }
}
