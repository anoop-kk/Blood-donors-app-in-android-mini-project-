package com.example.abcb;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfile extends BaseActivity {
	//static String URL="http://169.254.164.207:8080/Abcb/WebAbCb?WSDL";
	//static String NAMESPACE="http://abcb/";
	static String METHOD="Update";
	static String SOAP="http://abcb/Update";
	
	EditText email,mobile;
	TextView name,label;
	MenuItem item;
	Spinner sp;
	Button btn;
	String s[]={"YES","NO"};
	int  uid=Integer.parseInt(SignUpPage.uid);
	String uid1=SignUpPage.uid;
	String get;
	String s1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLayoutInflater().inflate(R.layout.activity_edit_profile,frameLayout);
		//setContentView(R.layout.activity_edit_profile);
		mobile=(EditText)findViewById(R.id.editText1);
		email=(EditText)findViewById(R.id.editText2);
		name=(TextView)findViewById(R.id.textView1);
		btn=(Button)findViewById(R.id.button1);
		sp=(Spinner)findViewById(R.id.spinner1);
		ArrayAdapter<String> ad=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,s);
		sp.setAdapter(ad);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),uid1,Toast.LENGTH_SHORT).show();
				s1=sp.getSelectedItem().toString();
				if(s1.equals("YES")){
					s1="Y";
				}
				else{
					s1="N";
					}
				
				Toast.makeText(getApplicationContext(),getUpdate(),Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(getApplicationContext(),UserView.class);
				finish();
				startActivity(intent);
			}
			
		});
		//label=(TextView)findViewById(R.id.textView3);
		
		
		
		
		//mobile.setText(s);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_profile, menu);
		//item=(MenuItem)findViewById(R.id.action_home);
		//onOptionItemSelected();
		return true;
	}
	public String getUpdate() {
		if(android.os.Build.VERSION.SDK_INT>9){
			StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		try{
		SoapObject request=new SoapObject(UserView.NAMESPACE, METHOD);
		SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		request.addProperty("uid",uid);
		request.addProperty("status",s1);
		envelop.setOutputSoapObject(request);
		HttpTransportSE htse=new HttpTransportSE(UserView.URL);
		htse.call(SOAP, envelop);
		Object ob=null;
		ob=envelop.getResponse();
		if(ob!=null){
			get=ob.toString();
			/*String val[]=get.split("#");
			String emailget=val[0];
			String mobileget=val[1];
			String nameget=val[2];
			email.setText(emailget);
			mobile.setText(mobileget);
			//label.setText(nameget);*/
		}
				
		}catch(Exception e){
			Log.e("SOAP ERROR","failed to call",e);
			
		}
		return get;
		
	}

/*public void onOptionItemSelected() {
	item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
		
		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
			// TODO Auto-generated method stub
			switch (item.getItemId()) {
			case R.id.action_home:
				homeOne();
				return true;
			default:
				return true;
				
			}
			
		}
	});
	
	
}

private void homeOne() {
	// TODO Auto-generated method 
	Intent intent= new Intent(getApplicationContext(),UserView.class);
	startActivity(intent);
	
}*/
}
