package com.example.android.bottomsheetjetpacktest

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.view.LocationDisplay
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var standardBottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupMap()
        setupLocationDisplay()
        setupButton()
        setupStandardBottomSheet()
        setupGPSButton()
    }

    private fun setupGPSButton() {
        gps_button.setOnClickListener { view ->
            mapView.locationDisplay.autoPanMode = LocationDisplay.AutoPanMode.RECENTER
//            Snackbar.make(view, "Recentering", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
        }
    }

    private fun setupMap() {
        // create a map
        val map = ArcGISMap(Basemap.Type.NAVIGATION_VECTOR, 34.056295, -117.195800, 16)

        // assign map to layout MapView and hide unnecessary stuff
        mapView.map = map
        mapView.isAttributionTextVisible = false
        ArcGISRuntimeEnvironment.setLicense("runtimeadvanced,1000,rud000228325,none,3M10F7PZB0YH463EM164")

        when (this?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                map.basemap = Basemap.createStreetsNightVector()
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                map.basemap = Basemap.createNavigationVector()
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> { }
        }
    }

    private fun setupLocationDisplay() {
        val locationDisplay = mapView.locationDisplay
        locationDisplay.autoPanMode = LocationDisplay.AutoPanMode.RECENTER

        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions, 2)

        locationDisplay.autoPanMode = LocationDisplay.AutoPanMode.RECENTER
        locationDisplay.startAsync()
    }

    private fun setupButton() {
        standardBottomSheetButton.setOnClickListener {
            standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }


    private fun setupStandardBottomSheet() {

//        val viewTreeObserver: ViewTreeObserver = mapView.viewTreeObserver
////        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
////            override fun onPreDraw(): Boolean {
////                mapView.viewTreeObserver.removeOnPreDrawListener(this)
////                standardBottomSheetBehavior.peekHeight = mapView.height / 2
////                return false
////            }
////        })

        standardBottomSheetBehavior = BottomSheetBehavior.from(standardBottomSheet)
        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                textView.text = when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> "STATE_EXPANDED"
                    BottomSheetBehavior.STATE_COLLAPSED -> "STATE_COLLAPSED"
                    BottomSheetBehavior.STATE_DRAGGING -> "STATE_DRAGGING"
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> "STATE_HALF_EXPANDED"
                    BottomSheetBehavior.STATE_HIDDEN -> "STATE_HIDDEN"
                    BottomSheetBehavior.STATE_SETTLING -> "STATE_SETTLING"
                    else -> null
                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        }
        standardBottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback)
        standardBottomSheetBehavior.saveFlags = BottomSheetBehavior.SAVE_ALL
    }


    override fun onPause() {
        super.onPause()
        mapView.pause()
    }
    override fun onResume() {
        super.onResume()
        mapView.resume()
    }
    override fun onDestroy() {
        super.onDestroy()
        mapView.dispose()
    }
}
