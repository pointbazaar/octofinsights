<?php

include("../../base.php");
include("../../authentication/is_authenticated_otherwise_redirect.php");

if(!$_SERVER["REQUEST_METHOD"] === "POST"){
    exit();
}

//TODO: check post parameters and insert into db

header("Location: " . $baseurl . "/controllers/sales/get_sales.php");