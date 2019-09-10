package org.vanautrui.octofinsights.controllers.other.expenses;

import j2html.tags.ContainerTag;

import static j2html.TagCreator.*;

public class ExpensesJ2HTMLUtils {

  public static ContainerTag makeExpenseInsertWidget(int user_id){
    return
            div(
                    div("Add an Expense").withClasses("card-header"),
                    div(
                            form(
                                    div(
                                            div(
                                                    div(
                                                            span("Expense Name:").withClasses("input-group-text")
                                                    ).withClasses("input-group-prepend"),
                                                    input().withType("text").withName("expense_name").withClasses("form-control")
                                            ).withClasses("input-group","col"),
                                            div(
                                                    input().withName("expense_value")
                                                            .withPlaceholder("expense_value")
                                                            .withType("number")
                                                            .attr("min","0")
                                                            .withClasses("form-control"),
                                                    div(
                                                            span("$").withClasses("input-group-text")
                                                    ).withClasses("input-group-append")
                                            ).withClasses("input-group","col")
                                    ).withClasses("row","mb-3"),

                                    div(
                                            div(
                                              input().withName("expense_date").withPlaceholder("expense date").withType("date").withClasses("form-control")
                                            ).withClasses("input-group","col")
                                    ).withClasses("row","mb-3"),

                                    button("Insert")
                                            .withType("submit")
                                            .withClasses("btn","btn-outline-success","btn-block")


                            ).withAction("/expenses?action=insert").withMethod("post")
                    ).withClasses("card-body")
            ).withClasses("m-3","card","mb-4");
  }
}
