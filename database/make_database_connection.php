<?php

include_once($_SERVER["DOCUMENT_ROOT"] . "/octofinsights/base.php");
include_once($absolute_file_url . "/model/employee_entity.php");


function getConnection(){

    $servername = "vanautrui.org";

    $username = $_SESSION["db-username"];
    $password = $_SESSION["db-password"];

    $conn=null;

    try{
        $conn=new PDO("mysql:host=" . $servername . ";",$username,$password);

        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        $conn->exec("USE octofinsights;");

        $inventory_table_description = "( id INT AUTO_INCREMENT PRIMARY KEY, item_name VARCHAR(128) NOT NULL, item_price INT NOT NULL, amount INT NOT NULL)";

        $conn->exec("CREATE TABLE IF NOT EXISTS inventory " . $inventory_table_description . ";");



        $sales_table_description=    "(id INT AUTO_INCREMENT PRIMARY KEY, customer_name VARCHAR(128), time_of_sale TIMESTAMP,   price_of_sale INT,        product_or_service VARCHAR(128) )";
        $conn->exec("CREATE TABLE IF NOT EXISTS sales " . $sales_table_description . ";");


        $conn->exec("CREATE TABLE IF NOT EXISTS employees " . Employee_Entity::getSchemaString() . ";");



        //$conn=null;
    }catch (Exception $e){
        echo("connection failed" . $e->getMessage());
    }
    return $conn;
}

function getPreparedStatement($statement){
    return getConnection()->prepare($statement);
}

function fetch_all_from($table){
    $statement = getConnection()->prepare("SELECT * FROM ". $table . ";");
    //$statement->bindParam(":table_name",$table);

    $statement->execute();

    return $statement->fetchAll();
}