<?php



echo("database status: <br>");

include_once("make_database_connection.php");

$my_connection = getConnection();
if($my_connection==null){
    echo("ERROR");
}else{
    echo("connected");
}

$my_connection=null;

//https://www.w3schools.com/php/php_mysql_create_table.asp
