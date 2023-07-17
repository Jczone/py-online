<template>
    <div>
        <!-- 工具栏 -->
        <el-row>
            <!-- 返回签 -->
            <el-col :span="1" :offset="1">
                <span id="returnIcon" @click="$router.go(-1)"><i class="el-icon-back">返回</i></span>
            </el-col>
            <!-- 编辑/删除 图标 -->
            <el-col :span="5" :offset="1">
                <el-button-group>
                    <el-button type="primary" round plain size="small" icon="el-icon-circle-plus-outline" @click="openCreateExperiment">新建实验</el-button>
                    <!-- 编辑图标 -->
                    <el-button type="primary" plain size="small" icon="el-icon-edit" @click="updateSection">编辑</el-button>
                    <!-- 删除图标 -->
                    <el-button type="primary" round plain size="small" icon="el-icon-delete" @click="deleteSection">删除</el-button>
                </el-button-group>
            </el-col>
            <!-- 提示文本 -->
            <el-col :span="1">
                <span id="pageTitle">当前章节</span>
            </el-col>
            <!-- 章节选择器 -->
            <el-col :span="4">
                <el-select size="small"
                           style="margin-left: 5px"
                           v-model="nowSection.index"
                           placeholder="请选择要编辑的章节">
                    <el-option v-for="(item,index) in sections"
                               :key="index"
                               :label="item.title"
                               :value="index">
                    </el-option>
                </el-select>
            </el-col>
        </el-row>
        <!-- 分割线 -->
        <el-divider></el-divider>
        <!-- 实验 -->
        <colorful-card :data="sections[nowSection.index].experiments"
                       title="title"
                       date="shortDate"
                       :show-detail1="true"
                       detail1-title="分值："
                       detail1="score"
                       @cardClick="editExperiment"
                       :show-del="true"
                       @deleteClick="deleteExperiment">
        </colorful-card>
        <!-- 发布实验表单 -->
        <el-dialog
                v-if="closeDialog"
                title="新建实验"
                :visible.sync="createExpDialogVisible"
                @close="closeDialog = false"
                width="50%">
            <!-- 实验标题 -->
            <el-row>
                <el-col :span="3">
                    <div class="noticeInputTitle">实验标题</div>
                </el-col>
                <!-- 标题输入 -->
                <el-col :span="14">
                    <el-input v-model="editExpModel.title" maxlength="30" show-word-limit></el-input>
                </el-col>
                <el-col :span="2" :offset="3">
                    <div class="noticeInputTitle">分值</div>
                </el-col>
                <!-- 分值输入 -->
                <el-col :span="2">
                    <el-input v-model="editExpModel.score" placeholder="分"></el-input>
                </el-col>
            </el-row>
            <!-- 实验内容 -->
            <div class="noticeInputTitle">实验内容</div>
            <Ckeditor4 v-model="editExpModel.description"></Ckeditor4>
            <!-- 预期结果 -->
            <div class="noticeInputTitle">预期结果</div>
            <el-input type="textarea" v-model="editExpModel.result" maxlength="2000" show-word-limit></el-input>
            <!-- 提交按钮 -->
            <el-button type="primary" round plain size="small" class="submitBtn" @click="createExperiment(editExpModel)">发布实验</el-button>
        </el-dialog>
        <!-- 编辑实验表单 -->
        <el-dialog
                v-if="closeDialog"
                title="编辑实验"
                :visible.sync="editExpDialogVisible"
                @close="closeDialog = false"
                width="50%">
            <!-- 实验标题 -->
            <el-row>
                <el-col :span="3">
                    <div class="noticeInputTitle">实验标题</div>
                </el-col>
                <!-- 标题输入 -->
                <el-col :span="14">
                    <el-input v-model="editExpModel.title" maxlength="30" show-word-limit></el-input>
                </el-col>
                <el-col :span="2" :offset="3">
                    <div class="noticeInputTitle">分值</div>
                </el-col>
                <!-- 分值输入 -->
                <el-col :span="2">
                    <el-input v-model="editExpModel.score" placeholder="分"></el-input>
                </el-col>
            </el-row>
            <!-- 实验内容 -->
            <div class="noticeInputTitle">实验内容</div>
            <Ckeditor4 v-model="editExpModel.description"></Ckeditor4>
            <!-- 预期结果 -->
            <div class="noticeInputTitle">预期结果</div>
            <el-input type="textarea" v-model="editExpModel.result" maxlength="2000" show-word-limit></el-input>
            <!-- 提交按钮 -->
            <el-button type="primary" round plain size="small" class="submitBtn" @click="updateExperiment(editExpModel)">保存编辑</el-button>
        </el-dialog>
    </div>
</template>

<script>
    import Ckeditor4 from "@/components/Ckeditor4";
    import ColorfulCard from "@/components/ColorfulCard";
    export default {
        name: "TeaConPane",
        components:{
            Ckeditor4,
            ColorfulCard
        },
        data(){
            return{
                // 章节列表数据
                sections:[],
                // 当前操作的章节
                nowSection:{
                    // 当前操作的章节在列表中的位置
                    index:0,
                    // 章节数据
                    sectionId:'',
                    title:'',
                },
                // 编辑实验数据模型
                editExpModel:{
                    experimentId:'',
                    title:'',
                    description:'',
                    result:'',
                    score:0,
                },
                // 编辑实验对话框开关
                editExpDialogVisible:false,
                // 发布实验对话框开关
                createExpDialogVisible:false,
                // 对话框销毁器
                closeDialog:false,
            }
        },
        mounted() {
            // 初始化章节列表
            this.seaSectionOnSubmit('');
        },
        methods:{
            // 查找章节列表
            seaSectionOnSubmit(title){
                // 发送post请求创建新章节
                this.$axios.post("/ExperimentCon/seaSectionOnSubmit",{title:title}).then((res)=>{
                    if(res.data.flag){
                        this.sections = res.data.data;
                        this.nowSection.index = 0;
                    }else{
                        this.$message.error(res.data.msg);
                    }
                })
            },
            // 更新当前章节
            updateSection(){
                // 初始化当前操作的章节数据
                this.nowSection.sectionId = this.sections[this.nowSection.index].sectionId;
                this.nowSection.title = this.sections[this.nowSection.index].title;
                // 弹窗获取章节标题
                this.$prompt('新章节标题', '修改章节标题', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputPattern:/^.{1,20}$/,
                    inputValue:this.nowSection.title,
                    inputErrorMessage:'标题不能为空或大于20个字符'
                }).then(({ value }) => {
                    this.nowSection.title = value;
                    // 发送post请求创建新章节
                    this.$axios.post("/ExperimentCon/updateSection",this.nowSection).then((res)=>{
                        if(res.data.flag){
                            this.$message.success(res.data.msg);
                            // 无条件刷新章节列表
                            this.seaSectionOnSubmit('');
                        }else{
                            this.$message.error(res.data.msg);
                        }
                    })
                });
            },
            // 删除当前章节
            deleteSection(){
                this.$confirm('删除该章节及所含实验?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    // 初始化当前操作的章节数据
                    this.nowSection.sectionId = this.sections[this.nowSection.index].sectionId;
                    // 发送post请求创建新章节
                    this.$axios.post("/ExperimentCon/deleteSection",this.nowSection).then((res)=>{
                        if(res.data.flag){
                            this.$message.success(res.data.msg);
                            // 无条件刷新章节列表
                            this.seaSectionOnSubmit('');
                            this.nowSection.index = 0;
                        }else{
                            this.$message.error(res.data.msg);
                        }
                    })
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
            // 打开实验发布编辑栏
            openCreateExperiment(){
                this.editExpModel.title = '';
                this.editExpModel.description = '';
                this.editExpModel.score = 0;
                this.editExpModel.result = '';
                this.closeDialog = true;
                this.createExpDialogVisible = true;
            },
            // 发布实验
            createExperiment(experiment){
                if(experiment.title.length === 0 || experiment.description.length === 0){
                    this.$message.warning("请填写完整再提交");
                    return;
                }else if(experiment.description.length > 5000){
                    this.$message.warning("内容超出字数限制（5000）");
                    return;
                }
                experiment.sectionId = this.sections[this.nowSection.index].sectionId;
                // 发送post请求更新实验数据
                this.$axios.post("/ExperimentCon/createExperiment",experiment).then((res)=>{
                    if(res.data.flag){
                        this.$message.success(res.data.msg);
                        // 关闭弹窗
                        this.createExpDialogVisible = false;
                        this.closeDialog = false;
                        // 无条件刷新章节列表
                        this.seaSectionOnSubmit('');
                    }else{
                        this.$message.error(res.data.msg);
                    }
                })
            },
            // 打开实验数据编辑栏
            editExperiment(experiment){
                this.editExpModel.experimentId = experiment.experimentId;
                this.editExpModel.title = experiment.title;
                this.editExpModel.description = experiment.description;
                this.editExpModel.score = experiment.score;
                this.editExpModel.result = experiment.result;
                this.closeDialog = true;
                this.editExpDialogVisible = true;
            },
            // 更新实验数据
            updateExperiment(experiment){
                if(experiment.title.length === 0 || experiment.description.length === 0){
                    this.$message.warning("请填写完整再提交");
                    return;
                }else if(experiment.description.length > 5000){
                    this.$message.warning("内容超出字数限制（5000）");
                    return;
                }
                // 发送post请求更新实验数据
                this.$axios.post("/ExperimentCon/updateExperiment",experiment).then((res)=>{
                    if(res.data.flag){
                        this.$message.success(res.data.msg);
                        // 关闭弹窗
                        this.editExpDialogVisible = false;
                        this.closeDialog = false;
                        // 无条件刷新章节列表
                        this.seaSectionOnSubmit('');
                    }else{
                        this.$message.error(res.data.msg);
                    }
                })
            },
            // 删除实验数据
            deleteExperiment(experiment){
                this.$confirm('删除该实验?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    // 发送post请求更新实验数据
                    this.$axios.post("/ExperimentCon/deleteExperiment",experiment).then((res)=>{
                        if(res.data.flag){
                            this.$message.success(res.data.msg);
                            // 无条件刷新章节列表
                            this.seaSectionOnSubmit('');
                        }else{
                            this.$message.error(res.data.msg);
                        }
                    })
                });
            }
        }
    }
</script>

<style scoped>
    /* 分割线 */
    /deep/.el-divider--horizontal {
        margin: 12px 0;
    }
    /* 页面返回签 */
    #returnIcon{
        font-size: 13px;
        line-height: 32px;
    }
    #returnIcon:hover{
        color: #029ef3;
        cursor:pointer;
    }
    /* 页面标题 */
    #pageTitle{
        line-height: 32px;
        font-size: 13px;
        color: grey;
    }
    /* 章节编辑/删除图标 */
    .sectionEdit, .sectionDelete{
        line-height: 32px;
        font-size: 13px;
        color: grey;
        margin: 0 4px;
    }
    .sectionEdit:hover{
        color: #029ef3;
        cursor:pointer;
    }
    .sectionDelete:hover{
        color: #f30202;
        cursor:pointer;
    }
    /* 表头字体 */
    /deep/.el-table th.el-table__cell>.cell {
        font-size: 14px;
    }
    /* 表格文字大小 */
    .el-table{
        font-size: 14px;
    }
    /* 表格行高控制 */
    ::v-deep .el-table .el-table__cell{
        padding: 4px 0;
    }
    /* 删除实验按钮 */
    .delExperimentBtn{
        color: #f36602;
        margin-left: 5px;
    }
    .delExperimentBtn:hover{
        color: #ff9243;
        cursor: pointer;
    }
    /* Dialog标题文字 */
    /deep/.el-dialog__title {
        font-size: 15px;
    }
    /* Dialog内边框 */
    /deep/.el-dialog__body {
        padding: 0 20px;
    }
    /* 输入框字体大小 */
    /deep/.el-input__inner{
        font-size: 13px;
    }
    /* 通知输入框标题 */
    .noticeInputTitle{
        margin: 10px;
        font-size: 14px;
    }
    /* Dialog提交按钮 */
    .submitBtn{
        margin: 10px 0;
        font-size: 13px;
        position: relative;
        left: 85%;
    }
</style>