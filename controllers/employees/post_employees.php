<?php

include_once("../../base.php");
include_once($absolute_file_url . "/authentication/is_authenticated_otherwise_redirect.php");

if(!$_SERVER["REQUEST_METHOD"] === "POST"){
    http_response_code(405);
    exit();
}

session_start();

//check for post parameters
if(isset($_POST["employee_name"]) && isset($_POST["employee_role"])  && isset($_POST["employee_email"])  ){

    echo("<p>post parameters correct</p>");

    $name= $_POST["employee_name"];
    $role = $_POST["employee_role"];
    $email = $_POST["employee_email"];

    //insert item into inventory

    include($absolute_file_url . "/database/make_database_connection.php");

    try {

        $conn = getConnectionAndInitDBWithTables();

        $statement = $conn->prepare("INSERT INTO employees (employee_name,employee_role,employee_email) VALUES (:employee_name,:employee_role,:employee_email);");
        $statement->bindParam(":employee_name", $name);
        $statement->bindParam(":employee_role", $role);
        $statement->bindParam(":employee_email", $email);
        $statement->execute();

        echo("inserted employee");

    }catch (Exception $exception){
        echo($exception->getMessage());
    }
}

header("Location: " . $baseurl . "/controllers/employees/get_employees.php");