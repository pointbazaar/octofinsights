<?php

$baseurl="/octofinsights";

$absolute_file_url = $_SERVER["DOCUMENT_ROOT"] . $baseurl;

//https://www.chartjs.org/samples/latest/

function echo_td($contents){
    echo("<td>" . $contents . "</td>");
}

function echo_strong($contents){
    echo("<strong>" . $contents . "</strong>");
}

function echo_div($contents){
    echo("<div>" . $contents . "</div>");
}


function echo_bootstrap_form_group($type, $name, $placeholder){
    echo("<div class='form-group'>");
        echo("<input type='" . $type . "' name='" . $name . "' placeholder='" . $placeholder . "' >");
    echo("</div>");
}

function echo_bootstrap_submit_button($text){
    echo("<button type='submit'  class='btn btn-primary'>" . $text . "</button>");
}

?>