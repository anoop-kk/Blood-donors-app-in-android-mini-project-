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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author manoj,anoop
 *
 * This activity will add Navigation Drawer for our application and all the code related to navigation drawer.
 * We are going to extend all our other activites from this BaseActivity so that every activity will have Navigation Drawer in it.
 * This activity layout contain one frame layout in which we will add our child activity layout.    
 */
public class BaseActivity extends Activity {
	
	static String METHOD="donorTip";
	static String SOAP="http://abcb/donorTip";

	/**
	 *  Frame layout: Which is going to be used as parent layout for child activity layout.
	 *  This layout is protected so that child activity can access this  
	 *  */
	protected FrameLayout frameLayout;
	
	/**
	 * ListView to add navigation drawer item in it.
	 * We have made it protected to access it in child class. We will just use it in child class to make item selected according to activity opened.  
	 */
	
	protected ListView mDrawerList;
	
	/**
	 * List item array for navigation drawer items. 
	 * */
	String[] listArray;
	
	/**
	 * Static variable for selected item position. Which can be used in child activity to know which item is selected from the list.  
	 * */
	protected static int position;
	
	/**
	 *  This flag is used just to check that launcher activity is called first time 
	 *  so that we can open appropriate Activity on launch and make list item position selected accordingly.    
	 * */
	private static boolean isLaunch = true;
	
	/**
	 *  Base layout node of this Activity.    
	 * */
	private DrawerLayout mDrawerLayout;
	
	/**
	 * Drawer listner class for drawer open, close etc.
	 */
	private ActionBarDrawerToggle actionBarDrawerToggle;

	private String mTitle = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		
		listArray = getResources().getStringArray(R.array.list);
		
		frameLayout = (FrameLayout)findViewById(R.id.content_frame);
		//frameLayout.setBackgroundColor(Color.WHITE);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.drawer_list);
		mDrawerList.setBackgroundColor(Color.WHITE);
		
		mTitle  = (String) getTitle();
		
		// set a custom shadow that overlays the main content when the drawer opens
		//mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, listArray));
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), ""+position, Toast.LENGTH_SHORT).show();
				openActivity(arg2);
				
			}
		});
		
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		// ActionBarDrawerToggle ties together the the proper interactions between the sliding drawer and the action bar app icon
		actionBarDrawerToggle = new ActionBarDrawerToggle(
				this,						/* host Activity */
				mDrawerLayout, 				/* DrawerLayout object */
				R.drawable.ic_menu_white_24dp,     /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,       /* "open drawer" description for accessibility */
				R.string.drawer_close)      /* "close drawer" description for accessibility */ 
		{ 
			/** Called when drawer is closed */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("Menu");
                invalidateOptionsMenu();
            }

		};
		mDrawerLayout.setDrawerListener(actionBarDrawerToggle);		

		/**
		 * As we are calling BaseActivity from manifest file and this base activity is intended just to add navigation drawer in our app.
		 * We have to open some activity with layout on launch. So we are checking if this BaseActivity is called first time then we are opening our first activity.
		 * */
		if(isLaunch){
			 /**
			  *Setting this flag false so that next time it will not open our first activity.
			  *We have to use this flag because we are using this BaseActivity as parent activity to our other activity. 
			  *In this case this base activity will always be call when any child activity will launch.
			  */
			isLaunch = false;
			openActivity(0);
		}
	}
	
	/**
	 * @param position
	 * 
	 * Launching activity when any list item is clicked. 
	 */
	protected void openActivity(int position) {
		
		/**
		 * We can set title & itemChecked here but as this BaseActivity is parent for other activity, 
		 * So whenever any activity is going to launch this BaseActivity is also going to be called and 
		 * it will reset this value because of initialization in onCreate method.
		 * So that we are setting this in child activity.    
		 */
		mDrawerList.setItemChecked(position, true);
		setTitle(mTitle);
		mDrawerLayout.closeDrawer(mDrawerList);
		BaseActivity.position = position; //Setting currently selected position in this field so that it will be available in our child activities. 
		
		switch (position) {
			case 0:
				this.onStop();
				startActivity(new Intent(this, UserView.class));				
				break;
			case 1:
				startActivity(new Intent(this, SearchDonor.class));				
				break;
			case 2:
				startActivity(new Intent(this,SearchDonor.class));
				break;
			case 3:
				startActivity(new Intent(this, ManageRequest.class));
				break;
			case 4:
				startActivity(new Intent(this, SendFeedBack.class));
				//startActivity(new Intent(this, LocateHospital.class));
				//Toast.makeText(getApplicationContext(), "will be update soon"+position, Toast.LENGTH_SHORT).show();
				break;
			case 5:

				getTip();
				break;
				//Toast.makeText(getApplicationContext(), "will update soon"+position, Toast.LENGTH_SHORT).show();
				
			case 6:
				startActivity(new Intent(this,EditProfile.class));
				break;
			case 7:
				Intent intent=new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				break;
			default:
				break;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.base, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		
		// The action bar home/up action should open or close the drawer. 
		// ActionBarDrawerToggle will take care of this.
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	/* Called whenever we call invalidateOptionsMenu() */
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
 
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    
    /* We can override onBackPressed method to toggle navigation drawer*/
	@Override
	public void onBackPressed() {
		if(mDrawerLayout.isDrawerOpen(mDrawerList)){
			mDrawerLayout.closeDrawer(mDrawerList);
		}else {
			mDrawerLayout.openDrawer(mDrawerList);
		}
	}
	public void getTip() {
		String getmessage = null;
		int id1=1003;
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		SoapObject request = new SoapObject(UserView.NAMESPACE,METHOD);
		SoapSerializationEnvelope envelop=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		/*if(id1==1006){
			id1=1001;
			}
		else{
			id1++;}*/
		String id=String.valueOf(id1);
		request.addProperty("id",id);
		envelop.setOutputSoapObject(request);
		HttpTransportSE htse=new HttpTransportSE(UserView.URL);
		try {
			htse.call(SOAP, envelop);
			Object ob=null;
			ob=envelop.getResponse();
			if(ob!=null){
				getmessage=ob.toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Tips for Donor");
		builder.setMessage(getmessage);
		builder.setPositiveButton("Close",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Do nothing just Close
				dialog.dismiss();
			}
		});
		AlertDialog alert=builder.create();
		alert.show();
		
		
	}
	
}

