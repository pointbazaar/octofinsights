<?php
    echo "sidebar";

    include("../base.php");

    $links = array(
            new MenuItem("Dashboard",$baseurl . "/index.php"),
        new MenuItem("Reports(TODO)","."),
        new MenuItem("Sales(TODO)","."),
        new MenuItem("Employees(TODO)","."),
        new MenuItem("Inventory(TODO)",".")
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

<ul class="list-group">
    <?php
        for($i=0;$i<sizeof($links);$i++){
            echo "<a href='" . $links[$i]->link . "'>";
                echo "<li class='list-group-item'>";
                    echo $links[$i]->name;
                echo "</li>";
            echo "</a>";
        }
    ?>

    <!--
    <a href=".">
        <li class="list-group-item">
            Reports
        </li>
    </a>
    -->
</ul>
