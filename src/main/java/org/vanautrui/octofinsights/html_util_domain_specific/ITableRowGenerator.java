package org.vanautrui.octofinsights.html_util_domain_specific;


import j2html.tags.ContainerTag;

public interface ITableRowGenerator<T>{
	public ContainerTag maketr(T elem);
}