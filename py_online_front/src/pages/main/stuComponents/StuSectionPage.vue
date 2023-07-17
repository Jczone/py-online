<template>
    <div>
        <!-- 工具栏 -->
        <el-row>
            <!-- 标题 -->
            <el-col :span="2" :offset="1">
                <span style="font-size: 22px; line-height: 30px">实验中心</span>
            </el-col>
            <!--搜索表单-->
            <el-col :span="7" :offset="14">
                <el-form :inline="true" size="small" style="height: 30px" :model="searchTitle">
                    <el-form-item>
                        <el-input v-model="searchTitle.title" placeholder="请输入标题">
                            <el-button slot="prepend" icon="el-icon-search" @click="searchSection(searchTitle.title)"></el-button>
                            <el-button slot="append" icon="el-icon-refresh" @click="searchSection('')"></el-button>
                        </el-input>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
        <!-- 分割线 -->
        <el-divider></el-divider>
        <!-- 目录 -->
        <el-row>
            <el-col :span="22" :offset="1">
                <!-- 标题 -->
                <el-row>
                    <el-col :span="1">
                        <div class="catalogue">目录</div>
                    </el-col>
                    <el-col :span="1" style="padding-left: 10px">
                        <i class="allCollapseCon el-icon-circle-plus-outline" @click="openAll"></i>
                        <i class="allCollapseCon el-icon-remove-outline" @click="closeAll"></i>
                    </el-col>
                </el-row>
                <!-- 折叠面板 -->
                <el-collapse v-model="collapseArray">
                    <el-collapse-item v-for="(section,index) in sections" :name="index" :key="index">
                        <!-- 标题 -->
                        <template slot="title" style="position:relative;">
                            <!-- 章节序号 -->
                            <span class="secIndex">{{index+1}}</span>
                            <!-- 章节标题 -->
                            <span class="secTitle">{{section.title}}</span>
                        </template>
                        <!-- 折叠体 -->
                        <div class="experiment"
                             v-for="(experiment,exIndex) in section.experiments"
                             @click="enterExperiment(experiment)"
                             :key="experiment.experimentId">
                            <!-- 垂直连接线 -->
                            <div v-if="exIndex === 0" class="line-ver" style="height: 60px;top:-10px"></div>
                            <div v-else class="line-ver" :style="exIndex + 1 !== section.experiments.length ?'height:50px':'height:20px'"></div>
                            <!-- 水平连接线 -->
                            <div class="line-hov"></div>
                            <!-- -->
                            <!-- 实验序号 -->
                            <span class="expIndex">{{index+1}}.{{exIndex+1}}</span>
                            <!-- 实验标题 -->
                            <span class="expTitle">{{experiment.title}}</span>
                        </div>
                    </el-collapse-item>
                </el-collapse>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "StuSectionPage",
        data(){
            return{
                // 搜索表单数据模型
                searchTitle:{
                    title:'',
                },
                // 章节数据
                sections:[],
                // 激活的折叠面板
                collapseArray:[],
            }
        },
        created() {
            // 初始化章节列表
            this.seaSectionOnSubmit();
        },
        methods:{
            // 查找章节列表
            seaSectionOnSubmit(){
                // 发送post请求创建新章节
                this.$axios.post("/ExperimentCon/seaSectionOnSubmit",this.searchTitle).then((res)=>{
                    if(res.data.flag){
                        this.sections = res.data.data;
                        // 展开所有章节
                        this.openAll();
                    }else{
                        this.$message.error(res.data.msg);
                    }
                })
            },
            searchSection(title){
                this.searchTitle.title = title;
                this.seaSectionOnSubmit();
                this.searchTitle.title = "";
            },
            // 展开全部章节
            openAll(){
                this.closeAll();
                for(let i = 0; i < this.sections.length; i += 1)
                    this.$set(this.collapseArray,i,i);
            },
            // 折叠全部章节
            closeAll(){
                this.collapseArray = [];
            },
            // 跳转到实验空间
            enterExperiment(experiment){
                /* 加载动画 */
                let loading = this.$loading({
                    lock: true,
                    fullscreen:true,
                    text:'实验环境加载中..'
                });
                /* 定时1秒 */
                setTimeout(() => {loading.close();}, 1000);
                // 将experimentId存入session
                this.$axios.post("/ExperimentCon/setExperimentIdToSession",experiment).then((res)=>{
                    if(res.data.flag){
                        // 跳转到实验环境
                        this.$router.push({name:"StuCodingPage"});
                    }else{
                        this.$message.error(res.data.msg);
                    }
                })
            },
        }
    }
</script>

<style scoped>
    /* 分割线 */
    /deep/.el-divider--horizontal {
        margin: 12px 0;
    }
    /* 折叠面板标题栏样式-重写父样式 */
    /deep/.el-collapse-item__header {
        background-color: #F5F7FA;
        color: #303133;
        border-bottom: 8px solid #ffffff;
        padding: 0 20px 0 20px;
        position: relative;
        z-index: 0;
    }
    /deep/.el-collapse-item__header.is-active {
        border-bottom-color: #ffffff;
    }
    /* 目录 */
    .catalogue{
        font-size: 13px;
        color: #A8A8B3;
        margin: 10px 0 10px 20px;
    }
    /* 展开控制器 */
    .allCollapseCon{
        font-size: 13px;
        color: #A8A8B3;
        margin: 12px 2px 12px 2px;
    }
    .allCollapseCon:hover{
        cursor:pointer;
        color: #029ef3;
    }
    /* 章节序号 */
    .secIndex{
        /* 框样式 */
        margin-right: 10px;
        border-radius: 0.5em;
        /* 框宽度 */
        width: 2em;
        /* 框高度 */
        line-height: 2em;
        /* 背景色 */
        background-color: white;
        color: #909093;
        /* 字体 */
        font-size: 10px;
        font-weight: 600;
        /* 文字居中 */
        text-align: center;
    }
    /* 章节标题 */
    .secTitle{
        font-size: 13px;
        font-weight: 600;
    }
    /* 实验行 */
    .experiment{
        position: relative;
        /* 框高度 */
        line-height: 40px;
        padding: 4px 38px;
        font-size: 12px;
    }
    .experiment:hover{
        cursor:pointer;
        /* 背景和文字色*/
        background-color: #eaf4ff;
        color: #029ef3;
    }
    /* 垂直连接线 */
    .line-ver{
        width: 1px;
        border-left: 1px dashed #9edcff;
        position: absolute;
        left: 30px;
        z-index: 2;
    }
    /* 水平连接线 */
    .line-hov{
        width: 10px;
        height: 1px;
        border-top: 1px dashed #9edcff;
        position: absolute;
        left: 30px;
        top: 25px;
        z-index: 2;
    }
    /* 实验序号 */
    .expIndex{
        display: inline-block;
        /* 框样式 */
        margin-right: 15px;
        border-radius: 0.5em;
        /* 框宽度 */
        width: 3.6em;
        /* 框高度 */
        line-height: 2.5em;
        /* 背景色 */
        background-color: #9edcff;
        color: #ffffff;
        /* 字体 */
        font-size: 8px;
        font-weight: 600;
        /* 文字居中 */
        text-align: center;
        /* 定位-避免被虚线覆盖*/
        position: relative;
        z-index: 2;
    }
    /* 实验标题 */
    .expTitle{

    }
</style>