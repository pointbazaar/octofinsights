package org.vanautrui.octofinsights.html_util_domain_specific;

import j2html.tags.ContainerTag;

import java.util.Arrays;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

public class NavigationUtil {


    public static ContainerTag createNavbar(String username, String currentPageName){
        String[][] sidebar_links = new String[][]{
                new String[]{"/","Dashboard"},
                new String[]{"/sales","Sales"},
                new String[]{"/leads","Leads"},
                new String[]{"/expenses","Expenses"},
                new String[]{"/projects","Projects"},
                new String[]{"/invoices","Invoices"},
                new String[]{"/profile","Profile"},
                new String[]{"/customers","Customers"}
        };

        ContainerTag navbar=
                nav(
                        attrs(".navbar .navbar-expand-xl .navbar-dark "),
                        a(
                                BrandUtil.createBrandLogoAndText()
                        ).withHref("/").withClasses("navbar-brand"),
                        button(
                                attrs(".navbar-toggler"),
                                span(attrs(".navbar-toggler-icon"))
                        ).withType("button").withData("toggle","collapse").withData("target","#navbarSupportedContent"),
                        div(
                                attrs(".collapse .navbar-collapse"),
                                ul(

                                        each(
                                                Arrays.stream(sidebar_links).collect(Collectors.toList()),
                                                link->li(
                                                        attrs(".nav-item"),
                                                        makeLinkCustom(link[1],link[0],currentPageName)
                                                )
                                        ),

                                        li(
                                                a("Logout "+username).withClasses("nav-link mt-1").withHref("/logout")
                                        ).withClasses("nav-item"),
                                        
                                ).withClasses("navbar-nav", "mr-auto")
                        ).withId("navbarSupportedContent")

                ).withStyle("background-color:#014421;");



        return navbar;
    }

    private static ContainerTag makeLinkCustom(String name,String href, String currentPageName){
        ContainerTag result = a(
                attrs(".nav-link .mt-1"),
                (name.equals(currentPageName))?strong(name):span(name)
        ).withHref(href);
        return result;
    }

}
