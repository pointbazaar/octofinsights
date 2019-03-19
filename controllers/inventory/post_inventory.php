<?php

include("../../base.php");
include("../../authentication/is_authenticated_otherwise_redirect.php");

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

    //TODO: insert item into inventory
    $absolute_file_url = $_SERVER["DOCUMENT_ROOT"] . "/octofinsights";
    include($absolute_file_url . "/database/make_database_connection.php");

    try {

        $conn = getConnection();

        $statement = $conn->prepare("INSERT INTO inventory (item_name,item_price,amount) VALUES (?,?,?);");
        $statement->bindParam("sii", $item_name, $item_price, $amount);
        $statement->execute();

    }catch (Exception $exception){
        echo($exception->getMessage());
    }
}

header("Location: " . $baseurl . "/controllers/inventory/get_inventory.php");