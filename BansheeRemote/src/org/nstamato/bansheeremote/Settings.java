package org.nstamato.bansheeremote;


import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Settings extends Activity {
	
	//public void onConfigurationChanged(Configuration newConfig){
		//setup();
	//}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setup();
    }
    public void setup(){
    	setContentView(R.layout.init);
        Button data = (Button) findViewById(R.id.submit);

        data.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				EditText iptext = (EditText)findViewById(R.id.ip);
		        EditText porttext = (EditText)findViewById(R.id.port);
				String ip = iptext.getText().toString();
				Socket s;
				OutputStream os;
				String command = "test/";
				boolean canConnect = false;
				boolean isReachable = false;
				int port=Integer.parseInt(porttext.getText().toString());
				
				try{
	    			InetAddress address = InetAddress.getByName(ip);
	            	isReachable = address.isReachable(3000);
	    			if(isReachable){
	    				try {
	    					s = new Socket(ip,port); 
	    					os = s.getOutputStream();
	    					os.write(command.getBytes(), 0, command.length());
	    					s.close();
	    					os.close();
	    					canConnect = true;
	    				} catch(Exception e) {
	    					canConnect = false;
	    					Toast.makeText(Settings.this,"Can't connect to Server. Check your settings.",Toast.LENGTH_SHORT).show();
	    				}
	    			}
	    			else{
						Toast.makeText(Settings.this,"Unreachable host.",Toast.LENGTH_SHORT).show();

	    			}
	    		}catch(Exception e){
					Toast.makeText(Settings.this,"Can't connect to Server. Check your settings.",Toast.LENGTH_SHORT).show();

	    		}
				
				if(canConnect){
					Intent response = new Intent();
					response.putExtra("ip",ip);
					response.putExtra("port", port);
					setResult(RESULT_OK,response);
					finish();
				}
			}
        	
        });
    }

}
