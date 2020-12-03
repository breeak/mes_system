<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="排班号" prop="arrangeno">
        <el-input
          v-model="queryParams.arrangeno"
          placeholder="请输入排班号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!--<el-form-item label="排班起始时间" prop="arrangestart">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.arrangestart"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择排班起始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="排班结束时间" prop="arrangeend">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.arrangeend"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择排班结束时间">
        </el-date-picker>
      </el-form-item>-->
      <el-form-item label="排班类别" prop="workergroup">
        <el-select v-model="queryParams.arrangeclass" placeholder="请选择排班类别" clearable size="small">
          <el-option
            v-for="dict in workergroupOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="排班状态" prop="workergroup">
        <el-select v-model="queryParams.status" placeholder="请选择排班状态" clearable size="small">
          <el-option
            v-for="dict in hrCommonStatus"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

   <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['hr:arrange:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['hr:arrange:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['hr:arrange:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['hr:arrange:export']"
        >导出</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>-->

    <el-table v-loading="loading" :data="arrangeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="排班号" align="center" width="180" prop="arrangeno" />
      <el-table-column label="排班起始时间" align="center" prop="arrangestart" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.arrangestart, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="排班结束时间" align="center" prop="arrangeend" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.arrangeend, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <!--<el-table-column label="排班内容" align="center" prop="macworkerids" />-->
      <el-table-column label="排班类别" align="center" prop="arrangeclass" :formatter="workergroupFormat" />
      <el-table-column label="排班状态" align="center" prop="status" :formatter="hrCommonStatusFormat "/>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleInfo(scope.row)"
            v-hasPermi="['hr:arrange:edit']"
          >查看详情</el-button>
         <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['hr:arrange:edit']"
          >修改</el-button>-->
          <!--<el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['hr:arrange:remove']"
          >删除</el-button>-->
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改排班信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="排班号" prop="arrangeno">
          <el-input v-model="form.arrangeno" placeholder="请输入排班号" />
        </el-form-item>
        <el-form-item label="排班起始时间" prop="arrangestart">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.arrangestart"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择排班起始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="排班结束时间" prop="arrangeend">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.arrangeend"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择排班结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="排班内容" prop="macworkerids">
          <el-input v-model="form.macworkerids" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="排班类别" prop="arrangeclass">
          <el-input v-model="form.arrangeclass" placeholder="请输入排班类别"  />
        </el-form-item>
        <el-form-item label="排班状态">
          <el-radio-group v-model="form.status">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 排班详情 -->
    <el-dialog :title="title" :visible.sync="openArea" width="70%" append-to-body>
      <el-form :inline="true" ref="arrangeForm" :model="form" :rules="rulesArrange" label-width="80px">
        <el-form-item label="员工角色" >
          <el-input size=mini v-model="form.workerrole" :disabled="true" />
        </el-form-item>
        <el-button type="primary" @click="submitArrangeForm">搜 索</el-button>
        <el-form-item  label="开始日期" prop="arrangestart">
          <el-date-picker  clearable
                           v-model="form.arrangestart"
                           type="datetime"
                           format = "yyyy-MM-dd HH:mm:ss"
                           value-format="yyyy-MM-dd HH:mm:ss"
                           placeholder="选择开始日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item  label="结束日期" prop="arrangeend">
          <el-date-picker clearable
                          v-model="form.arrangeend"
                          type="datetime"
                          format = "yyyy-MM-dd HH:mm:ss"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="选择结束日期">
          </el-date-picker>
        </el-form-item>
        <el-radio-group v-model="showclass" size="small">
          <el-radio-button value="A" label="A">A组</el-radio-button>
          <el-radio-button value="B" label="B">B组</el-radio-button>
          <el-radio-button value="C" label="C">C组</el-radio-button>
          <el-radio-button value="D" label="其他">其他</el-radio-button>
        </el-radio-group>

        <!--TODO 员工划片 详情等 这些功能都是待完善的 先完成基础的-->
        <!--<el-button type="primary" @click="detailWindow">查看详情</el-button>
        <el-button type="primary" @click="searchWindow">搜索员工</el-button>
        <el-button type="primary" @click="lastWorker">上一员工</el-button>
        <el-button type="primary" @click="nextWorker">下一员工</el-button>-->
      </el-form>


      <el-row>
        <machine v-on:update="receiveData"  :machineList="machineList" :allRow="allRow" :allColumn="allColumn"
                 :macworkerMap="macworkerMap" :oldworker="form.workerid" :newworker="form.otherworkerid"/>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitArrangeForm">搜 索</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>



  </div>
</template>

<script>
import { listArrange, getArrange, delArrange, addArrange, updateArrange, exportArrange } from "@/api/hr/arrange";

export default {
  name: "Arrange",
  data() {
    return {
      machineList:[],
      macworkerMap:Object,
      macworkerList:[],
      arrange:Object,
      showclass:"A",
      allRow:0,
      allColumn:0,

      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 排班信息表格数据
      arrangeList: [],
      // 排班类别字典
      workergroupOptions: [],
      // 排班状态字典
      hrCommonStatus: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,

      // 是否显示弹出层划区
      openArea: false,

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        arrangeno: null,
        arrangestart: null,
        arrangeend: null,
        macworkerids: null,
        arrangeclass: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("hr_worker_group").then(response => {
      this.workergroupOptions = response.data;
    });
    this.getDicts("hr_common_status").then(response => {
      this.hrCommonStatus = response.data;
    });
  },
  methods: {
    /** 查询排班信息列表 */
    getList() {
      this.loading = true;
      listArrange(this.queryParams).then(response => {
        this.arrangeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        arrangeno: null,
        arrangestart: null,
        arrangeend: null,
        macworkerids: null,
        arrangeclass: null,
        status: "0",
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    // 员工组别字典翻译
    workergroupFormat(row, column) {
      return this.selectDictLabel(this.workergroupOptions, row.workergroup);
    },
    // 状态字典翻译
    hrCommonStatusFormat(row, column) {
      return this.selectDictLabel(this.hrCommonStatus, row.status);
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加排班信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getArrange(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改排班信息";
      });
    },
    /** 查看排班详情 */
    handleInfo(row) {
      this.reset();
      const id = row.id || this.ids
      getArrange(id).then(response => {
        this.form = response.data;
        this.openArea = true;
        this.title = "查看排班详情";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateArrange(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addArrange(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              }
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除排班信息编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delArrange(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有排班信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportArrange(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    }
  }
};
</script>
