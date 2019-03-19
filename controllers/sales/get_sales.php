

<html>
<head>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link href="../../style.css" rel="stylesheet">
</head>

<body>

<?php
include_once("../../base.php");
include_once("../../authentication/is_authenticated_otherwise_redirect.php");
?>

<div id="container" class="container-fluid">
    <div class="row">

        <?php
        include_once("../../fragments/sidebar_fragment.php");
        ?>

        <div id="main-content" class="col-md-10">
            <h1>Sales Overview</h1>
            <hr>

            <table class="">
                <tr>
                    <td>
                        <div id="sales-statistics">
                            <div id="row justify-content-center">
                                <h3>
                                    Sales in last 30 days(TODO) : 30 $
                                </h3>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <?php
                            echo("<form class='col' method='post' action='" . $baseurl . "/controllers/sales/post_sales.php'>");
                        ?>
                            <div class="form-group">
                                <input type="text" name="customer_name" placeholder="customer_name">
                            </div>
                            <div class="form-group">
                                <input type="date" name="time_of_sale" placeholder="time_of_sale">
                            </div>
                            <div class="form-group">
                                <input type="number" name="price_of_sale" placeholder="price_of_sale">
                            </div>
                            <div class="form-group">
                                <input type="text" name="product_or_service" placeholder="product_or_service">
                            </div>
                            <button type="submit" value="insert sale" class="btn btn-primary">insert sale</button>
                        </form>

                    </td>
                </tr>

            </table>

            <div class="m-3 p-3"></div>
            <div id="salestable">
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Customer Name</th>
                        <th scope="col">Date</th>
                        <th scope="col">Amount Received</th>
                        <th scope="col">Product or Service</th>
                        <th scope="col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <?php
                        try{
                            include_once($absolute_file_url . "/services/sales_service.php");

                            $results = get_all_sales();

                            for($i=0;$i<sizeof($results);$i++){
                                $sale=$results[$i];

                                echo("<tr>");
                                    echo_td($sale->id);
                                    echo_td($sale->customer_name);
                                    echo_td($sale->time_of_sale);
                                    echo_td($sale->price_of_sale . " $");
                                    echo_td($sale->product_or_service);
                                    $delete_button = "<button type='submit' class='btn btn-outline-warning'>" . "delete" . "</button>";

                                echo("<td>");
                                    echo("<form action='" . $baseurl . "/controllers/sales" . "/post_delete_sale.php" . "' method='post'>");
                                        echo("<input type='number' name='id' hidden value='" . $sale->id . "'>");
                                    echo($delete_button);
                                    echo("</form>");
                                echo("</td>");
                                echo("</tr>");
                            }

                        }catch (Exception $exception){
                            echo($exception->getMessage());
                        }
                    ?>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

</body>

</html>
