<template>
    <div>
        <!-- 功能菜单 -->
        <el-row style="margin-bottom: 12px">
            <!-- 返回签 -->
            <el-col :span="1" :offset="1">
                <span id="returnIcon" @click="$router.go(-1)"><i class="el-icon-back">返回</i></span>
            </el-col>
            <!-- 标题 -->
            <el-col :span="2" :offset="1">
                <span style="font-size: 22px; line-height: 30px">提交记录</span>
            </el-col>
            <!--搜索表单-->
            <el-col :span="7" :offset="12">
                <el-form :inline="true" size="small" style="height: 30px" :model="searchTitle">
                    <el-form-item>
                        <el-input v-model="searchTitle.title" placeholder="请输入实验标题">
                            <el-button slot="prepend" icon="el-icon-search" @click="searchReportOnSubmit(searchTitle.title)"></el-button>
                            <el-button slot="append" icon="el-icon-refresh" @click="searchReportOnSubmit('')"></el-button>
                        </el-input>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>

        <!-- 分割线 -->
        <el-divider></el-divider>

        <!--表格-->
        <el-row>
            <el-col :span="22" :offset="1">
                <template>
                    <el-table
                            ref="reportMultipleTable"
                            :data="reportTableData"
                            style="width: 100%"
                            border
                            :row-class-name="tableRowClassName">
                        <el-table-column
                                label="序号"
                                type="index"
                                align="center"
                                width="100">
                        </el-table-column>
                        <el-table-column
                                label="实验标题"
                                header-align="center"
                                sortable
                                width="300">
                            <template slot-scope="scope">
                                <div style="padding:0 40px">{{scope.row.title}}</div>
                            </template>
                        </el-table-column>
                        <el-table-column
                                label="提交者"
                                align="center"
                                width="200">
                            <template slot-scope="scope">
                                <user-avatar :src="scope.row.avatarUrl" width="30" height="30"></user-avatar>
                                <span style="position:relative;bottom: 10px">{{scope.row.username}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column
                                label="实验程序"
                                align="center"
                                width="100">
                            <template slot-scope="scope">
                                <i class="downBtn el-icon-download" @click="downloadFile(scope.row.codeFile)">下载</i>
                            </template>
                        </el-table-column>
                        <el-table-column
                                label="实验参数"
                                align="center"
                                width="100">
                            <template slot-scope="scope">
                                <i class="downBtn el-icon-download" @click="downloadFile(scope.row.paramsFile)">下载</i>
                            </template>
                        </el-table-column>
                        <el-table-column
                                label="实验结果"
                                align="center"
                                width="100">
                            <template slot-scope="scope">
                                <i class="downBtn el-icon-download" @click="downloadFile(scope.row.resultFile)">下载</i>
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="score"
                                label="得分"
                                align="center"
                                width="100"
                                sortable>
                        </el-table-column>
                        <el-table-column
                                prop="formatDate"
                                label="提交日期"
                                align="center"
                                sortable>
                        </el-table-column>

                    </el-table>
                </template>
                <!-- 分页工具条 数据小于10条不展示 -->
                <el-pagination
                        id="pageBar"
                        v-show="reportTotalCount >= 10"
                        @size-change="reportHandleSizeChange"
                        @current-change="reportHandleCurrentChange"
                        :current-page="reportCurrentPage"
                        :page-sizes="[5, 10, 15, 20, 50, 100]"
                        :page-size="reportPageSize"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="reportTotalCount">
                </el-pagination>
            </el-col>
        </el-row>

    </div>
</template>

<script>
    import UserAvatar from "@/components/UserAvatar";
    export default {
        name: 'TeaReportPage',
        components:{
            UserAvatar,
        },
        data(){
            return{
                // 当前页码
                reportCurrentPage: 1,
                // 分页大小
                reportPageSize: 10,
                // 总页码数
                reportTotalCount: 1,
                // 搜索表单数据模型
                searchTitle:{
                    title:'',
                },
                // 内置查询模型
                searchReportByTitle:{
                    title:'',
                },
                // 通知数据列表
                reportTableData:[],
            }
        },
        created() {
            this.getReportByTitle();
        },
        methods:{
            // 查找报告
            // 系统内置一个title，默认为空，即全查询。每次条件查询都会把条件赋给该title。
            // 因此这样点击分页栏时可以正常显示查询结果。
            getReportByTitle(){
                this.searchReportByTitle["pageSize"]=this.reportPageSize;
                this.searchReportByTitle["currentPage"]=this.reportCurrentPage;
                this.$axios.post("/ReportCon/getTeaReportByTitle",this.searchReportByTitle).then((res)=>{
                    if(res.data.flag){
                        /* 保存实验报告数据 */
                        this.reportTableData = res.data.data.records;
                        /* 保存分页数据 */
                        this.reportTotalCount = res.data.data.total;
                        this.reportCurrentPage = res.data.data.current;
                        this.reportPageSize = res.data.data.size;
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
            // 查询报告
            searchReportOnSubmit(title){
                // 刷新查询条件
                this.searchReportByTitle.title = title;
                // 执行查询
                this.getReportByTitle();
                /* 清空查询条件 */
                this.searchTitle.title='';
            },
            // 文件下载 相对路径/二进制流
            downloadFile(fileName){
                let url = "http://" + window.location.host + "/Files/reportDownload";
                const xhr = new XMLHttpRequest();
                xhr.open('post', url);
                xhr.responseType = 'blob';
                //3.5 请求头（POST请求时设置）
                xhr.setRequestHeader('Content-Type','application/json')
                // xhr.setRequestHeader("Content-type","application/json;charset=UTF-8");
                xhr.send(JSON.stringify({fileName:fileName}));
                xhr.onload = function () {
                    if (this.status === 200 || this.status === 304) {
                        const fileReader = new FileReader();
                        fileReader.readAsDataURL(this.response);
                        fileReader.onload = function () {
                            const a = document.createElement('a');
                            a.style.display = 'none';
                            a.href = this.result;
                            a.download = fileName;
                            document.body.appendChild(a);
                            a.click();
                            document.body.removeChild(a);
                        };
                    }
                };
                // 解决多弹窗重叠问题
                setTimeout(()=>{this.$message.success("下载:"+fileName+" 完成！")},1000);
            },
            // 分页大小变化
            reportHandleSizeChange(val) {
                this.reportPageSize = val;
                // 查询所有通知
                this.getReportByTitle();
            },
            // 当前页变化
            reportHandleCurrentChange(val) {
                this.reportCurrentPage = val;
                // 查询所有实验报告
                this.getReportByTitle();
            },
        }
    }
</script>

<style scoped>

    /* 页面返回签 */
    #returnIcon{
        font-size: 13px;
        line-height: 32px;
    }
    #returnIcon:hover{
        color: #029ef3;
        cursor:pointer;
    }
    /* 分割线 */
    /deep/.el-divider--horizontal {
        margin: 12px 0;
    }
    /* 表头字体 */
    /deep/.el-table th.el-table__cell>.cell {
        font-size: 14px;
    }
    /* 表格文字大小 */
    .el-table{
        font-size: 14px;
    }
    /* 下载按钮 */
    .downBtn{
        color: #21aeff;
    }
    .downBtn:hover{
        cursor:pointer;
        color: #029ef3;
    }
    /* 分页工具条上框 */
    #pageBar{
        margin-top: 20px;
    }
</style>