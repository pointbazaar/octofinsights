<?php

include_once($_SERVER["DOCUMENT_ROOT"] . "/base.php");

function include_once_in_folder($folder){
    $files = glob($folder . "/*.php");
    foreach ($files as $file){
        include_once($file);
    }
}

//order is important

//first load interfaces
include_once($absolute_file_url . "/model/IEntity.php");

//base entity is needed first
include_once($absolute_file_url . "/model/Transaction_Base_Entity.php");

include_once_in_folder($absolute_file_url . "/model");

include_once($absolute_file_url . "/services/IEntityService.php");
include_once_in_folder($absolute_file_url . "/services");