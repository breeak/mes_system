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
      <el-form-item label="织机类型" prop="mactype">
        <el-select v-model="queryParams.mactype" placeholder="请选择织机类型" clearable size="small">
          <el-option
            v-for="dict in mactypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="织机状态" prop="macstatus">
        <el-select v-model="queryParams.macstatus" placeholder="请选择织机状态" clearable size="small">
          <el-option
            v-for="dict in macstatusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <!--<el-form-item label="织机速度" prop="macspeed">
        <el-input
          v-model="queryParams.macspeed"
          placeholder="请输入织机速度"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="织机效率" prop="macefficiency">
        <el-input
          v-model="queryParams.macefficiency"
          placeholder="请输入织机效率"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="品种编号" prop="pdtcode">
        <el-input
          v-model="queryParams.pdtcode"
          placeholder="请输入品种编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上机卡号" prop="shaftcode">
        <el-input
          v-model="queryParams.shaftcode"
          placeholder="请输入上机卡号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="更新时间">
        <el-date-picker v-model="dateRange" size="small" style="width: 240px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
      </el-form-item>

      <!--<el-form-item label="更新时间" prop="updatetime">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.updatetime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择更新时间">
        </el-date-picker>
      </el-form-item>-->
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['manufacture:machine:add']"
        >新增织机</el-button>  <!--TODO 权限管理 检查-->
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['manufacture:machine:edit']"
        >修改织机</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['manufacture:machine:remove']"
        >删除织机</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['manufacture:machine:export']"
        >导出列表</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          icon="el-icon-download"
          size="mini"
          @click="handleShangZhou"
          v-hasPermi="['manufacture:shaft:edit']"
        >织机上轴</el-button>
      </el-col>

	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="machineList" @selection-change="handleSelectionChange" @sort-change='sortChange' >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column sortable='custom' width="100"  label="织机编号" align="center" prop="maccode" />
      <el-table-column label="织机类型"  align="center" prop="mactype" :formatter="mactypeFormat" />
      <el-table-column label="织机状态" align="center" prop="macstatus" :formatter="macstatusFormat" />
      <el-table-column sortable='custom' width="100" label="织机速度" align="center" prop="macspeed" />
      <el-table-column sortable='custom' width="100" label="织机效率" align="center" prop="macefficiency" >
        <template slot-scope="scope">
          <span>{{ parseFloat(scope.row.macefficiency).toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="品种编号" align="center" prop="pdtcode" />
      <el-table-column label="纬密根/10cm"  width="100" align="center" prop="weftdensity" />
      <el-table-column label="上机卡号" align="center" prop="shaftcode" />
      <!--<el-table-column label="订单编号" align="center" prop="ordercode" />-->
      <el-table-column sortable='custom' label="更新时间" align="center" prop="updatetime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatetime, ) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="位置列" align="center" prop="maccolumn" width="55"  />
      <el-table-column label="位置行" align="center" prop="macrow" width="55"  />
      <!--<el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['manufacture:machine:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['manufacture:machine:remove']"
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

    <!-- 添加或修改织机列表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="织机编号" prop="maccode">
          <el-input v-model="form.maccode" placeholder="请输入织机编号" />
        </el-form-item>
        <el-form-item label="织机类型" prop="mactype">
          <el-select v-model="form.mactype" placeholder="请选择织机类型">
            <el-option
              v-for="dict in mactypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <!--<el-form-item label="织机状态">
          <el-radio-group v-model="form.macstatus">
            <el-radio
              v-for="dict in macstatusOptions"
              :key="dict.dictValue"
              :label="parseInt(dict.dictValue)"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="织机速度" prop="macspeed">
          <el-input v-model="form.macspeed" placeholder="请输入织机速度" />
        </el-form-item>
        <el-form-item label="织机效率" prop="macefficiency">
          <el-input v-model="form.macefficiency" placeholder="请输入织机效率" />
        </el-form-item>
        <el-form-item label="品种编号" prop="pdtcode">
          <el-input v-model="form.pdtcode" placeholder="请输入品种编号" />
        </el-form-item>
        <el-form-item label="上机卡号" prop="shaftcode">
          <el-input v-model="form.shaftcode" placeholder="请输入上机卡号" />
        </el-form-item>
        <el-form-item label="订单编号" prop="ordercode">
          <el-input v-model="form.ordercode" placeholder="请输入订单编号" />
        </el-form-item>-->
        <el-form-item label="位置列" prop="maccolumn">
          <el-input v-model="form.maccolumn" placeholder="请输入位置列" />
        </el-form-item>
        <el-form-item label="位置行" prop="macrow">
          <el-input v-model="form.macrow" placeholder="请输入位置行" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改织机上轴对话框 -->
    <el-dialog :title="title" :visible.sync="openShangZhou" width="500px" append-to-body>
      <el-form ref="formShangZhou" :model="form" :rules="rulesShangZhou" label-width="80px">
        <el-form-item label="织机编号" prop="actmaccode">
          <el-select v-model="form.actmaccode" placeholder="请选择织机编号" clearable filterable size="small">
            <el-option
              v-for="mac in machineAllList"
              :key="mac.maccode"
              :label="mac.maccode"
              :value="mac.maccode"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="织轴编号" prop="shaftcode">
          <el-select v-model="form.shaftcode" placeholder="请选择织轴编号" clearable filterable size="small">
            <el-option
              v-for="mac in shaftAllList"
              :key="mac.shaftcode"
              :label="mac.shaftcode"
              :value="mac.shaftcode"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="上机时间" prop="actstart">
          <el-date-picker clearable
                          v-model="form.actstart"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="选择上机时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormShangZhou">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listMachine, getMachine, delMachine, addMachine, updateMachine, exportMachine } from "@/api/manufacture/machine";

import { listShaft, doShangZhou} from "@/api/manufacture/shaft";

export default {
  name: "Machine",
  data() {
    var checkMaccode = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('织机编号不能为空'));
      }
      listShaft({'maccode':value}).then(response => {
        if (response.total>0){
          if (response.rows[0].id ==this.form.id){
            callback();
          } else {
            callback(new Error('织机编号已经存在'));
          }
        } else{
          callback();
        }
      });
    };

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
      // 织机列表表格数据
      machineList: [],
      // 织轴总数据
      shaftAllList: [],
      // 织机总数据
      machineAllList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示上轴界面
      openShangZhou:false,
      // 织机类型字典
      mactypeOptions: [],
      // 织机状态字典
      macstatusOptions: [],
      //日期范围
      dateRange:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        maccode: null,
        mactype: null,
        macstatus: null,
        macspeed: null,
        macefficiency: null,
        pdtcode: null,
        shaftcode: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        maccode: [
          { required: true, validator:checkMaccode, trigger: "blur" }
        ],
      },
      // 上轴表单校验
      rulesShangZhou: {
        actmaccode: [
          { required: true, message: "织机编号不能为空", trigger: "blur" }
        ],
        shaftcode: [
          { required: true, message: "织轴编号不能为空", trigger: "blur" }
        ],
        actstart: [
          { required: true, message: "上机时间不能为空", trigger: "blur" }
        ],
      }

    };
  },
  created() {
    this.getList();
    this.getDicts("mac_common_type").then(response => {
      this.mactypeOptions = response.data;
    });
    this.getDicts("mac_common_status").then(response => {
      this.macstatusOptions = response.data;
    });
  },
  methods: {
    /** 查询织机列表列表 */
    getList() {
      this.loading = true;
      listMachine(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.machineList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 织机类型字典翻译
    mactypeFormat(row, column) {
      return this.selectDictLabel(this.mactypeOptions, row.mactype);
    },
    // 织机状态字典翻译
    macstatusFormat(row, column) {
      return this.selectDictLabel(this.macstatusOptions, row.macstatus);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.openShangZhou = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        maccode: null,
        mactype: null,
        macstatus: 0,
        macspeed: null,
        macefficiency: null,
        pdtcode: null,
        shaftcode: null,
        ordercode: null,
        updatetime: null,
        maccolumn: null,
        macrow: null,
        actstart:null,
        actmaccode:null,
        remark: null
      };
      this.resetForm("form");
      this.resetForm("formShangZhou");
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
      this.title = "添加织机列表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMachine(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改织机列表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMachine(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addMachine(this.form).then(response => {
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
      this.$confirm('是否确认删除织机列表编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delMachine(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有织机列表数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportMachine(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    },
    /** 织机上轴 */
    handleShangZhou() {
      this.reset();
      listMachine().then(response => {
        this.machineAllList = response.rows;
        // console.log(this.machineList);
      });
      listShaft({shaftstatus:"0"}).then(response => {
        this.shaftAllList = response.rows;
        this.openShangZhou = true;
        this.title = "织机上轴";
        this.form.actstart = new Date();
        // console.log(this.shaftList)
      });
    },
    /** 提交上轴 */
    submitFormShangZhou() {
      this.$refs["formShangZhou"].validate(valid => {
        if (valid) {
          //console.log(this.form)
          doShangZhou(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("上轴成功");
              this.openShangZhou = false;
              this.getList();
            }
          });
        }
      });
    },

  }
};
</script>
