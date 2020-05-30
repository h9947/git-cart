<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/21
  Time: 11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    <link rel="stylesheet" href="admin/css/bootstrap.min.css" />
    <link rel="stylesheet" href="admin/css/font-awesome.min.css" />
    <link rel="stylesheet" href="admin/css/notifications/Lobibox.min.css">
    <script src="admin/js/vendor/jquery-1.12.4.min.js"></script>
    <script src="admin/js/bootstrap.min.js"></script>
    <script src="admin/js/notifications/Lobibox.js"></script>
    <script src="admin/js/jquery.cookie.min.js"></script>
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
        <p class="navbar-text navbar-right">
            <a href="register.jsp">注册</a>
        </p>
    </div>
</nav>
<div class="container">
    <div class="row">
        <div class="col-xs-6 col-xs-push-3">
            <div class="panel panel-danger login-panel">
                <div class="panel-heading">
                    <!-- 面板的头部内容 -->
                    <h3 class="panel-title">用户登录</h3>
                </div>
                <div class="panel-body">
                    <form onsubmit="javascript: return false;">
                        <div class="form-group">
                            <label for="username">用户名</label>
                            <input type="text" class="form-control" autocomplete="off" id="username">
                        </div>
                        <div class="form-group">
                            <label for="password">密码</label>
                            <input type="password" class="form-control" autocomplete="off" id="password">
                        </div>
                        <div class="form-group">
                            <button onclick="userLogin()" class="btn btn-danger btn-block">登录</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    /**
     * 用户登录, 登录成功时候需要合并购物车数据
     */
    function userLogin() {
        var username = $('#username').val();
        var password = $('#password').val();

        $.ajax({
            url: '${pageContext.servletContext.contextPath}/login',
            type: 'post',
            data: {username: username, password: password},
            dataType: 'json',
            success: function (_data) {
                if(_data.code > 0) {
                    /**
                     * 如果登录成功:
                     *     1. 将用户名存储在本地cookie中。
                     *     2. 同步购物车数据；
                     *     3. 跳转上次的页面
                     */
                    //  1. 将用户名存储在本地cookie中。
                    $.cookie('frontedUsername', username);

                    var localShoppingCart = localStorage.getItem('local_shopping_cart');
                    /**
                     * 1. 如果本地购物车有数据，就将本地购物车数据同步到服务器端, 并于服务器端对应购物车数据合并。
                     * 2. 如果本地购物车没有数据还要去看服务器端对应购物车是否有数据
                     */
                    // console.log(localShoppingCart + '  ')
                    syncShappingCart(localShoppingCart);

                    // 跳转到上一次的页面
                    if(document.referrer) {  //表示有上次的访问页面
                        window.location.href = document.referrer;
                    }else { // 没有上次访问的页面, 说白了就是直接过来的
                        window.location.href = '前端首页.html';
                    }
                }else {  // 登录失败
                    showErrorBox(_data.msg);
                }
            }
        })
    }

    // 同步购物车数据
    function syncShappingCart(localShoppingCart) {
        $.ajax({
            url: '${pageContext.servletContext.contextPath}/sync-cart',
            data: {method: 'getCartInfo', cartData: localShoppingCart},
            type: 'post',
            dataType: 'json',
            success: function(_data) {
                if(_data.code > 0) { //购物车有数据, 没有数据就不作任何处理
                    localStorage.setItem('local_shopping_cart', JSON.stringify(_data.obj));
                }
            }
        })
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
