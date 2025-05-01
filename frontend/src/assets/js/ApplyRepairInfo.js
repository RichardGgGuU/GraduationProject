import request from "@/utils/request";

const {ElMessage} = require("element-plus");

export default {
    name: "ApplyRepairInfo",
    components: {},
    data() {
        return {
            loading: true,
            dialogVisible: false,
            detailDialog: false,
            search: "",
            currentPage: 1,
            pageSize: 10,
            total: 0,
            tableData: [],
            detail: {},
            name: '',
            username: '',
            form: {},
            room: {
                dormRoomId: '',
                dormBuildId: '',
            },
            rules: {
                title: [{required: true, message: "请输入标题", trigger: "blur"}],
                content: [{required: true, message: "请输入内容", trigger: "blur"}],
                orderBuildTime: [{required: true, message: "请选择时间", trigger: "blur"},],
            },
        };
    },
    created() {
        this.init()
        this.getInfo()
        this.load()
        this.loading = true
        setTimeout(() => {
            //设置延迟执行
            this.loading = false
        }, 1000);
    },
    methods: {
        init() {
            let user = JSON.parse(sessionStorage.getItem("access-user"));
            console.log('从sessionStorage获取的用户信息:', user);
            if (user) {
                this.form = user;
                this.name = user.name;
                this.username = user.username;
                console.log('设置的用户名:', this.username);
                console.log('设置的用户姓名:', this.name);
            } else {
                ElMessage({
                    message: '请先登录',
                    type: "error",
                });
                this.$router.push('/login');
            }
        },
        async load() {
            console.log('开始加载报修信息，用户姓名:', this.name);
            try {
                const res = await request.get("http://localhost:9090/repair/find/" + this.name, {
                    params: {
                        pageNum: this.currentPage,
                        pageSize: this.pageSize,
                        search: this.search,
                    },
                });
                console.log('后端返回的报修信息:', res);
                if (res.code === "0") {
                    this.tableData = res.data.records || [];
                    this.total = res.data.total || 0;
                } else {
                    ElMessage({
                        message: res.msg || '获取报修信息失败',
                        type: "error",
                    });
                }
            } catch (error) {
                console.error('加载报修信息出错:', error);
                ElMessage({
                    message: '加载报修信息失败，请检查网络连接',
                    type: "error",
                });
            } finally {
                this.loading = false;
            }
        },
        getInfo() {
            request.get("http://localhost:9090/room/getMyRoom/" + this.username).then((res) => {
                if (res.code === "0") {
                    this.room = res.data;
                    console.log('获取到的房间信息:', this.room);
                } else {
                    ElMessage({
                        message: res.msg,
                        type: "error",
                    });
                }
            });
        },
        filterTag(value, row) {
            return row.state === value;
        },
        showDetail(row) {
            this.detailDialog = true;
            this.$nextTick(() => {
                this.detail = row;
            });
        },
        closeDetails() {
            this.detailDialog = false;
        },
        add() {
            this.dialogVisible = true;
            this.$nextTick(() => {
                this.$refs.form.resetFields();
                this.form.repairer = this.name
                this.form.dormBuildId = this.room.dormBuildId
                this.form.dormRoomId = this.room.dormRoomId
            });
        },
        save() {
            this.$refs.form.validate(async (valid) => {
                if (valid) {
                    //新增
                    console.log('提交的表单数据:', this.form)
                    try {
                        const res = await request.post("http://localhost:9090/repair/add", this.form);
                        if (res.code === "0") {
                            ElMessage({
                                message: "新增成功",
                                type: "success",
                            });
                            this.search = "";
                            this.load();
                            this.dialogVisible = false;
                        } else {
                            ElMessage({
                                message: res.msg || '新增失败',
                                type: "error",
                            });
                        }
                    } catch (error) {
                        console.error('提交报修信息出错:', error);
                        ElMessage({
                            message: '提交失败，请检查网络连接',
                            type: "error",
                        });
                    }
                }
            })
        },
        cancel() {
            this.$refs.form.resetFields();
            this.dialogVisible = false;
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
};