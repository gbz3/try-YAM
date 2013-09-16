package com.example.yam;

import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MapActivity;
import jp.co.yahoo.android.maps.MapView;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends MapActivity {

	private static final String LAT_OF_CENTER = "LATITUDE_OF_CENTER";	// 緯度
	private static final String LON_OF_CENTER = "LONGITUDE_OF_CENTER";	// 軽度

	private MapView _mv;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		_mv = new MapView( this, "dj0zaiZpPU12WHdRV3RESkZUNCZzPWNvbnN1bWVyc2VjcmV0Jng9Zjg-" );
		_mv.setScalebar( true );

		FrameLayout fl = new FrameLayout( this );
		fl.addView( _mv );
		setContentView( fl );
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onSaveInstanceState( Bundle outState ) {
		if( _mv != null ) {
			outState.putInt( LAT_OF_CENTER, _mv.getMapCenter().getLatitudeE6() );
			outState.putInt( LON_OF_CENTER, _mv.getMapCenter().getLongitudeE6() );
		}
	}

	@Override
	protected void onRestoreInstanceState( Bundle inState ) {
		if( _mv != null && inState.containsKey( LAT_OF_CENTER ) && inState.containsKey( LON_OF_CENTER ) ) {
			_mv.getMapController().setCenter(
					new GeoPoint( inState.getInt( LAT_OF_CENTER ), inState.getInt( LON_OF_CENTER ) ) );
		}
	}

}
