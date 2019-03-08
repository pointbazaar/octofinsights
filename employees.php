

<html>
<head>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link href="style.css" rel="stylesheet">
</head>

<body>

<?php

include("base.php");


?>

<div id="container" class="container-fluid">
    <div class="row">

        <?php
            include("fragments/sidebar_fragment.php");
        ?>

        <div id="main-content" class="col-md-10">
            <h1>Employees</h1>

            <?php
                include("model/employee.php");
                $employees=array(
                    new Employee("Peter","Muster","Admin"),
                    new Employee("Sarah","Wiese","Accounting")
                );
            ?>

            <ul class="list-group">
                <?php
                    for($i=0;$i<sizeof($employees);$i++){
                        echo "<li class='list-group-item'>";
                            echo "<span>";
                                echo($employees[$i]->toString());
                            echo "</span>";
                            echo "<button class='btn btn-outline-danger float-right'>";
                                echo "Fire";
                            echo "</button>";
                        echo "</li>";
                    }
                ?>
                <li class="list-group-item">
                    <span>peter birne [Role: CEO]</span>
                    <button class="btn btn-outline-danger float-right">
                        Fire
                    </button>
                </li>
            </ul>
        </div>
    </div>
</div>

</body>

</html>
