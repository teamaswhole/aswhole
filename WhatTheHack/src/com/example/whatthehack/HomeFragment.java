package com.example.whatthehack;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.whatthehack.JSONParser;
import com.example.whatthehack.R;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

public class HomeFragment extends Fragment {
	ImageButton b;
	private ProgressDialog pDialog;
	JSONParser jParser = new JSONParser();
	JSONArray landmarks = null;
	private static String url_all_landmarks = "http://10.91.0.169/wth/getDestination.php";
	private static final String TAG_SUCCESS = "Success";
	private static final String TAG_LANDMARK = "data";
	int  pos;
	int region;
	ArrayAdapter<String> arrayAdapter;
	public HomeFragment() {
	}

	public int[] ids = { R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5,
			R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn10, R.id.btn11,
			R.id.btn12, R.id.btn13, R.id.btn14, R.id.btn15, R.id.btn16,
			R.id.btn17 };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);

		for (int i = 0; i < ids.length; i++) {
			final int x = i;
			b = (ImageButton) rootView.findViewById(ids[i]);
			b.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					pos = x;
					region = pos;
					new GetLandmarks().execute();
				}
			});
			
			
		}		
		return rootView;
	}

	/*private class MyListener implements ImageButton.OnClickListener {
		int a;
		public MyListener(int position) {
			a = position;
			
			Log.d("Position", ""+position);
		}

		@Override
		public void onClick(View v) {
			region = pos;
			Log.d("Pos", ""+a);
			new GetLandmarks().execute();
			
			//showDialog(pos,"Your Selected Button is " + (pos + 1));
		}
	}*/

	public void showDialog(int id ,final String string) {
		//new GetLandmarks().execute();
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                getActivity());
		
        builderSingle.setTitle("Region "+id +": ");
       
        
        
        builderSingle.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
                               getActivity());
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Test");
                        builderInner.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builderInner.show();
                    }
                });
        builderSingle.show();
	}
	
	
	class GetLandmarks extends AsyncTask<String, String, String> {
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(getActivity());
	        pDialog.setMessage("Loading Landmarks details. Please wait...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
	    }
	
	    protected String doInBackground(String... args) {
	                int success;
	                try {
	                    List<NameValuePair> params = new ArrayList<NameValuePair>();
	                    params.add(new BasicNameValuePair("region", String.valueOf(region)));
	                    JSONObject json = jParser.makeHttpRequest(url_all_landmarks, "POST", params);
	                    Log.d("Landmarks", json.toString());
	                    arrayAdapter = new ArrayAdapter<String>(
	                            getActivity(),
	                            android.R.layout.select_dialog_singlechoice);
	                    success = json.getInt(TAG_SUCCESS);
	                    if (success == 1) {
	                    	landmarks = json.getJSONArray(TAG_LANDMARK);
	    					for (int i = 0; i < landmarks.length(); i++) {
	    						JSONObject c = landmarks.getJSONObject(i);
	    						String destination = c.getString("destination");
	    						arrayAdapter.add(destination);
	    						
	    					}
	                       
	
	                    }else{
	
	                    }
	                } catch (JSONException e) {
	                    e.printStackTrace();
	                } catch(Exception e){
	                	
	                }
	            
	
	        return null;
	    }
	
	
	    protected void onPostExecute(String file_url) {
	        // dismiss the dialog once got all details
	        pDialog.dismiss();
	        showDialog(region,"Your Selected Button is " + (region + 1 ));
	    }

}
	}
