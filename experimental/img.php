<?php

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
imageline($im,10,10,10,$height,$black);


header("Content-Type: image/png");
imagepng($im);
imagedestroy($im);

?>