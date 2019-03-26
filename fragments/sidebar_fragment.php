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



        include_once($_SERVER["DOCUMENT_ROOT"] . "/base.php");

        include_once($absolute_file_url . "/database/testdatabase.php");

        echo("<br>");



        $links = array(
            new MenuItem("Dashboard",    $baseurl . "/index.php",false),

            new MenuItem("Sales",        $baseurl . "/controllers/sales" . "/get_sales.php",true),
            new MenuItem("Expenses",     $baseurl . "/controllers/expenses" . "/get_expenses.php",true),

            new MenuItem("Leads",        $baseurl . "/controllers/leads/get_leads.php",true),
            new MenuItem("Employees",    $baseurl . "/controllers/employees/get_employees.php",false),
            new MenuItem("Inventory",    $baseurl . "/controllers/inventory" . "/get_inventory.php",false),

            new MenuItem("Shareholders(TODO)",$baseurl . "/controllers/shareholders/get_shareholders.php",false)
        );

        class MenuItem{

            public $name;
            public $link;
            public $is_important;

            function __construct($name,$link,$is_important)
            {
                $this->name=$name;
                $this->link=$link;
                $this->is_important=$is_important;
            }
        }
    ?>

    <div class="">
        <hr>
        <?php
            for($i=0;$i<sizeof($links);$i++){
                echo "<a href='" . $links[$i]->link . "'>";
                    if($links[$i]->is_important){
                        echo_strong($links[$i]->name);
                    }else{
                        echo_div($links[$i]->name);
                    }
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