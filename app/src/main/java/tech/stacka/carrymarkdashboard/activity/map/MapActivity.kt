package tech.stacka.carrymarkdashboard.activity.map

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_map.*
import org.json.JSONArray
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.EmployeeLocationResponse
import tech.stacka.carrymarkdashboard.models.data.ArrLocationDetail
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.AlertHelper
import tech.stacka.carrymarkdashboard.utils.Utilities
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class MapActivity : AppCompatActivity(), MapView, DatePickerDialog.OnDateSetListener {
    private var map: MapboxMap? = null
    private val presenter=MapPresenter(this,this);
    private var cordinates=ArrayList<String>()
    private var locationDetail=ArrayList<ArrLocationDetail>()
    private var currentDate=""
    private var selectDate=""
    private var strToken=""
    private var timeShown=""
    var strEmployeeId=""
    private val MARKER_IMAGE_ID = "MARKER_IMAGE_ID"
  //  private lateinit var datePicker: DatePickerDialog
  //  private lateinit var  mapView: MapView
    private var mapboxMap: MapboxMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map)
        mapbox.onCreate(savedInstanceState)
        strToken=SharedPrefManager.getInstance(this).user.strToken!!
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        currentDate = sdf.format(Date())
        tv_date_show.text=currentDate
        println(" C DATE is  $currentDate")
        Log.e("current Date",currentDate)
         strEmployeeId = intent.getStringExtra("employeeId")!!

        if(Utilities.checkInternetConnection(this)) {
            presenter.employeeLocations(strToken,strEmployeeId,currentDate)

        }else{
            AlertHelper.showNoInternetSnackBar(this@MapActivity, object :
                AlertHelper.SnackBarListener {
                override fun onOkClick() {
                    presenter.employeeLocations(strToken,strEmployeeId,currentDate)
                }
            })
        }

//        val calendar = Calendar.getInstance()
//        val datePickerListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//            calendar.set(Calendar.YEAR, year)
//            calendar.set(Calendar.MONTH, monthOfYear)
//            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
////                selectDate = calendar.get(Calendar.YEAR).toString() + "/" + (calendar.get(
////                    Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH)
//
//            selectDate = calendar.get(Calendar.DAY_OF_MONTH).toString() + "/" + (calendar.get(
//                Calendar.MONTH) + 1) + "/" +  calendar.get(Calendar.YEAR)
//            tv_date_show.text = selectDate
//        }
//            datePicker = DatePickerDialog(
//                this,
//                datePickerListener,
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//            )
//            datePicker.datePicker.maxDate = Calendar.getInstance().timeInMillis

        btDateSelection.setOnClickListener {
         //   datePicker.show()

            val dd = SimpleDateFormat("dd")
            val mm = SimpleDateFormat("MM")
            val yy = SimpleDateFormat("yyyy")
            val day = dd.format(Date()).toInt()
            val month = mm.format(Date()).toInt() - 1
            val year = yy.format(Date()).toInt()
//                println(" " + year + " " + month + " " + day)
            val datePickerDialog = DatePickerDialog(this, this, year, month, day)
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }


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

        if(locationDetail!=null) {
            map?.clear()
            if (locationDetail.isNotEmpty()) {

                mapbox.getMapAsync { mapBoxMap ->
                    mapBoxMap.clear()
                    mapBoxMap.setStyle(Style.LIGHT)

                    for (i in locationDetail) {
                        val lati = i.coordinates[0]
                        val longi = i.coordinates[1]

                        mapBoxMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(lati, longi))
                                .title(i.strTime)
                        )
                    }
                    map = mapBoxMap

                }
                tv_notFound.visibility = View.GONE
            } else {
                tv_notFound.visibility = View.VISIBLE
            }
        }


    }

    override fun onEmployeeLocationNull(apiResponse: EmployeeLocationResponse) {

    }

    override fun onEmployeeLocationFailed(apiResponse: JSONArray) {
        Toast.makeText(this@MapActivity,apiResponse.get(0).toString(),Toast.LENGTH_SHORT).show()
    }

    override fun onEmployeeLocationFailedServerError(string: String) {
        Toast.makeText(this@MapActivity,string,Toast.LENGTH_SHORT).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val sdt = SimpleDateFormat("hh:mm a")
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val year = year - 1900
        val date = sdf.format(Date(year, month, dayOfMonth))
        currentDate=date.toString()
        map?.clear()
        tv_date_show.text=currentDate
        if(Utilities.checkInternetConnection(this)) {
            presenter.employeeLocations(strToken,strEmployeeId,currentDate)

        }else{
            AlertHelper.showNoInternetSnackBar(this@MapActivity, object :
                AlertHelper.SnackBarListener {
                override fun onOkClick() {
                    presenter.employeeLocations(strToken,strEmployeeId,currentDate)
                }
            })
        }

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

