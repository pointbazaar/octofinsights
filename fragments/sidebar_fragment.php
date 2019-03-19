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
    <div class="p-3"></div>
    <?php

        include("../base.php");

        $links = array(
                new MenuItem("Dashboard",$baseurl . "/index.php"),
            new MenuItem("Reports(TODO)","."),
            new MenuItem("Sales",$baseurl . "/controllers/sales" . "/get_sales.php"),
            new MenuItem("Leads",$baseurl . "/leads.php"),
            new MenuItem("Employees",$baseurl . "/employees.php"),
            new MenuItem("Inventory",$baseurl . "/inventory.php")
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