<?php
include_once './db_functions.php';
//echo "hi";
//Create Object for DB_Functions clas
$db = new DB_Functions(); 
//Get JSON posted by Android Application
$json_indiviudal = $_POST["IndividualJSON"];
$json_location = $_POST["LocationJSON"];
$json_fhcp_gov = $_POST["FHCPGovJSON"];
$json_fhcp_non_gov = $_POST["FHCPNonGovJSON"];
$json_ifhcp = $_POST["IFHCPJSON"];
$json_qualification = $_POST["QualificationJSON"];
//Remove Slashes
if (get_magic_quotes_gpc()){
$json_indiviudal = stripslashes($json_indiviudal);
}
//Decode JSON into an Array
$data = json_decode($json_indiviudal);
//Util arrays to create response JSON
$a=array();
$b=array();
//Loop through an Array and insert data read from JSON into MySQL DB
for($i=0; $i<count($data) ; $i++)
{
//Store User into MySQL DB
$res = $db->storeIndividualMaster($data[$i]->Backup_Id, $data[$i]->Location_Id,
                      $data[$i]->First_Name, $data[$i]->Middle_Name, $data[$i]->Last_Name, 
					  $data[$i]->Father_First_Name, $data[$i]->Father_Middle_Name, $data[$i]->Father_Last_Name, 
                      $data[$i]->Mother_First_Name, $data[$i]->Mother_Middle_Name, $data[$i]->Mother_Last_Name,
		              $data[$i]->Date_Of_Birth, $data[$i]->Gender, $data[$i]->Marital_Status, $data[$i]->Mobile_Number, 
					  $data[$i]->Alternate_Phone_Number, $data[$i]->Email_Id, 
					  $data[$i]->Pan_Card, $data[$i]->Voter_Id, $data[$i]->Aadhar_Card, 
					  $data[$i]->Type_Of_Service, $data[$i]->Employment_Type,
					  $data[$i]->Data_Entered_On, $data[$i]->Data_Entered_By, $data[$i]->photo);
					 
    //Based on inserttion, create JSON response
    if($res){
        $b["Backup_Id"] = $data[$i]->Backup_Id; 
		$b["Location_Id"] = $data[$i]->Location_Id;
		$b["First_Name"] = $data[$i]->First_Name;
		$b["Middle_Name"] =  $data[$i]->Middle_Name;
 		$b["Last_Name"] = $data[$i]->Last_Name;
		$b["Father_First_Name"] = $data[$i]->Father_First_Name;
 		$b["Father_Middle_Name"] = $data[$i]->Father_Middle_Name;
 		$b["Father_Last_Name"] = $data[$i]->Father_Last_Name;
 		$b["Mother_First_Name"] = $data[$i]->Mother_First_Name;
 		$b["Mother_Middle_Name"] = $data[$i]->Mother_Middle_Name;
		$b["Mother_Last_Name"] = $data[$i]->Mother_Last_Name;
		$b["Date_Of_Birth"] = $data[$i]->Date_Of_Birth;
 		$b["Gender"] = $data[$i]->Gender;
 		$b["Marital_Status"] = $data[$i]->Marital_Status;
 		$b["Mobile_Number"] = $data[$i]->Mobile_Number;
		$b["Alternate_Phone_Number"] = $data[$i]->Alternate_Phone_Number;
 		$b["Email_Id"] = $data[$i]->Email_Id;
 		$b["Pan_Card"] = $data[$i]->Pan_Card;
 		$b["Voter_Id"] = $data[$i]->Voter_Id;
 		$b["Aadhar_Card"] = $data[$i]->Aadhar_Card;
 		$b["Type_Of_Service"] = $data[$i]->Type_Of_Service;
 		$b["Employment_Type"] = $data[$i]->Employment_Type;
		$b["Data_Entered_On"] = $data[$i]->Data_Entered_On;
 		$b["Data_Entered_By"] = $data[$i]->Data_Entered_By;
		$b["photo"] = $data[$i]->photo;
		$b["Update_Status"] = 'yes';
		////echo "<br><br> Value of $B: " . $b;
        array_push($a,$b);
    }
	else
	{
		$b["Backup_Id"] = $data[$i]->Backup_Id; 
		$b["Location_Id"] = $data[$i]->Location_Id;
		$b["First_Name"] = $data[$i]->First_Name;
		$b["Middle_Name"] =  $data[$i]->Middle_Name;
 		$b["Last_Name"] = $data[$i]->Last_Name;
		$b["Father_First_Name"] = $data[$i]->Father_First_Name;
 		$b["Father_Middle_Name"] = $data[$i]->Father_Middle_Name;
 		$b["Father_Last_Name"] = $data[$i]->Father_Last_Name;
 		$b["Mother_First_Name"] = $data[$i]->Mother_First_Name;
 		$b["Mother_Middle_Name"] = $data[$i]->Mother_Middle_Name;
		$b["Mother_Last_Name"] = $data[$i]->Mother_Last_Name;
		$b["Date_Of_Birth"] = $data[$i]->Date_Of_Birth;
 		$b["Gender"] = $data[$i]->Gender;
 		$b["Marital_Status"] = $data[$i]->Marital_Status;
 		$b["Mobile_Number"] = $data[$i]->Mobile_Number;
		$b["Alternate_Phone_Number"] = $data[$i]->Alternate_Phone_Number;
 		$b["Email_Id"] = $data[$i]->Email_Id;
 		$b["Pan_Card"] = $data[$i]->Pan_Card;
 		$b["Voter_Id"] = $data[$i]->Voter_Id;
 		$b["Aadhar_Card"] = $data[$i]->Aadhar_Card;
 		$b["Type_Of_Service"] = $data[$i]->Type_Of_Service;
 		$b["Employment_Type"] = $data[$i]->Employment_Type;
		$b["Data_Entered_On"] = $data[$i]->Data_Entered_On;
 		$b["Data_Entered_By"] = $data[$i]->Data_Entered_By;
		$b["photo"] = $data[$i]->photo;
		$b["Update_Status"] = 'no';
        array_push($a,$b);
	}
}
//Post JSON response back to Android Application
echo json_encode($a);

if (get_magic_quotes_gpc()){
$json_location = stripslashes($json_location);
}
//Decode JSON into an Array
$data = json_decode($json_location);
//Util arrays to create response JSON
$a=array();
$b=array();

//Loop through an Array and insert data read from JSON into MySQL DB
for($i=0; $i<count($data) ; $i++)
{
//Store User into MySQL DB
$res = $db->storeLocationOfIndividual($data[$i]->Backup_Id, $data[$i]->Location_Id,
                      $data[$i]->Permanent_Address, $data[$i]->Current_Address, $data[$i]->Data_Entered_From_Latitude, 
					  $data[$i]->Data_Entered_From_Longitude, $data[$i]->Data_Entered_From_Address);
					  
					  
    //Based on inserttion, create JSON response
    if($res){
        $b["Backup_Id"] = $data[$i]->Backup_Id; 
		$b["Location_Id"] = $data[$i]->Location_Id;
		$b["Permanent_Address"] = $data[$i]->Permanent_Address;
		$b["Current_Address"] =  $data[$i]->Current_Address;
 		$b["Data_Entered_From_Latitude"] = $data[$i]->Data_Entered_From_Latitude;
		$b["Data_Entered_From_Longitude"] = $data[$i]->Data_Entered_From_Longitude;
 		$b["Data_Entered_From_Address"] = $data[$i]->Data_Entered_From_Address;
 		$b["Update_Status"] = 'yes';
		array_push($a,$b);
    }
	else
	{
		$b["Backup_Id"] = $data[$i]->Backup_Id; 
		$b["Location_Id"] = $data[$i]->Location_Id;
		$b["Permanent_Address"] = $data[$i]->Permanent_Address;
		$b["Current_Address"] =  $data[$i]->Current_Address;
 		$b["Data_Entered_From_Latitude"] = $data[$i]->Data_Entered_From_Latitude;
		$b["Data_Entered_From_Longitude"] = $data[$i]->Data_Entered_From_Longitude;
 		$b["Data_Entered_From_Address"] = $data[$i]->Data_Entered_From_Address;
 		$b["Update_Status"] = 'no';
        array_push($a,$b);
	}
}
//Post JSON response back to Android Application
echo json_encode($a);


if (get_magic_quotes_gpc()){
$json_fhcp_gov = stripslashes($json_fhcp_gov);
}
//Decode JSON into an Array
$data = json_decode($json_fhcp_gov);
//Util arrays to create response JSON
$a=array();
$b=array();
//Loop through an Array and insert data read from JSON into MySQL DB
for($i=0; $i<count($data) ; $i++)
{
//Store User into MySQL DB
$res = $db->storeFHCPGov($data[$i]->Backup_Id, $data[$i]->Employment_Type,
                      $data[$i]->Employment, $data[$i]->Role, $data[$i]->Practicing_Since, 
					  $data[$i]->Practicing_Location, $data[$i]->Type_Of_Service, $data[$i]->Registration_Number, 
                      $data[$i]->Registering_Authority, $data[$i]->Registering_Authority_Other);
					  
					 
    //Based on inserttion, create JSON response
    if($res){
        $b["Backup_Id"] = $data[$i]->Backup_Id; 
		$b["Employment_Type"] = $data[$i]->Employment_Type;
		$b["Employment"] = $data[$i]->Employment;
		$b["Role"] =  $data[$i]->Role;
 		$b["Practicing_Since"] = $data[$i]->Practicing_Since;
		$b["Practicing_Location"] = $data[$i]->Practicing_Location;
 		$b["Type_Of_Service"] = $data[$i]->Type_Of_Service;
 		$b["Registration_Number"] = $data[$i]->Registration_Number;
 		$b["Registering_Authority"] = $data[$i]->Registering_Authority;
 		$b["Registering_Authority_Other"] = $data[$i]->Registering_Authority_Other;
		$b["Update_Status"] = 'yes';
		array_push($a,$b);
    }
	else
	{
		$b["Backup_Id"] = $data[$i]->Backup_Id; 
		$b["Employment_Type"] = $data[$i]->Employment_Type;
		$b["Employment"] = $data[$i]->Employment;
		$b["Role"] =  $data[$i]->Role;
 		$b["Practicing_Since"] = $data[$i]->Practicing_Since;
		$b["Practicing_Location"] = $data[$i]->Practicing_Location;
 		$b["Type_Of_Service"] = $data[$i]->Type_Of_Service;
 		$b["Registration_Number"] = $data[$i]->Registration_Number;
 		$b["Registering_Authority"] = $data[$i]->Registering_Authority;
 		$b["Registering_Authority_Other"] = $data[$i]->Registering_Authority_Other;
		$b["Update_Status"] = 'no';
        array_push($a,$b);
	}
}
//Post JSON response back to Android Application
echo json_encode($a);


if (get_magic_quotes_gpc()){
$json_fhcp_non_gov = stripslashes($json_fhcp_non_gov);
}
//Decode JSON into an Array
$data = json_decode($json_fhcp_non_gov);
//Util arrays to create response JSON
$a=array();
$b=array();
//Loop through an Array and insert data read from JSON into MySQL DB
for($i=0; $i<count($data) ; $i++)
{
//Store User into MySQL DB
$res = $db->storeFHCPNonGov($data[$i]->Backup_Id, $data[$i]->Employment_Type,
                      $data[$i]->Role, $data[$i]->Practicing_Since, 
					  $data[$i]->Practicing_Location, $data[$i]->Type_Of_Service, $data[$i]->Registration_Number, 
                      $data[$i]->Registering_Authority, $data[$i]->Registering_Authority_Other);
					  
					  
    //Based on inserttion, create JSON response
    if($res){
        $b["Backup_Id"] = $data[$i]->Backup_Id; 
		$b["Employment_Type"] = $data[$i]->Employment_Type;
		$b["Role"] =  $data[$i]->Role;
 		$b["Practicing_Since"] = $data[$i]->Practicing_Since;
		$b["Practicing_Location"] = $data[$i]->Practicing_Location;
 		$b["Type_Of_Service"] = $data[$i]->Type_Of_Service;
 		$b["Registration_Number"] = $data[$i]->Registration_Number;
 		$b["Registering_Authority"] = $data[$i]->Registering_Authority;
 		$b["Registering_Authority_Other"] = $data[$i]->Registering_Authority_Other;
		$b["Update_Status"] = 'yes';
		array_push($a,$b);
    }
	else
	{
		$b["Backup_Id"] = $data[$i]->Backup_Id; 
		$b["Employment_Type"] = $data[$i]->Employment_Type;
		$b["Role"] =  $data[$i]->Role;
 		$b["Practicing_Since"] = $data[$i]->Practicing_Since;
		$b["Practicing_Location"] = $data[$i]->Practicing_Location;
 		$b["Type_Of_Service"] = $data[$i]->Type_Of_Service;
 		$b["Registration_Number"] = $data[$i]->Registration_Number;
 		$b["Registering_Authority"] = $data[$i]->Registering_Authority;
 		$b["Registering_Authority_Other"] = $data[$i]->Registering_Authority_Other;
		$b["Update_Status"] = 'no';
        array_push($a,$b);
	}
}
//Post JSON response back to Android Application
echo json_encode($a);


if (get_magic_quotes_gpc()){
$json_ifhcp = stripslashes($json_ifhcp);
}
//Decode JSON into an Array
$data = json_decode($json_ifhcp);
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


if (get_magic_quotes_gpc()){
$json_qualification = stripslashes($json_qualification);
}
//Decode JSON into an Array
$data = json_decode($json_qualification);
//Util arrays to create response JSON
$a=array();
$b=array();
//Loop through an Array and insert data read from JSON into MySQL DB
for($i=0; $i<count($data) ; $i++)
{
//Store User into MySQL DB
$res = $db->storeQualification($data[$i]->Backup_Id, $data[$i]->Qualification, $data[$i]->Qualification_Location);

					 
    //Based on inserttion, create JSON response
    if($res){
        $b["Backup_Id"] = $data[$i]->Backup_Id; 
		$b["Qualification"] = $data[$i]->Qualification;
		$b["Qualification_Location"] = $data[$i]->Qualification_Location;
		$b["Update_Status"] = 'yes';
		array_push($a,$b);
    }
	else
	{
		$b["Backup_Id"] = $data[$i]->Backup_Id; 
		$b["Qualification"] = $data[$i]->Qualification;
		$b["Qualification_Location"] = $data[$i]->Qualification_Location;
		$b["Update_Status"] = 'no';
        array_push($a,$b);
	}
}
//Post JSON response back to Android Application
echo json_encode($a);

?>