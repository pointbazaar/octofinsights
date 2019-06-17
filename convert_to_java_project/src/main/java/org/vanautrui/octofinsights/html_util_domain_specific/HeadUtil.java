package org.vanautrui.octofinsights.html_util_domain_specific;

import j2html.tags.ContainerTag;

import static j2html.TagCreator.*;

public class HeadUtil {

    public static ContainerTag makeHead(){

        ContainerTag head = head(
                head(
                        title("Octofinsights"),
                        link().withRel("stylesheet").withHref("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"),
                        script().withSrc("https://code.jquery.com/jquery-3.3.1.slim.min.js"),
                        script().withSrc("https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"),
                        script().withSrc("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js")
                )
        );

        return head;
    }
}
