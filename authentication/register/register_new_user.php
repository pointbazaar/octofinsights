

<?php

session_start();
session_destroy();

$_SESSION=array();

?>

<?php
//include_once("../base.php");

if($_SERVER["REQUEST_METHOD"] == "POST"){

    //TODO: make an account
    if( isset($_POST["username"]) && isset($_POST["password"]) ){
        $username = $_POST["username"];
        $password = $_POST["password"];

        //TODO: check if that username exists already, if not, make the account.

        //use the java microservice for that.

        header("Location: " . $baseurl . "/index.php");
    }else{

        echo("<h1> Incorrect Form Submission. </h1>"
    }
}

?>

<html>
<head>
<meta charset="=UTF-8">
</head>

<body>

<h1> Register Page </h1>
<form action='" . "/authentication/register" ."/register_new_user.php" . "' method='post'>

    <input type="text" name="username" placeholder="username">
    <input type="text" name="password" placeholder="password">

    <input type="submit" value="Register">

</form>;

<!--
<p>
    Credentials for testing:<br>
    test<br>
    test<br>
</p>
-->

</body>
</html>
