<template>
    <div>
        <!-- 功能菜单 -->
        <el-row style="margin-top: 40px">
            <!-- 标题 -->
            <el-col :span="3" :offset="1">
                <span style="font-size: 25px">用户管理中心</span>
            </el-col>
            <!--按钮-->
            <el-col :span="4">
                <el-button type="primary" size="medium" plain @click="insertUserOnSubmit">添加账户</el-button>
                <TableExport style="margin-left: 1em;" table-id="user-table"></TableExport>
            </el-col>
            <!--搜索表单-->
            <el-col :span="10" :offset="5">
                <el-form :inline="true" :model="searchTitle">
                    <el-form-item>
                        <el-input size="medium" v-model="searchTitle.userId" placeholder="账号"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input size="medium" v-model="searchTitle.username" placeholder="用户名"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="medium" plain type="primary" @click="searchUserOnSubmit('1')">查询</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="medium" plain type="success" @click="searchUserOnSubmit('0')">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
            <el-col :span="1">
                <i id="quitBtn" class="ali-icontuichu-copy" @click="quitLogin"></i>
            </el-col>
        </el-row>

        <el-divider></el-divider>
        <!--表格-->
        <el-row>
            <el-col :span="22" :offset="1">
                <el-table
                        id="user-table"
                        :data="tableData"
                        style="width: 100%"
                        border
                        :row-class-name="tableRowClassName"
                        @row-click="rowClick">
                    <el-table-column
                            label="序号"
                            type="index"
                            align="center"
                            width="100">
                    </el-table-column>

                    <el-table-column
                            prop="userId"
                            label="账号"
                            align="center"
                            sortable>
                    </el-table-column>
                    <el-table-column
                            prop="username"
                            label="姓名"
                            align="center"
                            sortable>
                    </el-table-column>
                    <el-table-column
                            label="身份"
                            align="center">
                        <template slot-scope="scope">
                            <el-tag v-if="scope.row.identityBool" type="success">老师</el-tag>
                            <el-tag v-else>学生</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column
                            align="center"
                            label="操作">
                        <template slot-scope="scope">
                            <el-button type="text" style="font-size: 16px" size="medium" @click="updateUserPsw(scope.row)">修改密码</el-button>
                            <el-button type="text" style="color: #afafaf;font-size: 16px" @click="deleteUser(scope.row)">
                                <i style="margin:0 0.2em" class="el-icon-warning"></i>注销</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-col>
        </el-row>
        <!-- 添加账户 -->
        <el-dialog
                v-if="closeDialog"
                title="添加账户"
                :visible.sync="insertDialogVisible"
                @close="dialogHandleClose"
                width="25%">
            <el-form ref="form" :model="userData">
                <el-form-item> <!-- 账号 -->
                    <el-input class="input" v-model="userData.userId" placeholder="账号" maxlength="24" autocomplete="off" clearable></el-input>
                </el-form-item>
                <el-form-item> <!-- 密码 -->
                    <el-input class="input" v-model="userData.psw" placeholder="密码" maxlength="24" autocomplete="off" clearable show-password></el-input>
                </el-form-item>
                <el-form-item> <!-- 身份选择学生/老师 -->
                    <el-select v-model="userData.identity" placeholder="账户类型">
                        <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>  <!-- 确认按钮 -->
                    <el-button type="primary" @click="insertUser(userData)" style="width:100%; font-size: 16px;margin-top: 15px">
                        <span class="btLogin">提交</span>
                    </el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <!-- 修改密码 -->
        <el-dialog
                v-if="closeDialog"
                title="修改密码"
                :visible.sync="updatePswDialogVisible"
                @close="dialogHandleClose"
                width="25%">
            <el-form ref="form" :model="userData">
                <el-form-item> <!-- 密码 自带显示密码 -->
                    <el-input class="input" v-model="userData.psw" placeholder="新密码" maxlength="24" autocomplete="off" clearable show-password></el-input>
                </el-form-item>
                <el-form-item> <!-- 确认密码 自带显示密码 -->
                    <el-input class="input" v-model="userData.checkPsw" placeholder="确认密码" maxlength="24" autocomplete="off" clearable show-password></el-input>
                </el-form-item>
                <el-form-item>  <!-- 确认按钮 -->
                    <el-button type="primary" @click="updateUserData(userData)" style="width:100%; font-size: 16px;margin-top: 15px">
                        <span class="btLogin">确认</span>
                    </el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
        <!-- 分页工具条 数据小于10条不展示 -->
        <el-pagination
                id="pageBar"
                v-show="totalCount >= 10"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[5, 10, 15, 20, 50, 100]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="totalCount">
        </el-pagination>
    </div>
</template>

<script>
    // 引入导出Excel表格依赖
    import TableExport from "@/components/TableExport";
    // 自定义组件
    import Ckeditor4 from "@/components/Ckeditor4";
    import SHAEncrypt from "@/components/SHAEncrypt";
    export default {
        name: 'AdminPage',
        components:{
            Ckeditor4,
            SHAEncrypt,
            TableExport
        },
        data(){
            return{
                // 当前页码
                currentPage: 1,
                // 分页大小
                pageSize: 10,
                // 总页码数
                totalCount: 1,
                // 搜索表单数据模型
                searchTitle:{
                    userId:'',
                    username:'',
                },
                // 内置查询模型
                searchFormIn:{
                    userId:'',
                    username:'',
                },
                // 用户编辑数据
                userData:{
                    userId:'',
                    psw:'',
                    checkPsw:'',
                    identity:"",
                },
                options: [{
                    value: 0,
                    label: '我是老师'
                }, {
                    value: 1,
                    label: '我是学生'
                }],
                // 添加账户对话框
                insertDialogVisible:false,
                // 修改密码对话框
                updatePswDialogVisible:false,
                // 通知数据列表
                tableData:[],
                // 对话框销毁器
                closeDialog:false,
            }
        },
        created() {
            this.getTableData();
        },
        methods:{
            // 查询用户数据
            // 系统内置一个title，默认为空，即全查询。每次条件查询都会把条件赋给该title。
            // 因此这样点击分页栏时可以正常显示查询结果。
            getTableData(){
                this.searchFormIn["pageSize"]=this.pageSize;
                this.searchFormIn["currentPage"]=this.currentPage;
                this.$axios.post("/UserCon/getPageByUsername",this.searchFormIn).then((res)=>{
                    if(res.data.flag){
                        /* 保存用户数据 */
                        this.tableData = res.data.data.records;
                        /* 保存分页数据 */
                        this.totalCount = res.data.data.total;
                        this.currentPage = res.data.data.current;
                        this.pageSize = res.data.data.size;
                    }else{
                        // 服务故障
                        this.$message.error("信息获取失败！");
                    }
                });
            },
            // 表格颜色控制
            tableRowClassName({row, rowIndex}) {
                if (rowIndex%4 === 1) {
                    return 'warning-row';
                } else if (rowIndex%4 === 3) {
                    return 'success-row';
                }
                return '';
            },
            // 添加用户
            insertUserOnSubmit(){
                this.userData.userId = '';
                this.userData.psw = '';
                this.closeDialog=true;
                this.insertDialogVisible=true;
            },
            // 查询用户
            searchUserOnSubmit(flag){
                if(flag === '1'){
                    // 刷新查询条件
                    this.searchFormIn.userId =  this.searchTitle.userId;
                    this.searchFormIn.username =  this.searchTitle.username;
                }else{
                    // 刷新查询条件
                    this.searchFormIn.userId =  '';
                    this.searchFormIn.username = '';
                }
                // 执行查询
                this.getTableData();
                /* 清空查询条件 */
                this.searchTitle.userId='';
                this.searchTitle.username='';
            },
            // 创建账户
            insertUser(user){
                /* 数据检查 */
                if(user.userId === '' || user.psw === ''){
                    this.$message.warning("请填写完整再提交！");
                    return;
                }
                // 数据加密
                user.psw = SHAEncrypt.methods.sha1(user.psw);
                user.password = user.psw;
                // 保存到数据库
                this.$axios.post("/UserCon/createUser",user).then((res)=>{
                    if(res.data.flag){
                        // 修改成功
                        this.$message.success(res.data.msg);
                        // 刷新用户数据
                        this.getTableData();
                        // 关闭编辑窗口
                        this.insertDialogVisible=false;
                    }else{
                        // 服务故障
                        this.$message.error(res.data.msg);
                    }
                });
            },
            // 修改密码激活
            updateUserPsw(row){
                // 预先绑定数据
                this.userData.userId = row.userId;
                this.userData.psw = '';
                this.userData.checkPsw = '';
                this.closeDialog = true;
                this.updatePswDialogVisible = true;
            },
            // 更新用户数据
            updateUserData(user){
                if(user.psw !== user.checkPsw || user.checkPsw === ''){
                    this.$message.warning("两次密码不一致！");
                    return;
                }
                // 数据加密
                user.psw = SHAEncrypt.methods.sha1(user.psw);
                user.checkPsw = SHAEncrypt.methods.sha1(user.checkPsw);
                // 保存到数据库
                this.$axios.post("/UserCon/updatePswRoot",user).then((res)=>{
                    if(res.data.flag){
                        // 修改成功
                        this.$message.success(res.data.msg);
                        // 刷新用户数据
                        this.getTableData();
                        // 关闭编辑窗口
                        this.updatePswDialogVisible=false;
                    }else{
                        // 服务故障
                        this.$message.error(res.data.msg);
                    }
                });
            },
            // 编辑用户数据dialog销毁
            dialogHandleClose(){
                // 手动销毁
                this.closeDialog = false;
            },
            // 当某行数据被点击
            rowClick(row,column){
                // 排除操作列
                if(column.label === '操作')return;
                //

            },
            // 注销用户
            deleteUser(row){
                // 弹框确认
                this.$confirm('此操作将注销该账户及其相关全部数据, 是否继续?', '警告', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let user = {userId:row.userId};
                    // 执行删除
                    console.log(user)
                    this.$axios.post("/UserCon/deleteUser",user).then((res)=>{
                        if(res.data.flag){
                            // 删除成功，刷新数据
                            this.getTableData();
                            this.$message.success('删除成功！');
                        }else{
                            this.$message.error('删除失败！');
                        }
                    })
                }).catch(() => {
                    // 取消删除
                    this.$message.info('取消删除');
                });
            },
            // 退出登陆
            quitLogin(){
                this.$confirm('确认退出登陆吗？', '提示', {
                    confirmButtonText: '退出',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.get("/UserCon/clearUserIdInSession").then((res)=>{
                        if(res.data.flag){
                            this.user = {};
                            this.$message.success(res.data.msg);
                            // 跳转到登陆页面
                            this.$router.replace({name:"Login"});
                        }else {
                            this.$message.error(res.data.msg);
                        }
                    })
                });
            },
            // 分页大小变化
            handleSizeChange(val) {
                this.pageSize = val;
                // 查询所有用户数据
                this.getTableData();
            },
            // 当前页变化
            handleCurrentChange(val) {
                this.currentPage = val;
                // 查询所有用户数据
                this.getTableData();
            },
        }
    }
</script>

<style scoped>

    /deep/.el-form-item{
        margin-bottom: 0;
    }
    /deep/.el-divider--horizontal {
        margin: 24px 0;
    }
    /* 退出按钮 */
    #quitBtn{
        font-size: 25px;
        position: relative;
        top: 8px;
        right: 20px;
        color: #707070;
    }
    #quitBtn:hover{
        color: #029ef3;
        cursor: pointer;
    }
    /* 确认文字 */
    .btLogin{
        color: #ffffff;
        font-size: 17px;
        font-weight: 500;
        font-family: "微软雅黑";
        /* 文字间距 */
        letter-spacing: 10px;
    }
    /* 表格行高控制 */
    ::v-deep .el-table .el-table__cell{
        padding: 9px 0;
    }
    .el-table{
        font-size: 17px;
    }
    /* 分页工具条上框 */
    #pageBar{
        margin-top: 20px;
    }
</style>