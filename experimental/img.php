<?php

include_once("../base.php");
include_once($absolute_file_url . "/services/sales_service.php");
include_once($absolute_file_url . "/authentication/is_authenticated_otherwise_redirect.php");

$my_sales = get_all_sales();



$width = 600;
$height= 200;

$im = imagecreate($width,$height);
$grau = imagecolorallocate($im, 192,192,192);
$black = imagecolorallocate($im,0,0,0);
$white = imagecolorallocate($im,255,255,255);

imagefill($im,0,0,$white);

//make the x axis
imageline($im,10,$height/2,$width,$height/2,$black);
//make the y axis
//imageline($im,10,10,10,$height,$black);

//title
imagestring($im,3,$width/2,10,"Total Business Value over Time",$black);


//make the data points here
$total_earnings = 0;
$pos_x=20;

$step_size_x=30;

$position_1 = array(10,$height/2);
$position_2 = array(20,$height/2);

$sales_count = sizeof($my_sales);

foreach ($my_sales as $sale){

    $total_earnings += $sale->price_of_sale;

    $max_height = $height/2;
    $point_height = - (as_fraction($total_earnings,get_total_sales_price()) * ($height/2) );

    $position_2=array($pos_x,$point_height+($height/2));
    imageline($im,$position_1[0],$position_1[1],$position_2[0],$position_2[1],$black);

    $position_1=$position_2;
    $pos_x+=$step_size_x;
}


//top value the business ever was a
imageline($im,50,1,$width,0,$black);
imagestring($im,10,4,0,get_total_sales_price(),$black);

function as_fraction($value,$max){
    return $value/$max;
}


header("Content-Type: image/png");
imagepng($im);
imagedestroy($im);

?>