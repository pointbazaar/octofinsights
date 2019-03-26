<?php

include_once($_SERVER["DOCUMENT_ROOT"] . "/base.php");

function include_once_in_folder($folder){
    $files = glob($folder . "/*.php");
    foreach ($files as $file){
        include_once($file);
    }
}


include_once_in_folder($absolute_file_url . "/model");
include_once_in_folder($absolute_file_url . "/services");