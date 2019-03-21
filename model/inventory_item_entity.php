<?php

class InventoryItem{
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


}

?>