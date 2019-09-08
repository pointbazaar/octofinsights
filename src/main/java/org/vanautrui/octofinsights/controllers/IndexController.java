package org.vanautrui.octofinsights.controllers;

import j2html.tags.ContainerTag;
import org.vanautrui.octofinsights.html_util_domain_specific.BrandUtil;
import org.vanautrui.octofinsights.html_util_domain_specific.HeadUtil;
import org.vanautrui.octofinsights.services.TranslationService;
import org.vanautrui.vaquitamvc.VaquitaApp;
import org.vanautrui.vaquitamvc.VaquitaLogger;
import org.vanautrui.vaquitamvc.controller.VaquitaController;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPEntityEnclosingRequest;
import org.vanautrui.vaquitamvc.requests.VaquitaHTTPJustRequest;
import org.vanautrui.vaquitamvc.responses.VaquitaHTMLResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaHTTPResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaRedirectResponse;
import org.vanautrui.vaquitamvc.responses.VaquitaTextResponse;

import static j2html.TagCreator.*;


public class IndexController extends VaquitaController {

    @Override
    public VaquitaHTTPResponse handleGET(VaquitaHTTPJustRequest request, VaquitaApp app) throws Exception {

        if( request.session().isPresent() && request.session().get().containsKey("authenticated") && request.session().get().get("authenticated").equals("true") ){

            return new VaquitaRedirectResponse("/dashboard", request,app);

        }else {

            String lang="ru";
            try {
                VaquitaLogger.info("LANG PARAMETER");
                lang = request.getQueryParameter("lang");
                System.out.println("QUERRY PARAMETERS");
                System.out.println(request.getQueryParameters());
            }catch (Exception e){
                //pass
                e.printStackTrace();
            }

            //TODO: provide a choice between logging in or registering

            ContainerTag page=html(
                    HeadUtil.makeHead(),
                    body(
                            div(
                                    attrs(".container"),
                                    div(
                                            div(
                                                BrandUtil.createBrandLogoAndText()
                                            ).withClasses("col-md-5"),
                                            div().withClasses("col-md-3"),
                                            div(
                                              a(
                                                button(strong("Login")).withClasses("btn btn-primary btn-block m-3")
                                              ).withHref("/login").withClasses("row")
                                            ).withClasses("col-md-2"),
                                            div(
                                              a(
                                                button(strong("Register")).withClasses("btn btn-secondary btn-block m-3")
                                              ).withHref("/register").withClasses("row")
                                            ).withClasses("col-md-2")

                                    ).withClasses("row justify-content-center align-items-center")

                            ),
                            div().withClasses("m-3 p-3"),
                            div(
                                attrs(".container"),
                                img().withClasses("").withStyle("object-fit: cover;  object-position: center;  width: 100%;  max-height: 30vh;  margin-bottom: 1rem;").withSrc("https://images.unsplash.com/photo-1452065656801-6c60b6e7cbc5"),

                                //img().withSrc("https://images.unsplash.com/photo-1452065656801-6c60b6e7cbc5").withClasses("img-fluid"),


                                div(

                                    h3(TranslationService.translateText("Track Sales ",lang)),
                                    p(TranslationService.translateText("You can track customer name, price, date, product/service sold",lang)),
                                    p(TranslationService.translateText("Planned Feature: tracking referrers of that sale (e.g. Freelancer.com, a friend, salesperson,...) to understand where sales are coming from ",lang)),
                                    p(TranslationService.translateText("Planned Feature: show the burn rate or growth rate of company account. show remaining time left to operate before going bankrupt (assuming current expenses and no income)",lang)),


                                    h3("For Freelancers"),
                                    p("For many Freelancers, their personal account is their business account for Reasons of Practicality"),
                                    p("Planned Feature: apply an 'offset' to octofinsights and assume that this is your current value. octofinsights can then make a more accurate representation of your financial history"),

                                    h3("Track Expenses"),
                                    p("Servers, Rent, Equipment, ..."),
                                    p("Planned Feature: Track your Expenses and be able to tag them with user definedtags"),

                                    h3("Track Leads"),
                                    p("New Opportunities for Business and Learning arise often. Track your Leads."),
                                    p("Planned Feature: Track Leads with source, date, and what they probably want, also track if they were closed and if they were converted"),

                                    hr(),
                                    h3("Can i just test the software?"),
                                    p("Login with username and password:"),
                                    p("username: test3"),
                                    p("password: test3"),

                                    hr(),
                                    h3("Credits for Images, Vector Graphics, ..."),
                                    p("Icons made by Kiranshastry from flaticon.com")


                                ).withClasses("m-3")

                                //img().withClasses("")
                                //        .withStyle("object-fit: cover;  object-position: center;  width: 100%;  max-height: 40vh;  margin-bottom: 1rem;")
                                //        .withSrc("https://images.unsplash.com/photo-1512511025430-7c4c3300e186")
                                //img().withSrc("https://images.unsplash.com/photo-1512511025430-7c4c3300e186").withClasses("img-fluid")

                            ),
                            script().withSrc("target/org/vanautrui/octofinsights/frontend/FrontendApp.js")
                    )
            );



            return new VaquitaHTMLResponse(200,page.render());
            //String translated_content= InternationalizationTranslationServiceAndCachePoweredByYandexTranslationAPI.translateHTML(page.render(),"ru");
            //return new VaquitaHTMLResponse(200,translated_content);
        }
    }



    @Override
    public VaquitaHTTPResponse handlePOST(VaquitaHTTPEntityEnclosingRequest vaquitaHTTPEntityEnclosingRequest,VaquitaApp app) throws Exception {
        return new VaquitaTextResponse(404,"not implemented");
    }
}


