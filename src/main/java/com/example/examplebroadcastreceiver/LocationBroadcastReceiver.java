package com.example.examplebroadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;


public class LocationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent!=null){
            if(intent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)){

                String locationkey = LocationManager.KEY_LOCATION_CHANGED;
                Location location = (Location) intent.getExtras().get(locationkey);
                double latitude = location.getLatitude();
                double altirude = location.getAltitude();

                String location_string = ""+ latitude+"/"+altirude;

                try{
                    MainActivity.getInstance().updateTextView(location_string);
                }catch (Exception e){
                    Toast.makeText(context, location_string, Toast.LENGTH_SHORT).show();
                }

            }else if( intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)){

            }else if( intent.hasExtra(LocationManager.KEY_PROXIMITY_ENTERING)){

            }else if( intent.hasExtra(LocationManager.KEY_STATUS_CHANGED)){

            }
        }
    }
}
