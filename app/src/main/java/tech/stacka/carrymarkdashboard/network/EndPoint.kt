package tech.stacka.carrymarkdashboard.network

object EndPoint {
    /*** USER */
    const val baseUrl1 = "http://15.206.134.157:3000/"
    const val baseUrl2 = "http://15.206.134.157:3001/"
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

}