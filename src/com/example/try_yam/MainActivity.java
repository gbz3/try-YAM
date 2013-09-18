package com.example.try_yam;

import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MapActivity;
import jp.co.yahoo.android.maps.MapView;
import jp.co.yahoo.android.maps.MapView.MapTouchListener;
import jp.co.yahoo.android.maps.MyLocationOverlay;
import jp.co.yahoo.android.maps.PinOverlay;
import jp.co.yahoo.android.maps.routing.RouteOverlay;
import jp.co.yahoo.android.maps.routing.RouteOverlay.RouteOverlayListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

public class MainActivity extends MapActivity implements MapTouchListener, RouteOverlayListener {
	private static final String tag = MapActivity.class.getSimpleName();

	// Key of InstanceState
	private static final String LAT_OF_CENTER = "LATITUDE_OF_CENTER";	// 緯度
	private static final String LON_OF_CENTER = "LONGITUDE_OF_CENTER";	// 軽度
	private static final String ZOOM_LEVEL = "ZOOM_LEVEL";	// ズームレベル
	private static final String TOGGLE_LOCATION = "TOGGLE_LOCATION";	// ロケーションボタン選択状態
	private static final String ENABLE_LOCATION = "ENABLE_LOCATION";	// ロケーション利用状態

	private static final int LPWC = ViewGroup.LayoutParams.WRAP_CONTENT;
	private static final int LPMP = ViewGroup.LayoutParams.MATCH_PARENT;

	private MapView _mv;
	private ToggleButton _tb;
	private MyLocationOverlay _mlo;
	private RouteOverlay _ro;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		// MapView
		_mv = new MapView( this, getString( R.string.appid ) );
		_mv.setScalebar( true );
		_mv.setLongPress( true );
		_mv.setMapTouchListener( this );

		_ro = new RouteOverlay( this, getString( R.string.appid ) );
		_ro.setStartTitle( getString( R.string.start_pin_title ) );
		_ro.setGoalTitle( getString( R.string.goal_pin_title ) );
		_ro.setRouteOverlayListener( this );
		_mv.getOverlays().add( _ro );

		// My Location
		_mlo = new MyLocationOverlay( this, _mv );

		// Location Button
		_tb = new ToggleButton( this );
		_tb.setText( "Location OFF" );
		_tb.setTextOn( "Location ON" );
		_tb.setTextOff( "Location OFF" );
		FrameLayout.LayoutParams lp4loc = new FrameLayout.LayoutParams( LPWC, LPWC );
		lp4loc.gravity = Gravity.LEFT | Gravity.TOP;
		lp4loc.leftMargin = 20;
		lp4loc.topMargin = 20;
		_tb.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged( CompoundButton btnView, boolean isChecked ) {
				if( !isChecked ) {
					if( _mlo.isMyLocationEnabled() ) _mlo.disableMyLocation();
				} else {
					if( !_mlo.isMyLocationEnabled() ) _mlo.enableMyLocation();
					_mlo.runOnFirstFix( new Runnable() {
						@Override
						public void run() {
							if( _mv.getMapController() != null ) {
								GeoPoint here = _mlo.getMyLocation();
								Log.d( tag, "here:" + here.toString() );
								_mv.getMapController().animateTo( here );
							}
						}
					});
				}
			}
		});

		// Menu Button
		Button btnMenu = new Button( this );
		btnMenu.setBackgroundResource( R.drawable.ic_launcher );
		btnMenu.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MainActivity.this.getFragmentManager().beginTransaction().add( android.R.id.content, new MyListFragment() ).commit();
			}
		});
		FrameLayout.LayoutParams lp4menu = new FrameLayout.LayoutParams( LPWC, LPWC );
		lp4menu.gravity = Gravity.LEFT | Gravity.BOTTOM;
		lp4menu.leftMargin = 20;
		lp4menu.bottomMargin = 100;

		// View Setting
		FrameLayout fl = new FrameLayout( this );
		fl.addView( _mv, new FrameLayout.LayoutParams( LPMP, LPMP ) );
		fl.addView( _tb, lp4loc );
		fl.addView( btnMenu, lp4menu );
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
		if( _mlo != null ) {
			outState.putBoolean( TOGGLE_LOCATION, _tb.isChecked() );
			outState.putBoolean( ENABLE_LOCATION, _mlo.isMyLocationEnabled() );
		}
	}

	@Override
	protected void onRestoreInstanceState( Bundle inState ) {
		if( _mv != null ) {
			_mv.getMapController().setZoom( inState.getInt( ZOOM_LEVEL, 4 ) );
			_mv.getMapController().setCenter(
					new GeoPoint( inState.getInt( LAT_OF_CENTER, _mv.getMapCenter().getLatitudeE6() ),
							inState.getInt( LON_OF_CENTER, _mv.getMapCenter().getLongitudeE6() ) ) );
		}
		if( _mlo != null ) {
			_tb.setChecked( inState.getBoolean( TOGGLE_LOCATION, false ) );
			if( inState.getBoolean( ENABLE_LOCATION, false ) ) {
				_mlo.enableMyLocation();
			} else {
				_mlo.disableMyLocation();
			}
		}
	}

	@Override
	public boolean onLongPress( MapView mv, Object icon, PinOverlay pin, GeoPoint goal ) {
		Log.d( tag, "start:" + mv.getMapCenter().toString() + " end:" + goal.toString() );
		_ro.setRoutePos( ( _mlo.isMyLocationEnabled()? _mlo.getMyLocation(): mv.getMapCenter() ),
				goal, RouteOverlay.TRAFFIC_WALK );
		_ro.search();
		return false;
	}

	@Override
	public boolean onPinchIn(MapView arg0) {
		return false;
	}

	@Override
	public boolean onPinchOut(MapView arg0) {
		return false;
	}

	@Override
	public boolean onTouch(MapView arg0, MotionEvent arg1) {
		return false;
	}

	@Override
	public boolean errorRouteSearch(RouteOverlay arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean finishRouteSearch( RouteOverlay ro ) {
		if( _mv.getMapController() != null ) {
			_mv.getMapController().animateTo( ro.getStartPos() );
		}
		return false;
	}

}
