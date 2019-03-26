<?php

include_once($_SERVER["DOCUMENT_ROOT"] . "/base.php");
include_once($absolute_file_url . "/include_many.php");

function get_all_leads()
{

    $results = fetch_all_from("leads");
    $typed_results=array();

    for($i=0;$i<sizeof($results);$i++) {
        $lead = $results[$i];
        array_push($typed_results,new Lead_Entity($lead[1],$lead[2],$lead[3],$lead[4]));
        $typed_results[sizeof($typed_results)-1]->id=$lead[0];
    }

    return $typed_results;
}


function insert_lead($lead){

    try {

        $statement = getPreparedStatement("INSERT INTO leads(lead_name,lead_status,date_of_lead_entry,what_the_lead_wants) VALUES (:lead_name,:lead_status,:date_of_lead_entry,:what_the_lead_wants);");
        $statement->bindParam(":lead_name", $lead->lead_name);
        $statement->bindParam(":lead_status",$lead->lead_status);
        $statement->bindParam(":date_of_lead_entry", $lead->date_of_lead_entry);
        $statement->bindParam(":what_the_lead_wants", $lead->what_the_lead_wants);
        $statement->execute();

    }catch (Exception $exception){
        echo($exception->getMessage());
    }
}

function change_lead_status($id,$lead_status){
    try {

        $statement = getPreparedStatement("UPDATE leads SET lead_status=:lead_status WHERE id=:id;");
        $statement->bindParam(":id", $id);
        $statement->bindParam(":lead_status",$lead_status);
        $statement->execute();

    }catch (Exception $exception){
        echo($exception->getMessage());
    }
}

function delete_by_id($id){
    delete_from_where_id_is("leads",$id);
}

