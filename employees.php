

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
                <h1>Employees</h1>
            </div>

            <?php
                include("model/employee.php");
                $employees=array(
                    new Employee("Peter","Muster","Admin"),
                    new Employee("Sarah","Wiese","Accounting")
                );
            ?>

            <table class="">
                <tr>
                    <td>
                        <p>
                            TODO: have a form here to insert a employee
                        </p>
                        <form class="col">
                            <div class="form-group">
                                <input type="text" name="employee-name" placeholder="employee-name">
                            </div>
                            <div class="form-group">
                                <input type="date" name="date-of-entry" placeholder="date-of-entry">
                            </div>
                            <button type="submit" value="insert employee" class="btn btn-primary">insert Employee</button>
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
                        <th scope="col">Employee Name</th>
                        <th scope="col">Role</th>
                        <th scope="col">Date of Entry</th>
                        <th scope="col">Email</th>
                        <th scope="col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                        <?php
                        for($i=0;$i<sizeof($employees);$i++){
                            echo "<tr>";
                                echo("<td>");
                                    echo($i);
                                echo("</td>");

                                echo("<td>");
                                    echo($employees[$i]->name);
                                echo("</td>");

                                echo("<td>");
                                    echo($employees[$i]->role);
                                echo("</td>");

                                echo("<td>");
                                    echo("1.2.2018");
                                echo("</td>");

                                echo("<td>");
                                    echo($employees[$i]->name  . "@gmail.com");
                                echo("</td>");

                                echo "<td>";
                                    echo "<button class='btn btn-outline-danger'>";
                                        echo "Delete";
                                    echo "</button>";
                                echo("</td>");
                            echo "</tr>";
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
