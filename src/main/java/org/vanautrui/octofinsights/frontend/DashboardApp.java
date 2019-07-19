package org.vanautrui.octofinsights.frontend;

import static def.dom.Globals.console;
import static def.jquery.Globals.$;

public class DashboardApp {
    public static void main(String[] args) {
        console.log("Dashboard Frontend App Running");

        $("#test").get()[0].textContent="hi frontend apps in java !";
    }
}
