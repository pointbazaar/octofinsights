<?php
include_once($_SERVER["DOCUMENT_ROOT"] . "/octofinsights/base.php");
include_once($absolute_file_url . "/model/IEntity.php");

class Lead_Entity implements IEntity
{
    public $id;
    public $lead_name;
    public $date_of_lead_entry;
    public $what_the_lead_wants;

    public static function getSchemaString()
    {
        return "(id INT AUTO_INCREMENT PRIMARY KEY, lead_name VARCHAR(64), date_of_lead_entry TIMESTAMP, what_the_lead_wants VARCHAR(64) )";
    }
}