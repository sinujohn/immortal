package me.sinu.immortal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class ImmortalActivity extends Activity {
	
	private String immortalPrefs = "me.sinu.immortal";
	private String addressSetKey = "me.sinu.immortal.addresses";
	
	Button startButton;
	Button stopButton;
	WebView webView;
	AutoCompleteTextView urlBox;
	Button goButton;
	
	//String[] addresses;	
	SharedPreferences prefs;
	//final String SPL_DELIM = "[#/!DEL_IM;]";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        webView = (WebView) findViewById(R.id.webView1);
        urlBox = (AutoCompleteTextView) findViewById(R.id.urlBox);
        goButton = (Button) findViewById(R.id.goButton);
        
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        
        prefs = getSharedPreferences(immortalPrefs, Context.MODE_PRIVATE);
        //addresses = prefs.getString(addressSetKey, "").split(SPL_DELIM);
        String[] addrs = new String[] {prefs.getString(addressSetKey, "")};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, addrs);
        urlBox.setAdapter(adapter);
        
        startButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startService(new Intent(ImmortalActivity.this, ImmortalService.class));				
			}
		});
        
        stopButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopService(new Intent(ImmortalActivity.this, ImmortalService.class));				
			}
		});
        goButton.setOnClickListener( new OnClickListener() {
        	public void onClick(View view) {
        		openURL();
        	}
        });
        urlBox.setOnKeyListener( new OnKeyListener() {
        	public boolean onKey(View view, int keyCode, KeyEvent event) {
        		if(keyCode==KeyEvent.KEYCODE_ENTER) {
        			openURL();
        			return true;
        		} else {
        			return false;
        		}
        	}
        });
        
    }
    
    private void openURL() {
    	String pre="";
    	if(!(urlBox.getText().toString().startsWith("http://") || urlBox.getText().toString().startsWith("https://"))){
    		pre = "http://";
    	}
    	urlBox.setText(pre + urlBox.getText());
    	webView.loadUrl(urlBox.getText().toString());
    	webView.requestFocus();
    	/*boolean found = false;
    	for(int i=0; i<addresses.length; i++) {
    		if(addresses[i].equals(urlBox.getText())) {
    			found = true;
    			break;
    		}
    	}
    	
    	if(!found) {
    		StringBuilder sb = new StringBuilder();
        	sb.append(prefs.getString(addressSetKey, "")).append(SPL_DELIM).append(urlBox.getText().toString());
        	prefs.edit().putString(addressSetKey, sb.toString()).commit();
        	addresses = sb.toString().split(SPL_DELIM);
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, addresses);
            urlBox.setAdapter(adapter);
    	}*/
    	prefs.edit().putString(addressSetKey, urlBox.getText().toString()).commit();
    }
    
    @Override
    public void onBackPressed() {
    	moveTaskToBack(true);
    }
    
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }    
}