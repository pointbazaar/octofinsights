<?php
include_once($_SERVER["DOCUMENT_ROOT"] . "/octofinsights/base.php");
include_once($absolute_file_url . "/model/IEntity.php");
include_once($absolute_file_url . "/model/Transaction_Base_Entity.php");

class Sale_Entity extends Transaction_Base_Entity implements IEntity {
    public $id;
    public $customer_name;
    public $time_of_sale;
    public $price_of_sale;
    public $product_or_service;

    function __construct($id,$customer_name, $time_of_sale, $price_of_sale, $product_or_service)
    {
        $this->id = $id;
        $this->customer_name = $customer_name;
        $this->time_of_sale = $time_of_sale;
        $this->price_of_sale = $price_of_sale;
        $this->product_or_service = $product_or_service;
    }

    public static function getSchemaString()
    {
        return "(id INT AUTO_INCREMENT PRIMARY KEY, customer_name VARCHAR(128), time_of_sale TIMESTAMP,   price_of_sale INT,        product_or_service VARCHAR(128) )";
    }

    function getName()
    {
        return $this->product_or_service . " for " . $this->customer_name;
    }

    function getValue()
    {
        return $this->price_of_sale;
    }
}