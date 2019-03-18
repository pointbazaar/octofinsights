

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
                        <p>
                            TODO: have a form here to insert a sale
                        </p>
                        <form class="col">
                            <div class="form-group">
                                <input type="text" name="customer name" placeholder="customer name">
                            </div>
                            <div class="form-group">
                                <input type="date" name="date" placeholder="date">
                            </div>
                            <div class="form-group">
                                <input type="number" name="amount" placeholder="amount">
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
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>1</td>
                        <td>Mark</td>
                        <td>1.3.2019</td>
                        <td>300 $</td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>Jacob</td>
                        <td>2.3.2019</td>
                        <td>10300 $</td>
                    </tr>
                    <tr>
                        <td>3</td>
                        <td>Larry</td>
                        <td>3.3.2019</td>
                        <td>50 $</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

</body>

</html>
