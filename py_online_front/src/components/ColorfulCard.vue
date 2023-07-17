<template>
    <div>
        <el-row>
            <el-col class="box-card" :span="5" v-for="(item,index) in data" :key="index">
                <el-card
                        :style="'background:'+cardColor[index%cardColor.length]"
                        class="classCard">
                    <div  style="height:100px">
                        <!-- 标题 -->
                        <el-row  class="title">
                            <el-col :span="19"><div class="textOutIgnore" @click="cardClick(item)">{{item[title]}}</div></el-col>
                            <!-- 编辑课程按钮 -->
                            <el-col :span="3" v-if="showEdit || item[judge] === judgeText">
                                <i class="editClass el-icon-edit" @click="editClick(item)"></i>
                            </el-col>
                            <!-- 删除课程按钮 -->
                            <el-col :span="2" :offset="showEdit?0:3" v-if="showDel || item[judge] === judgeText"><i class="editClass el-icon-delete" @click="deleteClick(item)"></i></el-col>
                        </el-row>
                        <p class="describes1 textOutIgnore" v-if="showDetail1">{{detail1Title}}{{item[detail1]}}</p>
                        <p class="describes2 textOutIgnore" v-if="showDetail2">{{detail2Title}}{{item[detail2]}}</p>
                        <div class="fillPosBox" @click="cardClick(item)"></div>
                        <!-- 信息栏 -->
                        <el-row  class="infoBar" @click="cardClick(item)">
                            <el-col :span="4" class="stuNum" v-show="showInfo">
                                <i class="el-icon-view"></i>{{" "+item[info]}}
                            </el-col>
                            <el-col :span="10" :offset="showInfo ? 10 : 14" class="dates">
                                <i class="el-icon-date"></i>{{" "+item[date]}}
                            </el-col>
                        </el-row>
                    </div>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "ColorfulCard",
        props:{
            data:{
                default:[],
            },
            title: {    // 标题key值
                default:'',
            },
            detail1: {  // 详情1key值
                default:'',
            },
            detail2: {  // 详情2key值
                default:'',
            },
            info: {     // 信息量key值
                default:'',
            },
            date: {     // 日期key值
                default:'',
            },
            judge: {     // 功能条件key值
                default:'',
            },
            judgeText: {  // 功能条件对比值
                default:'judgeDefault'
            },
            detail1Title: {default:''},    // 详情1标题
            detail2Title: {default:''},    // 详情2标题
            showDetail1: {default:false},  // 详情1默认关闭
            showDetail2: {default:false},  // 详情2默认关闭
            showInfo: {default:false},     // 信息数量默认关闭
            showEdit: {default:false},     // 编辑按钮默认关闭
            showDel: {default:false},      // 删除按钮默认关闭
        },
        data(){
            return{
                // 卡片颜色
                cardColor:['linear-gradient(to bottom right, #4762f3, #5cc3ff)',
                    'linear-gradient(to bottom right, #8360fa, #cb8dff)',
                    'linear-gradient(to bottom right, #66a6ff, #89f7fe)',
                    'linear-gradient(to bottom right, #fda085, #f6d365)',
                    'linear-gradient(to bottom right, #1fbdb8, #47e894)',],
            }
        },
        methods:{
            // 卡片点击事件-返回参数当前数据项
            cardClick(item){
                // 调用父组件方法并传参
                this.$emit('cardClick',item);
            },
            // 编辑按钮事件-返回参数当前数据项
            editClick(item){
                // 调用父组件方法并传参
                this.$emit('editClick',item);
            },
            // 删除按钮事件-返回参数当前数据项
            deleteClick(item){
                // 调用父组件方法并传参
                this.$emit('deleteClick',item);
            },
        }
    }
</script>

<style scoped>
    /* 卡片内边距 */
    /deep/.el-card__body{
        padding: 16px;
    }
    /* 卡片外边框 */
    .box-card {
        margin: 10px 24px 10px 24px;
    }
    /* 班级卡片 */
    .classCard{
        border-radius: 20px;
        min-height: 120px;
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
        font-size: 14px;
        color: #ffffff;
    }
    /* 文字长度超出后省略 */
    .textOutIgnore{
        white-space:nowrap;
        word-break:break-all;
        text-overflow:ellipsis;
        -o-text-overflow:ellipsis;
        overflow: hidden;
    }
    /* 班级简介 */
    .describes1{
        font-family: 微软雅黑;
        color: white;
        font-size: 13px;
        font-weight: 200;
        text-indent: 14px;   /* 首行缩进 */
        width: 95%;         /* 控制宽度 */
        position: absolute; /* 脱离文档流，相对于卡片进行定位 */
        top: 36px;          /* 距卡片顶部的距离*/
    }
    /* 班级简介 */
    .describes2{
        font-family: 微软雅黑;
        color: white;
        font-size: 13px;
        font-weight: 200;
        text-indent: 14px;   /* 首行缩进 */
        width: 95%;         /* 控制宽度 */
        position: absolute; /* 脱离文档流，相对于卡片进行定位 */
        top: 56px;          /* 距卡片顶部的距离*/
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
        top: 70%;
    }
    /* 班级人数 */
    .stuNum{
        /* 字体样式 */
        font-family: 微软雅黑,serif;
        font-size: 13px;
        color: #ffffff;
    }
    /* 日期 */
    .dates{
        /* 字体样式 */
        font-family: 微软雅黑,serif;
        font-size: 13px;
        color: #ffffff;
    }
</style>