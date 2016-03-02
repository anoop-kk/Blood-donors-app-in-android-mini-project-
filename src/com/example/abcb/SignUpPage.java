package com.example.abcb;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.MonthDisplayHelper;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SignUpPage extends Activity {
	static String MTHOD="register";
	static String SOAP=UserView.NAMESPACE+MTHOD;
	String response="";
	EditText nameED,emailED,mobileED,bloodgroupED,genderED,weightED,heightED,housenameED,locatonED,districtED,stateED,ageED;
	Button btn;
	Spinner sp_blood,sp_gender,sp_district;
	String[] bgroup={"A +ve","A -ve","B +ve","B -ve","AB +ve","AB -ve","O +ve","O -ve"};
	String[] genderAr={"Male","Femail"};
	String[] distAr={"Thiruvananthapuram ","Kollam ","Pathanamthitta","Alappuzha","Kottayam","Idukki","Ernakulam","Thrissur","Palakkad","Malappuram","Kozhikode","Wayanad","Kannur","Kasaragod"};
	String name,email,mobile,bloodgroup,gender,weight,height,housename,locaton,district,state,age;
	public static String imei;
	public static String uid;

	SharedPreferences sh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_page);
		btn=(Button)findViewById(R.id.button1);
		nameED=(EditText)findViewById(R.id.editText1);
		emailED=(EditText)findViewById(R.id.editText2);
		mobileED=(EditText)findViewById(R.id.editText3);
		weightED=(EditText)findViewById(R.id.editText4);
		heightED=(EditText)findViewById(R.id.editText5);
		housenameED=(EditText)findViewById(R.id.editText6);
		locatonED=(EditText)findViewById(R.id.editText8);
		stateED=(EditText)findViewById(R.id.editText7);
		sp_blood=(Spinner)findViewById(R.id.spinner1);
		sp_gender=(Spinner)findViewById(R.id.spinner2);
		sp_district=(Spinner)findViewById(R.id.spinner3);
		ageED=(EditText)findViewById(R.id.editText9);
		sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String user_id = sh.getString("user_id", "0");
		uid=user_id;
		if(!user_id.equals("0")){
			
			Intent intent=new Intent(getApplicationContext(),UserView.class);
			startActivity(intent);
		}
		
		ArrayAdapter<String> ad = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,bgroup);
		sp_blood.setAdapter(ad);
		//sp_blood.setBackgroundColor(Color.CYAN);
		ArrayAdapter<String> ad1=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,genderAr);
		sp_gender.setAdapter(ad1);
		ArrayAdapter<String> ad2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,distAr);
		sp_district.setAdapter(ad2);
	
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO here gets value in on click method
				name=nameED.getText().toString();
				email=emailED.getText().toString();
				mobile=mobileED.getText().toString();
				bloodgroup=sp_blood.getSelectedItem().toString();
				gender=sp_gender.getSelectedItem().toString();
				weight=weightED.getText().toString();
				height=heightED.getText().toString();
				housename=housenameED.getText().toString();
				locaton=locatonED.getText().toString();
				district=sp_district.getSelectedItem().toString();
				state=stateED.getText().toString();
				age=ageED.getText().toString();
				imei=getImei();
				
				if(!isValidName(name)){
						nameED.setError("Enter name properly");
				}else if (!isValidEmail(email)) {
					emailED.setError("Invald email");
				}
				//if(response.equals("User already exists")){
					//Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
					//Intent intent=new Intent(getApplicationContext(),UserView.class);
					//startActivity(intent);
				//}
				else{
					/*register();
					String[] val = response.split("#");
					//uid=val[1];
					if(val.length>0){
						Editor ed = sh.edit();
						ed.putString("user_id", val[1]);
						ed.commit();
						Toast.makeText(getApplicationContext(), val[0], Toast.LENGTH_SHORT).show();
						Toast.makeText(getApplicationContext(), val[1], Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
					}
				Intent intent=new Intent(getApplicationContext(),UserView.class);
				startActivity(intent);
				
				*/}
			}

			private boolean isValidEmail(String email) {
				// TODO validating email
				String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
						+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

				Pattern pattern = Pattern.compile(EMAIL_PATTERN);
				Matcher matcher = pattern.matcher(email);
				return matcher.matches();
				
			}

			private boolean isValidName(String name) {
				// TODO validating name
				if(name.equals("")||TextUtils.isDigitsOnly(name)){
					
					return false;
				}
				else{
					return true;
					
				}
			}

			private void register() {
				// TODO Auto-generated method stub
				VerEXception();
				SoapObject request=new SoapObject(UserView.NAMESPACE,MTHOD);
				SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.setOutputSoapObject(request);
				request.addProperty("name", name);
				request.addProperty("email", email);
				request.addProperty("mobile", mobile);
				request.addProperty("bloodgroup", bloodgroup);
				request.addProperty("gender", gender);
				request.addProperty("weight", weight);
				request.addProperty("height", height);
				request.addProperty("housename", housename);
				request.addProperty("locaton", locaton);
				request.addProperty("district",district);
				request.addProperty("state",state);
				request.addProperty("age", age);
				request.addProperty("imei", imei);
				HttpTransportSE htse=new HttpTransportSE(UserView.URL);
				try {
					htse.call(SOAP, envelope);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Object ob=null;
				
				try {
					ob=envelope.getResponse();
				} catch (SoapFault e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(ob!=null){
					response=ob.toString();
					
				}
			}

			private String getImei() {
				// TODO here gets Imei
				TelephonyManager manager=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
				return manager.getDeviceId() ;
			}
		});
		
	}
	public void VerEXception() {
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
		}
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up_page, menu);
		return true;
	}

}
