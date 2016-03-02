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
import android.app.Activity;
import android.content.Intent;
import android.text.StaticLayout;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmReq extends BaseActivity {
	static String METHO="reqDetails";
	static String SOAP=UserView.NAMESPACE+METHO;
	TextView reqid,msg,phn,hsp,dte;
	ImageButton ok,cancel;
	String reqId;
	String[]dataSplit;
	String resp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_confirm_req);
		getLayoutInflater().inflate(R.layout.activity_confirm_req,frameLayout);
		reqid=(TextView)findViewById(R.id.textView3);
		phn=(TextView)findViewById(R.id.textView5);
		msg=(TextView)findViewById(R.id.textView9);
		hsp=(TextView)findViewById(R.id.textView7);
		dte=(TextView)findViewById(R.id.textView11);
		ok=(ImageButton)findViewById(R.id.imageButton2);
		cancel=(ImageButton)findViewById(R.id.imageButton1);
		reqId=getIntent().getStringExtra("reqId");
		getData();
		setData();
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				resp="Accepted";
				response();
				Toast.makeText(getApplicationContext(),resp, Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(getApplicationContext(),ManageRequest.class);
				finish();
				startActivity(intent);
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resp="Rejected";
				response();
				Toast.makeText(getApplicationContext(),resp, Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(getApplicationContext(),ManageRequest.class);
				finish();
				startActivity(intent);
				
			}
		});
	}

	private void response() {
		// TODO Auto-generated method stub
		final String METHODResp="donorResponse";
		final String SOAPRESP=UserView.NAMESPACE+METHODResp;
		if(android.os.Build.VERSION.SDK_INT>9){
			StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		SoapObject request1=new SoapObject(UserView.NAMESPACE,METHODResp);
		SoapSerializationEnvelope envelp=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		request1.addProperty("reqId",reqId);
		request1.addProperty("resp", resp);
		envelp.setOutputSoapObject(request1);
		HttpTransportSE htse=new HttpTransportSE(UserView.URL);
		try {
			htse.call(SOAPRESP, envelp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Object ob=null;
		try {
			ob=envelp.getResponse();
		} catch (SoapFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ob!=null){
			resp=ob.toString();
		}
	}

	private void setData() {
		// TODO Auto-generated method stub
		reqid.setText(reqId);
		hsp.setText(dataSplit[0]);
		phn.setText(dataSplit[1]);
		dte.setText(dataSplit[2]);
		msg.setText(dataSplit[3]);
		//Toast.makeText(getApplicationContext(), dataSplit[3],Toast.LENGTH_SHORT).show();
		
	}

	private void getData() {
		// TODO Auto-generated method stub
		if(android.os.Build.VERSION.SDK_INT>9){
			StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		SoapObject request=new SoapObject(UserView.NAMESPACE,METHO);
		SoapSerializationEnvelope evelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		request.addProperty("reqId",reqId);
		evelope.setOutputSoapObject(request);
		HttpTransportSE htse=new HttpTransportSE(UserView.URL);
		Object ob=null;
		try {
			htse.call(SOAP, evelope);
			
			ob = evelope.getResponse();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ob!=null){
		String data=ob.toString();
		dataSplit=data.split("#");
		}
		
	}
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(getApplicationContext(),ManageRequest.class);
		finish();
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.confirm_req, menu);
		return true;
	}

}
