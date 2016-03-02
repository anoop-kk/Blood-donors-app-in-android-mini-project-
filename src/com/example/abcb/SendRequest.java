package com.example.abcb;

import java.io.IOException;
import java.security.PublicKey;
import java.sql.Date;
import java.sql.SQLClientInfoException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.Inflater;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.DTDHandler;
import org.xmlpull.v1.XmlPullParserException;

import android.R.string;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendRequest extends BaseActivity {
	static String METHOD="reqSend";
	static String SOAP="http://abcb/reqSend";
	EditText date,hosp,mob,nte;
	Button btn;
	int year,month,day;
	Date sqlDate, datenow,maxDate,getsqlDate;
	String hospital,mobile,Note,serverResp;
	String donor_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_send_request);
		getLayoutInflater().inflate(R.layout.activity_send_request,frameLayout);
		donor_id=getIntent().getStringExtra("donor_id");
		//Toast.makeText(getApplicationContext(),donor_id,Toast.LENGTH_SHORT).show();
		hosp=(EditText)findViewById(R.id.editText1);
		date=(EditText)findViewById(R.id.editText2);
		btn=(Button)findViewById(R.id.button1);
		mob=(EditText)findViewById(R.id.editText4);
		nte = (EditText)findViewById(R.id.editText3);
		mob = (EditText)findViewById(R.id.editText4);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					
					hospital=hosp.getText().toString();
					mobile=mob.getText().toString();
					getsqlDate=getDate();
					Note=nte.getText().toString();
					Log.d("d",donor_id);
					if(hospital.equals("")){
						Toast.makeText(getApplicationContext(),"PLEASE FILL FIELD HOSPITAL",Toast.LENGTH_SHORT).show();
					}else if(mobile.equals("")){
						Toast.makeText(getApplicationContext(),"PLEASE FILL FIELD MOBILE",Toast.LENGTH_SHORT).show();
					}else if(Note.equals("")){
						Toast.makeText(getApplicationContext(),"PLEASE FILL FIELD REQUEST NOT",Toast.LENGTH_SHORT).show();
					}
					else{
					serverResp=sendDonorReq();
					Toast.makeText(getApplicationContext(),serverResp,Toast.LENGTH_SHORT).show();
					Intent intent=new Intent(getApplicationContext(), DonorList.class);
					//finish();
					startActivity(intent);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
				}
			}

			private String sendDonorReq() {
				// TODO here we are sends data to server and receives confirmation from server 
				//MAC is used to identify Donor there must be a table "request" 
				if (android.os.Build.VERSION.SDK_INT > 9) {
		            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		            StrictMode.setThreadPolicy(policy);
		        }
				
				SoapObject request = new SoapObject(UserView.NAMESPACE,METHOD);
				SoapSerializationEnvelope env=new SoapSerializationEnvelope(SoapEnvelope.VER11);
				request.addProperty("donor_id",donor_id);
				request.addProperty("user_id",SignUpPage.uid);
				
				request.addProperty("date", getsqlDate.toString());
				request.addProperty("hosp",hospital);
				request.addProperty("c_num",mob.getText().toString() );
				request.addProperty("req_not",nte.getText().toString());
				env.setOutputSoapObject(request);
				HttpTransportSE htse=new HttpTransportSE(UserView.URL);
				String Resp = "";
				try {
					htse.call(SOAP, env);
					Object ob=null;
					ob=env.getResponse();
					if(ob!=null){
						Resp=ob.toString();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Connection error",Toast.LENGTH_SHORT).show();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return Resp;
			}

			
		
			
		});
		
	}
	private Date getDate() {
		// TODO here gets data;
				DateFormat formater=new SimpleDateFormat("dd/MM/yyyy");
				String getdate=date.getText().toString();
				try {
					java.util.Date utDate=formater.parse(getdate);
					
					Calendar c=Calendar.getInstance();//here gets current Calendar Instance from phone
					//TODO here splits day year month for setting maximum limit
					int year=c.get(c.YEAR)+5;
					 int month=c.get(c.MONTH);
					 int day=c.get(c.DAY_OF_MONTH);
					 
					// String yearString=String.valueOf(month);
					
					java.util.Date curDate =formater.parse(formater.format(c.getTime())); //gets date using Calendar Instance
					//Toast.makeText(getApplicationContext(), ""+curDate, Toast.LENGTH_LONG).show();
					//TODO type Casting java.util.Date into sql.date
					datenow=new Date(curDate.getTime());
					sqlDate=new Date(utDate.getTime());
					maxDate = new Date(year-1900,month,day);
					//TODO here checks upper and lower limits
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					//Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				if(sqlDate.before(datenow)){
					Toast.makeText(getApplicationContext(),"you can only send request with minimal differents oneday ",Toast.LENGTH_SHORT).show();
					return null;
				}else if(sqlDate.after(maxDate)){
					Toast.makeText(getApplicationContext(),"your date achived greater than limit",Toast.LENGTH_SHORT).show();
					return null;
				}else{
					//Toast.makeText(getApplicationContext(),"your date"+sqlDate.toString(),Toast.LENGTH_SHORT).show();
					return sqlDate;
				}
			}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send_request, menu);
		return true;
	}
	
}
