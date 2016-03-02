package com.example.abcb;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
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
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DonorList extends BaseActivity {
	ListView li;
	//static String URL="http://169.254.164.207:8080/Abcb/WebAbCb?WSDL";
	//static String NAMESPACE="http://abcb/";
	static String METHOD="DonorSer";
	static String SOAP="http://abcb/DonorSer";
	String[] donorname;
	String[] donor_id;
	SharedPreferences sh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_donor_list);
		getLayoutInflater().inflate(R.layout.activity_donor_list,frameLayout);
		li=(ListView)findViewById(R.id.listView1);
		sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		String bld=sh.getString("blood", "");
		String pan=sh.getString("pan", "");
		String dis=sh.getString("dis", "");
		Toast.makeText(getApplicationContext(), bld+pan+dis,Toast.LENGTH_SHORT).show();
		
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
	
		SoapObject request = new SoapObject(UserView.NAMESPACE,METHOD);
		request.addProperty("blood", bld);
		request.addPropertyIfValue("panch", pan);
		request.addProperty("dist", dis);
		SoapSerializationEnvelope envelop=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelop.setOutputSoapObject(request);
		HttpTransportSE htse= new HttpTransportSE(UserView.URL);
		Object ob=null;
		try {
			htse.call(SOAP, envelop);
			
			ob=envelop.getResponse();
			/*if(ob!=null){
				String val=ob.toString();
				String[] get = val.split("#");
				donorget=new String[get.length];
				for(int i=0;i<get.length;i++){
					donorget[i]=get[i];
				}
			}*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ob!=null){
			String val=ob.toString();
			if(val.equalsIgnoreCase("anytype{}")){
				Toast.makeText(getApplicationContext(), "Sorry you have no request", Toast.LENGTH_SHORT).show();
			}
			else{
			String[] it = val.split("@");
			donorname=new String[it.length];
			donor_id=new String[it.length];
			//ar1=new String[it.length];
			int m=0;
			for(int i=0;i<it.length;i++,m++){
				String[]it2=it[i].split("#");
			donorname[m] =it2[0];
			donor_id[m]=it2[1];
			}
			li.setAdapter(new PanchAdapt(getApplicationContext(), donorname));
			}
		}
		 
		/*ArrayAdapter<String> ad = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item);
		li.setAdapter(ad);*/
		 li.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String get = donor_id[arg2];
				
				if(!get.equals("")){
					//Toast.makeText(getApplicationContext(),get, Toast.LENGTH_SHORT).show();
						Intent intent= new Intent(getApplicationContext(), DonoDetails.class);			
						intent.putExtra("donor_id",get);
						startActivity(intent);
					}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.donor_list, menu);
		return true;
	}

}
