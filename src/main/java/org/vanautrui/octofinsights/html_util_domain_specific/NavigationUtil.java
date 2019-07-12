package org.vanautrui.octofinsights.html_util_domain_specific;

import j2html.tags.ContainerTag;
import org.vanautrui.octofinsights.App;

import java.util.Arrays;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

public class NavigationUtil {


    public static ContainerTag createNavbar(String username, String currentPageName){
        String[][] sidebar_links = new String[][]{
                new String[]{"/","Dashboard"},
                new String[]{"/sales","Sales"},
                new String[]{"/leads","Leads"},
                new String[]{"/expenses","Expenses"}/*,
                new String[]{"/projects","Projects (Planned Feature,TODO)"}*/
        };

        ContainerTag navbar=
                nav(
                        attrs(".navbar .navbar-expand-lg .navbar-light .bg-light"),
                        a(
                                attrs(".navbar-brand"),
                                BrandUtil.createBrandLogoAndText()
                        ).withHref("/"),
                        button(
                                attrs(".navbar-toggler"),
                                span(attrs(".navbar-toggler-icon"))
                        ).withType("button").withData("toggle","collapse").withData("target","#navbarSupportedContent"),
                        div(
                                attrs(".collapse .navbar-collapse"),
                                ul(
                                        attrs(".navbar-nav .mr-auto"),
                                        each(
                                                Arrays.stream(sidebar_links).collect(Collectors.toList()),
                                                link->li(
                                                        attrs(".nav-item"),
                                                        makeLinkCustom(link[1],link[0],currentPageName)
                                                )
                                        ),

                                        li(
                                                a(span("Logout")).withClasses("nav-link p-2").withHref("/logout")
                                        ).withClasses("nav-item"),
                                        li(
                                                p("Logged in as : "+username).withClasses("m-2")
                                        ).withClasses("nav-item")
                                )
                        ).withId("navbarSupportedContent")

                );



        return navbar;
    }

    private static ContainerTag makeLinkCustom(String name,String href, String currentPageName){
        ContainerTag result = a(
                attrs(".nav-link .p-2"),
                p((name.equals(currentPageName))?strong(name):span(name))
        ).withHref(href);
        return result;
    }

}
