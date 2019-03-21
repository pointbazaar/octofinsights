<?php
include_once($_SERVER["DOCUMENT_ROOT"] . "/octofinsights/base.php");
include_once($absolute_file_url . "/model/IEntity.php");

class Lead_Entity implements IEntity
{
    function __construct($lead_name,$date_of_lead_entry,$what_the_lead_wants)
    {
        $this->lead_name=$lead_name;
        $this->date_of_lead_entry=$date_of_lead_entry;
        $this->what_the_lead_wants=$what_the_lead_wants;
    }

    public $id;
    public $lead_name;
    public $date_of_lead_entry;
    public $what_the_lead_wants;

    public static function getSchemaString()
    {
        return "(id INT AUTO_INCREMENT PRIMARY KEY, lead_name VARCHAR(64), date_of_lead_entry TIMESTAMP, what_the_lead_wants VARCHAR(64) )";
    }
}