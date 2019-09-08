package org.vanautrui.octofinsights.html_util_domain_specific;

import j2html.attributes.Attr;
import j2html.tags.ContainerTag;

import static j2html.TagCreator.*;

public class HeadUtil {

    public static ContainerTag makeHead(){
        return makeHead(new ContainerTag[]{});
    }

    public static ContainerTag makeHead(ContainerTag... otherchildren){

        ContainerTag head = head(
                head(
                        script().withSrc("https://www.googletagmanager.com/gtag/js?id=UA-38415314-2").attr(Attr.ASYNC,"true"),

                        meta().attr("charset","UTF-8"),

                        title("Octofinsights"),
                        link().withRel("stylesheet").withHref("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"),

                        link().withRel("stylesheet").withHref("style.css"),
                        link().withRel("stylesheet").withHref("../style.css"), //important, when relative path is used, as request could be issued from /projects/file.extension

                        //link().withRel("stylesheet").withHref("https://cdn.jsdelivr.net/gh/coliff/bootstrap-rfs/bootstrap-rfs.css"),

                        script().withSrc("https://code.jquery.com/jquery-3.4.1.js"),
                        script().withSrc("https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"),
                        script().withSrc("https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"),

                        script().withSrc("mygoogleanalyticsscript.js"),
                        div(
                            otherchildren
                        )
                )
        );



        return head;
    }
}
