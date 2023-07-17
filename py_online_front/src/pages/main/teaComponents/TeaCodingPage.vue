<template>
    <div>
        <!-- 工具栏 -->
        <el-row style="margin-bottom: 5px">
            <!-- 返回签 -->
            <el-col :span="1" :offset="1">
                <span id="returnIcon" @click="$router.go(-1)"><i class="el-icon-back">返回</i></span>
            </el-col>
            <!-- 运行按钮 -->
            <el-col :span="5" :offset="1">
                <el-button-group>
                    <el-button type="primary" round plain size="small" icon="el-icon-s-promotion" @click="runPython">运行</el-button>
                    <el-button type="primary" plain size="small" icon="el-icon-download" @click="downLoadCode">下载</el-button>
                    <el-button type="primary" round plain size="small" icon="el-icon-delete" @click="curCode=''">清空</el-button>
                </el-button-group>
            </el-col>
            <!-- 参数输入栏 -->
            <el-col :span="8">
                <el-input v-model="pyParams" size="small" clearable placeholder="运行参数（多个参数请用空格间隔）"></el-input>
            </el-col>
            <!-- 实验功能 -->
            <el-col :span="4" :offset="1">
                <!-- 查看实验 -->
                <el-button type="primary" round plain size="small" @click="dialogVisible=true">查看实验</el-button>
                <!-- 提交按钮 -->
                <el-button type="primary" round plain size="small" @click="dialogVisible = true">查看报告</el-button>
            </el-col>
            <!-- 主题选择器 -->
            <el-col :span="2">
                <el-select style="margin-left: 5px" v-model="cmOptions.theme" size="small" placeholder="主题选择">
                    <el-option-group
                            v-for="group in options"
                            :key="group.label"
                            :label="group.label">
                        <el-option
                                v-for="item in group.options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-option-group>
                </el-select>
            </el-col>
        </el-row>
        <!-- 分割线 -->
        <el-divider></el-divider>
        <!-- 代码编辑区 -->
        <el-row style="margin: 10px 0">
            <!-- 代码编辑 -->
            <el-col :span="18">
                <div class="myCodM">
                    <codemirror ref="myCM" v-model="curCode" :options="cmOptions" class="code"></codemirror>
                </div>
            </el-col>
            <!-- 运行窗口 -->
            <el-col :span="6">
                <label for="outArea"></label>
                <textarea readonly id="outArea">{{runRes}}</textarea>
            </el-col>
        </el-row>
        <!-- 实验数据显示弹窗 -->
        <el-dialog
                title="实验内容"
                :visible.sync="dialogVisible"
                width="70%">
            <div id="expTitleContainer">
                <span id="expTitle">{{experiment.title}}</span>
                <el-tag size="medium" id="expScore">{{experiment.score}}分</el-tag>
            </div>
            <div id="expDescription" v-html="experiment.description">{{experiment.description}}</div>
        </el-dialog>
    </div>
</template>

<script>
    // 主题css
    import 'codemirror/theme/monokai.css'
    import 'codemirror/theme/shadowfox.css'
    import 'codemirror/theme/darcula.css'
    import 'codemirror/theme/eclipse.css'
    import 'codemirror/theme/xq-light.css'
    import 'codemirror/theme/solarized.css'
    import 'codemirror/theme/mdn-like.css'
    // 功能css
    import 'codemirror/addon/fold/foldgutter.css'
    import 'codemirror/addon/lint/lint.css'
    import 'codemirror/addon/hint/show-hint.css'
    // 代码补全
    import 'codemirror/mode/python/python.js'
    import 'codemirror/keymap/sublime.js'
    import 'codemirror/addon/hint/show-hint.js'
    // 当前行高亮
    import 'codemirror/addon/selection/active-line.js'
    import 'codemirror/addon/comment/comment.js'
    // 代码折叠
    import 'codemirror/addon/fold/foldcode.js'
    import 'codemirror/addon/fold/foldgutter.js'
    import 'codemirror/addon/fold/brace-fold.js'
    import 'codemirror/addon/fold/indent-fold.js'
    import 'codemirror/addon/fold/comment-fold.js'
    // 括号匹配
    import 'codemirror/addon/edit/closebrackets.js'
    import 'codemirror/addon/edit/matchbrackets.js'
    // 引入文件下载组件-下面有使用，勿删
    import {saveAs} from 'file-saver';
    export default {
        name: "TeaCodingPage",
        data() {
            return {
                // 实验弹窗打开标志
                dialogVisible:false,
                // 实验数据
                experiment:{},
                // python运行参数，以空格间隔
                pyParams:'',
                // 用户代码
                curCode: '# version: Python3\n\n' +
                    '# please edit your code here:\n' +
                    '# code start\n\n' +
                    'print("hello world")' +
                    '\n\n# code end',
                // codemirror配置
                cmOptions: {
                    // 语法模式
                    mode: 'python',
                    // 功能设置
                    autoRefresh: true,
                    autofocus: true,        // 是否在初始化时自动获取焦点
                    autoCloseBrackets: true,// 自动闭合符号
                    matchBrackets: true,    // 括号匹配
                    // 代码设置
                    indentUnit: 4,          // 智能缩进单位为4个空格长度
                    indentWithTabs: true,   // 使用制表符进行智能缩进
                    lineNumbers: true,      // 是否在编辑器左侧显示行号。
                    styleActiveLine: true,  // 显示选中行的样式
                    lineWrapping: true,
                    spellcheck:true,        // 拼写检查
                    // 在行槽中添加行号显示器、折叠器、语法检测器
                    gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter", "CodeMirror-lint-markers"],
                    foldGutter: true, // 启用行槽中的代码折叠
                    keyMap:'sublime',   // 快捷键风格
                    // 以下为可调整功能
                    readOnly:false,         // 编辑器只读
                    theme: "shadowfox",       // 编辑器样式的主题
                },
                // 运行结果
                runRes:'Please run your program:',                  // 运行结果
                options: [{                 // 主题列表
                    label: '夜间主题',
                    options: [{
                        value: 'shadowfox',
                        label: '主题一'
                    },{
                        value: 'monokai',
                        label: '主题二'
                    }, {
                        value: 'darcula',
                        label: '主题三'
                    }]
                }, {
                    label: '日间主题',
                    options: [{
                        value: 'eclipse',
                        label: '主题一'
                    }, {
                        value: 'xq-light',
                        label: '主题二'
                    }, {
                        value: 'solarized',
                        label: '主题三'
                    }, {
                        value: 'mdn-like',
                        label: '主题四'
                    }]
                }],
            }
        },
        created() {
            this.getExperimentInfo();
        },
        methods:{
            // 初始化获取实验数据
            getExperimentInfo(){
                this.$axios.get("/ExperimentCon/getExperimentBySession").then((res)=>{
                    if(res.data.flag){
                        this.experiment = res.data.data;
                        this.dialogVisible = true;
                    }else{
                        this.$message.error(res.data.msg);
                    }
                })
            },
            // 运行
            runPython(){
                let form = {code:this.curCode,params:this.pyParams};
                this.runRes = "程序运行中..";
                this.$axios.post("/ReportCon/runPython",form).then((res)=>{
                    if(res.data.flag){
                        this.runRes = res.data.data;
                        console.log(this.runRes)
                    }else{
                        this.$message.error(res.data.msg);
                    }
                })
            },
            // 下载代码到本地
            downLoadCode(){
                let FileSaver = require('file-saver');
                let blob = new Blob([this.curCode], {type: "text/plain;charset=utf-8"});
                FileSaver.saveAs(blob, "myCode.py");
            },
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
    /* 修改代码区子组件内边距 */
    .el-main{
        padding: 5px;
    }
    /* 垂直代码容器大小 */
    .vrl-area{
        height: 420px;
    }
    /* 运行结果文本域 */
    #outArea{
        font-family: monospace;
        font-size: 12px;
        white-space: pre;
        height: 556px;
        width: 90%;
        margin-left: 10px;
        padding: 0.375rem 0.75rem;
    }
    /* 弹窗内边距 */
    /deep/.el-dialog__body{
        padding: 10px 20px;
    }
    /* 实验标题容器 */
    #expTitleContainer{
        margin: 0 30px;
    }
    /* 实验标题 */
    #expTitle{
        font-size: 16px;
        font-weight: 600;
    }
    /* 实验分值 */
    #expScore{
        margin-left: 10px;
    }
    /* 实验内容 */
    #expDescription{
        min-height: 200px;
        font-size: 13px;
        margin: 10px 30px;
    }
    /* 代码编辑器高度 */
    /deep/.CodeMirror {
        height: 570px;
    }
</style>