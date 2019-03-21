<?php

include_once($_SERVER["DOCUMENT_ROOT"] . "/octofinsights" . "/base.php");
include_once($absolute_file_url . "/database/make_database_connection.php");
include_once($absolute_file_url . "/model/sale_entity.php");

function get_all_sales()
{

    $conn = getConnection();

    $statement = $conn->prepare("SELECT * FROM sales;");
    $statement->execute();

    $results = $statement->fetchAll();

    $typed_results = array();

    for($i=0;$i<sizeof($results);$i++) {
        $sale = $results[$i];
        //add sale to result
        array_push($typed_results,new sale_entity($sale[0],$sale[1],$sale[2],$sale[3],$sale[4]));
    }

    return $typed_results;
}

function get_total_sales_price(){

    $total=0;

    foreach (get_all_sales() as $sale){
        $total+=$sale->price_of_sale;
    }

    return $total;
}