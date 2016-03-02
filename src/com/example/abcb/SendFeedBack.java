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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendFeedBack extends BaseActivity {
	EditText cap,fedbk;
	Button btn;
	String caption="",feedback="";
	static String METHOD="getFeedBack";
	static String SOAP=UserView.NAMESPACE+METHOD;
	String user_id=UserView.imei;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_send_feed_back);
		getLayoutInflater().inflate(R.layout.activity_send_feed_back,frameLayout);
		cap=(EditText)findViewById(R.id.editText1);
		fedbk=(EditText)findViewById(R.id.editText3);
		btn=(Button)findViewById(R.id.button1);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				caption=cap.getText().toString();
				feedback=fedbk.getText().toString();
				if(caption.equals("")){
					Toast.makeText(getApplicationContext(),"PLEASE FILL CAPTION",Toast.LENGTH_SHORT).show();
				}else if(feedback.equals("")){
					Toast.makeText(getApplicationContext(),"PLEASE FILL FEED BACK",Toast.LENGTH_SHORT).show();
				}else{
					
					Toast.makeText(getApplicationContext(), feedback+caption,Toast.LENGTH_SHORT).show();
					sendData(user_id,caption,feedback);
					finish();
					startActivity(new Intent(getApplicationContext(),UserView.class));
					
				}
				
			}

			
		});
	}
	private void sendData(String user_id,String caption,String feedback) {
		// TODO sends data to the table feed back on using soap;
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		SoapObject request=new SoapObject(UserView.NAMESPACE,METHOD);
		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		request.addProperty("user_id",user_id);
		request.addProperty("caption",caption);
		request.addProperty("feedback",feedback);
		envelope.setOutputSoapObject(request);
		HttpTransportSE htse=new HttpTransportSE(UserView.URL);
		try {
			htse.call(SOAP, envelope);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		Object ob=null;
		String response="";
		try {
			ob=envelope.getResponse();
			if(ob!=null){
				response=ob.toString();
			}else{
				Toast.makeText(getApplicationContext(),"Connectivity Problem",Toast.LENGTH_SHORT).show();
			}
			Toast.makeText(getApplicationContext(), response,Toast.LENGTH_SHORT).show();
		} catch (SoapFault e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), e.toString(),Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send_feed_back, menu);
		return true;
	}

}
