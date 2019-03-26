<?php

include_once($_SERVER["DOCUMENT_ROOT"] . "/base.php");
include_once($absolute_file_url . "/include_many.php");

class Employee_Entity implements IEntity {
    public $name;
    public $email;

    public $role;

    public $id;

    function __construct($id,$name,$role,$email)
    {
        $this->id=$id;
        $this->name=$name;
        $this->email=$email;
        $this->role=$role;
    }

    function toString(){
        return $this->name . " " . $this->email . " [Role: " . $this->role . "]";
    }

    public static function getSchemaString()
    {
        return "(id INT AUTO_INCREMENT PRIMARY KEY, employee_name VARCHAR(32) , employee_role VARCHAR(32), employee_email VARCHAR(64) )";
    }
}

?>