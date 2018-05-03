package health.care.hhrwb;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FHCPListAdapter extends BaseAdapter {

	Context context;
	ArrayList<ListHCP> fhcpList;
	TextView tv_backup_id,tv_first_name,tv_middle_name,tv_last_name,tv_gender,tv_role;
	int Gender_Code,Role_Code;
	String Gender,Role;
	Context mContext;

	public FHCPListAdapter(Context context, ArrayList<ListHCP> list) {

		this.context = context;
		fhcpList = list;
	}
	
	@Override
	public int getCount() {

		return fhcpList.size();
	}

	@Override
	public Object getItem(int position) {

		return fhcpList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListHCP fhcpListItems = fhcpList.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fhcp_list_row, null);

		}
		try{
			tv_backup_id = (TextView) convertView.findViewById(R.id.tvBackup_Id);
		tv_first_name = (TextView) convertView.findViewById(R.id.tvFirstName);
		tv_middle_name = (TextView) convertView.findViewById(R.id.tvMiddleName);
		tv_last_name = (TextView) convertView.findViewById(R.id.tvLastName);
		tv_gender = (TextView) convertView.findViewById(R.id.tvGender);
		tv_role = (TextView) convertView.findViewById(R.id.tvRole);
		
		tv_backup_id.setText(fhcpListItems.getBackupId());
		tv_first_name.setText(fhcpListItems.getFirstName());
		
		tv_middle_name.setText(fhcpListItems.getMiddleName());
		
		tv_last_name.setText(fhcpListItems.getLastName());
		
		Gender_Code = fhcpListItems.getGender();
		switch (Gender_Code) {
		case 1:
			Gender = this.context.getResources().getString(
					R.string.male);
			break;
		case 2:
			Gender = this.context.getResources().getString(
					R.string.female);
			break;
		case 3: 
			Gender = this.context.getResources().getString(
					R.string.others);		
			break;
		default:
			break;
		}
		tv_gender.setText(Gender);
		Role_Code = fhcpListItems.getRole();
		switch (Role_Code) {
		case 1:
			Role = this.context.getResources().getString(
					R.string.doctor);
			break;
		case 2:
			Role = this.context.getResources().getString(
					R.string.nurse);
			break;
		case 3:
			Role = this.context.getResources().getString(
					R.string.lab_technician);
			break;
		case 4:
			Role = this.context.getResources().getString(
					R.string.xray_technician);
			break;
		case 5:
			Role = this.context.getResources().getString(
					R.string.pharmacist);
			break;
		case 6:
			Role = this.context.getResources().getString(
					R.string.physiotherapist);
			break;
		case 7:
			Role = this.context.getResources().getString(
					R.string.other_technical_services);
			break;
		case 8:
			Role = this.context.getResources()
					.getString(R.string.ha);
			break;
		case 9:
			Role = this.context.getResources().getString(
					R.string.asha);
			break;

		default:
			break;
		}

		tv_role.setText(Role);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return convertView;
		
	}

}
