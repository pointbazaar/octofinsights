package org.vanautrui.octofinsights.html_util_domain_specific;

import j2html.tags.ContainerTag;
import org.vanautrui.octofinsights.App;

import static j2html.TagCreator.*;

public final class BrandUtil {

    public static ContainerTag createBrandLogoAndText(){

      return strong("Octofinsights").withStyle("color: "+ App.octofinsights_primary_color+"");
    }
}
