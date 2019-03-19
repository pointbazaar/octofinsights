

<html>
<head>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link href="../../style.css" rel="stylesheet">
</head>

<body>

<?php

include("../../base.php");
include("../../authentication/is_authenticated_otherwise_redirect.php");

?>

<div id="container" class="container-fluid">
    <div class="row">

        <?php
            include("../../fragments/sidebar_fragment.php");
        ?>

        <div id="main-content" class="col-md-10">
            <div class="row m-3">
                <h1>Inventory</h1>
            </div>

            <?php
                include($absolute_file_url . "/model/inventoryitem.php");
                $inventory_items=array(
                        new InventoryItem("Kitchen Sink",30,2),
                        new InventoryItem("Printer",100,1)
                );
            ?>

            <p>
                TODO: have a form here to insert an inventory item
            </p>
            <form class="col" action="post_inventory.php" method="post">
                <div class="form-group">
                    <span>item_name</span><input type="text" name="item_name" placeholder="item_name">

                    <span>amount</span>
                    <input type="number" name="amount" placeholder="1">

                    <span>item_price</span>
                    <input type="number" name="item_price" placeholder="1">
                </div>
                <button type="submit" class="btn btn-primary">Insert Item</button>
            </form>


            <div id="salestable">
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Item Name</th>
                        <th scope="col">Price</th>
                        <th scope="col">Amount</th>

                        <th scope="col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                        <?php
                        for($i=0; $i<sizeof($inventory_items); $i++){
                            echo "<tr class=''>";
                                echo("<td>" . $i . "</td>");
                                echo "<td>";
                                    echo($inventory_items[$i]->name);
                                echo "</td>";
                                echo("<td>"  . $inventory_items[$i]->price . "</td>");
                                echo("<td>" . $inventory_items[$i]->amount . "</td>");
                                echo("<td>");
                                    echo "<button class='btn btn-outline-danger'>";
                                        echo "It got lost";
                                    echo "</button>";
                                echo("</td>");
                            echo "</tr>";
                        }
                        ?>
                        <tr>

                        </tr>

                        <?php
                            echo("trying to output db values");
                            try{
                                include($absolute_file_url . "/database/make_database_connection.php");

                                $conn = getConnection();

                                $statement = $conn->prepare("SELECT * FROM inventory;");
                                echo("step 0");
                                $statement->execute();

                                echo("step 1");

                                $results = $statement->setFetchMode(PDO::FETCH_ASSOC);

                                foreach (new RecursiveArrayIterator($statement->fetchAll()) as $key=>$value){
                                    echo("<tr>");
                                        echo($value);
                                    echo("</tr>");
                                }

                                $conn->close();
                            }catch (Exception $exception){
                                echo($exception->getMessage());
                                echo($exception->getTraceAsString());
                            }
                        ?>
                    </tbody>
                </table>
        </div>
    </div>
</div>

</body>

</html>
