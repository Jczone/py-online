<template>
    <div>
        <el-container direction="vertical">
            <!--背景-->
            <el-main class="backGround" >
                <el-image src="img/login/bkImg.png" fit="cover" class="backGround"></el-image>
            </el-main>
            <!--logo-->
            <el-main id="logoM">
                <el-image src="img/login/logo.png" fit="fill" class="logo"></el-image>
            </el-main>
            <!--登录表单-->
            <el-main id="formM" >
                <span id="formTitle">用户登录</span>
                <!--账号输入框-->
                <el-form id="form" :model="form" label-width="80px">
                    <el-form-item> <!-- 账号 长度限制24 自带清除 -->
                        <el-input class="input" v-model="form.id" placeholder="用户ID" maxlength="24" clearable></el-input>
                    </el-form-item>
                    <el-form-item> <!-- 密码 自带显示密码 -->
                        <el-input  id="pswInput" class="input" v-model="form.psw" placeholder="密码" maxlength="24" clearable show-password></el-input>
                    </el-form-item>
                    <el-form-item> <!-- 验证码 -->
                        <el-row>
                            <el-col :span="12">
                                <el-input id="verifyCodeBox" class="input" v-model="form.verifyCode" maxlength="4" placeholder="验证码"></el-input>
                            </el-col>
                            <!-- 验证码图片 -->
                            <el-col :span="12">
                                <div id="verifyCode" v-show="verifyCodeShow">
                                    <img src="/checkCodeServlet" id="verifyCodeImg" @click="changeVerifyImg" alt="图片下班了~">
                                </div>
                            </el-col>
                        </el-row>

                    </el-form-item>
                    <el-form-item> <!-- 忘记密码，注册账号-->
                        <router-link id="forgotPsw-sy" :to="{name:'ForgetPsw'}">忘记密码？</router-link>
                        <router-link id="register-sy" :to="{name:'Register'}">注册账号</router-link>
                    </el-form-item>

                    <el-form-item>  <!-- 登陆确认按钮 -->
                        <el-button type="primary" @click="onSubmit" id="confirm"><span id="btLogin">登录</span></el-button>
                    </el-form-item>
                </el-form>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import SHAEncrypt from "@/components/SHAEncrypt";
    export default {
        name: "Login",
        components:{
            SHAEncrypt,
        },
        data(){
            return{
                form:{
                    id:'',
                    psw:'',
                    verifyCode:'',
                },
                verifyCodeShow:false,
            }
        },
        mounted() {
            // 生命周期钩子函数
            // 刷新验证码
            this.changeVerifyImg();
            this.verifyCodeShow = true;
        },
        methods:{
            onSubmit(){
                let form =  /^\w{4,24}$/;
                // 验证账号格式
                if(form.test(this.form.id) === false){
                    this.$message.error('账号格式有误哦~');
                    return;
                }
                // 验证密码格式
                if(form.test(this.form.psw) === false){
                    this.$message.error('密码格式有误哦~');
                    return;
                }
                // 验证码格式
                let formCode = /^\w{4}$/;
                if(formCode.test(this.form.verifyCode) === false){
                    this.$message.error('验证码格式有误哦~');
                    return;
                }
                // password加密
                this.form.psw = SHAEncrypt.methods.sha1(this.form.psw);
                this.$axios.post("/UserCon/verifyLogin",this.form).then((res)=>{
                    if(res.data.flag){
                        // 用户id存入session
                        this.$axios.post("/UserCon/setUserIdToSession",this.form).then((resp)=>{
                            if(resp.data.flag){
                                if(res.data.data.identity === 0){
                                    this.$router.push({
                                        name:"TeaClassPage"
                                    });
                                }else{
                                    this.$router.push({
                                        name:"StuClassPage"
                                    });
                                }
                            }else{
                                this.$message.error(res.data.msg);
                            }
                        });
                    }else{
                        this.form.psw='';
                        this.form.verifyCode = '';
                        this.changeVerifyImg();//刷新验证码图片
                        this.$message.error(res.data.msg);
                    }
                });
            },
            // 切换验证码图片
            changeVerifyImg(){
                document.getElementById("verifyCodeImg").src  = "/checkCodeServlet?"+new Date().getMilliseconds();
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
    /*logo容器*/
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
    #formM{
        position: absolute;
        top:15vh;
        right: 20vw;
        width: 28vw;
        height: 45vh;
        z-index: -4;
        background-color: #FFFFFF;
    }
    /*表单*/
    #form{
        position: relative;
        top:20px;
        right:40px;
    }
    /*表单项底边距*/
    .el-form-item {
        margin-bottom: 7px;
    }
    /*表单标题*/
    #formTitle{
        position:relative;
        top: 0;
        left : 9vw;
        font-size: 20px;
        z-index: -4;
    }
    /* 验证码 */
    #verifyCodeImg{
        /* 大小 */
        width: 80%;
    }
    /*验证码容器*/
    #verifyCode{
        position: relative;
        left: 15px;
    }
    /*表单项行高*/
    ::v-deep .el-form-item__content {
        line-height: 19px;
    }

    /* 忘记密码超链接 */
    #forgotPsw-sy{
        left: 10px;
        position: relative;
        text-decoration: none;
        font-size: 14px;
        font-weight: 500;
        color: #b5b7b4;
        /* 文字间距 */
        letter-spacing: 1px;
    }
    /* 忘记密码悬浮 */
    #forgotPsw-sy:hover{
        text-decoration: underline;
        color: #888a88;
    }
    /*注册账号超链接*/
    #register-sy{
        left: 11em;
        position: relative;
        text-decoration: none;
        font-size: 14px;
        font-weight: 500;
        color: #b5b7b4;
        /* 文字间距 */
        letter-spacing: 1px;
    }
    /* 注册账号悬浮 */
    #register-sy:hover{
        text-decoration: underline;
        color: #888a88;
    }
    /* 登录按钮 */
    #confirm{
        height: 40px;
        width: 100%;
        /* 圆角半径 */
        border-radius: 10px;
        background-color: #4395ff;
    }
    /* 登录按钮活动 */
    #confirm:hover{
        background-color: #328bff;
    }
    /* 登录文字 */
    #btLogin{
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