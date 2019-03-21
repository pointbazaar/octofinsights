<?php

include_once("../../base.php");
include_once($absolute_file_url . "/authentication/is_authenticated_otherwise_redirect.php");
include_once($absolute_file_url . "/services/inventory_service.php");

if(!$_SERVER["REQUEST_METHOD"] === "POST"){
    http_response_code(405);
    exit();
}

session_start();

//check for post parameters
if(isset($_POST["item_name"]) && isset($_POST["item_price"])  && isset($_POST["amount"])  ){

    insert_inventory(new InventoryItem($_POST["item_name"],$_POST["item_price"],$_POST["amount"]));
}

header("Location: " . $baseurl . "/controllers/inventory/get_inventory.php");