package org.vanautrui.octofinsights.html_util_domain_specific;

import j2html.tags.ContainerTag;
import org.vanautrui.octofinsights.App;

import java.util.Arrays;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;
import static j2html.TagCreator.strong;

public class BrandUtil {

    public static ContainerTag createBrandLogoAndText(){

        ContainerTag brand=
                                div(
                                        attrs(".row .align-items-center. justify-content-center"),
                                        img().withSrc("https://image.flaticon.com/icons/svg/1197/1197854.svg").withStyle("height: 14vw; max-height:2cm; min-height:0.5cm;"),
                                        h2("Octofinsights").withStyle("color: "+ App.octofinsights_primary_color+"")
                                );

        return brand;
    }
}
