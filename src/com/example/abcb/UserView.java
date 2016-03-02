package com.example.abcb;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class UserView extends BaseActivity {
	 public static String URL="http://169.254.164.207:8080/Abcb/WebAbCb?WSDL";
	public static String NAMESPACE="http://abcb/";
	public static String mac;
	static String METHOD="donorTip";
	static String SOAP="http://abcb/donorTip";
	Button sDonor,lHosp,tFD,maRe,fedB,stUp;
	String message="";
	TelephonyManager manager;
	public static String imei="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
//		setContentView(R.layout.activity_user_view);
		getLayoutInflater().inflate(R.layout.activity_user_view,frameLayout);
		manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imei = manager.getDeviceId();
		Toast.makeText(getApplicationContext(),"hi world",Toast.LENGTH_SHORT).show();
	}	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_view, menu);
		return true;
	}
	private String gettingId() {
	// TODO Auto-generated method stub
		String id="";
		StringBuilder sb=new StringBuilder();
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
			TelephonyManager mngr=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		sb.append(mngr.getDeviceId());
		id=sb.toString();
		}
		return id;
}

}
