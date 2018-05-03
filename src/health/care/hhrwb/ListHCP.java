package health.care.hhrwb;

public class ListHCP {

	String Backup_Id, First_Name, Middle_Name, Last_Name;
	int Gender_Code, Role_Code;

	// Set and Get method for Backup_Id
	public void setBackupId(String Backup_Id) {
		this.Backup_Id = Backup_Id;
	}

	public String getBackupId() {
		return Backup_Id;
	}

	// Set and Get method for First_Name
	public void setFirstName(String First_Name) {
		this.First_Name = First_Name;
	}

	public String getFirstName() {
		return First_Name;
	}

	// Set and Get method for Middle_Name
	public void setMiddleName(String Middle_Name) {
		this.Middle_Name = Middle_Name;
	}

	public String getMiddleName() {
		return Middle_Name;
	}

	// Set and Get method for Last_Name
	public void setLastName(String Last_Name) {
		this.Last_Name = Last_Name;
	}

	public String getLastName() {
		return Last_Name;
	}

	// Set and Get method for Gender
	public void setGender(int Gender_Code) {
		this.Gender_Code = Gender_Code;
	}
	public int getGender()
	{
		return Gender_Code;
	}
	
	// Set and Get method for Role
	public void setRole(int Role_Code) {
		this.Role_Code = Role_Code;
	}
	public int getRole()
	{
		return Role_Code;
	}
}
