<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/16
  Time: 10:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="admin/css/bootstrap.min.css" />
    <link rel="stylesheet" href="admin/css/font-awesome.min.css" />
    <link rel="stylesheet" href="admin/css/notifications/Lobibox.min.css">
    <script src="admin/js/vendor/jquery-1.12.4.min.js"></script>
    <script src="admin/js/bootstrap.min.js"></script>
    <script src="admin/js/notifications/Lobibox.js"></script>
    <script src="admin/js/fronted/register.js"></script>
    <style>
        .pl60 {
            padding-left: 60px;
        }
        .mgt20 {
            margin-top: 20px;
        }
        .common-index {
            height: 24px;
            width: 24px;
            display: inline-block;
        }
        .active-index {
            background: url("images/reg-icon.png") no-repeat 0 0;
        }
        .step-index {
            line-height: 24px;
            font-weight: 700;
            color: #ffffff;
            background: url("images/reg-icon.png") no-repeat 0 -200px;
        }
        .active {
            background-color: #21cb91;
        }
        div.process-label p.active {
            background-color: white;
            color: #21cb91;
        }
        .common-pointer {
            height: 10px;
            width: 140px;
            position: absolute;
        }
        div.pointer {
            background: url("images/reg-icon.png") no-repeat 0 -100px;
        }
        .active-pointer {
            background: url("images/reg-icon.png") no-repeat 0 -130px;
        }
        .line-1 {
            top: 7px;
            left: 73px;
        }
        .line-2 {
            top: 7px;
            left: 241px;
        }
        div.process-label {
            width: 100px;
            text-align: center;
        }
        div.other-label {
            margin-left: 69px;
        }
        div.process-label p{
            margin-bottom: 0;
            padding-top: 15px;
        }
        h3.success-text{
            color: #21cb91;
            text-align: center;
        }
        .username-success {
            color: forestgreen;
            position: absolute;
            top: 34px;
            right: 6px;
            display: none;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-default navbar-inverse navbar-static-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <a href="index.jsp" class="navbar-brand" style="color: #ffffff;"><i class="fa fa-sign-language"></i> 寻欢易购</a>
        </div>
    </div>
</nav>
<div class="container">
    <div class="row">
        <div class="col-sm-6 col-sm-push-3 col-xs-12 pl60">
            <!-- 控制注册导航 -->
            <div style="display: flex; flex-direction: row; position: relative;">
                <div class="process-label">
                    <span class="common-index step-index">1</span>
                    <p class="active">验证手机号</p>
                </div>
                <div class="common-pointer pointer line-1"></div>
                <div class="process-label other-label">
                    <span class="common-index step-index">2</span>
                    <p class="info-text">填写账号信息</p>
                </div>
                <div class="common-pointer pointer line-2"></div>
                <div class="process-label other-label">
                    <span class="common-index step-index">3</span>
                    <p class="info-text">注册成功</p>
                </div>
            </div>
        </div>
    </div>
    <div  id="phonePanel" class="row mgt20">
        <!-- 电话的form表单 --->
        <div class="col-sm-6 col-sm-push-3 col-xs-8 col-xs-push-2">
            <form onsubmit="javascript: return false;">
                <div class="form-group">
                    <label for="phone">电话</label>
                    <input type="text" autocomplete="off" id="phone" class="form-control">
                </div>
                <div class="form-group">
                    <label for="validateCode">验证码</label>
                    <div class="input-group">
                        <input type="text" autocomplete="off" id="validateCode" class="form-control">
                        <!--  cursor: pointer; 将鼠标的形状改为手状。-->
                        <span class="input-group-addon" id="secondCountDown" style="cursor: pointer;">获取验证码</span>
                    </div>
                </div>
                <div>
                    <button onclick="gotoNext()" class="btn btn-block btn-danger">下一步</button>
                </div>
            </form>
        </div>
    </div>
    <div id="otherInfoPanel" class="row mgt20" style="display: none;">
        <!-- 其他信息的form表单 -->
        <div class="col-sm-6 col-sm-push-3 col-xs-8 col-xs-push-2">
            <form id="remainInfoForm" onsubmit="javascript: return false;">
                <div class="form-group" style="position: relative;">
                    <label for="username">用户名(必须以字母开头,中间不能包含特殊符号(!、@、#、$、%、&、*(、)): </label>
                    <input type="text" autocomplete="off" class="form-control" name="username" id="username">
                    <i id="success-icon" class="fa fa-check username-success"></i>
                </div>
                <div class="form-group">
                    <label for="password">密码</label>
                    <input type="password" id="password" name="password" class="form-control">
                </div>
                <div class="form-group">
                    <label for="repassword">重复密码</label>
                    <input type="password" id="repassword" class="form-control">
                </div>
                <div class="form-group">
                    <label for="email">邮箱</label>
                    <input type="text" autocomplete="off" name="email" id="email" class="form-control">
                </div>
                <div class="form-group">
                    <label>性别</label>
                    <div>
                        <label>
                            <input type="radio" checked name="gender" value="F"> 女
                        </label>
                        <label>
                            <input type="radio" name="gender" value="M"> 男
                        </label>
                    </div>
                </div>
                <div class="form-group">
                    <button onclick="submitToRegister()" class="btn btn-block btn-danger">立即注册</button>
                </div>
            </form>
        </div>
    </div>
    <div id="successPanel" class="row mgt20" style="display: none;">
        <h3 class="success-text">恭喜你, 注册成功!</h3>
        <p style="text-align: center;" id="gotoText">页面将在 <span style="color: red;" id="countdownSecond"></span>s 之后, 返回上一次访问的页面. </p>
    </div>
</div>
</body>
<script>
    //给span绑定事件
    $('#secondCountDown').on('click', getValidateCode)

    /**
     * 1.首先页面一进来要给获取验证的span绑定一个事件。
     * 2.当用户点击获取验证的时候，然后点击事件无效(解绑点击事件); 将背景颜色变浅色; 开始进入倒计时；
     * 3.当倒计时完毕之后, 重新绑定事件。
     */
    function getValidateCode() {
        var phone = $('#phone').val();
        /**
         * 144 147
         * 191 199
         * 173 176 177
         * 130~9
         * 150~9
         * 180~9
         */
        var reg = /^1(44|47|91|99|73|76|77|[358]\d)\d{8}$/;
        if(!reg.test(phone)) {
            showErrorBox('请填写正确手机号码.')
            return;
        }

        // 解绑事件. 无论用户的手机号是否存在或者后端短信是否发送成功, 都必须要解绑
        $('#secondCountDown').unbind('click');

        $.ajax({
            url: '${pageContext.servletContext.contextPath}/sms',
            type: 'get',
            data: {phone: phone},
            dataType: 'json',
            success: function(_data) {
                if(_data.code <= 0) {  //错误
                    showErrorBox(_data.msg);
                    $('#secondCountDown').on('click', getValidateCode);
                }else {
                    $('#secondCountDown').css('background-color', '#ffffff');
                    countSeconde(60); //开始进入倒计时
                }
            }
        })
    }

    // 倒计时, 方法参数num是倒计时的时间，单位是s
    function countSeconde(num) {
        if(num > 0) { // 一直倒计时
            num--;
            $('#secondCountDown').html('剩余(' + num + "s)");
            // 每过一秒重新执行该方法
            setTimeout(() => {
                countSeconde(num);
            }, 1000);
        }else {  //表示已经倒计时完毕
            $('#secondCountDown').html('重新获取');
            $('#secondCountDown').on('click', getValidateCode); //重新绑定
            $('#secondCountDown').css('background-color', '#cccccc');
        }
    }

    // 在电话面板，点击下一步的按钮
    function gotoNext() {
        var phone = $('#phone').val(); //获取手机号
        var validateCode = $('#validateCode').val(); //获取验证码

        var reg = /^1(44|47|91|99|73|76|77|[358]\d)\d{8}$/;
        if(!reg.test(phone)) {
            showErrorBox('请填写正确手机号码.')
            return;
        }

        reg = /^\d{4}$/;  //验证码的长度只能为四位整数
        if(!reg.test(validateCode)) {
            showErrorBox('验证码错误.')
            return;
        }

        // 验证手机号与验证是否正确
        $.ajax({
            url: '${pageContext.servletContext.contextPath}/validate',
            type: 'get',
            data: {phone: phone, validateCode: validateCode},
            dataType: 'json',
            success: function(_data) {
                if(_data.code < 0) {
                    showErrorBox(_data.msg)
                }else {
                    //当点击电话面板的 “下一步” 按钮的时候，去调用。
                    // 隐藏手机号面板，打开其他信息面板。改变其他的样式
                    RegisterPanel.changePhoneLabel();
                }
            }
        })
    }

    $('#username').focus(function() {
        $('#success-icon').hide();
    });
    /**
     * 当用户名输入框失去焦点的时候，要验证两点：
     *      1.用户名不能为空.
     *      2.用户名不能重复.
     */
    $('#username').blur(function() {
        validateUsername();
    })

    // 密码失去焦点的时候
    $('#password').blur(function() {
        validatePassword();
    })

    // 重复密码失去焦点的时候进行的判断
    $('#repassword').blur(function() {
        validateRePassword();
    });

    // 当邮箱失去焦点的时候
    $('#email').blur(function() {
        validateEmail();
    })

    // 验证用户名
    function validateUsername() {
        var username = $('#username').val();  //获取用户名的值
        if(!username || "" == username.trim()) {
            showErrorBox('用户不能为空.')
            return false;
        }

        var wordReg = /^[a-zA-Z]+/;  //必须以字母开头
        var specialReg = /[`~!@#$^&*()=|{}':;',\[\].<>?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]/;  //特殊字符验证

        // 字母不是以字母开头，并且中间包含特殊字符
        if(!wordReg.test(username) || specialReg.test(username)) {
            showErrorBox('用户格式不对.')
            return false;
        }

        /**
         // 到后台验证用户名是否重复
         $.ajax({
            async: true,  //true表示异步操作(默认值), false是同步操作
            url: 'http://localhost:8081/my_cart_war_exploded/validate-username',
            data: {username: username},
            type: 'get',
            dataType: 'json',
            success: function(_data) {
                // 后台验证用户名有问题
                if(_data.code < 0) {
                    showErrorBox(_data.msg);
                }else { //验证成功
                    $('#success-icon').show();
                }
            }
        })
         return true;
         */
        /**
         * ajax的同步的方式, 为什么需要改为这种方式。原因在于异步的方式我们还没有确定用户名
         * 是否已经存在的时候，但是 $.ajax() 后面的代码已经执行了。
         */
        var resp = $.ajax({
            async: false,  //true表示异步操作(默认值), false是同步操作
            url: '${pageContext.servletContext.contextPath}/validate-username',
            data: {username: username},
            type: 'get',
            dataType: 'json'
        }).responseText;

        resp = JSON.parse(resp);  //需要换为标准的json

        if(resp.code < 0) {
            showErrorBox(_data.msg);
            return false;
        }

        $('#success-icon').show();
        return true;
    }
    // 校验密码
    function validatePassword() {
        var pwd = $('#password').val(); //取密码的值
        if(!pwd) {
            showErrorBox('密码不能为空.')
            return false;
        }
        return true;
    }

    // 验证重复密码
    function validateRePassword() {
        var repwd = $('#repassword').val(); //取重复密码的值
        if(!repwd) {
            showErrorBox('重复密码不能为空.')
            return false;
        }
        var pwd = $('#password').val();
        if(pwd != repwd) {
            showErrorBox('两次密码不能为空.')
            return false;
        }
        return true;
    }

    // 验证邮箱
    function validateEmail() {
        var email = $('#email').val();
        if(!email || '' == email.trim()) {
            showErrorBox('邮箱不能为空.')
            return false;
        }
        var reg = /^[a-zA-Z1-9_][-\w]{2,7}@[0-9a-zA-Z]{2,6}\.[a-zA-Z]{2,6}$/;
        if(!reg.test(email)) {
            showErrorBox('邮箱格式错误.')
            return false;
        }
        return true;
    }

    // 点击 "立即注册" 按钮
    function submitToRegister() {
        // 表示所有的校验如果全部成功,才执行后续代码
        // 该if是短路操作.
        if(validateUsername() && validatePassword() && validateRePassword() && validateEmail()) {
            // console.log($('#remainInfoForm').serialize())  //结果：username=yuyy&password=1&email=7yty%40qq.com&gender=F
            $.ajax({
                url: '${pageContext.servletContext.contextPath}/register',
                type: 'post',
                // data: $('#remainInfoForm').serialize() + '&phone=13478907890',
                data: $('#remainInfoForm').serialize() + '&phone=' + $('#phone').val(),
                dataType: 'json',
                success: function(_data)  {
                    if(_data.code > 0) {
                        // 隐藏手机号form和其他信息的form编面板，显示注册成功面板
                        RegisterPanel.changeOtherInfoLabel();

                        // console.log(document.referrer); // document.referrer 获取上一次页面, 可能有也可能没有
                        if(document.referrer) {  //表示有上次的访问页面
                            countdown(6);
                        }else { // 没有上次访问的页面, 说白了就是直接过来的
                            $('#gotoText').hide();
                        }
                    }else {
                        showErrorBox('注册失败, 请检查信息是否正确');
                    }
                }
            })
        }
    }

    /**
     *
     * @param num
     */
    function countdown(num) {
        if(num > 0) {
            num--;
            $('#countdownSecond').html(num);
            setTimeout(() => {
                countdown(num);
            }, 1000);
        }else {
            // 跳转到上一次的页面
            window.location.href = document.referrer;
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
