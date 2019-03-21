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
if(isset($_POST["lead_name"]) && isset($_POST["date_of_lead_entry"])  && isset($_POST["what_the_lead_wants"])  ){
    echo("post parameters correct");

    insert_lead(new Lead_Entity($_POST["lead_name"],$_POST["date_of_lead_entry"],$_POST["what_the_lead_wants"]));
}

header("Location: " . $baseurl . "/controllers/leads/get_leads.php");