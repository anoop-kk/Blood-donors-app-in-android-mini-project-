package com.example.abcb;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchDonor extends BaseActivity implements OnItemSelectedListener {
	
	static String METHOD="PanchSer";
	static String SOAP="http://abcb/PanchSer";
	static String METHOD2="DistSer";
	static String SOAP2="http://abcb/DistSer";
	SharedPreferences sh;
	Button btn;
	Spinner bld;
	Spinner panch;
	Spinner dist;
	String[] bgroup={"A +ve","A -ve","B +ve","B -ve","AB +ve","AB -ve","O +ve","O -ve"};
	String[] panchget;
	String[] distget;
	String flag1 ="1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_search_donor);
		getLayoutInflater().inflate(R.layout.activity_search_donor,frameLayout);
		btn=(Button)findViewById(R.id.button1);
		bld=(Spinner)findViewById(R.id.spinner1);
		panch=(Spinner)findViewById(R.id.spinner3);
		dist=(Spinner)findViewById(R.id.spinner2);
		sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		bld.setAdapter(new PanchAdapt(getApplicationContext(), bgroup));
		//dist.setAdapter(new PanchAdapt(getApplicationContext(), distget));
		distSet();
		dist.setOnItemSelectedListener(this);
		btn.setOnClickListener(new View.OnClickListener() {
		@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String bd = bld.getSelectedItem().toString();
				String pan = panch.getSelectedItem().toString();
				String dis = dist.getSelectedItem().toString();
				Toast.makeText(getApplicationContext(),bd+"BLOOD GROUP IN"+pan,Toast.LENGTH_LONG).show();
				Intent intent=new Intent(getApplicationContext(),DonorList.class);
				Editor ed = sh.edit();
				ed.putString("blood",bd);
				ed.putString("pan",pan);
				ed.putString("dis",dis);
				ed.commit();
				startActivity(intent);
			}
		});
		/*ArrayAdapter<String> ad = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,bgroup);
		bld.setAdapter(ad);*/
		
		
		
	}

	private void distSet() {
		// TODO Auto-generated method stub
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
		}
		
		SoapObject request=new SoapObject(UserView.NAMESPACE,METHOD2);
		//request.addProperty("dist",distget[arg2]);
		SoapSerializationEnvelope sop=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		sop.setOutputSoapObject(request);
		HttpTransportSE htse = new HttpTransportSE(UserView.URL);
		Object ob = null;
		try {
			htse.call(SOAP,sop);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (XmlPullParserException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			ob = sop.getResponse();
		} catch (SoapFault e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		if(ob!=null){
			
			String val=ob.toString();
			if(val.equalsIgnoreCase("anytype{}")){
				//Toast.makeText(getApplicationContext(), val,Toast.LENGTH_SHORT).show();
			}
			else{
			String[] it = val.split("#");
			distget=new String[it.length];
			//ar1=new String[it.length];
			for(int i=0;i<it.length;i++){
			distget[i] =it[i];
			dist.setAdapter(new PanchAdapt(getApplicationContext(),distget));
			}
				}
			//ar1[0]=it[1];
			/*ArrayAdapter<String> ad1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,panchget);
			panch.setAdapter(ad1);*/
		}
		else
		{
			Toast.makeText(getApplicationContext(),"id not found", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_donor, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		/* TODO getting selected district and sending into server and gets locations on the basis of selected item
		 * table donor_list on the web server
		 * */
		//Toast.makeText(getApplicationContext(),distget[arg2],Toast.LENGTH_SHORT).show();
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
		}
		
		SoapObject request=new SoapObject(UserView.NAMESPACE,METHOD);
		request.addProperty("dist",distget[arg2]);
		SoapSerializationEnvelope sop=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		sop.setOutputSoapObject(request);
		HttpTransportSE htse = new HttpTransportSE(UserView.URL);
		Object ob = null;
		try {
			htse.call(SOAP,sop);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (XmlPullParserException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			ob = sop.getResponse();
		} catch (SoapFault e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		if(ob!=null){
			
			String val=ob.toString();
			if(val.equalsIgnoreCase("anytype{}")){
				//Toast.makeText(getApplicationContext(), val,Toast.LENGTH_SHORT).show();
			}
			else{
			String[] it = val.split("#");
			panchget=new String[it.length];
			//ar1=new String[it.length];
			for(int i=0;i<it.length;i++){
			panchget[i] =it[i];
			panch.setAdapter(new PanchAdapt(getApplicationContext(),panchget));
			}
				}
			//ar1[0]=it[1];
			/*ArrayAdapter<String> ad1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,panchget);
			panch.setAdapter(ad1);*/
		}
		else
		{
			Toast.makeText(getApplicationContext(),"id not found", Toast.LENGTH_SHORT).show();
		}
			
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
