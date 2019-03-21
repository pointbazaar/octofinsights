

<html>
<head>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link href="style.css" rel="stylesheet">
</head>

<body>

<?php
include_once("base.php");
include_once("authentication/is_authenticated_otherwise_redirect.php");
?>

<div id="container" class="container-fluid">
    <div class="row">

        <?php
            include_once("fragments/sidebar_fragment.php");
        ?>

        <div id="main-content" class="col-md-10">
            <h1>Dashboard</h1>



            <div class="row">
                <?php
                    include_once("fragments/cashflow_fragment.php");
                ?>
                <div class="m-3 p-3"></div>
                <?php
                    include_once("fragments/total_balance_history_fragment.php");
                ?>

                <?php
                    include_once("fragments/lead_sources_fragment.php");
                ?>

                <?php
                    echo("<img src='" . $baseurl . "/experimental/cashflow_history.php" . "'>");
                ?>

                <?php
                    echo("<img src='" . $baseurl . "/experimental/img.php" . "'>");
                ?>
            </div>
            <div class="m-3 p-3"></div>
            <p>

            </p>
        </div>
    </div>
</div>

</body>

</html>
