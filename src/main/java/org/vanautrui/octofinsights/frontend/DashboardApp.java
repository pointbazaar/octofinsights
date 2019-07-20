package org.vanautrui.octofinsights.frontend;

import def.dom.HTMLElement;
import def.dom.ProgressEvent;
import def.dom.XMLHttpRequest;
import def.js.JSON;

import java.util.function.Function;

import static def.dom.Globals.console;
import static def.jquery.Globals.$;

public class DashboardApp {
    public static void main(String[] args) {
        console.log("Dashboard Frontend App Running");

        HTMLElement test = $("#test").get()[0];

        XMLHttpRequest Http = new XMLHttpRequest();

        Http.open("GET","/api/cashflow",true);
        Http.send();

        Http.onloadend=new Function<ProgressEvent, Object>() {
            @Override
            public Object apply(ProgressEvent progressEvent) {
                StringIntPair[] parse = (StringIntPair[]) JSON.parse(Http.responseText);

                console.log(parse[0]);

                console.log(parse);
                return null;
            }
        };

        //test.textContent="hi frontend apps in java !";

        main2();
        main3();
        main4();
        main5();
    }

    public static void main2(){
        XMLHttpRequest Http = new XMLHttpRequest();

        Http.open("GET","/api/value",true);


        Http.onloadend=new Function<ProgressEvent, Object>() {
            @Override
            public Object apply(ProgressEvent progressEvent) {
                IntObject parse = (IntObject) JSON.parse(Http.responseText);

                //console.log(parse.value);

                $("#balance").get()[0].textContent=parse.value +" €";
                return null;
            }
        };
        Http.send();
    }

    public static void main3(){
        XMLHttpRequest Http = new XMLHttpRequest();

        Http.open("GET","/api/salesthismonth",true);


        Http.onloadend=new Function<ProgressEvent, Object>() {
            @Override
            public Object apply(ProgressEvent progressEvent) {
                IntObject parse = (IntObject) JSON.parse(Http.responseText);

                //console.log(parse.value);

                $("#salesthismonth").get()[0].textContent=parse.value+" €";
                return null;
            }
        };

        Http.send();
    }


    public static void main4(){
        XMLHttpRequest Http = new XMLHttpRequest();

        Http.open("GET","/api/profit",true);


        Http.onloadend=new Function<ProgressEvent, Object>() {
            @Override
            public Object apply(ProgressEvent progressEvent) {
                IntObject parse = (IntObject) JSON.parse(Http.responseText);

                $("#profit").get()[0].textContent=parse.value+" €";
                return null;
            }
        };

        Http.send();
    }

    public static void main5(){
        XMLHttpRequest Http = new XMLHttpRequest();

        Http.open("GET","/api/expensesthismonth",true);


        Http.onloadend=new Function<ProgressEvent, Object>() {
            @Override
            public Object apply(ProgressEvent progressEvent) {
                IntObject parse = (IntObject) JSON.parse(Http.responseText);

                $("#expensesthismonth").get()[0].textContent=((-1)*parse.value)+" €";
                return null;
            }
        };

        Http.send();
    }
}
