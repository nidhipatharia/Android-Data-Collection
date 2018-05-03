<?php
include_once './db_functions.php';
//echo "hi";
//Create Object for DB_Functions clas
$db = new DB_Functions(); 
//Get JSON posted by Android Application
$json = $_POST["usersJSON"];
//Remove Slashes
if (get_magic_quotes_gpc()){
$json = stripslashes($json);
}
//Decode JSON into an Array
$data = json_decode($json);
//Util arrays to create response JSON
$a=array();
$b=array();
//Loop through an Array and insert data read from JSON into MySQL DB
for($i=0; $i<count($data) ; $i++)
{
//Store User into MySQL DB
$res = $db->storeIFHCP($data[$i]->Backup_Id, $data[$i]->Practicing_Since,
                      $data[$i]->Practicing_Location, $data[$i]->Prescribing_Method, $data[$i]->Mode_Of_Service, 
					  $data[$i]->Vehicle, $data[$i]->Vehicle_Other);
					  
					 
    //Based on inserttion, create JSON response
    if($res){
        $b["Backup_Id"] = $data[$i]->Backup_Id; 
		$b["Practicing_Since"] = $data[$i]->Practicing_Since;
		$b["Practicing_Location"] = $data[$i]->Practicing_Location;
		$b["Prescribing_Method"] =  $data[$i]->Prescribing_Method;
 		$b["Mode_Of_Service"] = $data[$i]->Mode_Of_Service;
		$b["Vehicle"] = $data[$i]->Vehicle;
 		$b["Vehicle_Other"] = $data[$i]->Vehicle_Other;
 		$b["Update_Status"] = 'yes';
		array_push($a,$b);
    }
	else
	{
		$b["Backup_Id"] = $data[$i]->Backup_Id; 
		$b["Practicing_Since"] = $data[$i]->Practicing_Since;
		$b["Practicing_Location"] = $data[$i]->Practicing_Location;
		$b["Prescribing_Method"] =  $data[$i]->Prescribing_Method;
 		$b["Mode_Of_Service"] = $data[$i]->Mode_Of_Service;
		$b["Vehicle"] = $data[$i]->Vehicle;
 		$b["Vehicle_Other"] = $data[$i]->Vehicle_Other;
 		$b["Update_Status"] = 'no';
        array_push($a,$b);
	}
}
//Post JSON response back to Android Application
echo json_encode($a);
?>