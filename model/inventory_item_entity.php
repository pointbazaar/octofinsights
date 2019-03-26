<?php
include_once($_SERVER["DOCUMENT_ROOT"] . "/base.php");
include_once($_SERVER["DOCUMENT_ROOT"] . "/include_many.php");


class InventoryItem_Entity implements IEntity {
    public $name;

    public $price;

    public $id;

    public $amount;

    function __construct($name,$price,$amount)
    {
        $this->name=$name;
        $this->price=$price;
        $this->amount=$amount;
    }

    function toString(){
        return $this->name . " " . " [Price: " . $this->price . "]" . $this->amount . " pieces";
    }


    public static function getSchemaString()
    {
        return "( id INT AUTO_INCREMENT PRIMARY KEY, item_name VARCHAR(128) NOT NULL, item_price INT NOT NULL, amount INT NOT NULL)";
    }
}

?>