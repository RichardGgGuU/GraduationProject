import request from "@/utils/request";

const {ElMessage} = require("element-plus");
export default {
    name: "AdjustRoomInfo",
    data() {
        const checkRoomState = (rule, value, callback) => {
            this.dormRoomId = value
            if (typeof value === "number") {
                request.get("/room/checkRoomState/" + value).then((res) => {
                    request.get("/room/checkRoomExist/" + value).then((result) => {
                        if (result.code === "-1") {
                            callback(new Error(result.msg));
                        }
                        if (res.code === "-1") {
                            callback(new Error(res.msg));
                        }
                        callback();
                    })
                });
            } else {
                callback(new Error("请输入正确的数据"));
            }
        };
        const checkBedState = (rule, value, callback) => {
            request.get("/room/checkBedState/" + this.dormRoomId + '/' + value).then((res) => {
                if (res.code === "0") {
                    callback();
                } else {
                    callback(new Error(res.msg));
                }
            });
        };
        return {
            loading: true,
            dialogVisible: false,
            detailDialog: false,
            search: "",
            currentPage: 1,
            pageSize: 10,
            total: 0,
            tableData: [],
            form: {},
            dormRoomId: 0,
            orderState: false,
            judgeOption: false,
            rules: {
                username: [
                    {required: true, message: "请输入学号", trigger: "blur"},
                    {pattern: /^[a-zA-Z0-9]{4,9}$/, message: "必须由 2 到 5 个字母或数字组成", trigger: "blur",},
                ],
                name: [
                    {required: true, message: "请输入姓名", trigger: "blur"},
                    {pattern: /^(?:[\u4E00-\u9FA5·]{2,10})$/, message: "必须由 2 到 10 个汉字组成", trigger: "blur",},
                ],
                currentRoomId: [
                    {required: true, message: "请输入当前房间号", trigger: "blur"},
                ],
                currentBedId: [
                    {required: true, message: "请输入当前床位号", trigger: "blur"},
                ],
                towardsRoomId: [
                    {validator: checkRoomState, trigger: "blur"},
                ],
                towardsBedId: [
                    {validator: checkBedState, trigger: "blur"},
                ],
            },
        }
    },
    created() {
        this.load();
        this.loading = true;
        setTimeout(() => {
            //设置延迟执行
            this.loading = false;
        }, 1000);
    },
    methods: {
        async load() {
            const userInfo = JSON.parse(sessionStorage.getItem('access-user'))
            request.get("/adjustRoom/find", {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize,
                    search: userInfo.username,
                },
            }).then((res) => {
                this.tableData = res.data.records;
                this.total = res.data.total;
                this.loading = false;
            });
        },
        filterTag(value, row) {
            return row.gender === value;
        },
        add() {
            // this.dialogVisible = true;
            this.$nextTick(async () => {
                try {
                    // this.$refs.form.resetFields();
                    // if (this.$refs.form?.resetFields) {
                    //     this.$refs.form.resetFields();
                    // }
                    // 1. 处理 sessionStorage 读取错误
                    let user;
                    try {
                        user = JSON.parse(sessionStorage.getItem("access-user"));
                    } catch (e) {
                        console.error("解析用户信息失败:", e);
                        this.$message.error("用户信息异常，请重新登录");
                        this.$router.push('/login');
                        return; // 终止后续执行
                    }

                    if (!user) {
                        this.$router.push('/login');
                        return; // 终止后续执行
                    }

                    // 2. 处理用户字段赋值
                    this.form.username = user.username || ""; // 防 undefined
                    this.form.name = user.name || "";

                    // 3. 处理 API 请求错误
                    const res = await request.get(`/room/getMyRoom/${this.form.username}`)
                        .catch(e => {
                            console.error("获取房间失败:", e);
                            this.$message.error("加载房间信息失败");
                            // throw e; // 进入外层 catch
                        });

                    // 4. 处理响应数据异常
                    if (!res?.data?.dormRoomId) {
                        console.error("无效的响应数据:", res);
                        this.$message.error("暂未分配宿舍无法调宿");
                        return;
                    }
                    this.dialogVisible = true;

                    this.$refs.form?.resetFields();

                    this.form.currentRoomId = res.data.dormRoomId;
                    this.form.currentBedId = this.calBedNum(this.form.username, res.data);

                    this.judgeOption = true;
                } catch (e) {
                    // 全局兜底错误处理
                    console.error("add 方法发生未预期错误:", e);
                    this.$message.error("系统异常，请稍后重试");
                    this.dialogVisible = false; // 关闭弹窗避免卡死
                }
            });
        },
        calBedNum(username, data) {
            if (data.firstBed === username) {
                return 1;
            } else if (data.secondBed === username) {
                return 2;
            } else if (data.thirdBed === username) {
                return 3;
            } else if (data.fourthBed === username) {
                return 4;
            }
        },
        judgeOrderState(state) {
            if (state === '通过') {
                this.orderState = true
            } else if (state === '驳回') {
                this.orderState = false
            } else if (state === '未处理') {
                this.orderState = false
            }
        },
        save() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    if (this.judgeOption === false) {
                        //修改
                        this.judgeOrderState(this.form.state)
                        request.put("/adjustRoom/update/" + this.orderState, this.form).then((res) => {
                            if (res.code === "0") {
                                ElMessage({
                                    message: "修改成功",
                                    type: "success",
                                });
                                this.search = "";
                                this.load();
                                this.dialogVisible = false;
                            } else if (res.msg === "重复操作") {
                                ElMessage({
                                    message: res.msg,
                                    type: "error",
                                });
                                this.search = "";
                                this.load();
                                this.dialogVisible = false;
                            } else {
                                ElMessage({
                                    message: res.msg,
                                    type: "error",
                                });
                            }
                        });
                    } else if (this.judgeOption === true) {
                        //添加
                        request.post("/adjustRoom/add", this.form).then((res) => {
                            if (res.code === "0") {
                                ElMessage({
                                    message: "添加成功",
                                    type: "success",
                                });
                                this.search = "";
                                this.load();
                                this.dialogVisible = false;
                            } else {
                                ElMessage({
                                    message: res.msg,
                                    type: "error",
                                });
                            }
                        });
                    }
                }
            });
        },
        cancel() {
            this.$refs.form.resetFields();
            this.dialogVisible = false;
            this.detailDialog = false;
        },
        showDetail(row) {
            // 查看详情
            this.detailDialog = true;
            this.$nextTick(() => {
                this.$refs.form.resetFields();
                this.form = JSON.parse(JSON.stringify(row));
            });
        },
        handleEdit(row) {
            //修改
            // 生拷贝
            this.dialogVisible = true;
            this.$nextTick(() => {
                this.$refs.form.resetFields();
                this.form = JSON.parse(JSON.stringify(row));
                this.judgeOption = false;
            });
        },
        handleSizeChange(pageSize) {
            //改变每页个数
            this.pageSize = pageSize;
            this.load();
        },
        handleCurrentChange(pageNum) {
            //改变页码
            this.currentPage = pageNum;
            this.load();
        },
    },
}