<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/13
  Time: 10:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="commons/header.jsp"%>
<style>
    .modal-dialog {
        width: 680px;
    }
    .pagination {
        margin-top: 0;
        margin-bottom: 0;
    }
</style>
<div style="background-color: #ffffff; border-radius: 3px; padding: 12px;">
    <form class="form-inline" onsubmit="javascript: return false;">
        <div class="form-group">
            <label for="name">用户名: </label>
            <input autocomplete="off" class="form-control" id="name">
        </div>
        <div class="form-group">
            <label for="gender">性别: </label>
            <select class="form-control" id="gender">
                <option value="-1">全部</option>
                <option value="F">女</option>
                <option value="M">男</option>
            </select>
        </div>
        <div class="form-group">
            <label for="status">状态: </label>
            <select class="form-control" id="status">
                <option value="-1">全部</option>
                <option value="1">激活</option>
                <option value="0">未激活</option>
            </select>
        </div>
        <div class="form-group">
            <label for="email">邮件: </label>
            <input autocomplete="off" class="form-control" id="email">
        </div>
        <div class="form-group">
            <label>注册日期: </label>
            <input class="form-control" readonly autocomplete="off" id="beginRegisterDate"> -
            <input autocomplete="off" readonly class="form-control" id="endRegisterDate">
        </div>
        <div class="form-group">
            <button class="btn btn-danger" onclick="querySearch()">搜索</button>
            <button autocomplete="off" class="btn btn-danger" onclick="resetSearch()">重置</button>
        </div>
    </form>
    <!--
    额外加一列，写个按钮查看用户的地址信息。
    -->
    <table id="dataTable" class="table table-hover table-bordered"></table>
    <div class="modal" id="showAddressModal" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button class="close" onclick="destoryTableAndHideModal()">
                        <span>&times;</span>
                    </button>
                    <h3 class="modal-title">收货地址</h3>
                </div>
                <div class="modal-body">
                    <table id="addressDataTable" class="table table-striped"></table>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="commons/footer.jsp"%>
<script>
    $('#beginRegisterDate, #endRegisterDate').datetimepicker({
        // 格式化 yyyy-mm-dd hh:ii:ss 年-月-日 时-分-秒
        format: 'yyyy-mm-dd hh:ii:ss'
    })
    /**
     //  https://lobianijs.com/site/lobibox#notifications
     // notify() 第一个参数值为：error success  warning  info
     Lobibox.notify('error', {
        size: 'mini', // 值为枚举：normal, mini, large
        title: '提示', // 信息头
        msg: '激活失败, 请联系管理员',  //信息的内容
        delay: 2000,  //提示框持续的时间
        delayIndicator: false,  //特效是否显示, 下面的进度条不显示
        position: 'bottom right',  //控制方向, 有两个单词, 第一个值为top或者bottom, 第二个是left right
        sound: 'error',  //音效的文件名
        // 谷歌浏览器禁止音效自动的播放, 突破限制的方式：chrome://flags -> 搜索： autoplay ->
        // 找到 "Autoplay policy" -> "No user Gusture..." 然后重启。
        soundExt: '.mp3'  //音效的后缀名
    })
     */
    /**
     "rows": [
     {
          "email": "223@qq.com",
          "id": 1,
          "name": "张三",
          "phone": "13000000000",
          "registerDate": 1589353095000,
          "sex": "F",
          "status": 1
        }
     ],
     "total": 1
     }
     */
    $('#dataTable').bootstrapTable({
        url: 'http://localhost:8081/my_cart_war_exploded/user',  //请求的地址
        method: 'GET', //请求的方式
        // columns: 是定义列的信息， title表头信息, field表示从返回的json数据中取对应的属性的
        columns: [
            {field: 'id', title: 'ID', align: 'center'},
            {field: 'name', title: '姓名', align: 'center'},
            // formatter要接收一个函数, 是对数据进行处理，该函数接收四个参数。
            {field: 'sex', title: '性别', align: 'center', formatter: formatSex},
            {field: 'status', title: '状态', align: 'center', formatter: formatStatus},
            {field: 'phone', title: '电话', align: 'center'},
            {field: 'email', title: '邮件', align: 'center'},
            {field: 'registerDate', title: '注册日期', align: 'center', formatter: formatRegisterDate},
            {title: '操作', formatter: addProcessBtns}
        ],
        // 查询的参数，是根据该方法的返回值来定的, params中默认的数据格式：
        //      {limit: 10, offset: 0, order: "asc", search: undefined, sort: undefined}
        // limit是每页默认的每页显示的数据, offset(中文翻译是偏移量), 映射分页中指的是 beginIndex   select * from user limit offset, 10;
        // order 是查询的顺序; 如果是get请求会默认将该函数的返回值拼接到请求的url后面:
        //                     http://localhost:8081/my_cart_war_exploded/user?offset=0&limit=10&order=asc&method=getUserPageData
        queryParams: function(params) {
            /**
             * 无论是 "初始化" 或者 "refresh" 都会执行该方法，来获取请求的参数.
             * 所以我们无论是 "初始化" 或者 “refresh” 都去获取表单的数据
             */
            var name = $('#name').val();  //获取查询表单name的值
            var gender = $('#gender').val();
            var status = $('#status').val();
            var email = $('#email').val();
            var begin =  $('#beginRegisterDate').val(); //重置name
            var end = $('#endRegisterDate').val(); //重置name

            params.name = name;
            params.gender = gender;
            params.status = status;

            params.email = email;
            params.begin = begin;
            params.end = end;
            params.method = 'getUserPageData';
            return params;
            // 最终的请求会默认帮我们带上这些参数
            //http://localhost:8081/my_cart_war_exploded/user?order=asc&offset=0&limit=10&name=&gender=-1&status=-1&email=&begin=&end=&method=getUserPageData
        },
        pageList: [10, 15, 20],   // 切换每页显示多少条数据
        pagination: true,  //表示意思是要分页, 默认是false
        // 在那边分页，值为 "client", "server", client的意思一次性将数据全部拿到，然后客户端分页。
        // server, 在服务器端分页, 每次查分页的数据的时候到服务器去请求。
        sidePagination: 'server'
    })

    /**
     * 最终表格中显示的数据以该函数的返回值为准
     * value是当前格子的值。
     * row是当前行的数据。
     * index是当前数据的索引。
     */
    function formatSex(value, row, index) {
        return 'F' == value ? '女' : '男';
    }

    // 格式化状态
    function formatStatus(value) {
        // 1表示激活状态(要看数据库)
        if(value == 1) {
            return '<span class="label label-success">激活</span>'
        }else {
            return '<span class="label label-danger">未激活</span>'
        }
    }

    //格式化注册日期
    function formatRegisterDate(value) {
        // 使用moment这个库来完成日期的格式化
        return moment(value).format('YYYY-MM-DD HH:mm:ss');
    }

    /**
     * 该方法会返回操作按钮：
     *      1. 查看用户所有的收货地址(必须的)。
     *      2. 对于未激活的用户, 得有激活按钮, 激活的用户不用加。
     */
    function addProcessBtns(value, row) {
        var btns = '<button class="btn btn-info btn-xs" onclick="showTakeDeliveryAddress(' + row.id + ')">查看收货地址</button>' +
            '  <button onclick="showShoppingCartData(' + row.id + ')" class="btn btn-primary btn-xs">查看购物车</button>';

        //如果用户是未激活状态, 添加激活按钮
        if(row.status != 1) {
            btns += '  <button class="btn btn-success btn-xs" onclick="activeUser(' + row.id + ')">激活</button>'
        }

        return btns;
    }

    // 查看收货地址
    function showTakeDeliveryAddress(userId) {
        // console.log(userId)
        $('#showAddressModal').modal('show');
        $('#addressDataTable').bootstrapTable({
            url: 'http://localhost:8081/my_cart_war_exploded/address',
            columns: [
                {field: 'name', align: 'center', title: '收件人姓名'},
                {field: 'phone', align: 'center', title: '收件人电话'},
                {field: 'detail', align: 'center', title: '收货地址'},
                {field: 'status', align: 'center', title: '默认地址',
                    formatter: function(value) {
                        if(value == 1) {  //当value的值为1的时候，表示默认收货地址
                            return '<span class="label label-success">默认</span>';
                        }
                        return "否";
                    }
                }
            ],
            // var obj = {age: 10};  obj.name = 'zhangsan';
            queryParams: function(params) {
                params.userId = userId;
                // console.log(params);
                return params;
            }
        })
    }

    // 销毁表格, 然后影藏模态窗
    function destoryTableAndHideModal() {
        $('#showAddressModal').modal('hide');
        // 因为模态窗中的表格, 没有分页，所以将数据直接缓存在客户端，当重新请求的时候并不会发送网络请求，
        // 导致看不同用户的收货地址的时候, 看到的数据是上一次的，所以每次影藏要将表格销毁掉。
        // https://www.bootstrap-table.com.cn/doc/api/methods/#destroy
        $('#addressDataTable').bootstrapTable('destroy'); // destroy是固定值
    }

    // 激活用户
    function activeUser(userId) {
        $.ajax({
            url: 'http://localhost:8081/my_cart_war_exploded/user',
            data: {method: 'activeUser', userId: userId},
            dataType: 'json',
            type: 'get',
            // 激活的时候返回的json数据格式：{code: 1, msg: 'success'}
            success: function(_data) {
                // 代表激活成功，成功之后要重新加载表格的数据
                if(_data.code > 0) {
                    // 重新加载表格。 https://www.bootstrap-table.com.cn/doc/api/methods/#refresh
                    $('#dataTable').bootstrapTable('refresh');
                }else { //激活失败, 提示用户
                    Lobibox.notify('info', {
                        title: '提示',
                        msg: '激活失败, 请联系管理员',
                        size: 'mini'
                    })
                }
            }
        })
    }

    // 搜索
    function querySearch() {
        // 解决翻页之后, 还是停留在以前的页面问题, 使用selectPage, 意思是回到第一页
        $('#dataTable').bootstrapTable('selectPage', 1);
    }

    /**
     * 重置搜索条件:
     *    1.将所有的搜索条件归位；
     *    2.刷新表格数据。
     */
    function resetSearch() {
        $('#name').val(''); //重置name
        $('#gender').val('-1'); //重置性别
        $('#status').val('-1'); //重置状态
        $('#email').val('');
        $('#beginRegisterDate').val('');
        $('#endRegisterDate').val('');
        querySearch();
    }
</script>


