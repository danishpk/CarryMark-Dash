package tech.stacka.carrymarkdashboard.activity.product.productDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.toolbar_child.*
import tech.stacka.carrymarkdashboard.R

class ProductDetailActivity : AppCompatActivity() {

   // private lateinit var product: Product
    private lateinit var uId: String
    private val TAG = "CartActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        nav_back.setOnClickListener {
            finish()
        }
    }
}