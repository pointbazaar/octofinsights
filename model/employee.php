<?php

class Employee{
    public $name;
    public $surname;

    public $role;

    public $id;

    function __construct($name,$surname,$role)
    {
        $this->name=$name;
        $this->surname=$surname;
        $this->role=$role;
    }

    function toString(){
        return $this->name . " " . $this->surname . " [Role: " . $this->role . "]";
    }
}

?>