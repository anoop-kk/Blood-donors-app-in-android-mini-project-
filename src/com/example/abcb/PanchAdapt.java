package com.example.abcb;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PanchAdapt extends BaseAdapter{
	public Context context;
	private final String[] panNames;
	public PanchAdapt(Context context,String[] panNames) {
		this.context=context;
		this.panNames=panNames;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return panNames.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return panNames[position];
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
			gridView=inflater.inflate(R.layout.panchayath_list,null);
		}else{
			gridView=convertView;
		}
		TextView t1=(TextView)gridView.findViewById(R.id.textView1);
		t1.setText(panNames[position]);
		t1.setTextColor(Color.WHITE);
		return gridView;
	}
}
