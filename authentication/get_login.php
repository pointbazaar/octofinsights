

<?php

session_start();
session_destroy();

$_SESSION=array();

?>

<?php
include("../base.php");
?>

<html>
<head>
<meta charset="=UTF-8">
</head>

<body>

<?php

    echo("<h1> Login Page </h1>");
    echo("<form action='" . $baseurl . "/authentication" ."/post_login.php" . "' method='post'>");
?>

<input type="text" name="username" placeholder="username">
<input type="password" name="password" placeholder="password">

<input type="submit" value="Login">


<?php
    echo("</form>");
?>

</body>
</html>
