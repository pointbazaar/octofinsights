

<html>
<head>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link href="../../style.css" rel="stylesheet">
</head>

<body>

<?php

include("../../base.php");
include($absolute_file_url  . "/authentication/is_authenticated_otherwise_redirect.php");

?>

<div id="container" class="container-fluid">
    <div class="row">

        <?php
            include($absolute_file_url . "/fragments/sidebar_fragment.php");
        ?>

        <div id="main-content" class="col-md-10">
            <div class="row m-3">
                <h1>Employees</h1>
            </div>





            <table class="">
                <tr>
                    <td>
                        <p>
                            TODO: have a form here to insert a employee
                        </p>
                        <?php
                            echo("<form class='col' action='" . $baseurl . "/controllers/employees/post_employees.php". "' method='post''>");
                        ?>

                            <div class="form-group">
                                <input type="text" name="employee_name" placeholder="employee_name">
                            </div>
                            <div class="form-group">
                                <input type="text" name="employee_role" placeholder="employee_role">
                            </div>
                            <div class="form-group">
                                <input type="email" name="employee_email" placeholder="employee_email">
                            </div>
                            <button type="submit" value="insert employee" class="btn btn-primary">insert Employee</button>
                        <?php
                            echo("</form>");
                        ?>

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
                        <th scope="col">Email</th>
                        <th scope="col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                        <?php

                        include_once($absolute_file_url . "/model/employee_entity.php");
                        include_once($absolute_file_url . "/services/employees_service.php");
                        $employees=get_all_employees();

                        for($i=0;$i<sizeof($employees);$i++){
                            echo "<tr>";
                                echo("<td>");
                                    echo($employees[$i]->id);
                                echo("</td>");

                                echo("<td>");
                                    echo($employees[$i]->name);
                                echo("</td>");

                                echo("<td>");
                                    echo($employees[$i]->role);
                                echo("</td>");

                                echo("<td>");
                                    echo($employees[$i]->email  . "@gmail.com");
                                echo("</td>");

                                $delete_button = "<button type='submit' class='btn btn-outline-warning'>" . "delete" . "</button>";

                                echo("<td>");
                                    echo("<form action='" . $baseurl . "/controllers/employees" . "/post_delete_employees.php" . "' method='post'>");
                                        echo("<input type='number' name='id' hidden value='" . $employees[$i]->id . "'>");
                                        echo($delete_button);
                                    echo("</form>");
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
