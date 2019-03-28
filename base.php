<?php



$baseurl="";

$absolute_file_url = $_SERVER["DOCUMENT_ROOT"] . $baseurl;

include_once($absolute_file_url . "/include_many.php");

//https://www.chartjs.org/samples/latest/

function tag($tag){
    return "<" . $tag . ">";
}

function m_tag($tag, $content){
    return tag($tag) . $content . tag("/" . $tag);
}

function a($href,$content){
    return "<a href='" . $href . "'>" . $content . "</a>";
}

function strong($content){
    return m_tag("strong",$content);
}

function make_td($contents){
    return m_tag("td",$contents);
}

function echo_td($contents){
    echo make_td($contents);
}


function div($contents){
    return m_tag("div",$contents);
}

function form($url, $contents)
{
    return "<form action='" . $url . "' method='post'>" . $contents . "</form>";
}

function make_hidden_input_number($name,$placeholder,$value){
    return input_number($name,$placeholder,$value,true);
}

function tr($contents){
    return "<tr>". $contents . "</tr>";
}

function th($contents){
    return "<th scope='col'>" . $contents . "</th>";
}

function thead($content){
    return "<thead>" . $content . "</thead>";
}

function tbody($content){
    return "<tbody>" . $content . "</tbody>";
}

function table_simple($class_attr, $contents){
    return "<table class='" . $class_attr . "'>"
        . $contents
        . "</table>";
}

function table($class_attr, $headers, $contents){
    $header_html = "";
    for($i=0;$i<sizeof($headers);$i++){
        $header_html .= th($headers[$i]);
    }
    return table_simple($class_attr, thead(tr($header_html)) . tbody($contents));
}

function hr(){
    return "<hr>";
}

function input_number($name, $placeholder, $value, $is_hidden){
    $type_str = " type='number' ";
    $name_str = " name='" . $name . "' ";
    $hidden_str = ($is_hidden)?" hidden ":" ";
    $placeholder_str = " placeholder='" . $placeholder . "' ";
    $value_str = " value='" . $value . "' ";
    return "<input ". $type_str . $name_str . $hidden_str . $placeholder_str . $value_str . ">";
}

function select($options, $selected_option, $name){
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

function submit_button($text, $class_attribute){
    return "<button type='submit'  class='" . $class_attribute ."'>" . $text . "</button>";
}

function bootstrap_submit_button($text){
    return submit_button($text,"btn btn-primary");
}

?>