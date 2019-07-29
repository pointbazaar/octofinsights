package org.vanautrui.octofinsights.controllers.auth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import j2html.tags.ContainerTag;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaAbsoluteRedirectResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import static j2html.TagCreator.*;

public class LoginWithOAuthViaGoogleController extends VaquitaController {

    static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    //static HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

    private static GoogleClientSecrets getClientSecrets()throws Exception{
        //GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
        //        new InputStreamReader(LoginWithOAuthViaGoogleController.class.getResourceAsStream("client_id.json")));

        InputStream in = Files.newInputStream(Paths.get("client_id.json"));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(in));

        return clientSecrets;
    }

    private static Credential authorize() throws Exception{



        //InputStream resourceAsStream = LoginWithOAuthViaGoogleController.class.getClassLoader().getResourceAsStream("client_id.json");

        //GoogleClientSecrets clientSecrets = getClientSecrets();

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
    public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest req, VaquitaApp app) throws Exception {



        ContainerTag page =
                html(
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
        location+="client_id="+clientSecrets.getDetails().getClientId();
        location+="&scope="+"https://www.googleapis.com/auth/calendar.events.readonly";
        location+="&access_type=online";
        location+="&response_type=code";
        location+="&redirect_uri=https://octofinsights.vanautrui.org";

        System.out.println("GET Request going to be made to :");
        System.out.println(location);

        //URL url = new URL(location);
        return new VaquitaAbsoluteRedirectResponse(location,app);
    }

    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest,VaquitaApp app) throws Exception {
        return null;
    }
}
