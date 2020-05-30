<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/22
  Time: 15:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>结算页面</title>
    <link rel="stylesheet" href="admin/css/bootstrap.min.css" />
    <link rel="stylesheet" href="admin/css/font-awesome.min.css" />
    <script src="admin/js/vue.js"></script>
    <script src="admin/js/jquery.min.js"></script>
    <script src="admin/js/bootstrap.min.js"></script>
    <style>
        h4.title {
            font-weight: 800;
        }
        .mgl30 {
            margin-left: 30px;
        }
        #allAddressTable a {
            display: none;
        }
        /** 当鼠标划到 tr上的时候，让它下面的 a标签展示 */
        #allAddressTable tr:hover a{
            display: inline-block;
        }

    </style>
</head>
<body>
<nav class="navbar navbar-default navbar-inverse navbar-static-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <a href="index.jsp" class="navbar-brand" style="color: #ffffff;"><i class="fa fa-sign-language"></i> 寻欢易购</a>
        </div>
        <p class="navbar-text navbar-right login-text">
            <a id="loginText" href="login.jsp">登录</a>
        </p>
        <p class="navbar-text navbar-right">
            <a href="register.jsp">注册</a>
        </p>
    </div>
</nav>
<div class="container-fluid" id="app">
    <div class="row">
        <div class="col-xs-8 col-xs-push-2">
            <h4 class="title">收货人信息</h4>
            <p id="pp" class="alert alert-danger">
                <input type="hidden" id="takeDeliveryAddressId" v-model="defalutTakeDeliveryAddressId">
                <span style="font-weight: 800;">{{defaultName}}</span>
                <span class="mgl30">{{defaultDetail}}</span>
                <span class="mgl30">{{defaultPhone}}</span>
            </p>
            <a data-toggle="collapse" href="#moreAddress">更多地址</a>

            <div class="collapse"  id="moreAddress" style="margin-top: 15px;">
                <table id="allAddressTable" class="table">
                    <tr v-for="addr in allAddresses">
                        <td>
                            {{addr.name}} <span class="label label-success" v-if="addr.status == 1">默认</span>
                        </td>
                        <td>{{addr.detail}}</td>
                        <td>{{addr.phone}}</td>
                        <td style="width: 130px;">
                            <a href="javascript: void(0);" :aid="addr.id" onclick="setTakeDeliveryAddress(this)">设为收货地址</a>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-8 col-xs-push-2">
            <h4 class="title">送货清单</h4>
            <table class="table">
                <thead>
                <tr>
                    <td>商品</td>
                    <td>单价</td>
                    <td>数量</td>
                </tr>
                </thead>
                <tbody >
                <tr v-for="g in goods">
                    <td style="max-width: 200px;">
                        <div class="media">
                            <div class="media-left">
                                <a href="javascript: void(0);">
                                    <img class="media-object" height="80px" width="62px" :src="g.imageSrc">
                                </a>
                            </div>
                            <div class="media-body">
                                <h4 class="media-heading">{{g.title}}</h4>
                            </div>
                        </div>
                    </td>
                    <td>{{g.price}}</td>
                    <td>{{g.num}}</td>
                </tr>
                </tbody>
                <tfoot>
                <tr v-if="goods.length != 0" style="text-align: right;">
                    <td colspan="5">
                        总计：￥{{totalPrice}} <button onclick="ensureOrder()" class="btn btn-danger">确认订单</button>
                    </td>
                </tr>
                </tfoot>
            </table>
        </div>
    </div>
</div>
<div class="modal" id="payModal" data-backdrop="static">
    <div class="modal-dialog" style="margin-top: 120px;">
        <div class="modal-content">
            <div class="modal-header">
                <button onclick="destoryPayModal()" class="close">
                    <span>&times;</span>
                </button>
                <h3 class="modal-title">支付信息</h3>
            </div>
            <div class="modal-body">
                <h4 style="font-weight: 800;"><span>应付金额: </span><span style="color: red;">￥50000</span></h4>
                <h4 style="font-weight: 800;">支付密码: </h4>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" onclick="destoryPayModal()">取消支付</button>
                <button type="button" class="btn btn-success">确认支付</button>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    var vm = new Vue({
        el: '#app',
        data: {
            defalutTakeDeliveryAddressId: 0,
            defaultDetail: '',    //默认详情信息
            defaultName: '',       //默认的名字
            defaultPhone: '',      // 默认电话
            allAddresses: [],     //其他的地址信息
            goods: [],
            totalPrice: 0
        }
    })

    // 获取地址
    function getAddress() {
        $.ajax({
            url: '${pageContext.servletContext.contextPath}/addressServlet',
            type: 'get',
            data: {method: 'getAddressOfUser'},
            dataType: 'json',
            success: function(_data) {
                vm.allAddresses = [];
                for(var i = 0; i < _data.length; i++) {
                    var addr = _data[i];  //取到每个地址
                    vm.allAddresses.push(addr);
                    if(addr.takeDeliveryStatus == 1) {  // 收货地址
                        vm.defalutTakeDeliveryAddressId = addr.id; //设置默认收货地址的id
                        vm.defaultDetail = addr.detail;
                        vm.defaultName = addr.name;
                        vm.defaultPhone = addr.phone;
                    }
                }
            }
        })
    }

    function getGoodsInfo() {
        // 转换为json数据: {'1001': 1, '1002': 2}
        var localShoppingCart = JSON.parse(localStorage.getItem('local_shopping_cart'));
        var goodIds = Object.keys(localShoppingCart); //取到所有的商品的id, 返回值为数组：['1001', '1002']

        // join(‘,’) 只用指定的字符将数组拼接成字符串
        var goodIdsStr = goodIds.join(','); // ['1001', '1002', '1003'] -> 1001,1002,1003

        $.ajax({
            url: '${pageContext.servletContext.contextPath}/cart',
            type: 'get',
            dataType: 'json',
            data: {ids: goodIdsStr},
            success:function(_data) {  // [{}, {}, {}]
                var totalPrice = 0;
                for(var i = 0; i < _data.length; i++) {
                    var good = _data[i];  //获取到每件商品
                    var goodId = good.goodId; //获取商品的id, 目的是获取商品的数量
                    // 动态的添加的属性, 购买的商品数量
                    good.num = localShoppingCart['' + goodId];

                    totalPrice += (good.num * good.price); //计算总的价格
                    vm.goods.push(good);
                }
                vm.totalPrice = totalPrice;
            }
        })
    }

    getAddress();  //获取收货地址
    getGoodsInfo(); //获取商品信息

    //设置当前地址为收货地址
    function setTakeDeliveryAddress(aTag) {
        var currentAddressId = $(aTag).attr('aid'); //获取当前地址的id

        $.ajax({
            url: '${pageContext.servletContext.contextPath}/addressServlet',
            data: {addressId: currentAddressId, method: 'changeTakeDeliveryAddress'},
            dataType: 'json',
            type: 'get',
            success: function (_data) {
                if(_data.code > 0) {  //更新收货地址成功
                    getAddress();  //重新获取地址信息
                }else {
                    console.log(_data.msg)
                }
            }
        })
    }

    // 点击 "确认订单"，去支付
    function ensureOrder() {
        var localShoppingCartData = localStorage.getItem('local_shopping_cart');
        var defalutTakeDeliveryAddressId = $('#takeDeliveryAddressId').val();

        // console.log(localShoppingCartData, defalutTakeDeliveryAddressId)
        /**
         * 1. 将所有的商品生产订单。
         * 2. 展示模态框开始支付。
         */
        $.ajax({
            url: '${pageContext.servletContext.contextPath}/order',
            type: 'post',
            dataType: 'json',
            data: {goodsInfo: localShoppingCartData, takeDelivertyAddressId: defalutTakeDeliveryAddressId},
            success: function(_data) {
                console.log(_data);
            }
        })

        // $('#payModal').modal('show');
    }

    // 关闭支付模态框
    function destoryPayModal() {
        $('#payModal').modal('hide');
    }

</script>
</html>
