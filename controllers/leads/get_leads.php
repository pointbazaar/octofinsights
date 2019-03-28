

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
                        <form class="col" method="post" action=<?=$baseurl . "/controllers/leads/post_leads.php"?>>
                            <div class="form-group">
                                <input type="text" name="lead_name" placeholder="lead_name">
                            </div>
                            <div class="form-group">
                                <select class="form-control" name="lead_status">
                                    <option>open_not_contacted</option>
                                    <option>open_contacted</option>
                                    <option>open_awaiting_response</option>
                                    <option>closed_not_converted</option>
                                    <option>closed_converted</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <input type="date" name="date_of_lead_entry" placeholder="date_of_lead_entry">
                            </div>
                            <div class="form-group">
                                <input type="text" name="what_the_lead_wants" placeholder="what_the_lead_wants">
                            </div>
                            <button type="submit" value="insert sale" class="btn btn-primary">insert lead</button>
                        </form>

                    </td>
                </tr>

            </table>
            <p>
                TODO: ability to sort the table by the different fields
            </p>

            <div class="m-3 p-3"></div>
            <div id="salestable">

                    <?php
                    try{

                        $map_to_html = function($item){return $item->get_table_row_html();};

                        $open_awaiting_response_result = join("",array_map($map_to_html,get_all_leads_with_status("open_awaiting_response")));

                        $open_not_contacted_result =join("",array_map($map_to_html,get_all_leads_with_status("open_not_contacted")));

                        $open_contacted_result = join("",array_map($map_to_html,get_all_leads_with_status("open_contacted")));

                        $closed_converted_result = join("",array_map($map_to_html,get_all_leads_with_status("closed_converted")));

                        $closed_not_converted_result = join("",array_map($map_to_html,get_all_leads_with_status("closed_not_converted")));

                        $table_headers = array("ID","Lead Name","Lead Status","Date of Contact","What the Lead wants","Actions");


                        echo(make_strong("NOT CONTACTED:"));
                        echo(make_table("table",$table_headers,$open_not_contacted_result));
                        echo(make_strong("Already Contacted:"));
                        echo(make_table("table",$table_headers,$open_contacted_result));
                        echo(make_strong("Waiting for Response:"));
                        echo(make_table("table",$table_headers,$open_awaiting_response_result));

                        echo(make_strong("Closed, Converted:"));
                        echo(make_table("table",$table_headers,$closed_converted_result));
                        echo(make_strong("Closed, Not Converted:"));
                        echo(make_table("table",$table_headers,$closed_not_converted_result));

                    }catch (Exception $exception){
                        echo($exception->getMessage());
                    }

                    ?>

            </div>

            <p>

                TODO: put the different stages that leads are in<br>
            </p>
        </div>
    </div>
</div>

</body>

</html>
