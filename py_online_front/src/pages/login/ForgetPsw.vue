<template>
    <div>
        <el-container>
            <!--背景-->
            <el-main class="backGround" >
                <el-image src="img/login/bkImg.png" fit="cover" class="backGround"></el-image>
            </el-main>
            <!--logo-->
            <el-main id="logoM">
                <el-image src="img/login/logo.png" fit="cover" class="logo"></el-image>
            </el-main>
            <!--登录表单-->
            <el-main id="formM" >
                <span id="formTitle">用户登录</span>
                <!--修改密码表单-->
                <el-form id="form" label-width="80px">
                    <!-- 显示-验证ID后隐藏 -->
                    <el-form-item v-if="showForm1">  <!-- form标题 -->
                        <div class="input-title" style="margin-top: 50px">在此输入您的ID</div>
                    </el-form-item>
                    <el-form-item v-if="showForm1"> <!-- 账号 长度限制24 自带清除 -->
                        <el-input class="input" v-model="form.id" placeholder="用户ID" maxlength="24" clearable></el-input>
                    </el-form-item>
                    <el-form-item v-if="showForm1">  <!-- 确认按钮 -->
                        <el-button type="primary" @click="onSubmit1" class="confirm"><span class="btLogin">确认</span></el-button>
                    </el-form-item>
                    <!-- 隐藏-验证ID后显示 -->
                    <el-form-item v-if="showForm2">  <!-- 问题1 -->
                        <div class="input-title" style="margin-top: 30px">{{form.question1}}</div>
                    </el-form-item>
                    <el-form-item v-if="showForm2"> <!-- 答案1 -->
                        <el-input class="input" v-model="form.answer1is" maxlength="24" clearable></el-input>
                    </el-form-item>
                    <el-form-item v-if="showForm2">  <!-- 问题2 -->
                        <div  class="input-title">{{form.question2}}</div>
                    </el-form-item>
                    <el-form-item v-if="showForm2"> <!-- 答案2 -->
                        <el-input class="input" v-model="form.answer2is" maxlength="24" clearable></el-input>
                    </el-form-item>
                    <el-form-item v-if="showForm2">  <!-- 确认按钮 -->
                        <el-button type="primary" @click="onSubmit2" class="confirm"><span class="btLogin">确认</span></el-button>
                    </el-form-item>
                    <!-- 隐藏-验证回答后显示 -->
                    <el-form-item v-if="showForm3"> <!-- 密码 自带显示密码 -->
                        <el-input class="input" v-model="form.psw" placeholder="新密码" maxlength="24"
                                  autocomplete="off" clearable show-password style="margin-top: 40px"></el-input>
                    </el-form-item>
                    <el-form-item v-if="showForm3"> <!-- 确认密码 自带显示密码 -->
                        <el-input class="input" v-model="form.checkPsw" placeholder="确认密码" maxlength="24" autocomplete="off" clearable show-password></el-input>
                    </el-form-item>
                    <el-form-item v-if="showForm3">  <!-- 确认按钮 -->
                        <el-button type="primary" @click="onSubmit3" class="confirm"><span class="btLogin">确认</span></el-button>
                    </el-form-item>

                </el-form>
            </el-main>
        </el-container>
    </div>

</template>

<script>
    import SHAEncrypt from "@/components/SHAEncrypt";
    export default {
        name: "ForgetPsw",
        components:{
            SHAEncrypt,
        },
        data(){
            return {
                /* 表单数据模型 */
                form:{
                    id:'',
                    psw:'',
                    checkPsw:'',
                    answer1is:'',
                    answer2is:'',

                    question1:'问题1',
                    answer1:'',
                    question2:'问题2',
                    answer2:'',
                },
                showForm1: true,
                showForm2: false,
                showForm3: false,
            }
        },
        // 方法模型
        methods: {
            // 提交表单1 验证账号
            onSubmit1(){
                let form =  /^\w{4,24}$/;
                // 验证账号格式
                if(form.test(this.form.id) === false){
                    this.$message.error('账号长度应在4-24位之间！');
                    return;
                }
                // 发送表单给后端
                this.$axios.post("/UserCon/getQuestion",this.form).then((res)=>{
                    if(res.data.flag){
                        // 验证成功
                        this.form.question1 = res.data.data.question1;
                        this.form.question2 = res.data.data.question2;
                        if(this.form.question1 == null ||  this.form.question2 == null){
                            // 未设置密保问题
                            setTimeout(()=>{this.$message.success("您未设置密保问题，请联系管理员修改。即将跳转到登陆界面！");},0)
                            // 2秒后跳转
                            setTimeout(()=>{this.$router.push({name:'Login'});}, 2000);
                        }else{
                            this.$message.success("验证成功");
                            this.form.answer1 = res.data.data.answer1;
                            this.form.answer2 = res.data.data.answer2;
                            // 切换显示表单
                            this.showForm1 = false ;
                            this.showForm2 = true;
                        }
                    }else{
                        // 验证失败
                        this.$message.warning("账号不存在");
                    }
                });
            },
            // 提交表单2 验证自定义问题
            onSubmit2(){
                if(this.form.answer1 === this.form.answer1is && this.form.answer2 === this.form.answer2is){
                    // 验证成功 切换显示表单
                    this.showForm2 = false;
                    this.showForm3 = true;
                    this.$message.success("验证成功");
                }else{
                    // 验证失败
                    this.$message.warning("答案错误");
                }
            },
            // 提交表单3
            onSubmit3(){
                if(this.form.psw !== this.form.checkPsw || this.form.checkPsw === ''){
                    this.$message.warning('两次输入的密码不同！');
                    return false;
                }else{
                    // password加密
                    this.form.psw = SHAEncrypt.methods.sha1(this.form.psw);
                    // checkPassword加密
                    this.form.checkPsw = SHAEncrypt.methods.sha1(this.form.checkPsw);
                    this.$axios.post("/UserCon/updatePsw",this.form).then((res)=>{
                        if(res.data.flag){
                            // 修改成功
                            this.$message.success(res.data.msg + "即将跳转到登陆界面！");
                            // 2秒后跳转
                            setTimeout(()=> {this.$router.push({name:'Login'});}, 2000);
                        }else{
                            this.form.psw = '';
                            this.form.checkPsw = '';
                            // 验证失败
                            this.$message.warning(res.data.msg);
                        }
                    });
                }
            }
        }
    }
</script>

<style scoped>
    /*背景*/
    .backGround {
        position: absolute;
        top: 0;
        left: 0;
        width: 100vw;
        height: 100vh;
        z-index: -5;
    }
    /*logo样式*/
    #logoM{
        position: absolute;
        top:73vh;
        left: 5vw;
        width: 50vw;
        height: 25vh;
        z-index: -4;
    }
    /*logo图片*/
    .logo{
        width: 40vw;
        height: 18vh;
    }
    /*表单样式*/
    /*表单容器*/
    /*表单容器*/
    #formM{
        position: absolute;
        top:15vh;
        right: 20vw;
        width: 28vw;
        height: 45vh;
        z-index: -4;
        background-color: #FFFFFF;
    }
    /* form 表单 */
    #form{
        position: relative;
        top:20px;
        right:40px;
    }
    /*表单项底边距*/
    .el-form-item {
        margin-bottom: 7px;
    }
    /*表单项行高*/
    ::v-deep .el-form-item__content {
        line-height: 19px;
    }
    /*表单标题*/
    #formTitle{
        position:relative;
        top: 0;
        left : 9vw;
        font-size: 20px;
        z-index: -4;
    }
    /* 输入提示 */
    .input-title{
        color: #000000;
        font-size: 14px;
        font-family: "微软雅黑";
        font-weight: 400;
        /* 文字间距 */
        letter-spacing: 1px;
    }
    /* 确认按钮 */
    .confirm{
        height: 40px;
        width: 100%;
        /* 圆角半径 */
        border-radius: 10px;
        background-color: #4395ff;
    }
    /* 确认按钮活动 */
    .confirm:hover{
        background-color: #328bff;
    }
    /* 确认文字 */
    .btLogin{
        color: #ffffff;
        position: relative;
        bottom: 3px;
        font-size: 17px;
        font-weight: 400;
        font-family: "微软雅黑";
        /* 文字间距 */
        letter-spacing: 10px;
    }
</style>