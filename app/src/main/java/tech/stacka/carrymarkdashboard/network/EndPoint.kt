package tech.stacka.carrymarkdashboard.network

object EndPoint {
    /*** USER */
    const val baseUrl1 = "http://15.206.134.157:3000/"
    const val baseUrl2 = "http://15.206.134.157:3001/"
    const val baseUrl3 = "http://15.206.134.157:3002/"
    const val strAppInfo = "strAppInfo: CAPTIONTOT"
    const val userLogin = "user/login_user"
    const val productList = "product/get_product"
    const val addProduct = "product/create_product"
    const val uploadProductImage = "file/files_upload"
    const val addProductCategoryList = "common/get_autocomplete"
    const val getMaster = "master/get_master"
    const val userList = "user/get_user"
    const val createUser = "user/register_user"
    const val deleteProduct = "product/delete_product"
    const val createMaster = "master/create_master"
    const val deleteMaster = "master/delete_master"
    const val productDetails = "product/get_product_details"
    const val orderList="order/get_order"
    const val orderDetail = "order/get_order_details"
    const val orderUpdate = "order/update_order"
    const val updateRetailer = "user/inactivate_user"
    const val getReports="report/get_report"
    const val getEmpLocations="employee/get_geo_location"
    const val createTarget="employee/create_target"
    const val notificationList="firebase/get_notifications"
    const val createNotification="firebase/create_notifications"
    const val deleteUser="user/delete_user"



}