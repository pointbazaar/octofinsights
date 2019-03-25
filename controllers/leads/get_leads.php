

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
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Lead Name</th>
                        <th scope="col">Lead Status</th>

                        <th scope="col">Date the Lead became interested</th>
                        <th scope="col">What the lead wants</th>
                        <th scope="col">Actions</th>
                    </tr>
                    </thead>
                    <tbody>


                    <?php
                    try{
                        include_once($absolute_file_url . "/services/lead_service.php");

                        $results = get_all_leads();

                        for($i=0;$i<sizeof($results);$i++){
                            $lead=$results[$i];

                            echo("<tr>");
                                echo_td($lead->id);
                                echo_td($lead->lead_name);
                                echo_td(
                                            make_form($baseurl . "/controllers/leads" . "/post_change_lead_status.php",
                                                make_hidden_input_number("id","",$lead->id)
                                                . make_select(Lead_Entity::get_lead_status_valid_values(),$lead->lead_status,"lead_status")
                                                . make_submit_button("update lead_status","btn btn-outline-secondary")
                                            )
                                );

                                echo_td(date("d-m-Y",strtotime($lead->date_of_lead_entry)));
                                echo_td($lead->what_the_lead_wants);

                                $delete_button = "<button type='submit' class='btn btn-outline-warning'>" . "delete" . "</button>";
                                echo_td(
                                    make_form($baseurl . "/controllers/leads" . "/post_delete_leads.php",
                                        make_hidden_input_number("id", "",$lead->id) . $delete_button
                                    )
                                );
                            echo("</tr>");
                        }

                    }catch (Exception $exception){
                        echo($exception->getMessage());
                    }
                    ?>

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
