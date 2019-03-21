<?php

class Employee_Entity{
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
}

?>