

<html>
<head>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link href="../../style.css" rel="stylesheet">
</head>

<body>

<?php
include("../../base.php");
include($absolute_file_url . "/authentication/is_authenticated_otherwise_redirect.php");
?>

<div id="container" class="container-fluid">
    <div class="row">

        <?php
            include($absolute_file_url . "/fragments/sidebar_fragment.php");
        ?>

        <div id="main-content" class="col-md-10">
            <h1>Leads</h1>

            <table class="">
                <tr>
                    <td>
                        <div id="lead-statistics">
                            <div id="row justify-content-center">
                                <h3>
                                    Leads aquired in last 30 days(TODO) : 2
                                </h3>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p>
                            TODO: have a form here to insert a lead
                        </p>
                        <form class="col">
                            <div class="form-group">
                                <input type="text" name="lead-name" placeholder="lead-name">
                            </div>
                            <div class="form-group">
                                <input type="date" name="date" placeholder="date">
                            </div>
                            <div class="form-group">
                                <input type="text" name="what-the-lead-wants" placeholder="what-the-lead-wants">
                            </div>
                            <button type="submit" value="insert sale" class="btn btn-primary">insert lead</button>
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
                        <th scope="col">Lead Name</th>
                        <th scope="col">Date the Lead became interested</th>
                        <th scope="col">What the lead wants</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>1</td>
                        <td>Mark</td>
                        <td>1.3.2019</td>
                        <td>A website for his small business</td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>Jacob</td>
                        <td>2.3.2019</td>
                        <td>A calculator android app</td>
                    </tr>
                    <tr>
                        <td>3</td>
                        <td>Larry</td>
                        <td>3.3.2019</td>
                        <td>An Ecommerce Store</td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <p>

                TODO: put the different stages that leads are in<br>
            </p>
        </div>
    </div>
</div>

</body>

</html>
