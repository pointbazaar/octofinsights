<?php

function getConnection(){

    $servername = "vanautrui.org";

    $username = $_SESSION["db-username"];
    $password = $_SESSION["db-password"];

    $conn=null;

    try{
        $conn=new PDO("mysql:host=" . $servername . ";",$username,$password);

        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        $conn->exec("USE octofinsights;");
        //$conn->commit();

        $inventory_table_description = "( id INT AUTO_INCREMENT PRIMARY KEY, item_name VARCHAR(128) NOT NULL, item_price INT NOT NULL, amount INT NOT NULL)";

        $conn->exec("CREATE TABLE IF NOT EXISTS inventory " . $inventory_table_description . ";");

        echo("Connected successfully to database");

        //$conn=null;
    }catch (Exception $e){
        echo("connection failed" . $e->getMessage());
    }
    return $conn;
}