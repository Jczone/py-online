<template>
    <div>
        <!-- 工具栏 -->
        <div>
            <div>
                <span id="pageTitle">Python在线实验系统</span>
                <span id="pageType">学生端</span>
                <span id="userAvatarBox">
                    <img :src="user.avatarUrl"
                         @click="openEditUserInfoDialog"
                         id="userAvatarImg"/>
                     <i id="quitBtn" class="ali-icontuichu-copy" @click="quitLogin"></i>
                </span>
            </div>
            <el-divider></el-divider>
        </div>
        <!-- 主空间 -->
        <el-main style="min-height: 500px">
            <el-row>
                <el-col class="box-card" :span="7" v-for="(item,index) in classViews" :key="index">
                    <el-card
                            :style="'background:'+cardColor[index%cardColor.length]"
                            class="classCard">
                        <!-- 一般班级 -->
                        <div v-if="index < classViews.length - 1" style="height: 106px">
                            <!-- 班级名称 -->
                            <el-row  class="title">
                                <el-col :span="22"><div @click="gotoClass(item.classId)">{{item.className}}</div></el-col>
                                <!-- 退出班级按钮 -->
                                <el-col :span="1"><i class="editClass el-icon-delete" @click="quitClass(item)"></i></el-col>
                            </el-row>
                             <p class="describes">{{item.describes}}</p>
                            <div class="fillPosBox" @click="gotoClass(item.classId)"></div>
                            <!-- 信息栏 -->
                            <div class="infoBar" @click="gotoClass(item.classId)">
                                <span class="stuNum">
                                    <i class="el-icon-user"></i>{{" "+item.stuNum}}
                                </span>
                                <span class="dates">
                                    <i class="el-icon-date"></i>{{" "+item.formatDate}}
                                </span>
                            </div>
                        </div>
                        <!-- 加入班级 -->
                        <div v-else style="height: 106px"  @click="joinClassGroup">
                            <div class="newClass">
                                <!-- 加入班级按钮-->
                                <i class="el-icon-plus"></i>加入班级
                            </div>
                        </div>

                    </el-card>
                </el-col>
            </el-row>
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
                    <!-- 提交按钮 -->
                    <el-form-item>
                        <el-button type="primary" @click="updateUserInfo">提交</el-button>
                        <el-button @click="updateUserInfoDialogVisible = false">取消</el-button>
                    </el-form-item>
                </el-form>
            </el-dialog>
        </el-main>
        <!-- 底栏 -->
        <page-footer></page-footer>
    </div>
</template>

<script>
    import PageFooter from "@/components/PageFooter";
    export default {
        name: "StuClassPage",
        components:{
            PageFooter,
        },
        data() {
            return {
                // 用户数据
                user:{
                    username:'',
                    avatarUrl:'',
                },
                // 修改个人信息对话框可见
                updateUserInfoDialogVisible: false,
                coverImgFit:'cover',
                // 编辑用户头像模型
                editAvatarUrl:'',
                classViews:[],  // 班级列表
                // 卡片颜色
                cardColor:['linear-gradient(to bottom right, #4762f3, #5cc3ff)',
                    'linear-gradient(to bottom right, #8360fa, #cb8dff)',
                    'linear-gradient(to bottom right, #66a6ff, #89f7fe)',
                    'linear-gradient(to bottom right, #fda085, #f6d365)',
                    'linear-gradient(to bottom right, #1fbdb8, #47e894)',],
            }
        },
        created() {
            this.getUserInfo();
            this.getJoinClass();   // 查询学生加入的班级信息
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
            // 查询学生加入的班级信息
            getJoinClass(){
                this.$axios.get("/ClassCon/getJoinClass").then((res)=>{
                    if(res.data.flag){
                        this.classViews = res.data.data;
                        this.classViews[this.classViews.length] = {className:"加入班级"};
                    }else{
                        // 服务故障
                        this.$message.error(res.data.msg);
                    }
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
            // 加入新的班级
            joinClassGroup(){
                // 弹窗获取班级邀请码
                this.$prompt('加入班级', '班级邀请码', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputPattern:/^\d{1,5}$/,
                    inputErrorMessage:'邀请码不能为空且必须为数字'
                }).then(({ value }) => {
                    // 发送post请求创建新章节
                    this.$axios.post("/ClassCon/joinClassGroup",{classId:value}).then((res)=>{
                        if(res.data.flag){
                            this.$message.success(res.data.msg);
                            // 无条件刷新班级列表
                            this.getJoinClass('');
                        }else{
                            this.$message.error(res.data.msg);
                        }
                    })
                });
            },
            // 退出班级
            quitClass(classView){
                this.$confirm('是否要退出班级？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.post("/ClassCon/quitClass",classView).then((res)=>{
                        if(res.data.flag){
                            this.getJoinClass();   // 刷新班级列表
                        }else{
                            // 服务故障
                            this.$message.error(res.data.msg);
                        }
                    });
                });
            },
            // 跳转到班级内部
            gotoClass(classId){
                let form = {classId:classId};
                this.$axios.post("/ClassCon/setClassIdToSession",form).then((res)=>{
                    if(res.data.flag){
                        this.$router.push({
                            name:"StuMainPage"
                        })
                    }else{
                        // 服务故障
                        this.$message.error(res.data.msg);
                    }
                });
            },
            // 头像点击事件-弹出信息修改窗
            openEditUserInfoDialog(){
                this.editAvatarUrl=this.user.avatarUrl;
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

    /* 页面标题 */
    #pageTitle{
        font-size: 30px;
        margin-left: 36px;
    }
    /* 页面类型 */
    #pageType{
        font-size: 20px;
        margin-left: 10px;
    }
    /* 头像布局 */
    #userAvatarBox{
        position: relative;
        top: 12px;
        left: 62%;
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
    /* 退出按钮 */
    #quitBtn{
        font-size: 25px;
        position: relative;
        left: 60px;
        bottom: 15px;
        color: #707070;
    }
    #quitBtn:hover{
        color: #029ef3;
        cursor: pointer;
    }
    /deep/.el-divider--horizontal {
        margin: 12px 0;
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

    /* 卡片内边距 */
    /deep/.el-card__body{
        padding: 16px;
    }
    /* 卡片外边框 */
    .box-card {
        margin: 10px 28px 10px 28px;
    }
    /* 班级卡片 */
    .classCard{
        min-height: 140px;
        transform: translateY(0);
        transition: transform 300ms linear;
    }
    /* 班级卡片效果*/
    .classCard:hover{
        cursor: pointer;
        position: relative;
        transform: translateY(-3px) translateZ(0);
        box-shadow: 0 1px 6px 0 rgba(0, 0, 0, 0.2), 0 3px 12px 0 rgba(0, 0, 0, 0.19);
    }
    /* 班级编辑图标 */
    .editClass{
        display: none;
    }
    .editClass:hover{
        color: #5ff0f0;
    }
    /* 班级卡片效果*/
    .classCard:hover .editClass{
        display: block;
    }
    /* 班级名称 */
    .title{
        font-family: 微软雅黑;
        font-size: 17px;
        color: #ffffff;
    }
    /* 班级简介 */
    .describes{
        font-family: 微软雅黑;
        color: white;
        font-size: 14px;
        font-weight: 200;
        text-indent: 2em;   /* 首行缩进 */
        width: 95%;         /* 控制宽度 */
        position: absolute; /* 脱离文档流，相对于卡片进行定位 */
        top: 30px;          /* 距卡片顶部的距离*/
    }
    /* 占位框 */
    .fillPosBox{
        position: absolute; /* 脱离文档流，相对于卡片进行定位 */
        top: 36px;          /* 距卡片顶部的距离*/
        height: 55%;
        width: 95%;
    }
    /* 信息栏 */
    .infoBar{
        position: relative;
        top: 70px;
    }
    /* 班级人数 */
    .stuNum{
        /* 字体样式 */
        font-family: 微软雅黑,serif;
        font-size: 14px;
        color: #ffffff;
    }
    /* 日期 */
    .dates{
        /* 字体样式 */
        font-family: 微软雅黑,serif;
        font-size: 14px;
        color: #ffffff;
        /* 框模型 */
        position: absolute;
        left: 280px;
    }
    /* 新建班级按钮 */
    .newClass{
        /* 字体样式 */
        font-family: 微软雅黑,serif;
        font-size: 25px;
        font-weight: 200;
        color: #ffffff;
        text-align: center;
        position: relative;
        top: 30px;
    }

</style>