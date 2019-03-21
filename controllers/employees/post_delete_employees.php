<?php

include_once("../../base.php");
include_once($absolute_file_url . "/authentication/is_authenticated_otherwise_redirect.php");
include_once($absolute_file_url . "/services/employees_service.php");

if(!$_SERVER["REQUEST_METHOD"] === "POST"){
    http_response_code(405);
    exit();
}

//check for post parameters
if( isset($_POST["id"]) ){

    echo("<p>post parameters correct</p>");

    $id=$_POST["id"];

    delete_by_id($id);
}

header("Location: " . $baseurl . "/controllers/employees/get_employees.php");