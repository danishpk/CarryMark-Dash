package tech.stacka.carrymarkdashboard.activity.employee.employeeDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.toolbar_child.*
import tech.stacka.carrymarkdashboard.R

class EmployeeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_detail)

        nav_back.setOnClickListener {
            finish()
        }
    }
}