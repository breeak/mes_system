<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="员工姓名" prop="workername">
        <el-input
          v-model="queryParams.workername"
          placeholder="请输入员工姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="员工编号" prop="workerno">
        <el-input
          v-model="queryParams.workerno"
          placeholder="请输入员工编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="员工组别" prop="workergroup">
        <el-select v-model="queryParams.workergroup" placeholder="请选择员工组别" clearable size="small">
          <el-option
            v-for="dict in workergroupOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="员工角色" prop="workerrole">
        <el-select v-model="queryParams.workerrole" placeholder="请选择员工角色" clearable size="small">
          <el-option
            v-for="dict in workerroleOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="员工状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择员工状态" clearable size="small">
          <el-option
            v-for="dict in workerstatusOptions"
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['hr:worker:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['hr:worker:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['hr:worker:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['hr:worker:export']"
        >导出</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="workerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!--<el-table-column label="id" align="center" prop="id" />-->
      <el-table-column label="员工姓名" align="center" prop="workername" />
      <el-table-column label="员工编号" align="center" prop="workerno" />
      <el-table-column label="员工组别" align="center" prop="workergroup" :formatter="workergroupFormat" />
      <el-table-column label="员工角色" align="center" prop="workerrole" :formatter="workerroleFormat" />
      <el-table-column label="员工状态" align="center" prop="status" />
      <el-table-column label="卡号" align="center" prop="cardid" />
      <!--<el-table-column label="关联用户" align="center" prop="userid" />-->
      <!--<el-table-column label="员工备注" align="center" prop="remark" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-share"
            @click="handleArrange(scope.row)"
            v-hasPermi="['hr:worker:edit']"
          >划区</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['hr:worker:edit']"
          >修改</el-button> <!-- TODO 修改员工组别等的排班也应该删除或者修改-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['hr:worker:remove']"
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

    <!-- 添加或修改员工列表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="员工姓名" prop="workername">
          <el-input v-model="form.workername" placeholder="请输入员工姓名" />
        </el-form-item>
        <el-form-item label="员工编号" prop="workerno">
          <el-input v-model="form.workerno" placeholder="请输入员工编号" />
        </el-form-item>
        <el-form-item label="员工组别" prop="workergroup">
          <el-select v-model="form.workergroup" placeholder="请选择员工组别">
            <el-option
              v-for="dict in workergroupOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="员工角色" prop="workerrole">
          <el-select v-model="form.workerrole" placeholder="请选择员工角色">
            <el-option
              v-for="dict in workerroleOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="员工状态">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in workerstatusOptions" :key="dict.dictValue" :label="dict.dictValue">{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="卡号" prop="cardid">
          <el-input v-model="form.cardid" placeholder="请输入卡号" />
        </el-form-item>
        <!--<el-form-item label="关联用户" prop="userid">
          <el-input v-model="form.userid" placeholder="请选择用户" />
        </el-form-item>-->

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 员工划片 -->
    <el-dialog :title="title" :visible.sync="openArea" width="70%" append-to-body>
      <el-form :inline="true" ref="arrangeForm" :model="form" :rules="rulesArrange" label-width="80px">
        <el-form-item label="员工姓名" prop="workername">
          <el-select  @change="changeWorker"  v-model="form.workerid" placeholder="请选择员工" clearable filterable size="mini">
            <el-option
              v-for="worker in workerList"
              :key="worker.id"
              :label="worker.workername"
              :value="worker.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="员工组别">
          <el-input size=mini v-model="form.workergroup" :disabled="true" />
        </el-form-item>
        <el-form-item label="员工角色" >
          <el-input size=mini v-model="form.workerrole" :disabled="true" />
        </el-form-item>
        <el-button type="primary" @click="submitArrangeForm">保 存</el-button>
        <br>
        <el-form-item label="顶替员工">
          <el-select @change="changeWorker2"  v-model="form.otherworkerid" placeholder="请选择员工" clearable filterable size="mini">
            <el-option
              v-for="worker in workerList2"
              :key="worker.id"
              :label="worker.workername"
              :value="worker.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="员工组别">
          <el-input size=mini v-model="form.workergroupnew" :disabled="true" />
        </el-form-item>
        <el-form-item label="员工角色" >
          <el-input size=mini v-model="form.workerrolenew" :disabled="true" />
        </el-form-item>
        <i style="padding-top: 10px" class="el-icon-info">快速选择区域</i>
        <br>
        <el-form-item label="顶替类型">
          <el-radio-group v-model="form.type">
            <el-radio key="安排区域" label="安排区域">新安排区域或复制区域</el-radio>
            <el-radio key="代班" label="代班">顶替此人的区域安排</el-radio>
            <el-radio key="换班" label="换班">二人区域交换</el-radio>
          </el-radio-group>
        </el-form-item>
        <br>
        <el-form-item label="划片状态">
          <el-radio-group  v-model="form.isever">
            <el-radio key="true" label="true">永久排班</el-radio>
            <el-radio key="false" label="false">临时排班(请输入开始结束时间)</el-radio>
          </el-radio-group>
        </el-form-item>
        <br>
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
        <el-radio-group v-model="showclass" size="small">  <!--TODO 这些应该是从字典里查出来的-->
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
        <el-row :gutter="10"   type="flex" align="middle"  justify="space-between" class="card-panel-tittle" >
          <div class="card-panel-info">点击选择区域 再次点击取消</div>
        </el-row>
        <machine v-on:update="receiveData"  :machineList="machineList" :allRow="allRow" :allColumn="allColumn"
                 :macworkerMap="macworkerMap" :oldworker="form.workerid" :newworker="form.otherworkerid"/>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitArrangeForm">保 存</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import machine from '../../components/hrmachine/machine'

import { listMachine } from "@/api/manufacture/machine";
import { listWorker, getWorker, delWorker, addWorker, updateWorker, exportWorker } from "@/api/hr/worker";
import { listArrange,updateArrangePart, addArrangePart} from "@/api/hr/arrange";
import { listMacworker} from "@/api/hr/macworker";


export default {
  name: "Worker",
  components: {
    machine
  },
  data() {
    var checkStatus = (rule, value, callback) => {
      console.log(this.form);
      console.log(this.macworkerList);
      if (!value) {
        return callback(new Error('员工姓名不能为空'));
      }
      if (this.macworkerList.length==0){
        return callback(new Error('员工未安排任何织机'));
      }
      if (this.form.type!="安排区域" && this.form.otherworkerid==undefined){
        return callback(new Error('请先选择要代班或换班的员工'));
        if (this.form.workerid == this.otherworkerid){
          return callback(new Error('代班或换班不能选择同一员工'));
        }
        if (this.form.workerid == this.otherworkerid){
          return callback(new Error('代班或换班不能选择同一员工'));
        }
        //TODO 如果换班替班的话 那个员工必须要有排班 查的时候注意；
        // 先不管是不是换了班次   同一个班次的也能换
      }
      if (this.form.isever=="false" && this.form.arrangeend==undefined){
        return callback(new Error('临时排班请选择结束日期'));
      }
      if (this.form.arrangestart!=undefined && this.form.arrangeend!=undefined){
        if(new Date(Date.parse(this.form.arrangestart)).getTime()>=new Date(Date.parse(this.form.arrangeend)).getTime()){
          return callback(new Error('结束日期应大于开始日期'));
        }
      }
      return callback();
    };

    return {
      machineList:[],
      macworkerMap:Object,
      arrangeList:Object,
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
      // 员工列表表格数据
      workerList: [],
      workerList2: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示弹出层划区
      openArea: false,
      // 员工组别字典
      workergroupOptions: [],
      // 员工角色字典
      workerroleOptions: [],
      // 员工状态字典
      workerstatusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        workername: null,
        workerno: null,
        cardid: null,
        userid: null,
        workergroup: null,
        workerrole: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        workername: [
          { required: true, message: "员工姓名不能为空", trigger: "blur" }
        ],
      },
      rulesArrange: {
        workername: [
          { required: true, validator: checkStatus, trigger: "blur" }
        ],
      },

    };
  },
  watch:{
    // 监听要安排的类别
    showclass:{
      handler(val, oldVal){
        if (val!=undefined){
          this.macworkerMap = this.arrangeList[val];
          if (this.macworkerMap!=undefined){
            console.log(this.macworkerMap);
          } else {
            this.macworkerMap = new Object();
          }
        }
      },
      deep:true,
    },
  },
  created() {
    this.getList();
    listMachine({"sortProp":"macspeed","sortOrder":"desc"}).then(response => {
      this.machineList = response.rows;
    });
    listArrange({status:"1"}).then(response => {
      if(response.rows.length>0){
        response.rows.forEach(val=>{
          this.arrangeList[val.arrangeclass]=val.macworkerMap;
        });
        this.macworkerMap=this.arrangeList[this.showclass];
        if (this.macworkerMap==undefined){
          this.macworkerMap=new Object();
        }
      }else {
        this.macworkerMap = new Object();
      }
    });
    this.getDicts("mac_common_arrangement").then(response => {
      this.allRow = Number(response.data[0].dictValue);
      this.allColumn = Number(response.data[1].dictValue);
    });
    this.getDicts("hr_worker_group").then(response => {
      this.workergroupOptions = response.data;
    });
    this.getDicts("hr_worker_role").then(response => {
      this.workerroleOptions = response.data;
    });
    this.getDicts("hr_worker_status").then(response => {
      this.workerstatusOptions = response.data;
    });
  },
  methods: {
    /** 查询员工列表列表 */
    getList() {
      this.loading = true;
      listWorker(this.queryParams).then(response => {
        this.workerList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 员工组别字典翻译
    workergroupFormat(row, column) {
      return this.selectDictLabel(this.workergroupOptions, row.workergroup);
    },
    // 员工角色字典翻译
    workerroleFormat(row, column) {
      return this.selectDictLabel(this.workerroleOptions, row.workerrole);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.openArea = false;
      this.reset();
    },

    // 表单重置
    reset() {
      this.form = {
        id: null,
        workername: null,
        workerno: null,
        cardid: null,
        userid: null,
        workergroup: null,
        workerrole: null,
        status: "正常",
        createTime: null,
        updateTime: null,
        createBy: null,
        type: "安排区域",
        isever: 'true',
        area: null,
        arrangeid: null,
        updateBy: null,
        arrangestart: new Date(),
        arrangeend: new Date(),
        workergroupnew: null,
        workerrolenew: null,
        workerid: null,
        otherworkerid: null,
        remark: null
      };
      this.resetForm("form");
      this.resetForm("arrangeForm");
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
      this.title = "添加员工列表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWorker(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改员工列表";
      });
    },
    /** 划区 */
    handleArrange(row) {
      this.reset();
      const id = row.id || this.ids;
      this.form.arrangestart = this.parseTime(new Date(),'{y}-{m}-{d} {h}:{i}:{s}');
      this.form.arrangeend = this.parseTime(new Date(new Date().getTime()+60*1000*60*24*5),'{y}-{m}-{d} {h}:{i}:{s}');
      getWorker(id).then(response => {
        this.form.workerid = response.data.id;
        this.form.workername = response.data.workername;
        this.form.workergroup = response.data.workergroup;
        this.form.workerrole = response.data.workerrole;
        this.showclass=response.data.workergroup;
        listWorker().then(response => {
          this.workerList = response.rows;
          listMacworker({"groupby":"workerid"}).then(response => {// 必须列出有安排的员工区域
            this.workerList2 = response.rows;
            this.openArea = true;
            this.title = "员工划区";
          });
        });
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWorker(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addWorker(this.form).then(response => {
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
    /** 提交安排按钮 */
    submitArrangeForm() {

      this.$refs["arrangeForm"].validate(valid => {
        if (valid) {
          this.form.macworkerList=this.macworkerList;
          this.form.arrangeclass=this.form.workergroup;//没用到
          var param = new Object();
          param.isever = this.form.isever;
          param.type = this.form.type;
          param.oldclass = this.form.workergroup;
          param.otherclass = this.form.workergroupnew;
          param.otherworkerid = this.form.otherworkerid;
          this.form.params = param;
          console.log(this.form);
          console.log(1);
          if (this.form.arrangeid != null) {
            updateArrangePart(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }
            });
          } else {
            addArrangePart(this.form).then(response => {
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
      this.$confirm('是否确认删除员工列表编号为"' + ids + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delWorker(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有员工列表数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportWorker(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    },
    /**
     * 接收点击传过来的machineid 进行封装
     * @param machineid
     * @param maccode
     * @param flag
     */
    receiveData(machineid,maccode,flag){
      console.log(flag);
      if (flag){//选中 增加该机台
        var macWorker = new Object();
        macWorker.machineid=machineid;
        macWorker.maccode=maccode;
        macWorker.workername=this.form.workername;
        macWorker.workerid=this.form.workerid;
        this.macworkerList.push(macWorker);
      }else {
        this.macworkerList = this.macworkerList.filter(item=>item.machineid != machineid);
      }
      console.log(this.macworkerList);
    },
    changeWorker(id){
      if (id==""){
        this.form.workergroup = "";
        this.form.workerrole = "";
        this.form.workerid = null;//防止未传入数据错误
      } else{
        getWorker(id).then(response => {
          this.form.workergroup = response.data.workergroup;
          this.form.workerrole = response.data.workerrole;
          this.showclass = response.data.workergroup;
          this.form.workerid = response.data.id;
        });
      }
    },
    changeWorker2(id){
      if (id==""){
        this.form.workergroupnew = "";
        this.form.workerrolenew = "";
        this.form.otherworkerid = null;
      } else{
        getWorker(id).then(response => {
          this.form.workergroupnew = response.data.workergroup;
          this.form.workerrolenew = response.data.workerrole;
          this.form.otherworkerid = response.data.id;
          this.showclass = response.data.workergroup;
        });
      }
    },

  }
};
</script>
