package tech.stacka.carrymarkdashboard.activity.order.orderDetail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.itextpdf.text.*
import com.itextpdf.text.PageSize.A4
import com.itextpdf.text.pdf.PdfContentByte
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.toolbar_child.*
import org.json.JSONArray
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.order.orderList.OrderListActivity
import tech.stacka.carrymarkdashboard.adapter.OrderDetailList
import tech.stacka.carrymarkdashboard.models.ArrOrderProduct
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.ErrCommon
import tech.stacka.carrymarkdashboard.models.OrderDetailResponse
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.AlertHelper
import tech.stacka.carrymarkdashboard.utils.Utilities
import tech.stacka.carrymarkexecutive.activity.order.orderDetail.OrderDetailPresenter
import tech.stacka.carrymarkexecutive.activity.order.orderDetail.OrderDetailView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderDetailActivity : AppCompatActivity(), OrderDetailView {
    var strToken:String=""

    private lateinit var orderDetailListAdapter: OrderDetailList
    var strOrderId:String=""
    var strOrderStatus:String=""
    var arrOrderProduct=ArrayList<ArrOrderProduct>()
    val colorPrimary = BaseColor(40, 116, 240)
    val PADDING_EDGE = 40f
    val TEXT_TOP_PADDING = 3f
    val TABLE_TOP_PADDING = 10f
    val TEXT_TOP_PADDING_EXTRA = 30f
    val BILL_DETAILS_TOP_PADDING = 80f
    var status=""
    private var dblDiscount:Double=0.0
    private var strShopName:String=""
    private var strGSTNo:String=""
    private var strPaymentMethod:String=""
    private var strDistributorName:String=""
    private var dblTotalPrice: Double = 0.0
    private var productValue: Double = 0.0
    val presenter= OrderDetailPresenter(this,this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        val orderId = intent.getStringExtra("orderID")
        tvOrderId.text = orderId

        nav_back.setOnClickListener {
            startActivity(Intent(this@OrderDetailActivity,OrderListActivity::class.java))
            finish()
        }
        rvOrderDetails.layoutManager = LinearLayoutManager(this)
        strToken= SharedPrefManager.getInstance(applicationContext).user.strToken!!

        if(Utilities.checkInternetConnection(this)) {
            presenter.orderDetails(strToken,orderId)
        }else{
            AlertHelper.showNoInternetSnackBar(this@OrderDetailActivity, object :
                AlertHelper.SnackBarListener {
                override fun onOkClick() {
                    presenter.orderDetails(strToken,orderId)
                }
            })
        }



        cl_one.setOnClickListener {
            if (cl_two.visibility == View.VISIBLE) {
                cl_two.visibility = View.GONE
                Glide.with(this).load(R.drawable.ic_arrow_right).centerCrop().into(img_icon)
            } else {
                cl_two.visibility = View.VISIBLE
                Glide.with(this).load(R.drawable.ic_arrow_down).centerCrop().into(img_icon)
            }
        }

        btUpdateStatus.setOnClickListener {
             status = sp_editStatus.selectedItem.toString().trim()
            if(Utilities.checkInternetConnection(this)) {
                presenter.updateOrder(strToken,strOrderId,status)
            }else{
                AlertHelper.showNoInternetSnackBar(this@OrderDetailActivity, object :
                    AlertHelper.SnackBarListener {
                    override fun onOkClick() {
                        presenter.updateOrder(strToken,strOrderId,status)
                    }
                })
            }

        }

        sp_editStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    status = sp_editStatus.selectedItem.toString().trim()
                    tvOrderStatus.text=status

                }
            }

        }

        btExport.setOnClickListener{
            Dexter.withActivity(this)
                .withPermissions(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                        if (report.areAllPermissionsGranted()) {

                            val doc = Document(A4, 0f, 0f, 0f, 0f)
                            val outPath =
                                getExternalFilesDir(null).toString() + "/my_invoice.pdf" //location where the pdf will store
                            Log.d("loc", outPath)
                            val writer = PdfWriter.getInstance(doc, FileOutputStream(outPath))
                            doc.open()
                            //Header Column Init with width nad no. of columns
                            initInvoiceHeader(doc)
                            doc.setMargins(0f, 0f, PADDING_EDGE, PADDING_EDGE)
                            initBillDetails(doc)
                            //  addLine(writer)
                            initTableHeader(doc)
                            initItemsTable(doc)
                            initPriceDetails(doc)
                            initFooter(doc)
                            doc.close()

                            val file = File(outPath)
                            val path: Uri = FileProvider.getUriForFile(
                                applicationContext,
                                "tech.stacka.carrymarkdashboard.provider",
                                file
                            )
                            try {
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.setDataAndType(path, "application/pdf")
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                startActivity(intent)
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(applicationContext,"no permission", Toast.LENGTH_SHORT).show()
                            }


                        } else {
                            Toast.makeText(applicationContext,"this is toast message", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()

        }
    }

    private fun initFooter(doc: Document) {

        val footerTable = PdfPTable(1)
        footerTable.totalWidth = A4.width
        footerTable.isLockedWidth = true
        val thankYouCell =
            PdfPCell(Phrase("THANK YOU FOR YOUR BUSINESS"))
        thankYouCell.border = Rectangle.NO_BORDER
        thankYouCell.paddingLeft = PADDING_EDGE
        thankYouCell.paddingTop = PADDING_EDGE
        thankYouCell.horizontalAlignment = Rectangle.ALIGN_CENTER
        footerTable.addCell(thankYouCell)
        doc.add(footerTable)

    }

    private fun initPriceDetails(doc: Document) {
        val priceDetailsTable = PdfPTable(2)
        priceDetailsTable.totalWidth = A4.width
        priceDetailsTable.setWidths(floatArrayOf(5f, 2f))
        priceDetailsTable.isLockedWidth = true


        val txtSubTotalCell = PdfPCell(Phrase("Total Price"))
        txtSubTotalCell.border = Rectangle.NO_BORDER
        txtSubTotalCell.horizontalAlignment = Rectangle.ALIGN_RIGHT
        txtSubTotalCell.paddingTop = TEXT_TOP_PADDING_EXTRA
        priceDetailsTable.addCell(txtSubTotalCell)

        val totalPriceCell = PdfPCell(Phrase("Rs $dblTotalPrice"))
        totalPriceCell.border = Rectangle.NO_BORDER
        totalPriceCell.horizontalAlignment = Rectangle.ALIGN_RIGHT
        totalPriceCell.paddingTop = TEXT_TOP_PADDING_EXTRA
        totalPriceCell.paddingRight = PADDING_EDGE
        priceDetailsTable.addCell(totalPriceCell)


        val txtTaxCell = PdfPCell(Phrase("Discount "))
        txtTaxCell.border = Rectangle.NO_BORDER
        txtTaxCell.horizontalAlignment = Rectangle.ALIGN_RIGHT
        txtTaxCell.paddingTop = TEXT_TOP_PADDING
        priceDetailsTable.addCell(txtTaxCell)

        val totalTaxCell = PdfPCell(Phrase("Rs "+dblDiscount.toString()))
        totalTaxCell.border = Rectangle.NO_BORDER
        totalTaxCell.horizontalAlignment = Rectangle.ALIGN_RIGHT
        totalTaxCell.paddingTop = TEXT_TOP_PADDING
        totalTaxCell.paddingRight = PADDING_EDGE
        priceDetailsTable.addCell(totalTaxCell)

        val txtTotalCell = PdfPCell(Phrase("Payable Amount : "))
        txtTotalCell.border = Rectangle.NO_BORDER
        txtTotalCell.horizontalAlignment = Rectangle.ALIGN_RIGHT
        txtTotalCell.paddingTop = TEXT_TOP_PADDING
        txtTotalCell.paddingBottom = TEXT_TOP_PADDING
        txtTotalCell.paddingLeft = PADDING_EDGE
        priceDetailsTable.addCell(txtTotalCell)

        val totalCell = PdfPCell( Phrase("Rs "+productValue.toString()))
        totalCell.border = Rectangle.NO_BORDER
        totalCell.horizontalAlignment = Rectangle.ALIGN_RIGHT
        totalCell.paddingTop = TEXT_TOP_PADDING
        totalCell.paddingBottom = TEXT_TOP_PADDING
        totalCell.paddingRight = PADDING_EDGE
        priceDetailsTable.addCell(totalCell)

        doc.add(priceDetailsTable)
    }

    private fun initItemsTable(doc: Document) {
        val itemsTable = PdfPTable(5)
        itemsTable.isLockedWidth = true
        itemsTable.totalWidth = A4.width
        itemsTable.setWidths(floatArrayOf(1.5f, .5f, .6f, 1f, 1.1f))


        for (item in arrOrderProduct ) {
            itemsTable.deleteBodyRows()
            val itemdetails = PdfPTable(1)


            val itemName = PdfPCell(Phrase(item.strName))
            itemName.border = Rectangle.NO_BORDER
            itemdetails.addCell(itemName)






            /*val itemDesc = PdfPCell(Phrase(m.productTitle))
            itemDesc.border = Rectangle.NO_BORDER

            itemdetails.addCell(itemDesc)*/


            val itemCell = PdfPCell(itemdetails)
            itemCell.border = Rectangle.NO_BORDER
            itemCell.paddingTop = TABLE_TOP_PADDING
            itemCell.paddingLeft = PADDING_EDGE
            itemsTable.addCell(itemCell)


//            val itemID = PdfPCell(Phrase(item.strOGProductId.toString()))
//            itemID.border = Rectangle.NO_BORDER
//            itemID.paddingTop = TABLE_TOP_PADDING
//            itemID.paddingLeft = PADDING_EDGE
//            itemsTable.addCell(itemID)


            val quantityCell = PdfPCell(Phrase(item.dblQty.toString()))
            quantityCell.border = Rectangle.NO_BORDER
            quantityCell.horizontalAlignment = Rectangle.ALIGN_RIGHT
            quantityCell.paddingTop = TABLE_TOP_PADDING
            itemsTable.addCell(quantityCell)

            val vat = PdfPCell(Phrase(item.dblOfferQty.toString()))
            vat.border = Rectangle.NO_BORDER
            vat.horizontalAlignment = Rectangle.ALIGN_RIGHT
            vat.paddingTop = TABLE_TOP_PADDING
            itemsTable.addCell(vat)

            val unitPrice = PdfPCell(Phrase("Rs ${item.dblAmount}"))
            unitPrice.border = Rectangle.NO_BORDER
            unitPrice.horizontalAlignment = Rectangle.ALIGN_RIGHT
            unitPrice.paddingTop = TABLE_TOP_PADDING
            itemsTable.addCell(unitPrice)


            val netAmount = PdfPCell(Phrase("Rs ${item.dblAmount.toString().toDouble()*item.dblQty.toString().toDouble()}"))
            netAmount.horizontalAlignment = Rectangle.ALIGN_RIGHT
            netAmount.border = Rectangle.NO_BORDER
            netAmount.paddingTop = TABLE_TOP_PADDING
            netAmount.paddingRight = PADDING_EDGE
            itemsTable.addCell(netAmount)
            doc.add(itemsTable)
        }
    }


    private fun initTableHeader(doc: Document) {

        doc.add(Paragraph("\n\n\n\n\n")) //adds blank line to place table header below the line

        val titleTable = PdfPTable(5)
        titleTable.isLockedWidth = true
        titleTable.totalWidth = A4.width
        titleTable.setWidths(floatArrayOf(1.5f, .5f, .6f, 1f, 1.1f))


        val itemCell = PdfPCell(Phrase("Product Name"))
        itemCell.border = Rectangle.NO_BORDER
        itemCell.paddingTop = TABLE_TOP_PADDING
        itemCell.paddingBottom = TABLE_TOP_PADDING
        itemCell.paddingLeft = PADDING_EDGE
        titleTable.addCell(itemCell)

//        val itemIdCell = PdfPCell(Phrase("Product Id"))
//        itemIdCell.border = Rectangle.NO_BORDER
//        itemIdCell.paddingTop = TABLE_TOP_PADDING
//        itemIdCell.paddingBottom = TABLE_TOP_PADDING
//        itemIdCell.paddingLeft = PADDING_EDGE
//        titleTable.addCell(itemIdCell)


        val quantityCell = PdfPCell(Phrase("Quantity"))
        quantityCell.border = Rectangle.NO_BORDER
        quantityCell.horizontalAlignment = Rectangle.ALIGN_RIGHT
        quantityCell.paddingBottom = TABLE_TOP_PADDING
        quantityCell.paddingTop = TABLE_TOP_PADDING
        titleTable.addCell(quantityCell)

        val vat = PdfPCell(Phrase("Offer Quantity"))
        vat.border = Rectangle.NO_BORDER
        vat.horizontalAlignment = Rectangle.ALIGN_RIGHT
        vat.paddingBottom = TABLE_TOP_PADDING
        vat.paddingTop = TABLE_TOP_PADDING
        titleTable.addCell(vat)

        val unitPrice = PdfPCell(Phrase("Unit Price"))
        unitPrice.border = Rectangle.NO_BORDER
        unitPrice.horizontalAlignment = Rectangle.ALIGN_RIGHT
        unitPrice.paddingBottom = TABLE_TOP_PADDING
        unitPrice.paddingTop = TABLE_TOP_PADDING
        titleTable.addCell(unitPrice)



        val netAmount = PdfPCell(Phrase("Net Amount"))
        netAmount.horizontalAlignment = Rectangle.ALIGN_RIGHT
        netAmount.border = Rectangle.NO_BORDER
        netAmount.paddingTop = TABLE_TOP_PADDING
        netAmount.paddingBottom = TABLE_TOP_PADDING
        netAmount.paddingRight = PADDING_EDGE
        titleTable.addCell(netAmount)
        doc.add(titleTable)
    }


    private fun addLine(writer: PdfWriter) {
        val canvas: PdfContentByte = writer.directContent
        canvas.setColorStroke(colorPrimary)
        canvas.moveTo(40.0, 480.0)

        // Drawing the line
        canvas.lineTo(560.0, 480.0)
        canvas.setLineWidth(3f)

        // Closing the path stroke
        canvas.closePathStroke()
    }

    private fun initBillDetails(doc: Document) {
        val billDetailsTable =
            PdfPTable(3)  // table to show customer address, invoice, date and total amount
        billDetailsTable.setWidths(
            floatArrayOf(
                2f,
                1.82f,
                2f
            )
        )
        billDetailsTable.isLockedWidth = true
        billDetailsTable.paddingTop = 30f

        billDetailsTable.totalWidth =
            A4.width // set content width to fill document
        val customerAddressTable = PdfPTable(1)

        val txtBilledToCell = PdfPCell(
            Phrase(
                "Ordered From"
            )
        )
        txtBilledToCell.border = Rectangle.NO_BORDER
        customerAddressTable.addCell(
            txtBilledToCell
        )

        val clientAddressCell1 = PdfPCell(
//            Paragraph(
//                getString(R.string.appIdentity)
//
//            )
            Paragraph("Shop : $strShopName")
        )
        clientAddressCell1.border = Rectangle.NO_BORDER
        clientAddressCell1.paddingTop = TEXT_TOP_PADDING
        customerAddressTable.addCell(clientAddressCell1)

        val clientAddressCell2 = PdfPCell(
            Paragraph("GST No. : $strGSTNo")
        )
        clientAddressCell2.border = Rectangle.NO_BORDER
        clientAddressCell2.paddingTop = TEXT_TOP_PADDING
        customerAddressTable.addCell(clientAddressCell2)


        val clientAddressCell3 = PdfPCell(
            Paragraph("Payment Method : $strPaymentMethod")
        )
        clientAddressCell3.border = Rectangle.NO_BORDER
        clientAddressCell3.paddingTop = TEXT_TOP_PADDING
        customerAddressTable.addCell(clientAddressCell3)


        val clientAddressCell4 = PdfPCell(
            Paragraph("Distributor : $strDistributorName")
            //        Paragraph("")
        )
        clientAddressCell4.border = Rectangle.NO_BORDER
        clientAddressCell4.paddingTop = TEXT_TOP_PADDING
        customerAddressTable.addCell(clientAddressCell4)

        val billDetailsCell1 = PdfPCell(customerAddressTable)
        billDetailsCell1.border = Rectangle.NO_BORDER

        billDetailsCell1.paddingTop = BILL_DETAILS_TOP_PADDING

        billDetailsCell1.paddingLeft = PADDING_EDGE

        billDetailsTable.addCell(billDetailsCell1)


        val totalPriceTable = PdfPTable(1)
        val txtInvoiceTotal = PdfPCell(Phrase(""))
        txtInvoiceTotal.paddingTop = BILL_DETAILS_TOP_PADDING
        txtInvoiceTotal.horizontalAlignment = Rectangle.ALIGN_RIGHT
        txtInvoiceTotal.border = Rectangle.NO_BORDER
        totalPriceTable.addCell(txtInvoiceTotal)


        val totalAomountCell = PdfPCell(Phrase(""))
        totalAomountCell.border = Rectangle.NO_BORDER
        totalAomountCell.horizontalAlignment = Rectangle.ALIGN_RIGHT
        totalPriceTable.addCell(totalAomountCell)
        val dataTotalAmount = PdfPCell(totalPriceTable)
        dataTotalAmount.border = Rectangle.NO_BORDER
        dataTotalAmount.paddingRight = PADDING_EDGE
        dataTotalAmount.verticalAlignment = Rectangle.ALIGN_BOTTOM
        billDetailsTable.addCell(dataTotalAmount)


        val invoiceNumAndData = PdfPTable(1)
        val txtInvoiceNumber = PdfPCell(Phrase("P.O Number"))
        //txtInvoiceNumber.horizontalAlignment = Rectangle.ALIGN_RIGHT
        txtInvoiceNumber.paddingTop = BILL_DETAILS_TOP_PADDING
        txtInvoiceNumber.border = Rectangle.NO_BORDER
        invoiceNumAndData.addCell(txtInvoiceNumber)

        val invoiceNumber = PdfPCell(Phrase(strOrderId))
        //invoiceNumber.horizontalAlignment = Rectangle.ALIGN_RIGHT
        invoiceNumber.border = Rectangle.NO_BORDER
        invoiceNumber.paddingTop = TEXT_TOP_PADDING
        invoiceNumAndData.addCell(invoiceNumber)


        val txtDate = PdfPCell(Phrase("Date Of Issue"))
        // txtDate.horizontalAlignment = Rectangle.ALIGN_RIGHT
        txtDate.paddingTop = TEXT_TOP_PADDING_EXTRA
        txtDate.border = Rectangle.NO_BORDER
        invoiceNumAndData.addCell(txtDate)

        var date = Date()
        val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
        val answer: String = formatter.format(date)

        val dateCell = PdfPCell(Phrase(answer))
        // dateCell.horizontalAlignment = Rectangle.ALIGN_RIGHT
        dateCell.border = Rectangle.NO_BORDER
        invoiceNumAndData.addCell(dateCell)

        val dataInvoiceNumAndData = PdfPCell(invoiceNumAndData)
        dataInvoiceNumAndData.border = Rectangle.NO_BORDER
        billDetailsTable.addCell(dataInvoiceNumAndData)
        doc.add(billDetailsTable)
    }

    private fun initInvoiceHeader(doc: Document) {
        val d = resources.getDrawable(R.drawable.gear)
        val bitDw = d as BitmapDrawable
        val bmp = bitDw.bitmap
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = Image.getInstance(stream.toByteArray())
        val headerTable = PdfPTable(3)
        headerTable.setWidths(
            floatArrayOf(
                1.3f,
                1f,
                1f
            )
        ) // adds 3 colomn horizontally
        headerTable.isLockedWidth = true
        headerTable.totalWidth = A4.width // set content width to fill document
        val cell = PdfPCell(Image.getInstance(image)) // Logo Cell
        cell.border = Rectangle.NO_BORDER // Removes border
        cell.paddingTop = TEXT_TOP_PADDING_EXTRA // sets padding
        cell.paddingRight = TABLE_TOP_PADDING
        cell.paddingLeft = PADDING_EDGE
        cell.horizontalAlignment = Rectangle.ALIGN_LEFT
        cell.paddingBottom = TEXT_TOP_PADDING_EXTRA

        cell.backgroundColor = colorPrimary // sets background color
        cell.horizontalAlignment = Element.ALIGN_CENTER
        headerTable.addCell(cell) // Adds first cell with logo

        val contactTable =
            PdfPTable(1) // new vertical table for contact details
        val phoneCell =
            PdfPCell(
                Paragraph(
                    SharedPrefManager.getInstance(this).user.strMobileNo
                )
            )
        phoneCell.border = Rectangle.NO_BORDER
        phoneCell.horizontalAlignment = Element.ALIGN_RIGHT
        phoneCell.paddingTop = TEXT_TOP_PADDING

        contactTable.addCell(phoneCell)

        val emailCellCell = PdfPCell(Phrase(""))
        emailCellCell.border = Rectangle.NO_BORDER
        emailCellCell.horizontalAlignment = Element.ALIGN_RIGHT
        emailCellCell.paddingTop = TEXT_TOP_PADDING

        contactTable.addCell(emailCellCell)

        val webCell = PdfPCell(Phrase(getString(R.string.appIdentity)))
        webCell.border = Rectangle.NO_BORDER
        webCell.paddingTop = TEXT_TOP_PADDING
        webCell.horizontalAlignment = Element.ALIGN_RIGHT

        contactTable.addCell(webCell)

        val headCell = PdfPCell(contactTable)
        headCell.border = Rectangle.NO_BORDER
        headCell.horizontalAlignment = Element.ALIGN_RIGHT
        headCell.verticalAlignment = Element.ALIGN_MIDDLE
        headCell.backgroundColor = colorPrimary
        headerTable.addCell(headCell)

        val address = PdfPTable(1)
        val line1 = PdfPCell(
            Paragraph(
                "Address Line 1"

            )
        )
        line1.border = Rectangle.NO_BORDER
        line1.paddingTop = TEXT_TOP_PADDING
        line1.horizontalAlignment = Element.ALIGN_RIGHT

        address.addCell(line1)

        val line2 = PdfPCell(Paragraph("Address Line 2"))
        line2.border = Rectangle.NO_BORDER
        line2.paddingTop = TEXT_TOP_PADDING
        line2.horizontalAlignment = Element.ALIGN_RIGHT

        address.addCell(line2)

        val line3 = PdfPCell(Paragraph("Address Line 3"))
        line3.border = Rectangle.NO_BORDER
        line3.paddingTop = TEXT_TOP_PADDING
        line3.horizontalAlignment = Element.ALIGN_RIGHT

        address.addCell(line3)


        val addressHeadCell = PdfPCell(address)
        addressHeadCell.border = Rectangle.NO_BORDER
        addressHeadCell.setLeading(22f, 25f)
        addressHeadCell.horizontalAlignment = Element.ALIGN_RIGHT
        addressHeadCell.verticalAlignment = Element.ALIGN_MIDDLE
        addressHeadCell.backgroundColor = colorPrimary
        addressHeadCell.paddingRight = PADDING_EDGE
        //headerTable.addCell(addressHeadCell)

        //doc.add(headerTable)
    }


    override fun onOrderDetailSuccess(apiResponse: OrderDetailResponse) {
        if(apiResponse!= null) {
            strOrderId = apiResponse.strOrderId
            productValue = apiResponse.dblTotalOrderAmount.toDouble()
            dblTotalPrice=apiResponse.dblTotalPrice.toDouble()
            dblDiscount=apiResponse.dblDiscountPrice.toDouble()
            strShopName=apiResponse.strShopName
            strDistributorName=apiResponse.strDistributerName
            strGSTNo=apiResponse.strGSTNo
            strPaymentMethod=apiResponse.strModePayment
            tvOrderId.text=apiResponse.strOrderId
            tvRetailerId.text = apiResponse.strUserId
            tvShopName.text = apiResponse.strShopName
            //tvShopArea.text = apiResponse.objAddress.strCity
            if(apiResponse.strGSTNo!=null) {
                tvGST.text = apiResponse.strGSTNo
            }
            tvPhoneNumber.text = apiResponse.strMobileNo
            if(apiResponse.strNote!=null) {
                tvNotes.text = apiResponse.strNote
            }
            tvTime.text=apiResponse.strCreatedTime
            tvAmount.text = "${apiResponse.dblTotalOrderAmount}₹"
            tvPaymentMethod.text = apiResponse.strModePayment
            tvDistributor.text=apiResponse.strDistributerName
            tvOrderStatus.text=apiResponse.strOrderStatus
            if(apiResponse.strExecutiveName!=null) {
                tvExecutive.text = apiResponse.strExecutiveName
            }
            arrOrderProduct = apiResponse.arrProducts as ArrayList<ArrOrderProduct>
            val orderLayoutManager = LinearLayoutManager(this)
            rvOrderDetails.layoutManager = orderLayoutManager
            orderDetailListAdapter = OrderDetailList(
                this, arrOrderProduct
            )
            rvOrderDetails.adapter = orderDetailListAdapter


        }

    }

    override fun onOrderDetailNull(apiResponse: OrderDetailResponse) {

    }

    override fun onOrderDetailFailed(apiResponse: ErrCommon) {
        AlertHelper.showNoInternetSnackBar(this@OrderDetailActivity, object :
            AlertHelper.SnackBarListener {
            override fun onOkClick() {
            }
        })
    }

    override fun onOrderServerFailed(toString: String) {

    }

    override fun onOrderUpdateSuccess(apiResponse: DefaultResponse) {
        tvOrderStatus.text=status
        AlertHelper.showOKSnackBarAlert(this@OrderDetailActivity,"Order updated successfully")
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    override fun onOrderUpdateNull(apiResponse: DefaultResponse) {

    }

    override fun onOrderUpdateFailed(apiResponse: JSONArray) {
        val listData = ArrayList<String>()
        for (i in 0 until apiResponse.length()) {
            listData += apiResponse.getString(i)
            Log.e("ListData",listData.toString())
            for(i in listData){
                when (i) {
                    "CANT_UPDATE" -> {
                        AlertHelper.showOKSnackBarAlert(this@OrderDetailActivity, "Can't be $status")
                    }
                    "INVALID_STATUS" -> {
                        AlertHelper.showOKSnackBarAlert(this@OrderDetailActivity, "Can't be marked as $status")
                    }
                    else -> {
                        AlertHelper.showOKSnackBarAlert(this@OrderDetailActivity, "Can't be $status")
                    }
                }
            }
        }
    }

    override fun onOrderUpdateFailedServerError(toString: String) {
        AlertHelper.showNoInternetSnackBar(this@OrderDetailActivity, object :
            AlertHelper.SnackBarListener {
            override fun onOkClick() {
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@OrderDetailActivity,OrderListActivity::class.java))
        finish()
    }
}