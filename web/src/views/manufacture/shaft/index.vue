<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="织轴卡号" prop="shaftcode">
        <el-input
          v-model="queryParams.shaftcode"
          placeholder="请输入织轴卡号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

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
      <el-form-item label="上机编号" prop="actmaccode">
        <el-input
          v-model="queryParams.actmaccode"
          placeholder="请输入上机编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="上机时间" >
        <el-date-picker v-model="dateActStartRange" size="small" type="datetimerange" :picker-options="pickerOptions" style="width: 360px"   range-separator="-" start-placeholder="开始时间" end-placeholder="结束时间"></el-date-picker>
      </el-form-item>
      <el-form-item label="预了时间" >
        <el-date-picker v-model="datePlanEndRange" size="small" type="datetimerange" :picker-options="pickerOptions" style="width: 360px"   range-separator="-" start-placeholder="开始时间" end-placeholder="结束时间"></el-date-picker>
      </el-form-item>
      <el-form-item label="开卡时间" >
        <el-date-picker v-model="dateRange" size="small" type="datetimerange" :picker-options="pickerOptions" style="width: 360px"   range-separator="-" start-placeholder="开始时间" end-placeholder="结束时间"></el-date-picker>
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
        <!--<el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['manufacture:shaft:remove']"
        >删除织轴</el-button>--> <!--// TODO 判断能否删除 先不批量删除-->
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
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-download"
          size="mini"
          @click="handleShangZhouAll"
          v-hasPermi="['manufacture:shaft:edit']"
        >新增上轴</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shaftList" @selection-change="handleSelectionChange" @sort-change='sortChange'>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column sortable='custom' width="100" label="织轴卡号" align="center" prop="shaftcode" />
      <el-table-column sortable='custom' width="100" label="织轴长度" align="center" prop="shaftlength" />
      <el-table-column label="品种编号" align="center" prop="pdtcode" />
      <el-table-column label="纬密根/英寸" align="center" prop="pdtweftdensity">
        <template slot-scope="scope">
          <span>{{(scope.row.pdtweftdensity*0.254).toFixed(2)}}</span>
        </template>
      </el-table-column>
      <el-table-column label="缩率%" align="center" prop="pdtshrinkage" />
      <el-table-column label="订单编号" align="center" prop="ordercode" />
      <el-table-column label="织轴状态" align="center" prop="shaftstatus" :formatter="shaftstatusFormat" />
      <el-table-column sortable='custom' width="100" label="余轴长度" align="center" prop="shaftremainlength" />
      <el-table-column sortable='custom' width="100" label="已织长度" align="center" prop="clothlength" />
      <el-table-column label="操作员" align="center" prop="shaftworker" />
      <el-table-column label="经纱批次" align="center" prop="warpbacth" />
      <el-table-column label="纬纱批次" align="center" prop="weftbacth" />
      <el-table-column sortable='custom' label="开卡时间" align="center" prop="createtime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createtime) }}</span>
        </template>
      </el-table-column>
      <el-table-column sortable='custom' label="更新时间" align="center" prop="updatetime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatetime) }}</span>
        </template>
      </el-table-column>
      <el-table-column sortable='custom' label="计划上机时间" align="center" prop="planstart" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planstart) }}</span>
        </template>
      </el-table-column>
      <el-table-column sortable='custom' label="实际上机时间" align="center" prop="actstart" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.actstart) }}</span>
        </template>
      </el-table-column>
      <el-table-column sortable='custom' label="预计下机时间" align="center" prop="planend" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planend) }}</span>
        </template>
      </el-table-column>
      <el-table-column sortable='custom'label="实际下机时间" align="center" prop="actend" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.actend) }}</span>
        </template>
      </el-table-column>
      <el-table-column sortable='custom' width="120" label="计划上机机台编号" align="center" prop="planmaccode" />
      <el-table-column sortable='custom' width="120" label="实际上机机台编号" align="center" prop="actmaccode" />
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
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item v-show="false" label="织轴状态 用于保存的 用户不可见" >
          <el-input v-model="form.shaftstatus" value="未上轴" />
        </el-form-item>
        <el-form-item label="织轴卡号" prop="shaftcode">
          <el-input v-model="form.shaftcode" placeholder="请输入织轴卡号(开卡用唯一)" />
        </el-form-item>
        <el-form-item label="织轴长度" prop="shaftlength">
          <el-input v-model.number="form.shaftlength" type="number" placeholder="请输入织轴长度" />
        </el-form-item>
        <el-form-item label="品种编号" prop="pdtcode">
          <el-input v-model="form.pdtcode" placeholder="请输入品种编号" />
        </el-form-item>
        <el-form-item label="纬密根/英寸" prop="pdtweftdensity">
          <el-input v-model.number="form.pdtweftdensity" type="number" placeholder="请输入品种纬密" />
        </el-form-item>
        <el-form-item label="缩率%" prop="pdtshrinkage">
          <el-input v-model.number="form.pdtshrinkage" type="number" placeholder="请输入缩率" />
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
            placeholder="选择计划上机时间"
          >
          </el-date-picker>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改织轴列表对话框 -->
    <el-dialog :title="title" :visible.sync="openShangZhouAll" width="500px" append-to-body>
      <el-form ref="formShangZhouAll" :model="form" :rules="rulesShangZhouAll" label-width="120px">
        <el-form-item v-show="false" label="织轴状态 用于保存的 用户不可见" >
          <el-input v-model="form.shaftstatus" value="未上轴" />
        </el-form-item>
        <el-form-item label="织轴卡号" prop="shaftcode">
          <el-input v-model="form.shaftcode" placeholder="请输入织轴卡号(开卡用唯一)" />
        </el-form-item>
        <el-form-item label="织轴长度" prop="shaftlength">
          <el-input v-model.number="form.shaftlength" type="number" placeholder="请输入织轴长度" />
        </el-form-item>
        <el-form-item label="品种编号" prop="pdtcode">
          <el-input v-model="form.pdtcode" placeholder="请输入品种编号" />
        </el-form-item>
        <el-form-item label="纬密根/英寸" prop="pdtweftdensity">
          <el-input v-model.number="form.pdtweftdensity" type="number" placeholder="请输入品种纬密" />
        </el-form-item>
        <el-form-item label="缩率" prop="pdtshrinkage">
          <el-input v-model.number="form.pdtshrinkage" type="number" placeholder="缩率" />
        </el-form-item>
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

        <el-form-item label="上机时间" prop="actstart">
          <el-date-picker clearable
                          v-model="form.actstart"
                          type="datetime"
                          format = "yyyy-MM-dd HH:mm:ss"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="选择上机时间">
          </el-date-picker>
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
        </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormShangZhouAll">确 定</el-button>
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
                          format = "yyyy-MM-dd HH:mm:ss"
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
import { doShangZhouAll,doShangZhou,listShaft, getShaft, delShaft, addShaft, updateShaft, exportShaft } from "@/api/manufacture/shaft";
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
      openShangZhouAll: false,
      // 织轴状态字典
      shaftstatusOptions: [],
      // 开卡时间
      //日期范围
      dateRange:[],
      datePlanEndRange:[],
      dateActStartRange:[],
      //快捷选项
      pickerOptions: {
        shortcuts: [{
          text: '未来一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            end.setTime(end.getTime() + 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
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
        actstart: new Date(),
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
          { required: true, message: "请输入织轴长度", trigger: "blur" }
          ,{ type:"number",min:1, message: '织轴长度应大于0', trigger: 'blur'  }
        ],
        pdtcode: [
          { required: true, message: "品种编号不能为空", trigger: "blur" }
        ],
        pdtweftdensity: [
          { required: true, message: "品种纬密不能为空", trigger: "blur" }
          ,{ type:"number",min:1, message: '品种纬密应大于0', trigger: 'blur'  }
        ],
        pdtshrinkage: [
          { required: true, message: "缩率不能为空", trigger: "blur" }
          ,{ type:"number",min:1,max:99, message: '缩率应大于0,小于100', trigger: 'blur'  }
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
    },
      // 上轴表单全部校验
      rulesShangZhouAll: {
        shaftcode: [
          { required: true,validator:checkShaftcode, trigger: "blur" }
        ],
        shaftlength: [
          { required: true, message: "织轴长度不能为空132", trigger: "blur" }
          ,{ type:"number",min:1,message: '织轴长度应大于0', trigger: 'blur'  }
        ],
        pdtcode: [
          { required: true, message: "品种编号不能为空", trigger: "blur" }
        ],
        pdtweftdensity: [
          { required: true, message: "品种纬密不能为空", trigger: "blur" }
          ,{ type:"number",min:1,message: '品种纬密应大于0', trigger: 'blur'  }
        ],
        pdtshrinkage: [
          { required: true, message: "缩率不能为空", trigger: "blur" }
          ,{ type:"number",min:1,max:99, message: '缩率应大于0,小于100', trigger: 'blur'  }
        ],
        actmaccode: [
          { required: true, message: "织机编号不能为空", trigger: "blur" }
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
      listShaft(this.addAllDateRange(this.queryParams,this.dateRange,this.datePlanEndRange,this.dateActStartRange)).then(response => {
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
      this.openShangZhouAll = false;
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
        pdtweftdensity: null,
        pdtshrinkage: null,
        ordercode: null,
        shaftstatus: "未上轴",
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
      this.title = "添加织轴列表";
      this.form.actstart = this.parseTime(new Date(),'{y}-{m}-{d} {h}:{i}:{s}')
      this.form.pdtshrinkage = 3;

    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      // TODO 修改织轴的话 先做成不能修改已经上轴了的
      if (row){
        if (row.actstart){
          this.$message("不能修改已上机的织轴");
          return;
        }
      }
      this.reset();
      const id = row.id || this.ids
      getShaft(id).then(response => {
        this.form = response.data;
        if (response.data.actstart){
          this.$message("不能修改已上机的织轴");
          return;
        }
        this.open = true;
        this.title = "修改织轴列表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.pdtweftdensity = (this.form.pdtweftdensity*10/2.54).toFixed(2);
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
      const ids = row.id || this.ids;
      if (row){
        if (row.actstart){
          this.$message("不能删除已上机的织轴");
          return;
        }
      }
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
      listShaft({"shaftstatus":"未上轴"}).then(response => {
        this.shaftAllList = response.rows;
        this.openShangZhou = true;
        this.title = "织机上轴";
        this.form.actstart = this.parseTime(new Date(),'{y}-{m}-{d} {h}:{i}:{s}')
        //this.form.pdtshrinkage = 3;
        // console.log(this.shaftList)
      });
    },
    /** 织机上轴 */
    handleShangZhouAll() {
      this.reset();
      listMachine().then(response => {
        this.machineAllList = response.rows;
        // console.log(this.machineList);
        this.openShangZhouAll = true;
        this.title = "织机上轴";
        this.form.actstart = this.parseTime(new Date(),'{y}-{m}-{d} {h}:{i}:{s}');
        this.form.pdtshrinkage = 3;
      });
    },

    /** 提交上轴 */
    submitFormShangZhou() {
      this.$refs["formShangZhou"].validate(valid => {
        if (valid) {
          this.form.pdtweftdensity = (this.form.pdtweftdensity*10/2.54).toFixed(2);
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
    /** 提交上轴所有 */
    submitFormShangZhouAll() {
      this.$refs["formShangZhouAll"].validate(valid => {
        if (valid) {
          this.form.pdtweftdensity = (this.form.pdtweftdensity*10/2.54).toFixed(2);
          doShangZhouAll(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("上轴成功");
              this.openShangZhouAll = false;
              this.getList();
            }
          });
        }
      });
    },
    // 添加日期选项
    addAllDateRange(params, dateRange,datePlanEndRange,dateActStartRange,) {
      var search = params;
      search.beginTime = "";
      search.endTime = "";
      if (null != dateRange && '' != dateRange) {
        search.beginTime = this.dateRange[0];
        search.endTime = this.dateRange[1];
      }
      if (null != datePlanEndRange && '' != datePlanEndRange) {
        search.beginPlanEndTime = this.datePlanEndRange[0];
        search.endPlanEndTime = this.datePlanEndRange[1];
      }
      if (null != dateActStartRange && '' != dateActStartRange) {
        search.beginActStartTime = this.dateActStartRange[0];
        search.endActStartTime = this.dateActStartRange[1];
      }
      return search;
    }
  }
};
</script>
