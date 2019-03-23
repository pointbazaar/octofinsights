<?php

include_once("../../base.php");
include_once($absolute_file_url ."/authentication/is_authenticated_otherwise_redirect.php");
include_once($absolute_file_url . "/services/Expense_Entity_service.php");

if(!$_SERVER["REQUEST_METHOD"] === "POST"){
    exit();
}

if(isset($_POST["expense_name"]) && isset($_POST["expense_date"]) && isset($_POST["expense_value"])  ){
    echo("post parameters correct");

    $expense_date= $_POST["expense_date"];
    $expense_name = $_POST["expense_name"];
    $expense_value = $_POST["expense_value"];
    $product_or_service = $_POST["product_or_service"];

    if($expense_value>=0){
        http_response_code(400);
        echo("<br>cannot have positive expense values");
        exit();
    }

    insert_expense(new Expense_Entity($expense_name,$expense_date,$expense_value));
}

header("Location: " . $baseurl . "/controllers/expenses/get_expenses.php");