

<html>
<head>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link href="style.css" rel="stylesheet">
</head>

<body>

<?php

include("base.php");

//echo $baseurl;

?>

<div id="container" class="container-fluid">
    <div class="row">
        <div id="sidebar" class="col-md-2 sidebar">
            <?php
                include("fragments/sidebar_fragment.php");
            ?>
        </div>
        <div id="main-content" class="col-md-10">
            <h1>Dashboard</h1>
            <?php
                echo "main dashboard";
            ?>

            <?php
                include("fragments/cashflow_fragment.php");
            ?>
            <div class="m-3 p-3"></div>
            <?php
                include("fragments/total_balance_history_fragment.php");
            ?>
            <div class="m-3 p-3"></div>
            <p>
                maybe make a total balance history chart across all accounts
                of this enterprise
            </p>
        </div>
    </div>
</div>

</body>

</html>
