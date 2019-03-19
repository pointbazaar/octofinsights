<?php

include_once("../base.php");

session_start();

if(isset($_POST["username"]) && isset($_POST["password"]) && isset($_POST["db-username"]) && isset($_POST["db-password"])  ){

    echo("<p>query parameters correct</p>");

    $username = $_POST["username"];
    $password = $_POST["password"];

    $_SESSION["db-username"]=$_POST["db-username"];
    $_SESSION["db-password"]=$_POST["db-password"];

    if($username == "test" && $password == "test"){

        $_SESSION["username"]=$username;
        $_SESSION["authenticated"]=true;

        header("Location: " . $baseurl . "/index.php");
    }else{

        echo("<p>username or password wrong</p>");
    }
}