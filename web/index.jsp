<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/13
  Time: 10:01
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
  <script src="admin/js/notifications/Lobibox.js"></script>
  <style>
    * {
      margin: 0;
      padding: 0;
    }
    body {
      background-color: #eeeeee;
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
<div class="container">
  <div class="row">
    <div class="col-xs-1 search-box">
      <a href="#" class="logo">
        <i class="fa fa-sign-language"></i>
      </a>
    </div>
    <div class="col-xs-9 search-box">
      <div class="input-group search-box">
        <input class="form-control search-box">
        <span class="input-group-addon">
                        <i class="fa fa-search"></i>
                    </span>
      </div>
    </div>
    <div class="col-xs-2 search-box">
      <a href="cart.jsp" class="btn btn-danger btn-block search-box">
        <i class="fa fa-cart-arrow-down" style="font-size: 20px;"></i>&nbsp;&nbsp;购物车
        <!-- 购物车数据的数量 -->
        <span id="shoppingCartNum" class="badge">0</span>
      </a>
    </div>
  </div>
  <div class="row">
    <div id="carousel-example" class="carousel slide mgt38" data-ride="carousel">
      <!-- 小圆点 -->
      <ol class="carousel-indicators">
        <li data-target="#carousel-example" data-slide-to="0" class="active"></li>
        <li data-target="#carousel-example" data-slide-to="1"></li>
        <li data-target="#carousel-example" data-slide-to="2"></li>
        <li data-target="#carousel-example" data-slide-to="4"></li>
      </ol>
      <!-- 放置图片 -->
      <div class="carousel-inner">

        <!-- 展示每张图片以及文字 -->
        <div class="item active">
          <img src="images/banner1.webp">
        </div>
        <div class="item">
          <img src="images/banner1.webp">
        </div>
        <div class="item">
          <img src="images/banner1.webp">
        </div>
        <div class="item">
          <img src="images/banner1.webp">
        </div>
      </div>

      <!-- 左边的箭头 -->
      <a class="left carousel-control" href="#carousel-example" data-slide="prev">
        <span class="fa fa-chevron-left control-icon"></span>
        <span class="sr-only">Previous</span>
      </a>
      <!-- 右边的箭头 -->
      <a class="right carousel-control" href="#carousel-example" data-slide="next">
        <span class="fa fa-chevron-right control-icon"></span>
        <span class="sr-only">Next</span>
      </a>
    </div>
  </div>
  <div class="row mgt38" id="goodsListInfo">
    <div class="col-xs-3" v-for="good in goodList">
      <div class="thumbnail">
        <a href="javascript: void(0);">
          <img :src="good.imageSrc">
          <div class="caption">
            <p class="description">
              {{good.title}}
            </p>
            <p>
              价格：￥{{good.price}}
            </p>
            <p>
              <button :id="good.goodId" onclick="addToShoppingCart(this)" class="btn btn-danger">加入购物车</button>
            </p>
          </div>
        </a>
      </div>
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

  // 从172行到185行代码的作用是统计 小圆点的数字。
  // 当页面首次进来的时候就需要计算购物车中商品的总数量
  // 计算购物车中商品的总数量, 参数是本地的购物车，是对象：{'1001':1, '1002':2}
  function countShoppingCartNum(localShoppingCart) {
    if(localShoppingCart) {
      var goodIds = Object.keys(localShoppingCart); // 返回值为一个数组：['1001', '1002']

      var num = 0;
      for(var i = 0; i < goodIds.length; i++) {
        num += localShoppingCart[goodIds[i]];
      }

      $('#shoppingCartNum').html(num);
    }
  }

  countShoppingCartNum(JSON.parse(localStorage.getItem('local_shopping_cart')));

  /** 例如说是登录
   $.ajax({
        url: 'http://localhost:8081/my_cart_war_exploded/good\'',
        type: 'post',
        dataType: 'json'
        data: {username: username, password: password},
        success: function(_data) {
            if(登录成功) {
                $.cookie('frontedUsername', 'lisi')
            }
        }
    })
   */


  /**
   * 我们先预设好，当我们登录成功之后，将用户名存储在cookie中，存储在cookie中用户名的键值叫做 "frontedUsername"
   */
  $.ajax({
    url: '${pageContext.servletContext.contextPath}/good',
    type: 'get',
    dataType: 'json',
    success:function(_data) {
      renderGoodsInfo(_data);
    }
  })

  var vm = new Vue({
    el: '#goodsListInfo',
    data: {
      goodList: []
    }
  })

  function renderGoodsInfo(goods) {
    for(var i = 0; i < goods.length; i++){
      vm.goodList.push(goods[i]);
    }
  }

  /**
   // 渲染商品信息数据
   function renderGoodsInfo(goods) {
        for(var i = 0; i < goods.length; i++){
            var good = goods[i];
            var goodsInfoHtml = '<div class="col-xs-3">'    +
                                    '<div class="thumbnail">' +
                                        '<a href="javascript: void(0);">' +
                                            '<img src="' + good.imageSrc + '">' +
                                            '<div class="caption">' +
                                                '<p class="description">' + good.title + '</p>' +
                                                '<p>价格: ￥' + good.price + '</p>' +
                                                '<p><button onclick="addToShoppingCart(' + good.goodId + ')" class="btn btn-danger">加入购物车</button></p>' +
                                            '</div>' +
                                        '</a>' +
                                    '</div>' +
                                '</div>';
            $('#goodsListInfo').append(goodsInfoHtml);
        }
    }
   */

  /**
   * 加入购物车：
   *   1. 改变购物车按钮(搜索框右边的那个按钮)上的数据。
   *   2.用户没有登录的情况下，将加入购物车的商品信息存在本地(localStorage)。
   *   3.用户登录的情况下, 既要存储在本地，也要插入到服务器端的数据库中。
   *
   *  针对上三点有一个共性：
   */
  function addToShoppingCart(btnTag) {
    var goodId = $(btnTag).prop('id');

    // 因为用户登录成功之后，会通过frontedUsername来存储用户名
    // 所以 $.cookie('frontedUsername') 来判定用户有没有登录。
    if($.cookie('frontedUsername')) {  // 表示用户登录了
      $.ajax({
        url: '${pageContext.servletContext.contextPath}/sync-cart',
        type: 'post',
        data: {method: 'addGood', goodId: goodId},
        dataType: 'json',
        success: function(_data) {
          if(_data.code > 0) {
            // 本地更新
            shoppingCart2LocalStorage(goodId); // 2 -> to   4 -> for
          }else {
            showErrorBox('添加到购物车失败.')
          }
        }
      })
    }else {
      shoppingCart2LocalStorage(goodId); // 2 -> to   4 -> for
    }
  }

  // 购物车数据的本地存储
  function shoppingCart2LocalStorage(goodId) {
    // 存储在本地的购物车中数据格式：{'1001':2, '1002':1}, 存储本地购物车中的数据的key: local_shopping_cart
    var shoppingCart = localStorage.getItem('local_shopping_cart'); //先取已经存在于本地的购物车数据, 取出的数据也是一个字符串

    goodId = '' + goodId;  //将goodId转换为字符串

    if(!shoppingCart) { //表示本地没有数据
      shoppingCart = {}; // 重新复制
      shoppingCart[goodId] = 1;
      $('#shoppingCartNum').html(1);  // 小圆点的数字
      localStorage.setItem('local_shopping_cart', JSON.stringify(shoppingCart));
    }else {  //之前已经加入到购物车中 {'1001':2, ''1002:2, '1003': 1}
      shoppingCart = JSON.parse(shoppingCart); //转换为js的对象
      /**
       * 需要处理两种情况：
       *      1.之前在购物车中已经存在了对应的商品, 把对应的商品在购物车中的数量 +1;
       *      2.之前购物车中不存在对应的商品，那么就添加对应的商品，数量为1.
       */
      var goodIds = Object.keys(shoppingCart); //返回一个数组，数组中就是所有的商品的id ['1001', '1002', '1003']

      /**
       var num = 0;
       for(var i = 0; i < goodIds.length; i++) {
                // goodIds[i] 是每个商品的id
                // shoppingCart[goodIds[i]]; 是商品的购物车对应的商品的数量
                num += shoppingCart[goodIds[i]];
            }
       */

      // 表示商品已经存在
      if(goodIds.indexOf(goodId) != -1) {
        shoppingCart[goodId]++;  //让数量 +1
      }else {  // 表示新添加到购物车中的商品是不存在
        shoppingCart[goodId] = 1;
      }

      // num += 1;
      countShoppingCartNum(shoppingCart);

      localStorage.setItem('local_shopping_cart', JSON.stringify(shoppingCart));
    }
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
