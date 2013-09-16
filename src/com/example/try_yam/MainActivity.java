package com.example.try_yam;

import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MapActivity;
import jp.co.yahoo.android.maps.MapView;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends MapActivity {

	private static final String LAT_OF_CENTER = "LATITUDE_OF_CENTER";	// 緯度
	private static final String LON_OF_CENTER = "LONGITUDE_OF_CENTER";	// 軽度
	private static final String ZOOM_LEVEL = "ZOOM_LEVEL";	// ズームレベル

	private MapView _mv;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		_mv = new MapView( this, getString( R.string.appid ) );
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
			outState.putInt( ZOOM_LEVEL, _mv.getZoomLevel() );
			outState.putInt( LAT_OF_CENTER, _mv.getMapCenter().getLatitudeE6() );
			outState.putInt( LON_OF_CENTER, _mv.getMapCenter().getLongitudeE6() );
		}
	}

	@Override
	protected void onRestoreInstanceState( Bundle inState ) {
		if( _mv != null && inState.containsKey( LAT_OF_CENTER ) && inState.containsKey( LON_OF_CENTER ) ) {
			_mv.getMapController().setZoom( inState.getInt( ZOOM_LEVEL ) );
			_mv.getMapController().setCenter(
					new GeoPoint( inState.getInt( LAT_OF_CENTER ), inState.getInt( LON_OF_CENTER ) ) );
		}
	}

}
