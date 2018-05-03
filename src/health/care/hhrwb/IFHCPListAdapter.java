package health.care.hhrwb;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IFHCPListAdapter extends BaseAdapter {

	Context context;
	ArrayList<ListHCP> fhcpList;
	TextView tv_backup_id,tv_first_name,tv_middle_name,tv_last_name,tv_gender;
	int Gender_Code;
	String Gender,Role;
	Context mContext;

	public IFHCPListAdapter(Context context, ArrayList<ListHCP> list) {

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
			convertView = inflater.inflate(R.layout.ifhcp_list_row, null);

		}
		try{
			tv_backup_id = (TextView) convertView.findViewById(R.id.tvBackup_Id);	
		tv_first_name = (TextView) convertView.findViewById(R.id.tvFirstName);
		tv_middle_name = (TextView) convertView.findViewById(R.id.tvMiddleName);
		tv_last_name = (TextView) convertView.findViewById(R.id.tvLastName);
		tv_gender = (TextView) convertView.findViewById(R.id.tvGender);
		
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
				}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return convertView;
		
	}

}
