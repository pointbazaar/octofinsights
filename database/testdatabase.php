<?php



echo("database status:");

include_once("make_database_connection.php");

$my_connection = getConnection();
if($my_connection==null){
    echo("<span class='float-right'>ERROR</span>");
}else{
    echo("<span class='float-right'>connected</span>");
}

$my_connection=null;

//https://www.w3schools.com/php/php_mysql_create_table.asp
