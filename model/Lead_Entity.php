<?php
include_once($_SERVER["DOCUMENT_ROOT"] . "/base.php");
include_once($_SERVER["DOCUMENT_ROOT"] . "/include_many.php");

class Lead_Entity implements IEntity
{
    function __construct($lead_name,$lead_status,$date_of_lead_entry,$what_the_lead_wants)
    {
        $this->lead_name=$lead_name;
        $this->lead_status=$lead_status;
        $this->date_of_lead_entry=$date_of_lead_entry;
        $this->what_the_lead_wants=$what_the_lead_wants;
    }

    public $id;
    public $lead_name;

    public $lead_status;

    public $date_of_lead_entry;
    public $what_the_lead_wants;

    public static function getSchemaString()
    {
        return "(id INT AUTO_INCREMENT PRIMARY KEY, lead_name VARCHAR(64), lead_status VARCHAR(32), date_of_lead_entry TIMESTAMP, what_the_lead_wants VARCHAR(64) )";
    }

    public static function get_lead_status_valid_values(){
        return array("open_not_contacted","open_contacted","open_awaiting_response","closed_not_converted","closed_converted");
    }

    public function get_lead_status_html(){
        $pretty_values = array("Open, not contacted","Open, contacted","Open, awaiting response","Closed, not converted","COSED, CONVERTED");

        $index = array_search($this->lead_status,$pretty_values);
        return $pretty_values[$index];
    }

    public function get_table_row_html(){
        $result=tr(
            make_td($this->id)
            .
            make_td($this->lead_name)
            .
            make_td(
                form("/controllers/leads" . "/post_change_lead_status.php",
                    make_hidden_input_number("id","",$this->id)
                    . select(Lead_Entity::get_lead_status_valid_values(),$this->lead_status,"lead_status")
                    . submit_button("update status","btn btn-outline-secondary")
                )
            )
            .
            make_td(date("d-m-Y",strtotime($this->date_of_lead_entry)))
            .
            make_td($this->what_the_lead_wants)
            .
            make_td(
                form("/controllers/leads" . "/post_delete_leads.php",
                    make_hidden_input_number("id", "",$this->id)
                    .
                    submit_button("delete","btn btn-outline-warning")
                )
            )
        );
        return $result;
    }
}