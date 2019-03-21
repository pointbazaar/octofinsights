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
$green = imagecolorallocate($im,0,255,0);
$red   = imagecolorallocate($im,255,0,0);

imagefill($im,0,0,$white);

//make the x axis
imageline($im,10,$height/2,$width,$height/2,$black);
imagestring($im,5,0,$height/2,"0",$black);

//make the y axis
//imageline($im,10,10,10,$height,$black);

//title
imagestring($im,3,20,$height-20,"Cash Flow History",$black);


//make the data points here
$pos_x=20;

$sales_count = sizeof($my_sales);

$step_size_x=20;

$position = array(20,$height/2);

foreach ($my_sales as $sale){
    $max_height = $height/2;
    $point_height = - (as_fraction($sale->price_of_sale,get_total_sales_price()) * ($height/2) );

    $position=array($pos_x,$point_height+($height/2));
    $color_of_bar = ($sale->price_of_sale>0)?$green:$red;
    imagefilledrectangle($im,$position[0],$position[1],$position[0]+$step_size_x-2,$height/2-1,$color_of_bar);

    imagestring($im,1,$position[0],$height/2 + 10,$sale->price_of_sale,$black);

    $pos_x+=$step_size_x;
}

function as_fraction($value,$max){
    return $value/$max;
}


header("Content-Type: image/png");
imagepng($im);
imagedestroy($im);

?>