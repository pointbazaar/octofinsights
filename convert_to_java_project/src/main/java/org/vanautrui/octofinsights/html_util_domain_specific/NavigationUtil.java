package org.vanautrui.octofinsights.html_util_domain_specific;

import j2html.tags.ContainerTag;
import org.vanautrui.octofinsights.App;

import java.util.Arrays;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

public class NavigationUtil {


    public static ContainerTag createNavbar(String username){
        String[][] sidebar_links = new String[][]{
                new String[]{"/","Dashboard"},
                new String[]{"/sales","Sales"},
                new String[]{"/leads","Leads"},
                new String[]{"/expenses","Expenses"}
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
                                                        a(
                                                                attrs(".nav-link .p-2"),
                                                                strong(link[1])
                                                        ).withHref(link[0])
                                                )
                                        ),
                                        
                                        li(
                                                a(strong("Logout")).withClasses("nav-link p-2").withHref("/logout")
                                        ).withClasses("nav-item"),
                                        li(
                                                p("Logged in as : "+username).withClasses("m-2")
                                        ).withClasses("nav-item")
                                )
                        ).withId("navbarSupportedContent")

                );

        return navbar;
    }


}
