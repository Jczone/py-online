<template>
    <div>
        <el-container>
            <!-- 侧边栏 -->
            <el-aside width="160" id="teaPageAside">
                <!-- 用户头像 -->
                <div id="userAvatarBox">
                    <img :src="user.avatarUrl"
                         @click="openEditUserInfoDialog"
                         id="userAvatarImg"/>
                </div>
                <!-- 导航栏 -->
                <el-menu
                        id="navigationMenu"
                        default-active="1"
                        @select="handleSelect"
                        :collapse="isCollapse">
                    <!-- 实验中心 -->
                    <el-menu-item index="1">
                        <i class="ali-iconpython-file-" style="margin: 0 7px 0 5px"></i>
                        <span slot="title">实验中心</span>
                    </el-menu-item>
                    <!-- 课程通知 -->
                    <el-menu-item index="2">
                        <i class="ali-icontongzhi" style="margin: 0 5px 0 5px; font-size: 18px"></i>
                        <span slot="title">通知中心</span>
                    </el-menu-item>
                    <!-- 返回首页 -->
                    <el-menu-item index="3">
                        <i class="el-icon-house"></i>
                        <span slot="title">返回首页</span>
                    </el-menu-item>
                    <!-- 退出登陆 -->
                    <el-menu-item index="4">
                        <i class="ali-icontuichu" style="margin: 0 7px 0 5px"></i>
                        <span slot="title">退出登录</span>
                    </el-menu-item>
                </el-menu>
                <!-- 折叠按钮 -->
                <div id="collapseFlagBox">
                    <span id="collapseFlag">
                        <i v-if="!isCollapse" class="el-icon-d-arrow-left" @click="foldSideBar"></i>
                        <i v-else class="el-icon-d-arrow-right" @click="openSideBar"></i>
                    </span>
                </div>
                <!-- 班级名称 -->
                <div id="className" :style="isCollapse?'display:none':'display:block'">{{classGroup.className}}</div>
            </el-aside>
            <!-- 主体 -->
            <el-main>
                <el-card shadow="never" style="min-height: 600px">
                    <router-view></router-view>
                </el-card>
            </el-main>
            <!-- 修改个人信息对话框 -->
            <el-main v-if="updateUserInfoDialogVisible">
                <el-dialog
                        title="编辑个人信息"
                        :visible.sync="updateUserInfoDialogVisible"
                        width="35%">
                    <el-form ref="form" :model="user" label-width="80px">
                        <el-form-item label="用户名">
                            <el-input v-model="user.username"></el-input>
                        </el-form-item>
                        <el-form-item label="密保问题1">
                            <el-input v-model="user.question1"></el-input>
                        </el-form-item>
                        <el-form-item label="答案1">
                            <el-input v-model="user.answer1"></el-input>
                        </el-form-item>
                        <el-form-item label="密保问题2">
                            <el-input v-model="user.question2"></el-input>
                        </el-form-item>
                        <el-form-item label="答案2">
                            <el-input v-model="user.answer2"></el-input>
                        </el-form-item>
                        <!-- 头像上传 -->
                        <el-form-item>
                            <el-upload
                                    id="userAvatarUploader"
                                    action="/Files/userAvatarUpload"
                                    :show-file-list="false"
                                    :on-success="handleUserAvatarSuccess"
                                    :before-upload="beforeAvatarUpload">
                                <el-image id="userAvatar" v-if="editAvatarUrl" :src="editAvatarUrl" :fit="coverImgFit"></el-image>
                                <i class="el-icon-plus" id="userAvatarUploader-icons">上传头像</i>
                            </el-upload>
                        </el-form-item>

                        <el-form-item>
                            <el-button type="primary" @click="updateUserInfo">提交</el-button>
                            <el-button @click="updateUserInfoDialogVisible = false">取消</el-button>
                        </el-form-item>
                    </el-form>
                </el-dialog>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "TeaMainPage",
        data(){
            return{
                // 导航栏折叠标志
                isCollapse: false,
                // 导航栏数据模型
                activeIndex: '',
                // 用户数据
                user:{
                    username:'',
                    avatarUrl:'',
                },
                // 班级数据
                classGroup:{},
                // 修改个人信息对话框可见
                updateUserInfoDialogVisible: false,
                // 编辑用户头像模型
                editAvatarUrl:'',
                // 图片显示的裁切方式
                coverImgFit:'cover',
            }
        },
        created() {
            // 获取当前用户数据
            this.getUserInfo();
            // 查询当前所在班级数据
            this.getClassGroupBySession();
            /* 默认启动子组件 */
            this.$router.replace({
                name:"TeaSectionPage"
            })
        },
        methods:{
            // 获取用户数据
            getUserInfo(){
                this.$axios.get("/UserCon/getUserInfo").then((res)=>{
                    if(res.data.flag){
                        this.user = res.data.data;
                    }else{
                        this.$message.error(res.data.msg);
                    }
                })
            },
            // 查询当前所在班级数据
            getClassGroupBySession(){
                this.$axios.get("/ClassCon/getClassGroupBySession").then((res)=>{
                    if(res.data.flag){
                        this.classGroup = res.data.data;
                    }else{
                        // 服务故障
                        this.$message.error(res.data.msg);
                    }
                });
            },
            // 导航栏事件
            handleSelect(key) {
                let n = Number(key);
                /* 格式检查 */
                if (isNaN(n)) {
                    this.$message.error('服务出错，请刷新重试！');
                } else {
                    switch (key) {
                        case '1':  // 实验中心
                            this.$router.replace({
                                name:"TeaSectionPage"
                            })
                            return;
                        case '2': // 课程通知
                            this.$router.replace({
                                name:"TeaNoticePage"
                            })
                            return;
                        case '3': // 返回首页
                            this.$router.replace({
                                name:"TeaClassPage"
                            })
                            return;
                        case '4': // 退出登录
                            this.$confirm('确认退出登录吗?', '提示', {
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
                            return;
                    }
                }
            },
            // 折叠侧边栏
            foldSideBar(){
                this.isCollapse = true;
                document.getElementById("userAvatarImg").style.left = "5px";
                document.getElementById("teaPageAside").style.width = "70px";

            },
            // 展开侧边栏
            openSideBar(){
                this.isCollapse = false;
                document.getElementById("userAvatarImg").style.left = "40px";
                document.getElementById("teaPageAside").style.width = "160px";
            },
            // 头像点击事件-弹出信息修改窗
            openEditUserInfoDialog(){
                this.editAvatarUrl = this.user.avatarUrl;
                this.updateUserInfoDialogVisible = true;
            },
            // 头像上传成功的钩子-回显到界面
            handleUserAvatarSuccess(res) {
                this.editAvatarUrl = res;
            },
            // 上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传。
            beforeAvatarUpload(file) {
                const isJPG = file.type === ('image/jpeg');
                const isPNG = file.type === ('image/png');

                const isLt2M = file.size / 1024 / 1024 < 40;
                if (!isJPG && !isPNG) {
                    this.$message.error('上传头像图片只能是 JPG/PNG 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传头像图片大小不能超过 40MB!');
                }
                return isJPG||isPNG && isLt2M;
            },
            // 更新用户信息
            updateUserInfo(){
                // 删除旧图
                this.$axios.post("/Files/deleteUserAvatar",this.user);
                // 写入图片路径
                this.user.avatarUrl = this.editAvatarUrl;
                this.$axios.post("/UserCon/updateUserInfo",this.user).then((res)=>{
                    if(res.data.flag){
                        // 修改成功
                        this.$message.success(res.data.msg);
                        this.updateUserInfoDialogVisible = false;
                        this.getUserInfo(); // 更新用户数据
                    }else{
                        // 服务故障
                        this.$message.error(res.data.msg);
                    }
                });
            },
        }
    }
</script>

<style scoped>
    /* 布局容器 */
    #teaPageAside{
        width: 160px;
        transition: width 0.6s;
        min-height: 720px;
        overflow: hidden;
    }
    /* 用户头像容器 */
    #userAvatarBox{
        margin-top: 40px;
        margin-bottom: 20px;
    }
    /* 头像图片 */
    #userAvatarImg{
        width: 50px;
        height: 50px;
        position: relative;
        left: 40px;
        transition: left 0.5s;
        /* 设置成圆形-圆角 */
        border-radius:50%;
        /* 图像缩放算法 */
        image-rendering: high-quality;
        -ms-interpolation-mode:nearest-neighbor;
        /* 图片适应 */
        object-fit: cover;
    }
    /* 头像活动鼠标指针 */
    #userAvatarImg:hover{
        cursor: pointer;
    }
    /* 导航菜单 */
    #navigationMenu:not(.el-menu--collapse){
        width: 155px;
        min-height: 550px;
    }
    /* 导航菜单 */
    #navigationMenu{
        min-height: 550px;
    }
    /* 导航栏折叠按钮容器 */
    #collapseFlagBox{
        margin-left: 25px;
    }
    /* 导航栏折叠按钮 */
    #collapseFlag{
        position:relative;
        bottom:60px;
    }
    /* 折叠按钮活动鼠标样式 */
    #collapseFlag:hover{
        cursor: pointer;
    }
    /* 班级名称 */
    #className{
        width: 90%;
        position: relative;
        bottom: 40px ;
        color: #474C59;
        font-size: 12px;
        text-align: center;
    }
    /*
     * 修改用户信息
     */
    #userAvatarUploader{
        border: 1px dashed #a7a7a7;
        border-radius: 6px; /* 圆角 */
        cursor: pointer;    /* 鼠标样式 */
        position: relative;
        overflow: hidden;   /* 隐藏滚动 */
        width: 140px;
        height: 140px;
    }
    #userAvatarUploader:hover{
        border-color: #409EFF;
    }
    /* 上传头像 */
    ::v-deep #userAvatar{
        width: 140px;
        height: 140px;
    }
    #userAvatarUploader-icons{
        font-size: 18px;
        font-family: "微软雅黑";
        color: #ffffff;
        /* 定位 */
        top: 45%;
        left: 25%;
        position: absolute;
        z-index: 5;
    }
</style>