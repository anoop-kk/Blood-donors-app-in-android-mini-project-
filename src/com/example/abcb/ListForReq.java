package com.example.abcb;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListForReq extends BaseAdapter{
	Context context;
	private final String message[];
	private final String date[];
	private final String hosp[];
	public ListForReq(Context context,String message[],String date[],String hosp[]) {
		this.context=context;
		this.hosp=hosp;
		this.message=message;
		this.date=date;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return hosp.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return hosp[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView;
		if(convertView==null){
			gridView=new View(context);
			gridView=inflater.inflate(R.layout.list_adapt_for_req,null);
		}else{
			gridView=convertView;
		}
		TextView t1=(TextView)gridView.findViewById(R.id.textView1);
		t1.setText(message[position]);
		t1.setTextColor(Color.RED);
		TextView t2=(TextView)gridView.findViewById(R.id.textView3);
		t2.setText(date[position]);
		t2.setTextColor(Color.BLACK);
		TextView t3=(TextView)gridView.findViewById(R.id.textView5);
		t3.setText(hosp[position]);
		t3.setTextColor(Color.BLUE);
		return gridView;
	}

}
