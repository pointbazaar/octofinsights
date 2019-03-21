<?php

include_once($_SERVER["DOCUMENT_ROOT"] . "/octofinsights" . "/base.php");
include_once($absolute_file_url . "/database/make_database_connection.php");
include_once($absolute_file_url . "/model/employee_entity.php");

function get_all_employees()
{

    $results = fetch_all_from("employees");

    $typed_results = array();

    for($i=0;$i<sizeof($results);$i++) {
        $employee = $results[$i];
        array_push($typed_results,new Employee_Entity($employee[0],$employee[1],$employee[2],$employee[3]) );
    }

    return $typed_results;
}

function delete_by_id($id){
    try {

        $statement = getPreparedStatement("DELETE FROM employees WHERE id=:id;");
        $statement->bindParam(":id", $id);
        $statement->execute();

    }catch (Exception $exception){
        echo($exception->getMessage());
    }
}