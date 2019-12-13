package org.vanautrui.octofinsights.html_util_domain_specific;

import j2html.tags.ContainerTag;

import static j2html.TagCreator.*;

public final class ErrorUtil {

    public static ContainerTag errorHTML(final String msg){
        return html(
            HeadUtil.makeHead(),
            body(
                div(
                    div(
                        h1(msg)
                    ).withClasses("row justify-content-center")
                ).withClasses("container")
            )
        );
    }
}
