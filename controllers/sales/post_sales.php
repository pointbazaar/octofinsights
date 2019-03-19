<?php

include_once("../../base.php");
include_once($absolute_file_url ."/authentication/is_authenticated_otherwise_redirect.php");

if(!$_SERVER["REQUEST_METHOD"] === "POST"){
    exit();
}

if(isset($_POST["customer_name"]) && isset($_POST["time_of_sale"]) && isset($_POST["price_of_sale"])  ){
    echo("post parameters correct");

    $customer_name = $_POST["customer_name"];
    $time_of_sale = $_POST["time_of_sale"];
    $price_of_sale = $_POST["price_of_sale"];

    //insert a sale into db

    include_once($absolute_file_url . "/database/make_database_connection.php");

    try{
        $conn = getConnection();
        $statement = $conn->prepare("INSERT INTO sales (customer_name,time_of_sale,price_of_sale) VALUES (:customer_name,:time_of_sale,:price_of_sale)");

        $statement->bindParam(":customer_name",$customer_name);
        $statement->bindParam(":time_of_sale",$time_of_sale);
        $statement->bindParam(":price_of_sale",$price_of_sale);
        $statement->execute();


        echo("inserted item");

    }catch (Exception $exception){
        echo($exception->getMessage());
    }
}

header("Location: " . $baseurl . "/controllers/sales/get_sales.php");