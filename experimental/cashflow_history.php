<?php

include_once("../base.php");
include_once($absolute_file_url . "/services/Transaction_Service.php");
include_once($absolute_file_url . "/authentication/is_authenticated_otherwise_redirect.php");

//error_log must be configured in php.ini
//journalctl -u php7.2-fpm.service

try{
    $my_transactions = Transaction_Service::get_all_transactions();

    //TODO: this seems to return 0, fix that
    $highest_absolute_transaction = Transaction_Service::getHighestAbsoluteTransactionValue();
}catch (Exception $exception){
    //echo($exception->getMessage());
    http_response_code(500);
}

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

//title
imagestring($im,3,20,$height-20,"Cash Flow History",$black);

$pos_x=20;

$step_size_x=20;

$position = array(20,$height/2);

foreach ($my_transactions as $transaction){
    $max_height = $height/2;
    $point_height = - (  ($transaction->getValue()/$highest_absolute_transaction) * ($height/2) );

    $position=array($pos_x,$point_height+($height/2));
    $color_of_bar = ($transaction->getValue()>0)?$green:$red;
    imagefilledrectangle($im,$position[0],$position[1],$position[0]+$step_size_x-2,$height/2-1,$color_of_bar);

    imagestring($im,1,$position[0],$height/2 + 10,$transaction->getValue(),$black);

    $pos_x+=$step_size_x;
}


header("Content-Type: image/png");
imagepng($im);
imagedestroy($im);

?>