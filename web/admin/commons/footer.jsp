<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/13
  Time: 10:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
</div>
</div>
</div>

<!-- jquery
============================================ -->
<script src="js/vendor/jquery-1.12.4.min.js"></script>
<!-- bootstrap JS
============================================ -->
<script src="js/bootstrap.min.js"></script>
<!-- wow JS  动画效果js
============================================ -->
<script src="js/wow.min.js"></script>
<!-- price-slider JS
============================================ -->
<script src="js/jquery-price-slider.js"></script>
<!-- meanmenu JS
============================================ -->
<script src="js/jquery.meanmenu.js"></script>
<!-- owl.carousel JS
============================================ -->
<script src="js/owl.carousel.min.js"></script>
<!-- sticky JS
============================================ -->
<script src="js/jquery.sticky.js"></script>
<!-- scrollUp JS
============================================ -->
<script src="js/jquery.scrollUp.min.js"></script>
<!-- counterup JS
============================================ -->
<script src="js/counterup/jquery.counterup.min.js"></script>
<script src="js/counterup/waypoints.min.js"></script>
<script src="js/counterup/counterup-active.js"></script>
<!-- mCustomScrollbar JS
============================================ -->
<script src="js/scrollbar/jquery.mCustomScrollbar.concat.min.js"></script>
<script src="js/scrollbar/mCustomScrollbar-active.js"></script>
<!-- metisMenu JS
============================================ -->
<script src="js/metisMenu/metisMenu.min.js"></script>
<script src="js/metisMenu/metisMenu-active.js"></script>


<script src="js/sparkline/jquery.sparkline.min.js"></script>
<script src="js/sparkline/jquery.charts-sparkline.js"></script>
<script src="js/sparkline/sparkline-active.js"></script>
<!-- calendar JS
============================================ -->
<script src="js/calendar/moment.min.js"></script>

<script src="js/calendar/fullcalendar.min.js"></script>
<script src="js/calendar/fullcalendar-active.js"></script>
<!-- plugins JS
============================================ -->
<script src="js/plugins.js"></script>
<!-- main JS
============================================ -->
<script src="js/main.js"></script>

<!-- tawk chat JS  聊天系统js
<script src="js/tawk-chat.js"></script>
============================================ -->

<script src="js/data-table/bootstrap-table.js"></script>
<script src="js/data-table/bootstrap-table-zh-CN.js"></script>

<script src="js/notifications/Lobibox.js"></script>

<script src="js/datepicker/bootstrap-datetimepicker.min.js"></script>
<script src="js/datepicker/bootstrap-datetimepicker.zh-CN.js"></script>


</body>
<script>
    /**
     * 1. 获取浏览器地址栏，然后截取路径。
     * 2. 根据路径使用jquery的选择器让其背景颜色变一下。
     */
    var url = window.location.href;   //获取浏览器地址栏 http://localhost:8081/my_cart_war_exploded/admin/index.jsp

    var index = url.lastIndexOf("/");  //获取最后一个 / 所在的索引的位置

    var path = '/admin' + url.substring(index);  // /admin/index.jsp

    $('a[href$="' + path + '"]').css('background-color', '#f7f8fb');
</script>
</html>
