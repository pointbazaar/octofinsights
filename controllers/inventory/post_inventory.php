<?php

include_once("../../base.php");
include_once($absolute_file_url . "/authentication/is_authenticated_otherwise_redirect.php");

if(!$_SERVER["REQUEST_METHOD"] === "POST"){
    http_response_code(405);
    exit();
}

session_start();

//check for post parameters
if(isset($_POST["item_name"]) && isset($_POST["item_price"])  && isset($_POST["amount"])  ){

    echo("<p>post parameters correct</p>");

    $item_name= $_POST["item_name"];
    $item_price = $_POST["item_price"];
    $amount = $_POST["amount"];

    //insert item into inventory

    include($absolute_file_url . "/database/make_database_connection.php");

    try {

        $conn = getConnection();

        $statement = $conn->prepare("INSERT INTO inventory (item_name,item_price,amount) VALUES (:item_name,:item_price,:amount);");
        $statement->bindParam(":item_name", $item_name);
        $statement->bindParam(":item_price", $item_price);
        $statement->bindParam(":amount", $amount);
        $statement->execute();

        echo("inserted item");

    }catch (Exception $exception){
        echo($exception->getMessage());
    }
}

header("Location: " . $baseurl . "/controllers/inventory/get_inventory.php");