<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/21
  Time: 11:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="admin/css/bootstrap.min.css" />
    <link rel="stylesheet" href="admin/css/font-awesome.min.css" />
    <script src="admin/js/vue.js"></script>
    <script src="admin/js/vendor/jquery-1.12.4.min.js"></script>
    <script src="admin/js/bootstrap.min.js"></script>
    <script src="admin/js/jquery.cookie.min.js"></script>
</head>
<style>
    * {
        margin: 0;
        padding: 0;
    }
    body {
        background-color: #EEEEEE;
    }
    .navbar-text a  {
        color: #ffffff;
    }
    .login-text {
        margin-right: 50px;
    }
    .logo {
        color: #E94F4C;
        font-size: 35px;
        display: inline-block;
    }
    .search-box {
        height: 48px;
    }
    .input-group-addon {
        background-color:#E94F4C;
        border: none;
        color: #ffffff;
    }
    .control-icon {
        position: absolute;
        top: 50%;
    }
    .mgt38 {
        margin-top: 30px;
    }
    .thumbnail a img {
        height: 200px;
        width: 200px;
    }
    .thumbnail .description {
        height: 40px;
        overflow: hidden;
    }
    a:hover {
        text-decoration: none;
    }
    h3, p {
        text-align: center;
    }
    table tr {
        background-color: #ffffff;
    }
</style>
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
<div class="container-fluid">
    <div class="row">
        <div class="col-xs-8 col-xs-push-2">
            <table id="app" class="table">
                <thead>
                <tr>
                    <td>商品</td>
                    <td>单价</td>
                    <td>数量</td>
                    <td>小计</td>
                    <td>操作</td>
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
                    <td>
                        <div class="input-group" style="width: 120px;">
                            <!-- v-if 如果成立就显示该dom元素 -->
                            <span v-if="g.num == 1"  style="background-color: #e2e2e2;" class="input-group-addon">-</span>
                            <!-- v-else必须和v-if放在一起 -->
                            <span :gid="g.goodId" onclick="reduceGoodNum(this)" v-else class="input-group-addon" style="cursor: pointer;">-</span>

                            <input class="form-control" v-model="g.num">
                            <span :gid="g.goodId" class="input-group-addon" onclick="addGoodNum(this)" style="cursor: pointer;">+</span>
                        </div>
                    </td>
                    <td>{{g.price * g.num}}</td>
                    <td>
                        <button :id="g.goodId" onclick="deleteSpecifyGood(this)" class="btn btn-default">删除</button>
                    </td>
                </tr>
                </tbody>
                <tfoot>
                <tr v-if="goods.length != 0">
                    <td colspan="5">
                        已选购 <span id="totalCount" style="color: red;">{{totalNum}}</span> 件商品,
                        总价 <span  style="color: red;" id="totalPrice">{{totalPrice}}</span>
                        <button onclick="gotoSettleCount()" class="btn btn-danger btn-lg">去结算</button>
                    </td>
                </tr>
                <tr v-else>
                    <td colspan="5">暂无商品.<a href="前端首页.html">去购物</a></td>
                </tr>
                </tfoot>
            </table>
        </div>
    </div>
</div>
</body>
<script>

    // 如果用户登录
    if($.cookie('frontedUsername')) {
        $('#loginText').html('欢迎你，' + $.cookie('frontedUsername'))
        $('#loginText').prop('href', 'javascript:void(0);')
    }

    var vm = new Vue({
        el: '#app',
        data: {
            goods: [],
            totalNum: 0,
            totalPrice: 0
        }
    })

    // 进来之后需要展示购物车数据
    var localShoppingCart = localStorage.getItem('local_shopping_cart');

    // 表示本地购物有数据
    if(localShoppingCart) {
        // 转换为json数据: {'1001': 1, '1002': 2}
        localShoppingCart = JSON.parse(localShoppingCart);
        var goodIds = Object.keys(localShoppingCart); //取到所有的商品的id, 返回值为数组：['1001', '1002']

        // join(‘,’) 只用指定的字符将数组拼接成字符串
        var goodIdsStr = goodIds.join(','); // ['1001', '1002', '1003'] -> 1001,1002,1003

        $.ajax({
            url: '${pageContext.servletContext.contextPath}/cart',
            type: 'get',
            dataType: 'json',
            data: {ids: goodIdsStr},
            success:function(_data) {  // [{}, {}, {}]
                var totalNum = 0;  //总数量
                var totalPrice = 0;
                for(var i = 0; i < _data.length; i++) {
                    /**
                     {
                           "createTime": 1572059525000,
                           "goodId": 1001,
                           "id": 1,
                           "imageSrc": "http://localhost/mobile/apple.jpg","price": 6599.00,
                           "status": "1",
                           "stock": 5,
                           "title": "Apple iPhone XS Max (A2104) 64GB 金色 移动联通电信4G手机 双卡双待",
                           "type": "手机",
                           "updateTime": 1572073952000
                           "num":
                         },
                     */
                    var good = _data[i];  //获取到每件商品
                    var goodId = good.goodId; //获取商品的id, 目的是获取商品的数量
                    // 动态的添加的属性, 购买的商品数量
                    good.num = localShoppingCart['' + goodId];

                    totalNum += good.num;   //计算总数量
                    totalPrice += (good.num * good.price); //计算总的价格
                    vm.goods.push(good);
                }
                vm.totalNum = totalNum;
                vm.totalPrice = totalPrice;
            }
        })
    }

    // 减少商品的数量
    function reduceGoodNum(reduceTag) {
        // 区别三：attr()一般修改自定义属性，prop()一般修改布尔值属性
        var goodId = $(reduceTag).attr('gid');

        // 返回值为一个字符串
        //var localShoppingCart = localStorage.getItem('local_shopping_cart');

        var localShoppingCart = JSON.parse(localStorage.getItem('local_shopping_cart'));

        // 点击 “-” 按钮
        var num = --localShoppingCart[goodId];

        // 给对应的商品的数量重新赋值
        localShoppingCart[goodId] = num;

        // 改变商品的数量：1.登录；2.未登录
        changeChartNum(goodId, num, localShoppingCart);
    }

    //添加购物车的数量
    function addGoodNum(addTag) {
        var goodId = $(addTag).attr('gid');  //goodId 是String类型
        // console.log(typeof goodId);
        // 获取本地购物车的数据
        var localShoppingCart = JSON.parse(localStorage.getItem('local_shopping_cart'));

        var num = ++localShoppingCart[goodId];

        localShoppingCart[goodId] = num;

        changeChartNum(goodId, num, localShoppingCart);
    }

    // 改变购物车对应的商品的数量， 分为远程和本地
    function changeChartNum(goodId, num, lsc) {
        // 表示用户登录了, 需要去同步远程服务器端的数据
        if($.cookie('frontedUsername')) {
            $.ajax({
                url: '${pageContext.servletContext.contextPath}/sync-cart',
                data: {method: 'changeGoodNum', goodId: goodId, num: num},
                dataType: 'json',
                type: 'post',
                success: function(_data) {
                    // 远程同步成功了
                    if(_data.code > 0) {
                        localStorage.setItem('local_shopping_cart', JSON.stringify(lsc));
                        reRenderCart(goodId, num)
                    }else {
                        showErrorBox('添加商品到购物失败了.')
                    }
                }
            })
        }else {  // 如果用户没有登录，只用同步本地就行了
            localStorage.setItem('local_shopping_cart', JSON.stringify(lsc));
            reRenderCart(goodId, num)
        }
    }

    // 渲染购物车数据
    function reRenderCart(goodId, num) {
        var goodList = vm.goods;  //取出所有的商品

        goodId = Number(goodId);

        var totalNum = 0;  // 商品的总数量
        var totalPrice = 0;  // 总价格
        // 改变vue中对应的商品的数量
        for(var i = 0; i < goodList.length; i++) {
            var good = goodList[i];
            if(good.goodId == goodId) {
                good.num = num;
            }
            totalNum += good.num;
            totalPrice += (good.num * good.price);
        }

        vm.totalNum = totalNum;
        vm.totalPrice = totalPrice;
    }

    // 删除购物车中指定的商品
    function deleteSpecifyGood(delbtnTag) {
        var goodId = $(delbtnTag).prop('id');

        var localShoppingCart = JSON.parse(localStorage.getItem('local_shopping_cart'));

        // 如果用户登录, 需要删除服务器端购物车中对应的商品
        if($.cookie('frontedUsername')) {
            removeServerCartData(goodId);
        }

        //删除对应的商品
        delete localShoppingCart[goodId]; // {}

        var totalNum = 0;  // 商品的总数量
        var totalPrice = 0;  // 总价格

        //表示清空了
        if(Object.keys(localShoppingCart).length == 0) {
            localStorage.removeItem('local_shopping_cart');  // 将存储购物车的信息删掉
            vm.totalNum = 0;
            vm.totalPrice = 0;
            vm.goods = [];
            return;
        }

        localStorage.setItem('local_shopping_cart', JSON.stringify(localShoppingCart));

        var goodList = vm.goods;  //取出所有的商品
        goodId = Number(goodId); // 将字符串转换为数字类型

        for(var i = 0; i < goodList.length; i++) {
            var good = goodList[i];
            if(goodId == good.goodId) {
                goodList.splice(i, 1);  // 删除指定的商品
                i--;
            }else {  //没有删除的商品要纳入计算
                totalNum += good.num;
                totalPrice += (good.num * good.price);
            }
        }
        vm.totalNum = totalNum;
        vm.totalPrice = totalPrice;
    }

    // 删除掉远程服务器端对应的购物车数据
    function removeServerCartData(goodId) {
        $.ajax({
            url: '${pageContext.servletContext.contextPath}/sync-cart',
            data: {method: 'removeGood', goodId: goodId},
            dataType: 'json',
            type: 'post'
        })
    }

    // 点击 "结算" 按钮，到达结算页面
    function gotoSettleCount() {
        window.location.href = "settleCount.jsp";
    }

    // 显示错误提示信息
    function showErrorBox(msgText) {
        Lobibox.notify('warning', {
            size: 'mini',
            delay: 2000,
            delayIndicator: false,
            title: false, //不给title
            msg: msgText,
            sound: false  // 不要音频
        })
    }
</script>
</html>
