package org.vanautrui.octofinsights;

import j2html.tags.ContainerTag;

import java.util.Arrays;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

public class SideBarUtil {

    public static ContainerTag createSidebar(){

        String[] sidebar_links = new String[]{"/sales","/leads","/expenses","/employees","/inventory"};

        ContainerTag sidebar = div(
                attrs("#sidebar .col-md-2 .sidebar .p-3 .bg-light"),
                a(h2("Octofinsights")).withHref("/"),
                div(
                        each(
                                Arrays.stream(sidebar_links).collect(Collectors.toList()),
                                link->div(
                                        attrs(".m-3"),
                                        hr(),
                                        a(strong(link.substring(1).toUpperCase()))
                                                .withHref(link)
                                )
                        )
                )
        );

        return sidebar;
    }
}
