<?php
 
class DB_Functions {
 
    private $db;
	private $Permanent_Id;
 
    //put your code here
    // constructor
    function __construct() {
        include_once './db_connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
		
	}
 
    // destructor
    function __destruct() {
 
    }
	
	function setPermanentId($Permanent_Id)
	{
		$this->Permanent_Id = $Permanent_Id;
	}
	function getPermanentId()
	{
		return $this->Permanent_Id;
	}
 
    /**
     * Storing new user
     * returns user details
     */
    public function storeIndividualMaster($Backup_Id, $Location_Id,$First_Name, $Middle_Name, $Last_Name, 
										  $Father_First_Name, $Father_Middle_Name, $Father_Last_Name, 
										  $Mother_First_Name, $Mother_Middle_Name, $Mother_Last_Name,
										  $Date_Of_Birth, $Gender, $Marital_Status, $Mobile_Number, $Alternate_Phone_Number,
										  $Email_Id, $Pan_Card, $Voter_Id, $Aadhar_Card, $Type_Of_Service, $Employment_Type,
										  $Data_Entered_On, $Data_Entered_By, $photo) {
											 
			$Sequential_Number = $this->generatePermanentId($Location_Id);	
			$Permanent_Id = $this->getPermanentId();
			$pic = $this->WriteToImageAndGetData($photo,"Output.png");
									  
        // Insert individual details into database
		$sql = "INSERT INTO individual_master (Backup_Id, Sequential_Number, Permanent_Id, Location_Id,
										  First_Name, Middle_Name, Last_Name, 
										  Father_First_Name, Father_Middle_Name, Father_Last_Name, 
										  Mother_First_Name, Mother_Middle_Name, Mother_Last_Name,
										  Date_Of_Birth, Gender, Marital_Status, Mobile_Number, Alternate_Phone_Number,
										  Email_Id, Pan_Card, Voter_Id, Aadhar_Card, Type_Of_Service, Employment_Type,
										  Data_Entered_By, Data_Entered_On,photo) 
										  VALUES(
										  '$Backup_Id', $Sequential_Number,'$Permanent_Id', '$Location_Id',
										  '$First_Name', '$Middle_Name', '$Last_Name', 
										  '$Father_First_Name', '$Father_Middle_Name', '$Father_Last_Name', 
										  '$Mother_First_Name', '$Mother_Middle_Name', '$Mother_Last_Name',
										  '$Date_Of_Birth', $Gender, $Marital_Status, '$Mobile_Number', '$Alternate_Phone_Number',
										  '$Email_Id', '$Pan_Card', '$Voter_Id', '$Aadhar_Card', $Type_Of_Service, 
										  $Employment_Type, '$Data_Entered_By', '$Data_Entered_On', '$pic')";
      // echo "<br><br>SQL: " . $sql;
	    $result = mysql_query($sql);
 
        if ($result) {
            return true;
        } else {
            if( mysql_errno() == 1062) {
                // Duplicate key - Primary Key Violation
                return true;
            } else {
                // For other errors
                return false;
            }            
        }
    }
	
	public function WriteToImageAndGetData($base64_string, $File) {
	  //Write to file
	  $ifp = fopen($File, "wb");
	  //$data = explode(',', $base64_string); //Split Base64 string
	  fwrite($ifp, base64_decode($base64_string)); //Decode Base64 string
	  fclose($ifp);
	  
	  //Read from file
	  $fp = fopen($File, 'r');
	  $data = fread($fp, filesize($File)); //Read file contents
	  $data = addslashes($data);
	  fclose($fp);
	  return $data;
}
    
	public function generatePermanentId($Location_Id)
	 {
		 $sql = "SELECT MAX( Sequential_Number ) as Max_Number FROM individual_master where Location_Id = '$Location_Id'";
		// ////echo $sql;
		 $results = mysql_query($sql);
		 while($result = mysql_fetch_array($results))
		 {
			 if(is_null($result['Max_Number']))
			 {
				$Sequential_Number = 1000000;
			 }
	  	 	else
		 	{
					$Sequential_Number = $result['Max_Number'] + 1;
			 }
		 }
		$Permanent_Id = $Location_Id . $Sequential_Number;
		$this->setPermanentId($Permanent_Id);
		
		//////echo "Permanent_Id: " . $this->Permanent_Id;
		return $Sequential_Number;
	 }
	 
	 

    public function storeLocationOfIndividual($Backup_Id, $Location_Id, $Permanent_Address, $Current_Address, 
											  $Data_Entered_From_Latitude, $Data_Entered_From_Longitude, $Data_Entered_From_Address) {
				
											 
			$Permanent_Id = $this->getPermanentId();
			
		// Insert individual details into database
		$sql = "INSERT INTO location_of_individual(Backup_Id, Permanent_Id, Location_Id,
										  Permanent_Address, Current_Address,
										  Data_Entered_From_Latitude, Data_Entered_From_Longitude, Data_Entered_From_Address) 
										  VALUES(
										  '$Backup_Id', '$Permanent_Id', '$Location_Id',
										  '$Permanent_Address', '$Current_Address', 
										  $Data_Entered_From_Latitude, $Data_Entered_From_Longitude, '$Data_Entered_From_Address')";
       //	////echo "<br><br>SQL: " . $sql;
	    $result = mysql_query($sql);
 
        if ($result) {
            return true;
        } else {
            if( mysql_errno() == 1062) {
                // Duplicate key - Primary Key Violation
                return true;
            } else {
                // For other errors
                return false;
            }            
        }
    }
    
	public function storeFHCPGov($Backup_Id, $Employment_Type, $Employment,
								 $Role, $Practicing_Since, $Practicing_Location, $Type_Of_Service,
								 $Registration_Number, $Registering_Authority, $Registering_Authority_Other) {
											 
			$Permanent_Id = $this->getPermanentId();	
									  
        // Insert individual details into database
		$sql = "INSERT INTO employment_government(Backup_Id, Permanent_Id, Employment_Type, Employment,
								 Role, Practicing_Since, Practicing_Location, Type_Of_Service,
								 Registration_Number, Registering_Authority, Registering_Authority_Other) 
								 VALUES(
								 '$Backup_Id', '$Permanent_Id', $Employment_Type, $Employment,
								 $Role, '$Practicing_Since', $Practicing_Location, $Type_Of_Service,
								 '$Registration_Number', $Registering_Authority, '$Registering_Authority_Other')";
       //	////echo "<br><br>SQL: " . $sql;
	    $result = mysql_query($sql);
 
        if ($result) {
            return true;
        } else {
            if( mysql_errno() == 1062) {
                // Duplicate key - Primary Key Violation
                return true;
            } else {
                // For other errors
                return false;
            }            
        }
    }
    
	public function storeFHCPNonGov($Backup_Id,	$Employment_Type, $Role,
				   				    $Practicing_Since, $Practicing_Location, $Type_Of_Service, $Registration_Number,
								    $Registering_Authority, $Registering_Authority_Other) {
											 
			$Permanent_Id = $this->getPermanentId();	
									  
        // Insert individual details into database
		$sql = "INSERT INTO employment_non_government
									(Backup_Id, Permanent_Id, Employment_Type, Role, 
									 Practicing_Since, Practicing_Location, Type_Of_Service, 
									 Registration_Number, Registering_Authority, Registering_Authority_Other) 
									 VALUES(
									 '$Backup_Id', '$Permanent_Id', $Employment_Type, $Role, 
									 '$Practicing_Since', $Practicing_Location, $Type_Of_Service, 	
									 '$Registration_Number', $Registering_Authority, '$Registering_Authority_Other')";
       //	////echo "<br><br>SQL: " . $sql;
	    $result = mysql_query($sql);
 
        if ($result) {
            return true;
        } else {
            if( mysql_errno() == 1062) {
                // Duplicate key - Primary Key Violation
                return true;
            } else {
                // For other errors
                return false;
            }            
        }
    }
    
	public function storeIFHCP($Backup_Id, $Practicing_Since, $Practicing_Location, 
							   $Prescribing_Method, $Mode_Of_Service, $Vehicle, $Vehicle_Other) {
											 
			$Permanent_Id = $this->getPermanentId();
									  
        // Insert individual details into database
		$sql = "INSERT INTO informal_health_care_providers(
										  Backup_Id, Permanent_Id, Practicing_Since, Practicing_Location, 
							              Prescribing_Method, Mode_Of_Service, Vehicle, Vehicle_Other) 
										  VALUES(
										  '$Backup_Id', '$Permanent_Id', '$Practicing_Since', $Practicing_Location, 
							              $Prescribing_Method, $Mode_Of_Service, $Vehicle, '$Vehicle_Other')";
       echo "\n\nSQL: " . $sql;
	    $result = mysql_query($sql);
 
        if ($result) {
            return true;
        } else {
            if( mysql_errno() == 1062) {
                // Duplicate key - Primary Key Violation
                return true;
            } else {
                // For other errors
                return false;
            }            
        }
    }
    
	public function storeQualification($Backup_Id, $Qualification, $Qualification_Location) {
											 
			$Permanent_Id = $this->getPermanentId();
									  
        // Insert individual details into database
		$sql = "INSERT INTO qualification (Backup_Id, Permanent_Id, Qualification, Qualification_Location) 
										  VALUES(
										  '$Backup_Id', '$Permanent_Id', '$Qualification', $Qualification_Location)";
      // echo "<br><br>SQL: " . $sql;
	    $result = mysql_query($sql);
 
        if ($result) {
            return true;
        } else {
            if( mysql_errno() == 1062) {
                // Duplicate key - Primary Key Violation
                return true;
            } else {
                // For other errors
                return false;
            }            
        }
    }
public function getAllUsers() {
        $result = mysql_query("select * FROM qualification");
        return $result;
    }   
}
 
?>