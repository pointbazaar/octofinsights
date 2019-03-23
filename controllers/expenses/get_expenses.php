

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
            <h1>Expenses Overview</h1>
            <hr>

            <table class="">
                <tr>
                    <td>
                        <?php
                            echo("<form class='col' method='post' action='" . $baseurl . "/controllers/expenses/post_expenses.php'>");

                            echo_bootstrap_form_group("text","expense_name","expense_name");
                            echo_bootstrap_form_group("date","expense_date","expense_date");
                            echo_bootstrap_form_group("number","expense_value","expense_value");

                            echo_bootstrap_submit_button("Insert Expense");
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
                        <th scope="col">Expense Name</th>
                        <th scope="col">Expense Date</th>
                        <th scope="col">Expense Value</th>
                        <th scope="col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <?php
                        try{
                            include_once($absolute_file_url . "/services/Expense_Entity_service.php");

                            $results = get_all_expenses();

                            for($i=0;$i<sizeof($results);$i++){
                                $expense=$results[$i];

                                echo("<tr>");
                                    echo_td($expense->id);
                                    echo_td($expense->expense_name);
                                    echo_td($expense->expense_date);
                                    echo_td($expense->expense_value);
                                echo("<td>");

                                    echo("<form action='" . $baseurl . "/controllers/expenses" . "/post_delete_expense.php" . "' method='post'>");
                                        echo("<input type='number' name='id' hidden value='" . $expense->id . "'>");
                                        echo_bootstrap_submit_button("delete");
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
