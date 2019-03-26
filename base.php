<?php

$baseurl="";

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

function make_form($url,$contents)
{
    return "<form action='" . $url . "' method='post'>" . $contents . "</form>";
}

function make_hidden_input_number($name,$placeholder,$value){
    return make_input_number($name,$placeholder,$value,true);
}

function make_input_number($name,$placeholder,$value,$is_hidden){
    $type_str = " type='number' ";
    $name_str = " name='" . $name . "' ";
    $hidden_str = ($is_hidden)?" hidden ":" ";
    $placeholder_str = " placeholder='" . $placeholder . "' ";
    $value_str = " value='" . $value . "' ";
    return "<input ". $type_str . $name_str . $hidden_str . $placeholder_str . $value_str . ">";
}

function make_select($options,$selected_option,$name){
    $result = "<select name='" . $name . "' >";

    foreach ($options as $option){
        $selected_str = ($selected_option==$option)?" selected ":"";
        $result .= "<option" . $selected_str . ">" . $option . "</option>";
    }

    $result .= "</select>";

    return $result;
}


function echo_bootstrap_form_group($type, $name, $placeholder){
    echo("<div class='form-group'>");
        echo("<input type='" . $type . "' name='" . $name . "' placeholder='" . $placeholder . "' >");
    echo("</div>");
}

function make_submit_button($text,$class_attribute){
    return "<button type='submit'  class='" . $class_attribute ."'>" . $text . "</button>";
}

function make_bootstrap_submit_button($text){
    return make_submit_button($text,"btn btn-primary");
}

?>