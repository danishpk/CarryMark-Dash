package tech.stacka.carrymarkdashboard.activity.map

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import kotlinx.android.synthetic.main.activity_map.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.EmployeeLocationResponse
import tech.stacka.carrymarkdashboard.models.data.ArrLocationDetail
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MapActivity : AppCompatActivity(), MapView {
    private var map: MapboxMap? = null
    private val presenter=MapPresenter(this,this);
    private var cordinates=ArrayList<String>()
    private var locationDetail=ArrayList<ArrLocationDetail>()
    private var currentDate=""
    private var strToken=""
    private var timeShown=""
    private val MARKER_IMAGE_ID = "MARKER_IMAGE_ID"
  //  private lateinit var  mapView: MapView
    private var mapboxMap: MapboxMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map)
        mapbox.onCreate(savedInstanceState)
        strToken=SharedPrefManager.getInstance(this).user.strToken!!
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        currentDate = sdf.format(Date())
        println(" C DATE is  $currentDate")
        Log.e("current Date",currentDate)
        val strEmployeeId = intent.getStringExtra("employeeId")!!

        presenter.employeeLocations(strToken,strEmployeeId,currentDate)



    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapbox.onSaveInstanceState(outState)
    }




    @SuppressWarnings("MissingPermission")
    override fun onStart() {
        super.onStart()
        mapbox.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapbox.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapbox.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapbox.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapbox.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapbox.onLowMemory()
    }

    override fun onEmployeeLocationSuccess(apiResponse: EmployeeLocationResponse) {
        locationDetail=apiResponse.arrList as ArrayList<ArrLocationDetail>

        if(locationDetail.size!=0){

            mapbox.getMapAsync { mapBoxMap ->
                mapBoxMap.clear()
                mapBoxMap.setStyle(Style.LIGHT)

                for (i in locationDetail){
                    var lati=i.coordinates[0]
                    var longi=i.coordinates[1]

                    mapBoxMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(lati, longi))
                            .title(i.strCreatedTime)
                    )
                }
                map = mapBoxMap

            }

        }else {
            tv_notFound.visibility = View.VISIBLE
        }


    }

    override fun onEmployeeLocationNull(apiResponse: EmployeeLocationResponse) {
        TODO("Not yet implemented")
    }

    override fun onEmployeeLocationFailed(apiResponse: ResponseBody) {
        TODO("Not yet implemented")
    }

    override fun onEmployeeLocationFailedServerError(string: String) {
        TODO("Not yet implemented")
    }

//    override fun onMapReady(mapboxMap: MapboxMap) {
//        mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
//
//            val symbolManager = SymbolManager(mapbox, mapboxMap, style)
//            symbolManager.iconAllowOverlap = true
//          //  setUpImage(style)
//            style.addImage("myMarker", BitmapFactory.decodeResource(resources,R.drawable.sparcot_logo))
//            symbolManager.create(
//                SymbolOptions()
//                .withLatLng(LatLng(-1.693314, 29.225241))
//                .withIconImage("myMarker")
//            )
//        }
//    }

//    private fun setUpImage(loadedStyle: Style) {
//        loadedStyle.addImage(
//            MARKER_IMAGE_ID, BitmapFactory.decodeResource(
//                this.resources, R.drawable.sparcot_logo
//            )
//        )
//    }


}

