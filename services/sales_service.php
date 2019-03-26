<?php

include_once($_SERVER["DOCUMENT_ROOT"] . "/base.php");
include_once($absolute_file_url . "/include_many.php");

function get_all_sales()
{

    $results = fetch_all_from("sales");
    $typed_results=array();

    for($i=0;$i<sizeof($results);$i++) {
        $sale = $results[$i];
        //add sale to result
        array_push($typed_results,new Sale_Entity($sale[0],$sale[1],$sale[2],$sale[3],$sale[4]));
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

function delete_by_id($id){

    delete_from_where_id_is("sales",$id);
}