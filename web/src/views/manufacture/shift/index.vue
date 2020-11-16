<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="织机编号" prop="maccode">
        <el-input
          v-model="queryParams.maccode"
          placeholder="请输入织机编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="品种编号" prop="pdtcodes">
        <el-input
          v-model="queryParams.pdtcodes"
          placeholder="请输入品种编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="班次类别" prop="shifttype">
        <el-select v-model="queryParams.shifttype" placeholder="请选择班次类别(早中晚)" clearable size="small">
          <el-option
            v-for="dict in shifttypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="班次日期" prop="shiftdate">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.shiftdate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择班次日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="班次时间" >
        <el-date-picker v-model="dateRange" size="small" type="datetimerange" :picker-options="pickerOptions" style="width: 360px"   range-separator="-" start-placeholder="开始时间" end-placeholder="结束时间"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!--<el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['manufacture:shift:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['manufacture:shift:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['manufacture:shift:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-s-check"
          size="mini"
          @click="handleUpdateAny"
          v-hasPermi="['manufacture:shift:add']"
        >更新班次</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['manufacture:shift:export']"
        >导出</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shiftList" @selection-change="handleSelectionChange" @sort-change='sortChange'>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column fixed sortable='custom' width="100" label="织机编号" align="center" prop="maccode" />
      <el-table-column fixed label="班次类别" align="center" prop="shifttype" :formatter="shifttypeFormat" />
      <el-table-column fixed sortable='custom'  label="开始时间" align="center" prop="shiftstarttime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.shiftstarttime,"{y}-{m}-{d} {h}:{i}:{s}") }}</span>
        </template>
      </el-table-column>
      <el-table-column fixed sortable='custom'  label="结束时间" align="center" prop="shiftendtime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.shiftendtime),"{y}-{m}-{d} {h}:{i}:{s}" }}</span>
        </template>
      </el-table-column>
      <el-table-column fixed label="品种列表" align="center" prop="pdtcodes" />
      <el-table-column fixed label="织轴列表" align="center" prop="shaftcodes" />
      <el-table-column sortable='custom' width="100" label="平均速度" align="center" prop="macspeed" />
      <el-table-column sortable='custom' width="100" label="平均效率" align="center" prop="macefficiency" />
      <el-table-column sortable='custom' width="120" label="运行时长s" align="center" prop="runtime" />
      <el-table-column sortable='custom' width="120" label="停机时长s" align="center" prop="stoptime" />
      <el-table-column sortable='custom' width="120" label="经停时长s" align="center" prop="warpstoptime" />
      <el-table-column sortable='custom' width="120" label="纬停时长s" align="center" prop="weftstoptime" />
      <el-table-column sortable='custom' width="120" label="离线时长s" align="center" prop="offlinetime" />
      <el-table-column sortable='custom' width="120" label="其他停时长s" align="center" prop="otherstoptime" />
      <el-table-column sortable='custom' width="100" label="经停次数" align="center" prop="warpstopnum" />
      <el-table-column sortable='custom' width="100" label="纬停次数" align="center" prop="weftstopnum" />
      <el-table-column sortable='custom' width="120" label="其他停次数" align="center" prop="otherstopnum" />
      <el-table-column sortable='custom' width="100" label="打纬次数" align="center" prop="picknum" />
      <el-table-column sortable='custom' width="100" label="班次产量" align="center" prop="shiftlength" />
      <!--<el-table-column label="班次日期" align="center" prop="shiftdate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.shiftdate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否当前班次" align="center" prop="shiftnow" />
      <el-table-column label="更新时间" align="center" prop="updatetime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatetime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>-->


      <!--<el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['manufacture:shift:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['manufacture:shift:remove']"
          >删除</el-button>
        </template>
      </el-table-column>-->
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改班次效率对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="织机编号" prop="maccode">
          <el-input v-model="form.maccode" placeholder="请输入织机编号" />
        </el-form-item>
        <el-form-item label="班次类别(早中晚)" prop="shifttype">
          <el-select v-model="form.shifttype" placeholder="请选择班次类别(早中晚)">
            <el-option
              v-for="dict in shifttypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="班次开始时间" prop="shiftstarttime">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.shiftstarttime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择班次开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="班次结束时间" prop="shiftendtime">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.shiftendtime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择班次结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="品种编号列表" prop="pdtcodes">
          <el-input v-model="form.pdtcodes" placeholder="请输入品种编号列表" />
        </el-form-item>
        <el-form-item label="织轴编号列表" prop="shaftcodes">
          <el-input v-model="form.shaftcodes" placeholder="请输入织轴编号列表" />
        </el-form-item>
        <el-form-item label="织机平均速度" prop="macspeed">
          <el-input v-model="form.macspeed" placeholder="请输入织机平均速度" />
        </el-form-item>
        <el-form-item label="织机平均效率" prop="macefficiency">
          <el-input v-model="form.macefficiency" placeholder="请输入织机平均效率" />
        </el-form-item>
        <el-form-item label="运行时长s" prop="runtime">
          <el-input v-model="form.runtime" placeholder="请输入运行时长s" />
        </el-form-item>
        <el-form-item label="停机时长" prop="stoptime">
          <el-input v-model="form.stoptime" placeholder="请输入停机时长" />
        </el-form-item>
        <el-form-item label="经停时长s" prop="warpstoptime">
          <el-input v-model="form.warpstoptime" placeholder="请输入经停时长s" />
        </el-form-item>
        <el-form-item label="纬停时长s" prop="weftstoptime">
          <el-input v-model="form.weftstoptime" placeholder="请输入纬停时长s" />
        </el-form-item>
        <!--<el-form-item label="离线时长s" prop="offlinetime">-->
          <!--<el-input v-model="form.offlinetime" placeholder="请输入离线时长s" />-->
        <!--</el-form-item>-->
        <el-form-item label="其他停时长s" prop="otherstoptime">
          <el-input v-model="form.otherstoptime" placeholder="请输入其他停时长s" />
        </el-form-item>
        <el-form-item label="经停次数" prop="warpstopnum">
          <el-input v-model="form.warpstopnum" placeholder="请输入经停次数" />
        </el-form-item>
        <el-form-item label="纬停次数" prop="weftstopnum">
          <el-input v-model="form.weftstopnum" placeholder="请输入纬停次数" />
        </el-form-item>
        <el-form-item label="其他停次数" prop="otherstopnum">
          <el-input v-model="form.otherstopnum" placeholder="请输入其他停次数" />
        </el-form-item>
        <el-form-item label="打纬次数" prop="picknum">
          <el-input v-model="form.picknum" placeholder="请输入打纬次数" />
        </el-form-item>
        <el-form-item label="信号上传次数" prop="uploadnum">
          <el-input v-model="form.uploadnum" placeholder="请输入信号上传次数" />
        </el-form-item>
        <el-form-item label="信号改变次数" prop="statuschangenum">
          <el-input v-model="form.statuschangenum" placeholder="请输入信号改变次数" />
        </el-form-item>
        <el-form-item label="班次产量" prop="shiftlength">
          <el-input v-model="form.shiftlength" placeholder="请输入班次产量" />
        </el-form-item>
        <el-form-item label="班次日期" prop="shiftdate">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.shiftdate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择班次日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="是否当前班次" prop="shiftnow">
          <el-input v-model="form.shiftnow" placeholder="请输入是否当前班次" />
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
import { listShift, getShift, delShift, addShift, updateShift,updateShiftAny, exportShift } from "@/api/manufacture/shift";

export default {
  name: "Shift",
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
      // 班次效率表格数据
      shiftList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 班次类别(早中晚)字典
      shifttypeOptions: [],
      //日期范围
      dateRange:[],
      //快捷选项
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        maccode: null,
        shifttype: null,
        shiftstarttime: null,
        shiftendtime: null,
        pdtcodes: null,
        shaftcodes: null,
        macspeed: null,
        macefficiency: null,
        runtime: null,
        stoptime: null,
        warpstoptime: null,
        weftstoptime: null,
        offlinetime: null,
        otherstoptime: null,
        warpstopnum: null,
        weftstopnum: null,
        otherstopnum: null,
        picknum: null,
        shiftlength: null,
        shiftdate: null,
        shiftnow: null,
        updatetime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        macspeed: [
          { required: true, message: "织机平均速度不能为空", trigger: "blur" }
        ],
        macefficiency: [
          { required: true, message: "织机平均效率不能为空", trigger: "blur" }
        ],
        runtime: [
          { required: true, message: "运行时长s不能为空", trigger: "blur" }
        ],
        stoptime: [
          { required: true, message: "停机时长不能为空", trigger: "blur" }
        ],
        warpstoptime: [
          { required: true, message: "经停时长s不能为空", trigger: "blur" }
        ],
        weftstoptime: [
          { required: true, message: "纬停时长s不能为空", trigger: "blur" }
        ],
        offlinetime: [
          { required: true, message: "离线时长s不能为空", trigger: "blur" }
        ],
        otherstoptime: [
          { required: true, message: "其他停时长s不能为空", trigger: "blur" }
        ],
        warpstopnum: [
          { required: true, message: "经停次数不能为空", trigger: "blur" }
        ],
        weftstopnum: [
          { required: true, message: "纬停次数不能为空", trigger: "blur" }
        ],
        otherstopnum: [
          { required: true, message: "其他停次数不能为空", trigger: "blur" }
        ],
        picknum: [
          { required: true, message: "打纬次数不能为空", trigger: "blur" }
        ],
        uploadnum: [
          { required: true, message: "信号上传次数不能为空", trigger: "blur" }
        ],
        statuschangenum: [
          { required: true, message: "信号改变次数不能为空", trigger: "blur" }
        ],
        shiftlength: [
          { required: true, message: "班次产量不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("mac_common_banci_type").then(response => {
      this.shifttypeOptions = response.data;
    });
  },
  methods: {
    /** 查询班次效率列表 */
    getList() {
      this.loading = true;
      listShift(this.addDateRange(this.queryParams,this.dateRange)).then(response => {
        this.shiftList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 班次类别(早中晚)字典翻译
    shifttypeFormat(row, column) {
      return this.selectDictLabel(this.shifttypeOptions, row.shifttype);
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
        maccode: null,
        shifttype: null,
        shiftstarttime: null,
        shiftendtime: null,
        pdtcodes: null,
        shaftcodes: null,
        macspeed: null,
        macefficiency: null,
        runtime: null,
        stoptime: null,
        warpstoptime: null,
        weftstoptime: null,
        offlinetime: null,
        otherstoptime: null,
        warpstopnum: null,
        weftstopnum: null,
        otherstopnum: null,
        picknum: null,
        uploadnum: null,
        statuschangenum: null,
        shiftlength: null,
        shiftdate: null,
        shiftnow: null,
        updatetime: null,
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
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    // 表格排序
    sortChange(selection) {
      this.queryParams.sortProp = selection.prop;
      if (selection.order){
        this.queryParams.sortOrder =  selection.order=="descending"?"desc":"asc"
      }
      this.getList();
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加班次效率";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getShift(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改班次效率";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateShift(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addShift(this.form).then(response => {
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
      this.$confirm('是否确认删除班次效率编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delShift(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有班次效率数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportShift(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    },
    /** 更新任意班次 */
    handleUpdateAny() {
      const queryParams = this.queryParams;
      if ((this.queryParams.shiftdate==null)||(this.queryParams.shifttype==null)){
        this.$message("请先输入班次及日期！");
        return;
      }
      var dateTime=new Date().setDate(new Date().getDate()-1);
      dateTime=new Date(dateTime);
      if ((new Date(this.formatDate(this.queryParams.shiftdate)).getTime()>=dateTime.getTime())){
        this.$message("请输入之前的班次日期，当天的班次还未更新！");
        return;
      }
      this.$confirm('是否确认要更新['+this.queryParams.shiftdate+this.queryParams.shifttype+']班次效率数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return updateShiftAny(queryParams);
        }).then(response => {
          this.getList();
        }).catch(function() {});
    }
  }
};
</script>
