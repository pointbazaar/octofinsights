<?php

include_once($_SERVER["DOCUMENT_ROOT"] . "/octofinsights" . "/base.php");
include_once($absolute_file_url . "/database/make_database_connection.php");
include_once($absolute_file_url . "/model/Lead_Entity.php");

function get_all_leads()
{

    $results = fetch_all_from("leads");
    $typed_results=array();

    for($i=0;$i<sizeof($results);$i++) {
        $lead = $results[$i];
        array_push($typed_results,new Lead_Entity($lead[1],$lead[2],$lead[3]));
        $typed_results[sizeof($typed_results)-1]->id=$lead[0];
    }

    return $typed_results;
}


function insert_lead($lead){

    try {

        $statement = getPreparedStatement("INSERT INTO leads(lead_name,date_of_lead_entry,what_the_lead_wants) VALUES (:lead_name,:date_of_lead_entry,:what_the_lead_wants);");
        $statement->bindParam(":lead_name", $lead->lead_name);
        $statement->bindParam(":date_of_lead_entry", $lead->date_of_lead_entry);
        $statement->bindParam(":what_the_lead_wants", $lead->what_the_lead_wants);
        $statement->execute();

    }catch (Exception $exception){
        echo($exception->getMessage());
    }
}

function delete_by_id($id){
    delete_from_where_id_is("leads",$id);
}

