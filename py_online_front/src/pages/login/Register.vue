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
                <span id="formTitle">用户注册</span>
                <!--账号输入框-->
                <el-form id="form" :model="form" label-width="80px">
                    <el-form-item> <!-- 账号 长度限制24 自带清除 -->
                        <el-input class="input" v-model="form.id" placeholder="用户ID" maxlength="24" clearable></el-input>
                    </el-form-item>
                    <el-form-item> <!-- 密码 自带显示密码 -->
                        <el-input  class="input" v-model="form.psw" placeholder="密码" maxlength="24" clearable show-password></el-input>
                    </el-form-item>
                    <el-form-item> <!-- 密码 自带显示密码 -->
                        <el-input  class="input" v-model="form.checkPsw" placeholder="确认密码" maxlength="24" clearable show-password></el-input>
                    </el-form-item>
                    <el-form-item> <!-- 密保问题1 -->
                        <el-input class="input" v-model="form.question1" placeholder="密保问题1" maxlength="255" clearable></el-input>
                    </el-form-item>
                    <el-form-item> <!-- 密保答案1 -->
                        <el-input class="input" v-model="form.answer1" placeholder="密保答案1" maxlength="255" clearable></el-input>
                    </el-form-item>
                    <el-form-item> <!-- 密保问题2 -->
                        <el-input class="input" v-model="form.question2" placeholder="密保问题2" maxlength="255" clearable></el-input>
                    </el-form-item>
                    <el-form-item> <!-- 密保答案2 -->
                        <el-input class="input" v-model="form.answer2" placeholder="密保答案2" maxlength="255" clearable></el-input>
                    </el-form-item>
                    <el-form-item> <!-- 身份选择学生/老师 -->
                        <el-select v-model="form.identity" placeholder="账户类型">
                            <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
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
                    <el-form-item>  <!-- 登陆确认按钮 -->
                        <el-button type="primary" @click="onSubmit" id="confirm"><span id="btLogin">注册</span></el-button>
                    </el-form-item>
                    <el-form-item> <!-- 同意协议 -->
                        <span id="user-rules"><el-checkbox id="iCheckbox" v-model="checked" label=" "></el-checkbox>我同意<router-link id="agree-rules" :to="{name:'PrivacyPolicy'}">《安全协议》和《隐私协议》</router-link></span>
                    </el-form-item>
                </el-form>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import SHAEncrypt from "@/components/SHAEncrypt";
    export default {
        name: "Register",
        components:{
            SHAEncrypt,
        },
        data(){
            return{
                // 账号规则
                format: /^\w{4,24}$/,
                form:{
                    id:'',
                    psw:'',
                    checkPsw:'',
                    question1:'',
                    question2:'',
                    answer1:'',
                    answer2:'',
                    identity:1,
                    verifyCode:'',
                },
                verifyCodeShow:false,
                options: [{
                    value: 0,
                    label: '我是老师'
                }, {
                    value: 1,
                    label: '我是学生'
                }],
                // 同意协议
                checked:false,
                // 已注册跳转
                registered:false,

            }

        },
        // 通过路由规则，离开该组件时被调用
        beforeRouteLeave (to, from, next) {
            this.verifyCodeShow = false;
            if(this.Registered){
                // 数据销毁
                this.form.id = '';
                this.form.psw = '';
                this.form.question1 = '';
                this.form.question2 = '';
                this.form.answer1 = '';
                this.form.answer2 = '';
                this.form.identity = '';
                this.form.checkPsw = '';
                this.form.verifyCode = '';
                this.checked = false;
                this.Registered = false;
            }
            next();
        },
        activated() {
            // 刷新验证码
            this.changeVerifyImg();
            this.verifyCodeShow = true;
        },
        methods:{
            // 验证账号格式
            idBlur(){
                if(this.format.test(this.form.id) === false){
                    this.$message.error('账号长度应在4-24位之间！');
                    return false;
                }
                return true;
            },
            // 验证二次密码格式
            checkPswBlur(){
                if(this.form.psw !== this.form.checkPsw || this.form.checkPsw === ''){
                    this.$message.error('两次输入的密码不同！');
                    return false;
                }
                return true;
            },

            onSubmit(){
                // 检查数据格式
                if(!this.idBlur()){
                    return;
                }
                if(!this.checkPswBlur()){
                    return;
                }
                // 检测密保格式
                if(this.form.question1.length === 0 || this.form.question2.length === 0 || this.form.answer1.length === 0 || this.form.answer2.length === 0){
                    this.$message.error('密保内容不能为空！');
                    return;
                }
                // 验证码格式
                let formCode = /^\w{4}$/;
                if(formCode.test(this.form.verifyCode) === false){
                    this.$message.error('验证码格式有误！');
                    return;
                }

                // 检查勾选协议
                if(this.checked === false){
                    this.$message.warning('请勾选同意协议~');
                    return;
                }
                // password加密
                this.form.psw = SHAEncrypt.methods.sha1(this.form.psw);
                // checkPassword加密
                this.form.checkPsw = SHAEncrypt.methods.sha1(this.form.checkPsw);
                // 发送表单给后端
                this.$axios.post("/UserCon/register",this.form).then((res)=>{
                    if(res.data.flag){
                        // 执行数据销毁
                        this.Registered = true;
                        // 注册成功
                        this.$message.success(res.data.msg + "即将跳转到登陆界面！");
                        // 2秒后跳转
                        setTimeout(()=> {this.$router.push({name:'Login'});}, 1000);
                    }else{
                        this.form.psw = '';
                        this.form.checkPsw = '';
                        // 注册失败
                        this.$message.warning(res.data.msg);
                    }
                    // 切换验证码
                    this.changeVerifyImg();
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
        top:5vh;
        right: 20vw;
        width: 28vw;
        height: 80vh;
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
    /* 注意项 */
    #user-rules{
        left: 10px;
        position: relative;
        font-size: 16px;
        font-weight: 500;
        color: #b5b7b4;
        /* 文字间距 */
        letter-spacing: 1px;
    }
    /* checkBox选中色 */
    .el-checkbox__input.is-checked .el-checkbox__inner{
        background-color: #29909e;
        border-color: #29909e;
    }
    /* checkbox选中边框色 */
    .el-checkbox__input.is-focus .el-checkbox__inner{
        border-color: #29909e;
    }
    /* checkBox悬浮鼠标边框色*/
    .el-checkbox__inner:hover{
        border-color: #29909e;
    }
    /* 用户协议超链接 */
    #agree-rules{
        text-decoration: none;
        color:  #29909e;
    }
    /* 用户协议悬浮 */
    #agree-rules:hover{
        text-decoration: underline;
        color: #29909e;
    }

</style>