<?php

include("../base.php");

session_start();

if(!$_SESSION["authenticated"]){
    echo($_SESSION);

    header("Location: " . $baseurl . "/authentication" . "/get_login.php");
}