<?php

include_once("../../base.php");
include_once($absolute_file_url . "/authentication/is_authenticated_otherwise_redirect.php");
include_once($absolute_file_url . "/services/lead_service.php");

if(!$_SERVER["REQUEST_METHOD"] === "POST"){
    http_response_code(405);
    exit();
}

session_start();

//check for post parameters
if(  isset($_POST["id"]) && isset($_POST["lead_status"])  ){
    echo("post parameters correct");

    $lead_status = $_POST["lead_status"];
    $id = $_POST["id"]; //id of the lead

    //check if lead_status is one of the correct values
    if( in_array($lead_status, Lead_Entity::get_lead_status_valid_values())){
        change_lead_status($id,$lead_status);
        header("Location: " . $baseurl . "/controllers/leads/get_leads.php");
    }else{
        echo("lead status not correct");
    }
}

