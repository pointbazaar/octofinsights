<?php

include_once($_SERVER["DOCUMENT_ROOT"] . "/base.php");

//session_start();

if (session_status() == PHP_SESSION_NONE) {
    session_start();
}

if(!$_SESSION["authenticated"]){
    echo($_SESSION);

    header("Location: " . $baseurl . "/authentication" . "/get_login.php");
}