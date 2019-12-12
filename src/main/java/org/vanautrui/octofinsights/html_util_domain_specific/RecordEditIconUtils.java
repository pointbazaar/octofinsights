package org.vanautrui.octofinsights.html_util_domain_specific;

import j2html.tags.ContainerTag;
import j2html.tags.EmptyTag;

import static j2html.TagCreator.*;

public final class RecordEditIconUtils {

    public static EmptyTag deleteIcon(){
        return img()
                .withSrc("https://image.flaticon.com/icons/svg/1214/1214428.svg")
                .withStyle("height: 2rem;");
    }

    public static ContainerTag deleteButton(){
        return button(
                RecordEditIconUtils.deleteIcon()
        ).withType("submit").withClasses("btn","btn-outline-danger","btn-sm");
    }

    public static EmptyTag updateIcon(){
        return img()
                .withSrc("https://image.flaticon.com/icons/svg/1159/1159633.svg")
                .withStyle("height: 2rem;");
    }

    public static ContainerTag updateButton(){
        return button(
                RecordEditIconUtils.updateIcon()
        ).withType("submit").withClasses("btn","btn-outline-info","btn-sm");
    }
}
