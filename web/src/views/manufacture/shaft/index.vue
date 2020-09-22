<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="织轴卡号" prop="shaftcode">
        <el-input
          v-model="queryParams.shaftcode"
          placeholder="请输入织轴卡号(开卡用唯一)"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!--<el-form-item label="织轴号" prop="shaftno">
        <el-input
          v-model="queryParams.shaftno"
          placeholder="请输入织轴号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="织轴长度" prop="shaftlength">
        <el-input
          v-model="queryParams.shaftlength"
          placeholder="请输入织轴长度"
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
      <el-form-item label="订单编号" prop="ordercode">
        <el-input
          v-model="queryParams.ordercode"
          placeholder="请输入订单编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="织轴状态" prop="shaftstatus">
        <el-select v-model="queryParams.shaftstatus" placeholder="请选择织轴状态" clearable size="small">
          <el-option
            v-for="dict in shaftstatusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <!--<el-form-item label="余轴长度" prop="shaftremainlength">
        <el-input
          v-model="queryParams.shaftremainlength"
          placeholder="请输入余轴长度"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="已织长度" prop="clothlength">
        <el-input
          v-model="queryParams.clothlength"
          placeholder="请输入已织长度"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="操作员" prop="shaftworker">
        <el-input
          v-model="queryParams.shaftworker"
          placeholder="请输入操作员"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="经纱批次" prop="warpbacth">
        <el-input
          v-model="queryParams.warpbacth"
          placeholder="请输入经纱批次"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="纬纱批次" prop="weftbacth">
        <el-input
          v-model="queryParams.weftbacth"
          placeholder="请输入纬纱批次"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!--<el-form-item label="开卡时间" prop="createtime">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.createtime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择开卡时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="计划上机时间" prop="planstart">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.planstart"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择计划上机时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="计划下机时间" prop="planend">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.planend"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择计划下机时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="实际上机时间" prop="actstart">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.actstart"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择实际上机时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="实际下机时间" prop="actend">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.actend"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择实际下机时间">
        </el-date-picker>
      </el-form-item>-->
      <el-form-item label="上机编号" prop="actmaccode">
        <el-input
          v-model="queryParams.actmaccode"
          placeholder="请输入上机编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
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
          v-hasPermi="['manufacture:shaft:add']"
        >新增织轴</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['manufacture:shaft:edit']"
        >修改织轴</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['manufacture:shaft:remove']"
        >删除织轴</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['manufacture:shaft:export']"
        >导出织轴</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          icon="el-icon-download"
          size="mini"
          @click="handleShangZhou"
          v-hasPermi="['manufacture:shaft:edit']"
        >织机上轴</el-button>
        <!--TODO 织轴了机或修改上轴编号-->
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shaftList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="织轴卡号" align="center" prop="shaftcode" />
      <el-table-column label="织轴号" align="center" prop="shaftno" />
      <el-table-column label="织轴长度" align="center" prop="shaftlength" />
      <el-table-column label="品种编号" align="center" prop="pdtcode" />
      <el-table-column label="品种纬密" align="center" prop="pdtweftshrinkage" />
      <el-table-column label="订单编号" align="center" prop="ordercode" />
      <el-table-column label="织轴状态" align="center" prop="shaftstatus" :formatter="shaftstatusFormat" />
      <el-table-column label="余轴长度" align="center" prop="shaftremainlength" />
      <el-table-column label="已织长度" align="center" prop="clothlength" />
      <el-table-column label="操作员" align="center" prop="shaftworker" />
      <el-table-column label="经纱批次" align="center" prop="warpbacth" />
      <el-table-column label="纬纱批次" align="center" prop="weftbacth" />
      <el-table-column label="开卡时间" align="center" prop="createtime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createtime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updatetime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatetime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="计划上机时间" align="center" prop="planstart" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planstart) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="计划下机时间" align="center" prop="planend" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planend) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实际上机时间" align="center" prop="actstart" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.actstart) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实际下机时间" align="center" prop="actend" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.actend) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="计划上机机台编号" align="center" prop="planmaccode" />
      <el-table-column label="实际上机机台编号" align="center" prop="actmaccode" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['manufacture:shaft:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['manufacture:shaft:remove']"
          >删除</el-button>
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

    <!-- 添加或修改织轴列表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="织轴卡号" prop="shaftcode">
          <el-input v-model="form.shaftcode" placeholder="请输入织轴卡号(开卡用唯一)" />
        </el-form-item>
        <el-form-item label="织轴长度" prop="shaftlength">
          <el-input v-model="form.shaftlength" placeholder="请输入织轴长度" />
        </el-form-item>
        <el-form-item label="品种编号" prop="pdtcode">
          <el-input v-model="form.pdtcode" placeholder="请输入品种编号" />
        </el-form-item>
        <el-form-item label="品种纬密" prop="pdtweftshrinkage">
          <el-input v-model="form.pdtweftshrinkage" placeholder="请输入品种纬密" />
        </el-form-item>
        <el-form-item label="订单编号" prop="ordercode">
          <el-input v-model="form.ordercode" placeholder="请输入订单编号" />
        </el-form-item>
        <el-form-item label="操作员" prop="shaftworker">
          <el-input v-model="form.shaftworker" placeholder="请输入操作员" />
        </el-form-item>
        <el-form-item label="经纱批次" prop="warpbacth">
          <el-input v-model="form.warpbacth" placeholder="请输入经纱批次" />
        </el-form-item>
        <el-form-item label="纬纱批次" prop="weftbacth">
          <el-input v-model="form.weftbacth" placeholder="请输入纬纱批次" />
        </el-form-item>
        <el-form-item label="计划上机时间" prop="planstart">
          <el-date-picker clearable
            v-model="form.planstart"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择计划上机时间">
          </el-date-picker>
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
import { doShangZhou,listShaft, getShaft, delShaft, addShaft, updateShaft, exportShaft } from "@/api/manufacture/shaft";
import { listMachine} from "@/api/manufacture/machine";

export default {
  name: "Shaft",
  data() {
    var checkShaftcode = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('织轴编号不能为空'));
      }
      listShaft({'shaftcode':value}).then(response => {
        if (response.total>0){
          if (response.rows[0].id ==this.form.id){
            callback();
          } else {
            callback(new Error('织轴编号已经存在'));
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
      // 织轴列表表格数据
      shaftList: [],
      // 织轴总数据
      shaftAllList: [],
      // 织机总数据
      machineAllList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示上轴界面
      openShangZhou: false,
      // 织轴状态字典
      shaftstatusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shaftcode: null,
        shaftno: null,
        shaftlength: null,
        pdtcode: null,
        ordercode: null,
        shaftstatus: null,
        shaftremainlength: null,
        clothlength: null,
        shaftworker: null,
        warpbacth: null,
        weftbacth: null,
        createtime: null,
        planstart: null,
        planend: null,
        actstart: null,
        actend: null,
        actmaccode: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        shaftcode: [
          { required: true, validator: checkShaftcode, trigger: 'blur' }
        ],
        shaftlength: [
          { required: true, message: "织轴长度不能为空", trigger: "blur" }
        ],
        pdtcode: [
          { required: true, message: "品种编号不能为空", trigger: "blur" }
        ],
        pdtweftshrinkage: [
          { required: true, message: "品种纬密不能为空", trigger: "blur" }
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
    this.getDicts("mac_common_shaft").then(response => {
      this.shaftstatusOptions = response.data;
    });
  },
  methods: {
    /** 查询织轴列表列表 */
    getList() {
      this.loading = true;
      listShaft(this.queryParams).then(response => {
        this.shaftList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 织轴状态字典翻译
    shaftstatusFormat(row, column) {
      return this.selectDictLabel(this.shaftstatusOptions, row.shaftstatus);
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
        shaftcode: null,
        shaftno: null,
        shaftlength: null,
        pdtcode: null,
        pdtweftshrinkage: null,
        ordercode: null,
        shaftstatus: null,
        shaftremainlength: null,
        clothlength: null,
        shaftworker: null,
        warpbacth: null,
        weftbacth: null,
        createtime: null,
        updatetime: null,
        planstart: null,
        planend: null,
        actstart: null,
        actend: null,
        planmaccode: null,
        actmaccode: null,
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
      this.title = "添加织轴列表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      // TODO 修改织轴的话 要根据状态来 如果已经上轴了 判断修改的值 修改相应的状态
      this.reset();
      const id = row.id || this.ids
      getShaft(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改织轴列表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateShaft(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addShaft(this.form).then(response => {
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
      console.log(this.ids);
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除织轴列表编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delShaft(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有织轴列表数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportShaft(queryParams);
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
