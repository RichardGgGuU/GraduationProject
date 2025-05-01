import request from "@/utils/request";

const {ElMessage} = require("element-plus");

export default {
    name: "MyRoomInfo",
    data() {
        return {
            name: "",
            form: {
                username: "",
            },
            room: {
                dormRoomId: "",
                dormBuildId: "",
                floorNum: "",
                maxCapacity: "",
                currentCapacity: "",
                firstBed: "",
                secondBed: "",
                thirdBed: "",
                fourthBed: "",
            },
        };
    },
    created() {
        this.init();
        this.getInfo();
    },
    methods: {
        init() {
            let user = JSON.parse(sessionStorage.getItem("access-user"));
            console.log('从sessionStorage获取的用户信息:', user);
            if (user) {
                this.form.username = user.username;
                this.name = user.name;
                console.log('设置的用户名:', this.form.username);
                console.log('设置的用户姓名:', this.name);
            } else {
                this.$router.push('/login');
            }
        },
        getInfo() {
            console.log('准备发送请求，参数:', this.form.username);
            request.get("http://localhost:9090/room/getMyRoom/" + this.form.username).then((res) => {
                console.log('后端返回的数据:', res);
                if (res.code === "0") {
                    this.room = res.data;
                    console.log('获取到的房间信息:', this.room);
                } else {
                    ElMessage({
                        message: res.msg || '获取宿舍信息失败',
                        type: "error",
                    });
                }
            }).catch(error => {
                console.error('请求出错:', error);
                ElMessage({
                    message: '请求失败，请检查网络连接',
                    type: "error",
                });
            });
        },
    },
};