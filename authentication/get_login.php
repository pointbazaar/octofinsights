

<?php

session_start();
session_destroy();

$_SESSION=array();

?>

<?php
//include_once("../base.php");
?>

<html>
<head>
<meta charset="=UTF-8">
</head>

<body>

<?php

    echo("<h1> Login Page </h1>");
    echo("<form action='" . "/authentication" ."/post_login.php" . "' method='post'>");
?>

<input type="text" name="username" placeholder="username">
<input type="text" name="password" placeholder="password">

<!--
<input type="text" name="db-username" placeholder="db-username">
<input type="text" name="db-password" placeholder="db-password">
-->

<input type="submit" value="Login">


<?php
    echo("</form>");
?>

<p>
    You do not have an account?<br>
    <a href="./register/register_new_user.php">Register here</a>
</p>

<!--
    Credentials for testing:<br>
    test<br>
    test<br>
-->

</body>
</html>
