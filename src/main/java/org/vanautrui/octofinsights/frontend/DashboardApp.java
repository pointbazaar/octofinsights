package org.vanautrui.octofinsights.frontend;

import def.dom.HTMLElement;
import def.dom.ProgressEvent;
import def.dom.XMLHttpRequest;
import def.js.JSON;
import org.jooq.tools.json.JSONObject;

import java.util.function.Function;

import static def.dom.Globals.console;
import static def.jquery.Globals.$;

public class DashboardApp {
    public static void main(String[] args) {
        console.log("Dashboard Frontend App Running");

        HTMLElement test = $("#test").get()[0];

        XMLHttpRequest Http = new XMLHttpRequest();

        Http.open("GET","/api/cashflow");
        Http.send();

        Http.onloadend=new Function<ProgressEvent, Object>() {
            @Override
            public Object apply(ProgressEvent progressEvent) {
                CashFlowObject[] parse = (CashFlowObject[]) JSON.parse(Http.responseText);

                console.log(parse[0]);

                console.log(parse);
                return null;
            }
        };

        test.textContent="hi frontend apps in java !";

        main2();

    }

    public static void main2(){
        XMLHttpRequest Http = new XMLHttpRequest();

        Http.open("GET","/api/balance");
        Http.send();

        Http.onloadend=new Function<ProgressEvent, Object>() {
            @Override
            public Object apply(ProgressEvent progressEvent) {
                BalanceObject parse = (BalanceObject) JSON.parse(Http.responseText);

                console.log(parse.balance);

                $("#balance").get()[0].textContent=parse.balance+" €";
                return null;
            }
        };
    }
}
