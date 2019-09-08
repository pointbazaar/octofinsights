package org.vanautrui.octofinsights.html_util_domain_specific;

import j2html.tags.ContainerTag;
import org.vanautrui.octofinsights.App;

import static j2html.TagCreator.*;

public class BrandUtil {

    public static ContainerTag createBrandLogoAndText(){

        ContainerTag brand=
                                div(
                                        attrs(".row .align-items-center. justify-content-center .mr-3 .ml-3"),
                                        //img().withSrc("https://vanautrui.org/pics/logo.svg").withStyle("height: 3em;").withClasses(""),
                                        strong("Octofinsights").withStyle("color: "+ App.octofinsights_primary_color+"")
                                );

        return brand;
    }
}
