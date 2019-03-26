<?php

include_once($_SERVER["DOCUMENT_ROOT"] . "/base.php");
include_once($_SERVER["DOCUMENT_ROOT"] . "/include_many.php");

function get_all_expenses()
{

    $results = fetch_all_from_order_by("expenses","expense_date","DESC");
    $typed_results=array();

    for($i=0;$i<sizeof($results);$i++) {
        $expense = $results[$i];
        array_push($typed_results,new Expense_Entity($expense[1],$expense[2],$expense[3]));
        $typed_results[sizeof($typed_results)-1]->id=$expense[0];

    }

    return $typed_results;
}

function insert_expense($expense){
    try {

        $statement = getPreparedStatement("INSERT INTO expenses (expense_name,expense_date,expense_value) VALUES (:expense_name,:expense_date,:expense_value);");
        $statement->bindParam(":expense_name", $expense->expense_name);
        $statement->bindParam(":expense_date", $expense->expense_date);
        $statement->bindParam(":expense_value", $expense->expense_value);
        $statement->execute();

    }catch (Exception $exception){
        echo($exception->getMessage());
    }

}

function delete_expense_entity_by_id($id){

    delete_from_where_id_is("expenses",$id);
}