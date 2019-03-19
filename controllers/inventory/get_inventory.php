

<html>
<head>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link href="../../style.css" rel="stylesheet">
</head>

<body>

<?php

include_once("../../base.php");
include_once($absolute_file_url . "/authentication/is_authenticated_otherwise_redirect.php");

?>

<div id="container" class="container-fluid">
    <div class="row">

        <?php
            include_once("../../fragments/sidebar_fragment.php");
        ?>

        <div id="main-content" class="col-md-10">
            <div class="row m-3">
                <h1>Inventory</h1>
            </div>

            <?php
                include_once($absolute_file_url . "/model/inventoryitem.php");
                $inventory_items=array(
                        new InventoryItem("Kitchen Sink",30,2),
                        new InventoryItem("Printer",100,1)
                );
            ?>
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
                            try{

                                include_once($absolute_file_url . "/database/make_database_connection.php");


                                $conn = getConnection();

                                $statement = $conn->prepare("SELECT * FROM inventory;");

                                $statement->execute();

                                //test
                                $results = $statement->fetchAll();

                                for($i=0;$i<sizeof($results);$i++){
                                    $item = $results[$i];
                                    echo("<tr>");
                                        echo_td($item[0]);
                                        echo_td($item[1]);
                                        echo_td($item[2]);
                                        echo_td($item[3]);

                                        $delete_button = "<button type='submit' class='btn btn-outline-warning'>" . "delete" . "</input>";

                                        echo("<td>");
                                            echo("<form action='" . $baseurl . "/controllers/inventory" . "/post_delete_inventory.php" . "' method='post'>");
                                                //id for the item to delete
                                                echo("<input type='number' name='id' hidden value='" . $item[0] . "'>");
                                                echo($delete_button);
                                            echo("</form>");
                                        echo("</td>");
                                    echo("</tr>");
                                }


                                $conn=null;
                            }catch (Exception $exception){
                                echo($exception->getMessage());
                            }
                        ?>
                    </tbody>
                </table>
        </div>
    </div>
</div>

</body>

</html>
