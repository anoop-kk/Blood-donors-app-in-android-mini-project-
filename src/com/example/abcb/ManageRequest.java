package com.example.abcb;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ManageRequest extends BaseActivity {
	static String METHOD="getRequests";
	static String SOAP="http://abcb/getRequests";
	String[] hosp;
	String[] date;
	String[] msg;
	String[] reqId;
	ListView li;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_manage_request);
		getLayoutInflater().inflate(R.layout.activity_manage_request,frameLayout);
		li=(ListView)findViewById(R.id.listView1);
		getFromWeb();
		
		getListdata();
	
	}

	private void getFromWeb() {
		// TODO Auto-generated method stub
		if(android.os.Build.VERSION.SDK_INT>9){
			StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		SoapObject request=new SoapObject(UserView.NAMESPACE,METHOD);
		SoapSerializationEnvelope evelop=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		request.addProperty("uid",SignUpPage.uid);
		evelop.setOutputSoapObject(request);
		HttpTransportSE htSe = new HttpTransportSE(UserView.URL);
		try {
			htSe.call(SOAP, evelop);
			Object ob=null;
			ob=evelop.getResponse();
			if(ob!=null){
				String data=ob.toString();
				if(data.equalsIgnoreCase("anytype{}")){
					Toast.makeText(getApplicationContext(), "Sorry you have no request", Toast.LENGTH_SHORT);
				}
				else{
					String []dataArr1=data.split("@");
					if(dataArr1.length>0){
						hosp=new String[dataArr1.length];
						date=new String[dataArr1.length];
						msg=new String[dataArr1.length];
						reqId=new String[dataArr1.length];
						int m=0;
						for(int i=0;i<dataArr1.length;i++,m++){
							String[]datAr2=dataArr1[i].split("#");
								msg[m]=datAr2[0];
								date[m]=datAr2[1];
								hosp[m]=datAr2[2];
								reqId[m]=datAr2[3];
						}

						li.setAdapter(new ListForReq(getApplicationContext(), msg, date, hosp));
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), e.toString(),Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	private void getListdata() {
		// TODO here gets date selected request 
	li.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			//Toast.makeText(getApplicationContext(),reqId[arg2],Toast.LENGTH_SHORT).show();
			Intent intent =new Intent(getApplicationContext(),ConfirmReq.class);
			intent.putExtra("reqId",reqId[arg2]);
			finish();
			startActivity(intent);
		}
	});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_request, menu);
		return true;
	}

}
