package com.example.abcb;

import java.io.IOException;
import java.security.PublicKey;

import javax.crypto.Mac;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DonoDetails extends BaseActivity {
	//static String URL="http://169.254.164.207:8080/Abcb/WebAbCb?WSDL";
	//static String NAMESPACE="http://abcb/";
	static String METHOD="donorDetails";
	static String SOAP="http://abcb/donorDetails";
	TextView txt;
	ListView li;
	String detail[];
	Button btn;
	String donor_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_dono_details);
		getLayoutInflater().inflate(R.layout.activity_dono_details,frameLayout);
		
		donor_id=getIntent().getStringExtra("donor_id");
		txt=(TextView)findViewById(R.id.editText1);
		li=(ListView)findViewById(R.id.listView1);
		btn=(Button)findViewById(R.id.button1);
		//Toast.makeText(getApplicationContext(),donor_id, Toast.LENGTH_SHORT).show();
		getDoorDet();
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(),"hiiiiiii",Toast.LENGTH_SHORT).show();
				Intent intent =new Intent(getApplicationContext(),SendRequest.class);
				intent.putExtra("donor_id", donor_id);
				//finish();
				startActivity(intent);
			}
		});

		
	}
	public void getDoorDet() {
		
		String donor_id = getIntent().getStringExtra("donor_id");
		//Toast.makeText(getApplicationContext(), donor_id, Toast.LENGTH_SHORT).show();
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		
		//txt.setText(donor);
		SoapObject request = new SoapObject(UserView.NAMESPACE,METHOD);
		SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		request.addProperty("donor_id",donor_id);
		envelop.setOutputSoapObject(request);
		HttpTransportSE htse = new HttpTransportSE(UserView.URL);
		Object ob=null;
		try {
			htse.call(SOAP, envelop);
			
			ob=envelop.getResponse();
			if(ob!=null){
			String val=ob.toString();
			if(val.equalsIgnoreCase("anytype{}")||val.equals("")){
				Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
			}
			else{
			//Toast.makeText(getApplicationContext(),val,Toast.LENGTH_SHORT).show();
			String get[]=val.split("#");
			detail = new String[get.length-1];
			for(int i=0;i<get.length-1;i++){
				detail[i] = get[i];
				Log.d("detail", detail[i]); 
				
			}
			donor_id=get[3];
			li.setAdapter(new PanchAdapt(getApplicationContext(), detail));
			}		
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		li.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 1:String val=detail[1];
						//Toast.makeText(getApplicationContext(),val,Toast.LENGTH_LONG).show();
						try{
						Intent i = new Intent(Intent.ACTION_DIAL);
						i.setData(Uri.parse(val));
						startActivity(i);
						}catch (Exception e) {
							// TODO: handle exception
							Log.e("DEMO APLICATION ","FAILED TO INVOKE CALL",e);
							Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
						}
						break;
				case 2:String val1=detail[2];
						Toast.makeText(getApplicationContext(),val1, Toast.LENGTH_SHORT).show();
						Intent mail = new Intent(Intent.CATEGORY_APP_EMAIL);
						break;

				default:
					break;
				}
				 
				
			}
			
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dono_details, menu);
		return true;
	}

}
