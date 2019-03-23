<div id="sidebar" class="col-md-2 sidebar" style="background-color: #f4f8f5">

    <h2 class="text-center">
        OctoFinsights
    </h2>
    <hr>
    <div class="text-center">
        <span class="float-left">
            Logged in as:
        </span>
        <span class="float-right">
        <?php
            echo($_SESSION["username"]);
        ?>
        </span>
        <br>
        <span class="float-left">
        on behalf of :
        </span>
        <span class="float-right">
            Example Enterprise
        </span>
        <br>


    </div>
    <?php


        //echo(getcwd());

        //echo($_SERVER["DOCUMENT_ROOT"]);

        $absolute_file_url = $_SERVER["DOCUMENT_ROOT"] . "/octofinsights";

        include_once($absolute_file_url . "/database/testdatabase.php");

        echo("<br>");

        include_once($absolute_file_url . "/base.php");

        $links = array(
            new MenuItem("Dashboard",    $baseurl . "/index.php"),
            new MenuItem("Sales",        $baseurl . "/controllers/sales" . "/get_sales.php"),
            new MenuItem("Leads",        $baseurl . "/controllers/leads/get_leads.php"),
            new MenuItem("Employees",    $baseurl . "/controllers/employees/get_employees.php"),
            new MenuItem("Inventory",    $baseurl . "/controllers/inventory" . "/get_inventory.php"),
            new MenuItem("Expenses",     $baseurl . "/controllers/expenses" . "/get_expenses.php"),
            new MenuItem("Shareholders(TODO)",$baseurl . "/controllers/shareholders/get_shareholders.php")
        );

        class MenuItem{

            public $name;
            public $link;

            function __construct($name,$link)
            {
                $this->name=$name;
                $this->link=$link;
            }
        }
    ?>

    <div class="">
        <hr>
        <?php
            for($i=0;$i<sizeof($links);$i++){
                echo "<a href='" . $links[$i]->link . "'>";
                    echo "<div class=''>";
                        echo $links[$i]->name;
                    echo "</div>";
                echo "</a>";
                echo("<hr>");
            }
        ?>
        <?php
            echo("<a href='"  . $baseurl  . "/authentication/get_login.php' >");
        ?>
        <div class="">
            Logout
        </div>
        <?php
            echo("</a>");
        ?>
        <hr>
    </div>

</div>