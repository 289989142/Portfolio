<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import {
  createAdminCategory,
  createAdminItem,
  deleteAdminCategory,
  deleteAdminItem,
  fetchAdminCategories,
  fetchAdminItems,
  fetchAdminSettings,
  updateAdminCategory,
  updateAdminItem,
  updateAdminSettings
} from '../../api/admin'
import FileUploadField from '../../components/admin/FileUploadField.vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const activeTab = ref('settings')
const loading = ref(false)
const savingSettings = ref(false)
const categoryDialogVisible = ref(false)
const itemDialogVisible = ref(false)
const categorySaving = ref(false)
const itemSaving = ref(false)

const settingsForm = reactive({
  adminPassword: '',
  bioContent: '',
  resumeUrl: '',
  phone: '',
  wechatQrUrl: '',
  bgImageUrl: '',
  bgMusicUrl: ''
})

const categories = ref([])
const items = ref([])
const categoryForm = reactive({
  id: null,
  title: '',
  coverImageUrl: '',
  sortOrder: 0
})
const itemForm = reactive({
  id: null,
  categoryId: null,
  mediaUrl: '',
  description: '',
  sortOrder: 0
})

const selectedCategoryId = ref(null)

const selectedCategory = computed(() => {
  return categories.value.find((item) => item.id === selectedCategoryId.value) || null
})

async function loadSettings() {
  const data = await fetchAdminSettings()
  Object.assign(settingsForm, {
    adminPassword: '',
    bioContent: data?.bioContent || '',
    resumeUrl: data?.resumeUrl || '',
    phone: data?.phone || '',
    wechatQrUrl: data?.wechatQrUrl || '',
    bgImageUrl: data?.bgImageUrl || '',
    bgMusicUrl: data?.bgMusicUrl || ''
  })
}

async function loadCategories() {
  categories.value = await fetchAdminCategories()
  if (categories.value.length === 0) {
    selectedCategoryId.value = null
    items.value = []
    return
  }
  if (
    !selectedCategoryId.value ||
    !categories.value.some((item) => item.id === selectedCategoryId.value)
  ) {
    selectedCategoryId.value = categories.value[0].id
  }
}

async function loadItems() {
  if (!selectedCategoryId.value) {
    items.value = []
    return
  }
  items.value = await fetchAdminItems(selectedCategoryId.value)
}

async function loadAll() {
  loading.value = true
  try {
    await Promise.all([loadSettings(), loadCategories()])
    await loadItems()
  } catch (error) {
    ElMessage.error(error?.message || '加载失败')
  } finally {
    loading.value = false
  }
}

async function saveSettings() {
  savingSettings.value = true
  try {
    await updateAdminSettings({ ...settingsForm })
    ElMessage.success('基础设置已保存')
  } catch (error) {
    ElMessage.error(error?.message || '保存失败')
  } finally {
    savingSettings.value = false
  }
}

function openCreateCategory() {
  Object.assign(categoryForm, {
    id: null,
    title: '',
    coverImageUrl: '',
    sortOrder: 0
  })
  categoryDialogVisible.value = true
}

function openEditCategory(row) {
  Object.assign(categoryForm, { ...row })
  categoryDialogVisible.value = true
}

async function submitCategory() {
  if (!categoryForm.title) {
    ElMessage.warning('请输入分类名称')
    return
  }
  categorySaving.value = true
  try {
    if (categoryForm.id) {
      await updateAdminCategory({ ...categoryForm })
      ElMessage.success('分类已更新')
    } else {
      await createAdminCategory({ ...categoryForm })
      ElMessage.success('分类已创建')
    }
    categoryDialogVisible.value = false
    await loadCategories()
  } catch (error) {
    ElMessage.error(error?.message || '操作失败')
  } finally {
    categorySaving.value = false
  }
}

async function removeCategory(row) {
  await ElMessageBox.confirm(`确认删除分类「${row.title}」吗？`, '提示', {
    type: 'warning'
  })
  await deleteAdminCategory(row.id)
  ElMessage.success('分类已删除')
  await loadCategories()
  await loadItems()
}

function openCreateItem() {
  Object.assign(itemForm, {
    id: null,
    categoryId: selectedCategoryId.value,
    mediaUrl: '',
    description: '',
    sortOrder: 0
  })
  itemDialogVisible.value = true
}

function openEditItem(row) {
  Object.assign(itemForm, { ...row })
  itemDialogVisible.value = true
}

async function submitItem() {
  if (!itemForm.categoryId) {
    ElMessage.warning('请选择作品集')
    return
  }
  if (!itemForm.mediaUrl) {
    ElMessage.warning('请先上传作品图片')
    return
  }
  itemSaving.value = true
  try {
    if (itemForm.id) {
      await updateAdminItem({ ...itemForm })
      ElMessage.success('作品已更新')
    } else {
      await createAdminItem({ ...itemForm })
      ElMessage.success('作品已创建')
    }
    itemDialogVisible.value = false
    await loadItems()
  } catch (error) {
    ElMessage.error(error?.message || '操作失败')
  } finally {
    itemSaving.value = false
  }
}

async function removeItem(row) {
  await ElMessageBox.confirm('确认删除该作品吗？', '提示', {
    type: 'warning'
  })
  await deleteAdminItem(row.id)
  ElMessage.success('作品已删除')
  await loadItems()
}

function logout() {
  localStorage.removeItem('portfolio_admin_logged_in')
  router.push({ name: 'admin-login' })
}

watch(selectedCategoryId, async () => {
  await loadItems()
})

onMounted(loadAll)
</script>

<template>
  <div class="min-h-screen bg-slate-100 p-4 text-slate-900">
    <div class="mx-auto max-w-7xl">
      <div class="mb-4 flex flex-col gap-3 rounded-2xl bg-white p-4 shadow-sm sm:flex-row sm:items-center sm:justify-between">
        <div>
          <p class="text-xs uppercase tracking-[0.3em] text-slate-500">Admin Panel</p>
          <h1 class="mt-1 text-2xl font-semibold">管理后台</h1>
        </div>
        <el-button @click="logout">退出登录</el-button>
      </div>

      <el-tabs v-model="activeTab" type="card">
        <el-tab-pane label="基础设置" name="settings">
          <el-card shadow="never">
            <el-form label-width="120px">
              <el-form-item label="后台密码">
                <el-input v-model="settingsForm.adminPassword" type="password" show-password placeholder="留空则不修改" />
              </el-form-item>
              <el-form-item label="个人简介">
                <el-input v-model="settingsForm.bioContent" type="textarea" :rows="5" placeholder="站点简介" />
              </el-form-item>
              <el-form-item label="联系电话">
                <el-input v-model="settingsForm.phone" placeholder="手机号或其他联系方式" />
              </el-form-item>
              <el-form-item label="简历地址">
                <el-input v-model="settingsForm.resumeUrl" placeholder="简历文件或网页地址" />
              </el-form-item>
              <el-form-item label="微信二维码">
                <FileUploadField
                  v-model="settingsForm.wechatQrUrl"
                  label="微信二维码"
                  tip="上传后会返回可访问的图片地址"
                  accept="image/*"
                  preview-type="image"
                  button-text="上传二维码"
                />
              </el-form-item>
              <el-form-item label="背景图">
                <FileUploadField
                  v-model="settingsForm.bgImageUrl"
                  label="背景图片"
                  tip="建议上传大图"
                  accept="image/*"
                  preview-type="image"
                  button-text="上传背景图"
                />
              </el-form-item>
              <el-form-item label="背景音乐">
                <FileUploadField
                  v-model="settingsForm.bgMusicUrl"
                  label="背景音乐"
                  tip="支持 mp3 / wav 等音频文件"
                  accept="audio/*"
                  preview-type="audio"
                  button-text="上传音乐"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :loading="savingSettings" @click="saveSettings">保存设置</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </el-tab-pane>

        <el-tab-pane label="作品集管理" name="categories">
          <el-card shadow="never">
            <div class="mb-4 flex items-center justify-between gap-3">
              <div class="text-sm text-slate-500">管理作品集分类</div>
              <el-button type="primary" @click="openCreateCategory">新增分类</el-button>
            </div>

            <el-table :data="categories" border style="width: 100%">
              <el-table-column prop="title" label="名称" min-width="180" />
              <el-table-column label="封面" min-width="160">
                <template #default="{ row }">
                  <img v-if="row.coverImageUrl" :src="row.coverImageUrl" class="h-14 w-24 rounded-lg object-cover" />
                  <span v-else class="text-slate-400">未设置</span>
                </template>
              </el-table-column>
              <el-table-column prop="sortOrder" label="排序" width="100" />
              <el-table-column label="操作" width="180" fixed="right">
                <template #default="{ row }">
                  <el-button link type="primary" @click="openEditCategory(row)">编辑</el-button>
                  <el-button link type="danger" @click="removeCategory(row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>

          <el-dialog v-model="categoryDialogVisible" :title="categoryForm.id ? '编辑分类' : '新增分类'" width="560px">
            <el-form label-width="100px">
              <el-form-item label="名称">
                <el-input v-model="categoryForm.title" placeholder="分类名称" />
              </el-form-item>
              <el-form-item label="排序">
                <el-input-number v-model="categoryForm.sortOrder" :min="0" />
              </el-form-item>
              <el-form-item label="封面图">
                <FileUploadField
                  v-model="categoryForm.coverImageUrl"
                  label="分类封面"
                  tip="上传分类封面图片"
                  accept="image/*"
                  preview-type="image"
                  button-text="上传封面"
                />
              </el-form-item>
            </el-form>
            <template #footer>
              <el-button @click="categoryDialogVisible = false">取消</el-button>
              <el-button type="primary" :loading="categorySaving" @click="submitCategory">确定</el-button>
            </template>
          </el-dialog>
        </el-tab-pane>

        <el-tab-pane label="作品管理" name="items">
          <el-card shadow="never">
            <div class="mb-4 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
              <div class="flex items-center gap-3">
                <span class="text-sm text-slate-500">选择作品集</span>
                <el-select v-model="selectedCategoryId" placeholder="请选择分类" style="width: 220px">
                  <el-option
                    v-for="category in categories"
                    :key="category.id"
                    :label="category.title"
                    :value="category.id"
                  />
                </el-select>
              </div>
              <el-button type="primary" :disabled="!selectedCategoryId" @click="openCreateItem">新增作品</el-button>
            </div>

            <el-table :data="items" border style="width: 100%">
              <el-table-column label="缩略图" width="150">
                <template #default="{ row }">
                  <img v-if="row.mediaUrl" :src="row.mediaUrl" class="h-16 w-24 rounded-lg object-cover" />
                </template>
              </el-table-column>
              <el-table-column prop="description" label="描述" min-width="260" />
              <el-table-column prop="sortOrder" label="排序" width="100" />
              <el-table-column label="操作" width="180" fixed="right">
                <template #default="{ row }">
                  <el-button link type="primary" @click="openEditItem(row)">编辑</el-button>
                  <el-button link type="danger" @click="removeItem(row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>

          <el-dialog v-model="itemDialogVisible" :title="itemForm.id ? '编辑作品' : '新增作品'" width="620px">
            <el-form label-width="100px">
              <el-form-item label="作品集">
                <el-select v-model="itemForm.categoryId" placeholder="请选择分类" style="width: 100%">
                  <el-option
                    v-for="category in categories"
                    :key="category.id"
                    :label="category.title"
                    :value="category.id"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="排序">
                <el-input-number v-model="itemForm.sortOrder" :min="0" />
              </el-form-item>
              <el-form-item label="作品描述">
                <el-input v-model="itemForm.description" type="textarea" :rows="4" placeholder="填写作品说明" />
              </el-form-item>
              <el-form-item label="作品图片">
                <FileUploadField
                  v-model="itemForm.mediaUrl"
                  label="作品图片"
                  tip="上传作品图片或展示图"
                  accept="image/*"
                  preview-type="image"
                  button-text="上传作品图"
                />
              </el-form-item>
            </el-form>
            <template #footer>
              <el-button @click="itemDialogVisible = false">取消</el-button>
              <el-button type="primary" :loading="itemSaving" @click="submitItem">确定</el-button>
            </template>
          </el-dialog>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
