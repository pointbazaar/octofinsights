<?php

include_once("../../base.php");
include_once($absolute_file_url . "/authentication/is_authenticated_otherwise_redirect.php");

if(!$_SERVER["REQUEST_METHOD"] === "POST"){
    http_response_code(405);
    exit();
}

//check for post parameters
if( isset($_POST["id"]) ){

    echo("<p>post parameters correct</p>");

    $id=$_POST["id"];

    include($absolute_file_url . "/database/make_database_connection.php");

    try {

        $conn = getConnection();

        $statement = $conn->prepare("DELETE FROM sales WHERE id=:id;");
        $statement->bindParam(":id", $id);
        $statement->execute();

        echo("deleted item");

    }catch (Exception $exception){
        echo($exception->getMessage());
    }
}

header("Location: " . $baseurl . "/controllers/sales/get_sales.php");