<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { adminLogin } from '../../api/admin'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const form = reactive({
  password: ''
})

async function submit() {
  if (!form.password) {
    ElMessage.warning('请输入密码')
    return
  }
  loading.value = true
  try {
    await adminLogin(form.password)
    localStorage.setItem('portfolio_admin_logged_in', 'true')
    ElMessage.success('登录成功')
    router.push({ name: 'admin-dashboard' })
  } catch (error) {
    ElMessage.error(error?.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="min-h-screen bg-slate-950 px-4 py-10 text-slate-100">
    <div class="mx-auto flex min-h-[calc(100vh-5rem)] max-w-md items-center">
      <div class="w-full rounded-3xl border border-white/10 bg-white/5 p-8 shadow-2xl backdrop-blur-xl">
        <div class="mb-8 text-center">
          <p class="text-xs uppercase tracking-[0.35em] text-cyan-200/80">Admin Login</p>
          <h1 class="mt-3 text-3xl font-semibold">管理后台登录</h1>
          <p class="mt-2 text-sm text-slate-300">请输入后台管理密码</p>
        </div>

        <el-form @submit.prevent>
          <el-form-item>
            <el-input
              v-model="form.password"
              type="password"
              size="large"
              placeholder="请输入密码"
              show-password
              @keyup.enter="submit"
            />
          </el-form-item>
          <el-button
            type="primary"
            size="large"
            class="w-full"
            :loading="loading"
            @click="submit"
          >
            登录
          </el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>
