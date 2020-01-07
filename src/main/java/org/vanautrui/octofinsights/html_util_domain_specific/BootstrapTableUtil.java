package org.vanautrui.octofinsights.html_util_domain_specific;

import j2html.tags.ContainerTag;
import org.jooq.Record;
import org.jooq.Result;

import java.util.List;

import static j2html.TagCreator.*;

public final class BootstrapTableUtil {

	//to help create bootstrap tables with j2html in a generic manner
	//and to remove boilerplate from controllers

	public static <T extends Record> ContainerTag makeBootstrapTable(
			final List<String> column_names,
			final List<T> records,
			final ITableRowGenerator<T> trgen
	){
		ContainerTag mytable = table(
				thead(
						each(column_names, cname -> th(cname).attr("scope","col"))
				).withClasses("thead-light"),
				tbody(
						each(
								records,
								record ->
										trgen.maketr(record)
						)
				)
		).withClasses("table","table-sm");
		return mytable;
	}
}

