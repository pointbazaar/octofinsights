package org.vanautrui.octofinsights.html_util_domain_specific;

import j2html.tags.ContainerTag;
import org.vanautrui.octofinsights.App;

import java.util.Arrays;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

public class NavigationUtil {


    public static ContainerTag createNavbar(){
        String[][] sidebar_links = new String[][]{new String[]{"/","Dashboard"},new String[]{"/sales","Sales"},new String[]{"/leads","Leads"},new String[]{"/logout","Logout"}};

        ContainerTag navbar=
                nav(
                        attrs(".navbar .navbar-expand-lg .navbar-light .bg-light"),
                        a(
                                attrs(".navbar-brand"),
                                div(
                                        attrs(".row .align-items-center"),
                                        img().withSrc("https://image.flaticon.com/icons/svg/1197/1197854.svg").withStyle("height: 14vw; max-height:2cm; min-height:0.5cm;"),
                                        h2("Octofinsights").withStyle("color: "+ App.octofinsights_primary_color+"")
                                )
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
                                        )
                                )
                        ).withId("navbarSupportedContent")

                );

        return navbar;
    }


}
