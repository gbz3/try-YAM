package com.example.try_yam;

import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MapActivity;
import jp.co.yahoo.android.maps.MapView;
import jp.co.yahoo.android.maps.MyLocationOverlay;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends MapActivity {
	private static final String tag = MapActivity.class.getSimpleName();

	// Key of InstanceState
	private static final String LAT_OF_CENTER = "LATITUDE_OF_CENTER";	// 緯度
	private static final String LON_OF_CENTER = "LONGITUDE_OF_CENTER";	// 軽度
	private static final String ZOOM_LEVEL = "ZOOM_LEVEL";	// ズームレベル

	private static final int LPWC = ViewGroup.LayoutParams.WRAP_CONTENT;
	private static final int LPMP = ViewGroup.LayoutParams.MATCH_PARENT;

	private MapView _mv;
	private MyLocationOverlay _mlo;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		// MapView
		_mv = new MapView( this, getString( R.string.appid ) );
		_mv.setScalebar( true );

		// My Location
		_mlo = new MyLocationOverlay( this, _mv );

		// Location Button
		final Button btnLoc = new Button( getApplicationContext(), null, R.style.LocationButtonStyle );
		btnLoc.setBackgroundResource( R.drawable.ic_launcher );
		FrameLayout.LayoutParams lp4loc = new FrameLayout.LayoutParams( LPWC, LPWC );
		lp4loc.gravity = Gravity.LEFT | Gravity.TOP;
		lp4loc.leftMargin = 20;
		lp4loc.topMargin = 20;
		btnLoc.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText( MainActivity.this, "onClick", Toast.LENGTH_LONG ).show();
				_mlo.runOnFirstFix( new Runnable() {
					@Override
					public void run() {
						if( _mv.getMapController() != null ) {
							GeoPoint here = _mlo.getMyLocation();
							Log.d( tag, "here:" + here.toString() );
							_mv.getMapController().animateTo( here );
						}
						_mlo.disableMyLocation();
					}
				});
			}
		});

		// View Setting
		FrameLayout fl = new FrameLayout( this );
		fl.addView( _mv, new FrameLayout.LayoutParams( LPMP, LPMP ) );
		fl.addView( btnLoc, lp4loc );
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
