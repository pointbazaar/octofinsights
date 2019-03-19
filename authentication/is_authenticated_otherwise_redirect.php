<?php
$absolute_file_url = $_SERVER["DOCUMENT_ROOT"] . $baseurl;
include_once($absolute_file_url . "/base.php");

session_start();

if(!$_SESSION["authenticated"]){
    echo($_SESSION);

    header("Location: " . $baseurl . "/authentication" . "/get_login.php");
}