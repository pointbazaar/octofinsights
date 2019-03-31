<?php



session_start();

if(  isset($_POST["username"]) && isset($_POST["password"])  ) {

    echo("<p>query parameters correct</p>");

    $username = $_POST["username"];
    $password = $_POST["password"];


    //$_SESSION["db-username"]=$_POST["db-username"];
    //$_SESSION["db-password"]=$_POST["db-password"];

    $credentials = file($_SERVER["DOCUMENT_ROOT"] . "/.credentials");

    $credentials[0]=trim($credentials[0]);
    $credentials[1]=trim($credentials[1]);

    echo("'" . $credentials[0] . "'<br>");
    echo("'" . $credentials[1] . "'<br>");



    if (sizeof($credentials) < 2) {
        echo("internal error reading file");
        exit();
    }

    $_SESSION["db-username"] = $credentials[0];
    $_SESSION["db-password"] = $credentials[1];

    if ($username == "test" && $password == "test") {

        $_SESSION["username"] = $username;
        $_SESSION["authenticated"] = true;

        header("Location: " . $baseurl . "/index.php");
    } else {

        echo("<p>username or password wrong</p>");
    }


}