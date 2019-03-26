<?php

include_once($_SERVER["DOCUMENT_ROOT"] . "/base.php");
include_once($absolute_file_url . "/include_many.php");

function get_all_inventory()
{
    $results = fetch_all_from("inventory");
    $typed_results = array();

    for($i=0;$i<sizeof($results);$i++) {
        $item = $results[$i];
        array_push($typed_results,new InventoryItem_Entity($item[0],$item[1],$item[2]) );
    }

    return $typed_results;
}

function insert_inventory($item){

    try {

        $statement = getPreparedStatement("INSERT INTO inventory (item_name,item_price,amount) VALUES (:item_name,:item_price,:amount);");
        $statement->bindParam(":item_name", $item->name);
        $statement->bindParam(":item_price", $item->price);
        $statement->bindParam(":amount", $item->amount);
        $statement->execute();

        echo("inserted item");

    }catch (Exception $exception){
        echo($exception->getMessage());
    }
}

function delete_by_id($id){
    delete_from_where_id_is("inventory",$id);
}