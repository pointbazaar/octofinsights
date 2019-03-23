<?php
include_once($_SERVER["DOCUMENT_ROOT"] . "/octofinsights/base.php");
include_once($absolute_file_url . "/model/IEntity.php");

class Expense_Entity implements IEntity
{

    public $id;
    public $expense_name;
    public $expense_date;
    public $expense_value;

    public function __construct($expense_name,$expense_date,$expense_value)
    {
        $this->expense_name=$expense_name;
        $this->expense_date=$expense_date;
        $this->expense_value=$expense_value;
    }

    public static function getSchemaString()
    {
        return "(id INT AUTO_INCREMENT PRIMARY KEY, expense_name VARCHAR(64), expense_date TIMESTAMP, expense_value INT )";
    }
}