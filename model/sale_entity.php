<?php

class sale_entity{
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
}