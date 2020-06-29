package tech.stacka.carrymarkdashboard.activity.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.distributer.addDistributer.AddDistributerActivity
import tech.stacka.carrymarkdashboard.activity.distributer.distributerList.DistributorListActivity
import tech.stacka.carrymarkdashboard.activity.employee.addEmployee.AddEmployeeActivity
import tech.stacka.carrymarkdashboard.activity.employee.employeeList.EmployeeListActivity
import tech.stacka.carrymarkdashboard.activity.master.addMaster.AddMasterActivity
import tech.stacka.carrymarkdashboard.activity.master.masterList.MasterListActivity
import tech.stacka.carrymarkdashboard.activity.product.addProduct.AddProductActivity
import tech.stacka.carrymarkdashboard.activity.product.productList.ProductListActivity
import tech.stacka.carrymarkdashboard.activity.retailer.retailerList.RetailerListActivity

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(main_toolbar)
        nav_toggle.setOnClickListener {
            drawer_layout.openDrawer(Gravity.LEFT)
        }
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_dashboard -> {
                Toast.makeText(this, "Dashboard", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_productList -> {
                startActivity(Intent(this@HomeActivity, ProductListActivity::class.java))
            }
            R.id.nav_addProduct -> {
                startActivity(Intent(this@HomeActivity, AddProductActivity::class.java))
            }
            R.id.nav_employeeList -> {
                startActivity(Intent(this@HomeActivity, EmployeeListActivity::class.java))
            }
            R.id.nav_distributorList -> {
                startActivity(Intent(this@HomeActivity, DistributorListActivity::class.java))
            }
            R.id.nav_retailerList -> {
                startActivity(Intent(this@HomeActivity, RetailerListActivity::class.java))
            }
            R.id.nav_addDistributor -> {
                startActivity(Intent(this@HomeActivity, AddDistributerActivity::class.java))
            }
            R.id.nav_addEmployee -> {
                startActivity(Intent(this@HomeActivity, AddEmployeeActivity::class.java))
            }
            R.id.nav_addMaster -> {
                startActivity(Intent(this@HomeActivity, AddMasterActivity::class.java))
            }
            R.id.nav_master -> {
                startActivity(Intent(this@HomeActivity, MasterListActivity::class.java))
            }
        }
        drawer_layout.closeDrawer(Gravity.LEFT)
        return false
    }
}