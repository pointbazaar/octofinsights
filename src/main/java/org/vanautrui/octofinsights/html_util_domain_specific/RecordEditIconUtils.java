package org.vanautrui.octofinsights.html_util_domain_specific;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import j2html.tags.EmptyTag;

import static j2html.TagCreator.*;

public final class RecordEditIconUtils {

    private static ContainerTag smallbtn(final String content, final String extra_class){
        return button(
                content
        ).withType("submit").withClasses("btn",extra_class,"btn-sm","ml-1");
    }

    public static ContainerTag deleteButton(){
        return smallbtn("delete","btn-outline-danger");
    }

    public static ContainerTag editButton(){
        return smallbtn("edit","btn-outline-info");
    }

    public static ContainerTag blueButton(String content) {
        return smallbtn(content,"btn-outline-info");
    }
}
