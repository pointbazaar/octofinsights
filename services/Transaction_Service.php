<?php
include_once($_SERVER["DOCUMENT_ROOT"] . "/base.php");
include_once($absolute_file_url . "/database/make_database_connection.php");

include_once($absolute_file_url . "/model/sale_entity.php");
include_once($absolute_file_url . "/model/Expense_Entity.php");

include_once($absolute_file_url . "/services/sales_service.php");
include_once($absolute_file_url . "/services/Expense_Entity_service.php");

include_once($absolute_file_url . "/model/Transaction_Base_Entity.php");

class Transaction_Service{

    private static $get_all_transactions_unordered = "(select expense_name as position_name,expense_value as value,expense_date as date from expenses) UNION ALL (select customer_name,price_of_sale,time_of_sale from sales)";

    private static function get_sql_for_get_all_transactions(){
        return Transaction_Service::$get_all_transactions_unordered . " ORDER BY date;";
    }

    public static function get_all_transactions(){

        $query = Transaction_Service::get_sql_for_get_all_transactions();
        //echo($query);
        //echo("<br>");

        $results = getConnectionAndInitDBWithTables()->query($query);

        $results->execute();

        $results2 =  $results->fetchAll();

        $typed_results = array();

        for($i=0;$i<sizeof($results2);$i++){
            $transaction = $results2[$i];
            array_push($typed_results,new Transaction_Base_Entity($transaction[1],$transaction[1]));
        }

        return $typed_results;
    }

    public static function getHighestAbsoluteTransactionValue(){

        $query = "select max(abs(value)) from ((select expense_name as position_name,expense_value as value,expense_date as date from expenses) UNION ALL (select customer_name,price_of_sale,time_of_sale from sales) ORDER BY date) AS T;";

        $result = getConnectionAndInitDBWithTables()->query($query);
        $result->execute();
        $results2 = $result->fetchAll();

        if(!sizeof($results2)>0){
            return 1000;
        }

        return abs($results2[0][0]);
    }
}