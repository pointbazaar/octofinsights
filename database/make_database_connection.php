<?php

include_once($_SERVER["DOCUMENT_ROOT"] . "/octofinsights/base.php");
include_once($absolute_file_url . "/model/employee_entity.php");
include_once($absolute_file_url . "/model/Lead_Entity.php");
include_once($absolute_file_url . "/model/sale_entity.php");
include_once($absolute_file_url . "/model/inventory_item_entity.php");

function getConnectionPrevious(){
    $servername = "vanautrui.org";

    $username = $_SESSION["db-username"];
    $password = $_SESSION["db-password"];

    $conn=null;

    try {
        $conn = new PDO("mysql:host=" . $servername . ";", $username, $password);

        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        $conn->exec("USE octofinsights;");

    }catch (Exception $e){
        echo("connection failed" . $e->getMessage());
    }
    return $conn;

}

function getConnectionAndInitDBWithTables(){

    $connection = getConnectionPrevious();

    create_table_if_not_exists($connection,"inventory",InventoryItem_Entity::getSchemaString());
    create_table_if_not_exists($connection,"sales",Sale_Entity::getSchemaString());
    create_table_if_not_exists($connection,"employees",Employee_Entity::getSchemaString());
    create_table_if_not_exists($connection,"leads",Lead_Entity::getSchemaString());

    return $connection;
}

function create_table_if_not_exists($connection,$table_name,$schema_string){
    $connection->exec("CREATE TABLE IF NOT EXISTS " . $table_name . "  " . $schema_string .";");
}

function getPreparedStatement($statement){
    return getConnectionAndInitDBWithTables()->prepare($statement);
}

function fetch_all_from($table){
    $statement = getConnectionAndInitDBWithTables()->prepare("SELECT * FROM ". $table . ";");
    //$statement->bindParam(":table_name",$table);

    $statement->execute();

    return $statement->fetchAll();
}