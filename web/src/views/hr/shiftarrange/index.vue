<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="班次日期" prop="shiftdate">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.shiftdate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择班次日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="班次类别" prop="shifttype">
        <el-select v-model="queryParams.shifttype" placeholder="请选择班次类别" clearable size="small">
          <el-option
            v-for="dict in shifttypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <!--<el-form-item label="排班id" prop="arrangeid">
        <el-input
          v-model="queryParams.arrangeid"
          placeholder="请输入排班id"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="排班编号" prop="arrangeno">
        <el-input
          v-model="queryParams.arrangeno"
          placeholder="请输入排班编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
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

    <!--<el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['hr:shiftarrange:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['hr:shiftarrange:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['hr:shiftarrange:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['hr:shiftarrange:export']"
        >导出</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>-->

    <el-table v-loading="loading" :data="shiftarrangeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="班次日期" align="center" prop="shiftdate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.shiftdate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="班次类别" align="center" prop="shifttype" :formatter="shifttypeFormat" />
      <el-table-column label="排班id" align="center" prop="arrangeid" />
      <el-table-column label="排班编号" align="center" prop="arrangeno" />
      <el-table-column label="排班状态" align="center" prop="status" :formatter="hrCommonStatusFormat " />
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
          <!--<el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['hr:shiftarrange:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['hr:shiftarrange:remove']"
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

    <!-- 添加或修改班次排班对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="班次日期" prop="shiftdate">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.shiftdate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择班次日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="班次类别" prop="shifttype">
          <el-select v-model="form.shifttype" placeholder="请选择班次类别">
            <el-option
              v-for="dict in shifttypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排班id" prop="arrangeid">
          <el-input v-model="form.arrangeid" placeholder="请输入排班id" />
        </el-form-item>
        <el-form-item label="排班编号" prop="arrangeno">
          <el-input v-model="form.arrangeno" placeholder="请输入排班编号" />
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
  </div>
</template>

<script>
import { listShiftarrange, getShiftarrange, delShiftarrange, addShiftarrange, updateShiftarrange, exportShiftarrange } from "@/api/hr/shiftarrange";

export default {
  name: "Shiftarrange",
  data() {
    return {
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
      // 班次排班表格数据
      shiftarrangeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 班次类别字典
      shifttypeOptions: [],
      // 排班状态字典
      hrCommonStatus: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shiftdate: null,
        shifttype: null,
        arrangeid: null,
        arrangeno: null,
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
    this.getDicts("mac_common_banci_type").then(response => {
      this.shifttypeOptions = response.data;
    });
    this.getDicts("hr_common_status").then(response => {
      this.hrCommonStatus = response.data;
    });
  },
  methods: {
    /** 查询班次排班列表 */
    getList() {
      this.loading = true;
      listShiftarrange(this.queryParams).then(response => {
        this.shiftarrangeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 班次类别字典翻译
    shifttypeFormat(row, column) {
      return this.selectDictLabel(this.shifttypeOptions, row.shifttype);
    },
    // 状态字典翻译
    hrCommonStatusFormat(row, column) {
      return this.selectDictLabel(this.hrCommonStatus, row.status);
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
        shiftdate: null,
        shifttype: null,
        arrangeid: null,
        arrangeno: null,
        status: "0",
        createTime: null,
        updateTime: null,
        createBy: null,
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加班次排班";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getShiftarrange(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改班次排班";
      });
    },
    /** 查看排班详情 */
    handleInfo(row) {
      this.reset();
      const id = row.id || this.ids
      getArrange(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "查看排班详情";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateShiftarrange(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addShiftarrange(this.form).then(response => {
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
      this.$confirm('是否确认删除班次排班编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delShiftarrange(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有班次排班数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportShiftarrange(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    }
  }
};
</script>
