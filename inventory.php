

<html>
<head>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link href="style.css" rel="stylesheet">
</head>

<body>

<?php

include("base.php");
include("authentication/is_authenticated_otherwise_redirect.php");

?>

<div id="container" class="container-fluid">
    <div class="row">

        <?php
            include("fragments/sidebar_fragment.php");
        ?>

        <div id="main-content" class="col-md-10">
            <div class="row m-3">
                <h1>Inventory</h1>
                <div class="m-3"></div>
                <button class="btn btn-outline-primary ">
                    Insert new Inventory Item (TODO)
                </button>
            </div>

            <?php
                include("model/inventoryitem.php");
                $inventory_items=array(
                        new InventoryItem("Kitchen Sink",30)
                );
            ?>

            <ul class="list-group">
                <?php
                    for($i=0; $i<sizeof($inventory_items); $i++){
                        echo "<li class='list-group-item'>";
                            echo "<span>";
                                echo($inventory_items[$i]->toString());
                            echo "</span>";
                            echo "<button class='btn btn-outline-danger float-right'>";
                                echo "It got lost";
                            echo "</button>";
                        echo "</li>";
                    }
                ?>
                <li class="list-group-item">
                    <span>Good Knife [Price: 30 Euro]</span>
                    <button class="btn btn-outline-danger float-right">
                        It got Lost
                    </button>
                </li>
            </ul>
        </div>
    </div>
</div>

</body>

</html>
