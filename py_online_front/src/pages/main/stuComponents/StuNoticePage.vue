<template>
    <div>
        <!-- 功能菜单 -->
        <el-row style="margin-bottom: 12px">
            <!-- 标题 -->
            <el-col :span="2" :offset="1">
                <span style="font-size: 22px; line-height: 30px">通知中心</span>
            </el-col>
            <!--搜索表单-->
            <el-col :span="7" :offset="14">
                <el-form :inline="true" size="small" style="height: 30px" :model="searchTitle">
                    <el-form-item>
                        <el-input v-model="searchTitle.title" placeholder="请输入标题">
                            <el-button slot="prepend" icon="el-icon-search" @click="searchNoticeOnSubmit(searchTitle.title)"></el-button>
                            <el-button slot="append" icon="el-icon-refresh" @click="searchNoticeOnSubmit('')"></el-button>
                        </el-input>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>

        <!-- 分割线 -->
        <el-divider></el-divider>

        <!-- 公告详情-抽屉 -->
        <!-- 此处默认不增加点击量 -->
        <notice-drawer v-model="noticeDrawer" :notice="noticeDrawerContent" :addHits="false"></notice-drawer>

        <!--表格-->
        <el-row>
            <el-col :span="22" :offset="1">
                <template>
                    <el-table
                            ref="noticeMultipleTable"
                            :data="noticeTableData"
                            style="width: 100%"
                            border
                            :row-class-name="tableRowClassName"
                            @row-click="noticeRowClick">
                        <el-table-column
                                class-name="tablePointClass"
                                label="序号"
                                type="index"
                                align="center"
                                width="100">
                        </el-table-column>

                        <el-table-column
                                class-name="tablePointClass"
                                label="标题"
                                header-align="center"
                                sortable
                                width="400">
                            <template slot-scope="scope">
                                <div style="padding:0 40px">{{scope.row.title}}</div>
                            </template>
                        </el-table-column>
                        <el-table-column
                                class-name="tablePointClass"
                                prop="hits"
                                label="点击量"
                                align="center"
                                width="200"
                                sortable>
                        </el-table-column>
                        <el-table-column
                                class-name="tablePointClass"
                                prop="formatDate"
                                label="创建日期"
                                align="center"
                                sortable>
                        </el-table-column>

                    </el-table>
                </template>
                <!-- 分页工具条 数据小于10条不展示 -->
                <el-pagination
                        id="pageBar"
                        v-show="noticeTotalCount >= 10"
                        @size-change="noticeHandleSizeChange"
                        @current-change="noticeHandleCurrentChange"
                        :current-page="noticeCurrentPage"
                        :page-sizes="[5, 10, 15, 20, 50, 100]"
                        :page-size="noticePageSize"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="noticeTotalCount">
                </el-pagination>
            </el-col>
        </el-row>

    </div>
</template>

<script>
    import NoticeDrawer from "@/components/NoticeDrawer";
    export default {
        name: 'StuNoticePage',
        components:{
            NoticeDrawer,
        },
        data(){
            return{
                // 当前页码
                noticeCurrentPage: 1,
                // 分页大小
                noticePageSize: 10,
                // 总页码数
                noticeTotalCount: 1,
                // 搜索表单数据模型
                searchTitle:{
                    title:'',
                },
                // 内置查询模型
                searchNoticeByTitle:{
                    title:'',
                },
                // 通知数据列表
                noticeTableData:[],
                // 公告抽屉
                noticeDrawer:false,
                noticeDrawerContent:'',     // 内容
            }
        },
        created() {
            this.getNoticeByTitle();
        },
        methods:{
            // 查找通知
            // 系统内置一个title，默认为空，即全查询。每次条件查询都会把条件赋给该title。
            // 因此这样点击分页栏时可以正常显示查询结果。
            getNoticeByTitle(){
                this.searchNoticeByTitle["pageSize"]=this.noticePageSize;
                this.searchNoticeByTitle["currentPage"]=this.noticeCurrentPage;
                this.$axios.post("/Notice/getNoticeByTitle",this.searchNoticeByTitle).then((res)=>{
                    if(res.data.flag){
                        /* 保存公告数据 */
                        this.noticeTableData = res.data.data.records;
                        /* 保存分页数据 */
                        this.noticeTotalCount = res.data.data.total;
                        this.noticeCurrentPage = res.data.data.current;
                        this.noticePageSize = res.data.data.size;
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
            // 查询通知
            searchNoticeOnSubmit(title){
                // 刷新查询条件
                this.searchNoticeByTitle.title = title;
                // 执行查询
                this.getNoticeByTitle();
                /* 清空查询条件 */
                this.searchTitle.title='';
            },
            // 当某行数据被点击
            noticeRowClick(row){
                this.noticeDrawer = true;
                this.noticeDrawerContent = row;
                // 发送一次请求
                setTimeout(()=>{this.noticeDrawer = false; },500);
            },
            // 分页大小变化
            noticeHandleSizeChange(val) {
                this.noticePageSize = val;
                // 查询所有通知
                this.getNoticeByTitle();
            },
            // 当前页变化
            noticeHandleCurrentChange(val) {
                this.noticeCurrentPage = val;
                // 查询所有通知
                this.getNoticeByTitle();
            },
        }
    }
</script>

<style scoped>

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
    /* 表格列指针 */
    .tablePointClass:hover{
        cursor: pointer;
    }
    /* 分页工具条上框 */
    #pageBar{
        margin-top: 20px;
    }
</style>
