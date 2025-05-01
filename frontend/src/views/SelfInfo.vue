<template>
  <div>
    <el-breadcrumb separator-icon="ArrowRight" style="margin: 16px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>个人信息</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card style="margin: 15px; min-height: calc(100vh - 111px)">
      <div style="display: flex">
        <div style="width: 600px; margin-left: 30px; position: relative">
          <div style="margin-bottom: 20px">
            <div class="avatar-container">
              <div class="AvatarDiv" @click.stop="showPreview">
                <el-avatar 
                  :size="80" 
                  :src="avatarUrl"
                  v-if="avatarUrl"
                ></el-avatar>
                <el-avatar 
                  :size="80" 
                  icon="UserFilled" 
                  v-else
                ></el-avatar>
              </div>
              <el-upload 
                :on-success="uploadSuccess" 
                :on-error="uploadError"
                :before-upload="handleUploadChange"
                :show-file-list="false"
                action="http://localhost:9090/files/upload"
                class="upload-demo"
                name="file"
              >
                <div class="edit-avatar-btn">
                  <el-button type="primary" size="small" plain>
                    <el-icon><edit /></el-icon>
                    更换头像
                  </el-button>
                </div>
              </el-upload>
            </div>
          </div>
          <el-descriptions :column="1" :size="large" border style="min-width: 500px" title="">
            <el-descriptions-item>
              <template #label>
                <div>
                  <el-icon>
                    <user/>
                  </el-icon>
                  用户名
                </div>
              </template>
              {{ form.username }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div>
                  <el-icon>
                    <location/>
                  </el-icon>
                  姓名
                </div>
              </template>
              {{ form.name }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div>
                  <el-icon>
                    <tickets/>
                  </el-icon>
                  性别
                </div>
              </template>
              {{ form.gender }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div>
                  <el-icon>
                    <office-building/>
                  </el-icon>
                  年龄
                </div>
              </template>
              {{ form.age }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div>
                  <el-icon>
                    <iphone/>
                  </el-icon>
                  手机号
                </div>
              </template>
              {{ form.phoneNum }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div>
                  <el-icon>
                    <office-building/>
                  </el-icon>
                  邮箱
                </div>
              </template>
              {{ form.email }}
            </el-descriptions-item>
          </el-descriptions>
          <el-tooltip content="修改信息" placement="bottom">
            <el-button icon="Edit" size="large" style="margin-top: 30px; width: 80px" type="primary" @click="Edit">
            </el-button>
          </el-tooltip>
        </div>
        <div style="margin-left: 130px">
          <img alt="" src="../../public/self_Space.png"/>
        </div>
      </div>
      <!-- 头像预览弹窗 -->
      <el-dialog
        v-model="previewVisible"
        title="头像预览"
        width="30%"
        :show-close="false"
        :close-on-click-modal="false"
        :close-on-press-escape="false"
        :destroy-on-close="true"
        :append-to-body="true"
      >
        <div class="preview-container">
          <img :src="avatarUrl" class="preview-image" v-if="avatarUrl">
          <el-avatar :size="200" icon="UserFilled" v-else></el-avatar>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="previewVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>
      <!-- 修改信息弹窗 -->
        <el-dialog 
          v-model="dialogVisible" 
          title="操作" 
          width="30%" 
          @close="cancel"
          :destroy-on-close="true"
          :append-to-body="true"
        >
        <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
            <el-form-item label="账号" prop="username">
              <el-input v-model="form.username" disabled style="width: 80%"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="form.password" :disabled="disabled" :show-password="showpassword" show-password
                        style="width: 80%"></el-input>
              <el-tooltip content="修改密码" placement="right">
                <el-icon size="large" style="margin-left: 5px; cursor: pointer" @click="EditPass">
                  <edit/>
                </el-icon>
              </el-tooltip>
            </el-form-item>
            <el-form-item :style="display" label="确认密码" prop="checkPass">
              <el-input v-model="form.checkPass" show-password style="width: 80%"></el-input>
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" style="width: 80%"></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="gender">
              <el-radio v-model="form.gender" label="男">男</el-radio>
              <el-radio v-model="form.gender" label="女">女</el-radio>
            </el-form-item>
            <el-form-item label="年龄" prop="age">
              <el-input v-model.number="form.age" style="width: 80%"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phoneNum">
              <el-input v-model.number="form.phoneNum" style="width: 80%"></el-input>
            </el-form-item>
            <el-form-item label="邮箱地址" prop="email">
              <el-input v-model="form.email" style="width: 80%"></el-input>
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="cancel">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </span>
          </template>
        </el-dialog>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import { useRoute, useRouter } from 'vue-router'

export default {
  name: 'SelfInfo',
  setup() {
    const router = useRouter()
    const formRef = ref(null)
    const avatarUrl = ref('')
    const userType = ref('')
    const userId = ref('')
    const previewVisible = ref(false)
    const dialogVisible = ref(false)
    const disabled = ref(true)
    const showpassword = ref(true)
    const display = ref('display: none')

    const form = ref({
      username: '',
      password: '********',  // 默认显示星号
      checkPass: '',
      name: '',
      gender: '',
      age: '',
      phoneNum: '',
      email: ''
    })

    const rules = {
      username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
      password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
      checkPass: [{ 
        required: true, 
        message: '请确认密码', 
        trigger: 'blur',
        validator: (rule, value, callback) => {
          if (!disabled.value) {
            if (!value) {
              callback(new Error('请确认密码'))
            } else if (value !== form.value.password) {
              callback(new Error('两次输入的密码不一致'))
            } else {
              callback()
            }
          } else {
            callback()
          }
        }
      }],
      name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
      gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
      age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
      phoneNum: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
      email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }]
    }

    // 获取用户信息
    const getUserInfo = async () => {
      try {
        let res
        console.log('当前用户类型:', userType.value)
        
        // 从 sessionStorage 获取用户信息
        const userInfo = JSON.parse(sessionStorage.getItem('access-user'))
        if (!userInfo) {
          ElMessage.error('未获取到用户信息，请重新登录')
          router.push('/login')
          return
        }
        
        if (userType.value === 'student' || userType.value === 'stu') {
          console.log('正在请求学生信息...')
          res = await request.post('/stu/login', userInfo)
        } else if (userType.value === 'admin') {
          console.log('正在请求管理员信息...')
          res = await request.post('/admin/login', userInfo)
        } else if (userType.value === 'dormmanager' || userType.value === 'dormManager') {
          console.log('正在请求宿管信息...')
          res = await request.post('/dormManager/login', userInfo)
        } else {
          console.error('未知的用户类型:', userType.value)
          ElMessage.error('用户类型错误')
          return
        }
        
        console.log('获取用户信息响应:', res)
        
        if (res.code === '0') {
          // 更新sessionStorage
          window.sessionStorage.setItem('access-user', JSON.stringify(res.data))
          // 更新表单数据
          form.value = {
            ...form.value,
            username: res.data.username,
            name: res.data.name,
            gender: res.data.gender,
            age: res.data.age,
            phoneNum: res.data.phoneNum,
            email: res.data.email
          }
          if (res.data.avatar) {
            getAvatar(res.data.avatar)
          }
        } else {
          ElMessage.error(res.msg || '获取用户信息失败')
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
        if (error.response) {
          console.error('错误响应:', error.response)
          ElMessage.error(`服务器错误: ${error.response.status}`)
        } else if (error.request) {
          console.error('请求未收到响应:', error.request)
          ElMessage.error('网络连接失败，请检查网络设置')
        } else {
          console.error('请求配置错误:', error.message)
          ElMessage.error('请求配置错误')
        }
      }
    }

    // 获取头像
    const getAvatar = async (filename) => {
      try {
        console.log('正在获取头像:', filename)
        const res = await request.get(`http://localhost:9090/files/initAvatar/${filename}`)
        console.log('获取头像响应:', res)
        if (res.code === '0') {
          // 确保res.data是字符串类型
          if (typeof res.data === 'string') {
            avatarUrl.value = `data:image;base64,${res.data}`
          } else if (res.data && res.data.data) {
            avatarUrl.value = `data:image;base64,${res.data.data}`
          } else {
            console.error('头像数据格式不正确:', res.data)
            ElMessage.error('头像数据格式不正确')
          }
        } else {
          ElMessage.error('获取头像失败')
        }
      } catch (error) {
        console.error('获取头像失败:', error)
        ElMessage.error('获取头像失败，请检查网络连接')
      }
    }

    // 显示头像预览
    const showPreview = (e) => {
      e.preventDefault()
      if (avatarUrl.value) {
        previewVisible.value = true
      }
    }

    // 处理上传文件变化
    const handleUploadChange = (file) => {
      if (file) {
        const isImage = file.type.startsWith('image/')
        if (!isImage) {
          ElMessage.error('请上传图片文件')
          return false
        }
        const isLt10M = file.size / 1024 / 1024 < 10
        if (!isLt10M) {
          ElMessage.error('图片大小不能超过 10MB')
          return false
        }
        return true
      }
    }

    // 上传成功后的处理
    const uploadSuccess = async (response) => {
      try {
        console.log('上传响应:', response)
        if (response.code === '0') {
          // 更新用户对象的avatar字段
          const userInfo = JSON.parse(sessionStorage.getItem('access-user'))
          userInfo.avatar = response.data
          sessionStorage.setItem('access-user', JSON.stringify(userInfo))

          let res
          if (userType.value === 'student') {
            res = await request.post('/files/uploadAvatar/stu', userInfo)
          } else if (userType.value === 'admin') {
            res = await request.post('/files/uploadAvatar/admin', userInfo)
          } else if (userType.value === 'dormManager') {
            res = await request.post('/files/uploadAvatar/dormManager', userInfo)
          }

          if (res.code === '0') {
            ElMessage.success('头像更新成功')
            getAvatar(res.data)
          } else {
            ElMessage.error(res.msg || '头像更新失败')
          }
        } else {
          ElMessage.error(response.msg || '文件上传失败')
        }
      } catch (error) {
        console.error('更新头像失败:', error)
        ElMessage.error('头像更新失败')
      }
    }

    // 上传失败处理
    const uploadError = (error) => {
      console.error('上传失败:', error)
      ElMessage.error('文件上传失败')
    }

    // 编辑信息
    const Edit = () => {
      dialogVisible.value = true
    }

    // 取消编辑
    const cancel = () => {
      dialogVisible.value = false
      form.value.password = '********'  // 恢复为星号
      form.value.checkPass = ''  // 清空确认密码
      disabled.value = true
      showpassword.value = true
      display.value = 'display: none'
      getUserInfo() // 重新获取用户信息，恢复原始数据
    }

    // 修改密码
    const EditPass = () => {
      if (disabled.value) {
        form.value.password = ''  // 清空密码，让用户输入新密码
        form.value.checkPass = ''  // 清空确认密码
        disabled.value = false
        showpassword.value = false
        display.value = 'display: block'
      } else {
        form.value.password = '********'  // 恢复为星号
        form.value.checkPass = ''  // 清空确认密码
        disabled.value = true
        showpassword.value = true
        display.value = 'display: none'
      }
    }

    // 保存修改
    const save = async () => {
      if (!formRef.value) return
      
      await formRef.value.validate(async (valid) => {
        if (valid) {
          try {
            // 获取原始用户信息
            const userInfo = JSON.parse(sessionStorage.getItem('access-user'))
            
            // 创建要提交的表单数据
            const submitForm = { ...form.value }
            
            // 如果密码未修改（显示为星号），则不提交密码字段
            if (submitForm.password === '********') {
              delete submitForm.password
            }
            // 删除确认密码字段，因为后端不需要
            delete submitForm.checkPass
            
            let res
            if (userType.value === 'student') {
              res = await request.put('/stu/update', submitForm)
            } else if (userType.value === 'admin') {
              res = await request.put('/admin/update', submitForm)
            } else if (userType.value === 'dormManager') {
              res = await request.put('/dormManager/update', submitForm)
            }

            if (res.code === '0') {
              ElMessage.success('修改成功')
              dialogVisible.value = false
              getUserInfo() // 重新获取用户信息
            } else {
              ElMessage.error(res.msg || '修改失败')
            }
          } catch (error) {
            console.error('修改失败:', error)
            ElMessage.error('修改失败，请检查网络连接')
          }
        }
      })
    }

    onMounted(() => {
      // 从sessionStorage获取用户类型
      const identity = sessionStorage.getItem('identity')
      console.log('从sessionStorage获取的原始identity:', identity)
      
      userType.value = JSON.parse(identity)
      console.log('解析后的用户类型:', userType.value)
      
      // 添加错误处理
      const originalErrorHandler = window.onerror
      window.onerror = function (message, source, lineno, colno, error) {
        if (message.toString().includes('ResizeObserver')) {
          return true
        }
        if (originalErrorHandler) {
          return originalErrorHandler(message, source, lineno, colno, error)
        }
        return false
      }

      if (!userType.value) {
        ElMessage.error('未获取到用户类型，请重新登录')
        router.push('/login')
        return
      }
      
      // 确保用户类型是小写
      userType.value = userType.value.toLowerCase()
      console.log('转换为小写后的用户类型:', userType.value)
      
      // 将 'stu' 转换为 'student'
      if (userType.value === 'stu') {
        userType.value = 'student'
      }
      // 将 'dorm' 或 'dormmanager' 转换为 'dormManager'
      if (userType.value === 'dorm' || userType.value === 'dormmanager') {
        userType.value = 'dormManager'
      }
      console.log('最终使用的用户类型:', userType.value)
      
      // 获取用户信息
      const userInfo = JSON.parse(sessionStorage.getItem('access-user'))
      console.log('从sessionStorage获取的用户信息:', userInfo)
      
      if (userInfo) {
        form.value = {
          ...form.value,
          username: userInfo.username,
          name: userInfo.name,
          gender: userInfo.gender,
          age: userInfo.age,
          phoneNum: userInfo.phoneNum,
          email: userInfo.email
        }
        if (userInfo.avatar) {
          getAvatar(userInfo.avatar)
        }
      }
      
      getUserInfo()
    })

    return {
      form,
      formRef,
      rules,
      avatarUrl,
      previewVisible,
      dialogVisible,
      disabled,
      showpassword,
      display,
      handleUploadChange,
      uploadSuccess,
      uploadError,
      showPreview,
      Edit,
      cancel,
      EditPass,
      save
    }
  }
}
</script>

<style scoped>
.avatar-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.AvatarDiv {
  position: relative;
  width: 80px;
  height: 80px;
  cursor: pointer;
  transition: transform 0.3s;
}

.AvatarDiv:hover {
  transform: scale(1.05);
}

.edit-avatar-btn {
  text-align: center;
}

.edit-avatar-btn .el-button {
  width: 100px;
}

.preview-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.preview-image {
  max-width: 100%;
  max-height: 400px;
  object-fit: contain;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.el-dialog {
  overflow: hidden;
}
</style>