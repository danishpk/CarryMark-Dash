package tech.stacka.carrymarkdashboard

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var fromDate=""
    private lateinit var fromDatePicker: DatePickerDialog
    private lateinit var toDatePicker: DatePickerDialog
    private var toDate=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fromDateCalendar = Calendar.getInstance()
        val fromDatePickerListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                fromDateCalendar.set(Calendar.YEAR, year)
                fromDateCalendar.set(Calendar.MONTH, monthOfYear)
                fromDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                fromDate = fromDateCalendar.get(Calendar.YEAR).toString() + "-" + (fromDateCalendar.get(
                        Calendar.MONTH
                    ) + 1) + "-" + fromDateCalendar.get(Calendar.DAY_OF_MONTH)
                //etFromDate.setText(fromDate);
                toDatePicker.datePicker.minDate = fromDateCalendar.timeInMillis
            tvfromDate.text = fromDate
            }

        val toDateCalendar = Calendar.getInstance()
        val toDatePickerListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                toDateCalendar.set(Calendar.YEAR, year)
                toDateCalendar.set(Calendar.MONTH, monthOfYear)
                toDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                toDate = toDateCalendar.get(Calendar.YEAR).toString() + "-" + (toDateCalendar.get(
                    Calendar.MONTH
                ) + 1) + "-" + toDateCalendar.get(Calendar.DAY_OF_MONTH)
                //etToDate.setText(toDate);

            tvEndDate.text = toDate
            }

        toDatePicker = DatePickerDialog(
            this,
            toDatePickerListener,
            toDateCalendar.get(Calendar.YEAR),
            toDateCalendar.get(Calendar.MONTH),
            toDateCalendar.get(Calendar.DAY_OF_MONTH)
        )
        toDatePicker.datePicker.maxDate = Calendar.getInstance().timeInMillis

         fromDatePicker = DatePickerDialog(
            this,
            fromDatePickerListener,
            fromDateCalendar.get(Calendar.YEAR),
            fromDateCalendar.get(Calendar.MONTH),
            fromDateCalendar.get(Calendar.DAY_OF_MONTH)
        )
        fromDatePicker.datePicker.maxDate = Calendar.getInstance().timeInMillis


        tvFrom.setOnClickListener {
            fromDatePicker.show()

        }
        tvTo.setOnClickListener {
            toDatePicker.show()
        }



    }
}