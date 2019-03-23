<?php
class Transaction_Base_Entity
{
    public $name;
    public $value;

    public function __construct($name,$value)
    {
        $this->name=$name;
        $this->value=$value;
    }

    function getName()
    {
        return $this->name;
    }

    function getValue()
    {
        return $this->value;
    }
}