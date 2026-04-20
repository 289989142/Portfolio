<script setup>
import { computed } from 'vue'
import { uploadAdminFile } from '../../api/admin'
import { ElMessage } from 'element-plus'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  label: {
    type: String,
    default: '文件上传'
  },
  tip: {
    type: String,
    default: ''
  },
  accept: {
    type: String,
    default: '*/*'
  },
  previewType: {
    type: String,
    default: 'image'
  },
  buttonText: {
    type: String,
    default: '选择文件'
  }
})

const emit = defineEmits(['update:modelValue'])

const previewAvailable = computed(() => Boolean(props.modelValue))

async function handleUpload(options) {
  try {
    const url = await uploadAdminFile(options.file)
    emit('update:modelValue', url)
    options.onSuccess?.({ url }, options.file)
    ElMessage.success('上传成功')
  } catch (error) {
    options.onError?.(error)
    ElMessage.error(error?.message || '上传失败')
  }
}
</script>

<template>
  <div class="space-y-3">
    <div class="flex items-center justify-between gap-4">
      <div>
        <p class="text-sm font-medium text-slate-800">{{ label }}</p>
        <p v-if="tip" class="text-xs text-slate-500">{{ tip }}</p>
      </div>
      <el-upload
        :accept="accept"
        :show-file-list="false"
        :http-request="handleUpload"
      >
        <el-button type="primary" plain>{{ buttonText }}</el-button>
      </el-upload>
    </div>

    <div v-if="previewAvailable" class="rounded-xl border border-slate-200 bg-white p-3">
      <img
        v-if="previewType === 'image'"
        :src="modelValue"
        class="max-h-44 w-full rounded-lg object-contain"
        alt="preview"
      />
      <audio
        v-else-if="previewType === 'audio'"
        :src="modelValue"
        controls
        class="w-full"
      ></audio>
      <div v-else class="break-all text-sm text-slate-600">
        {{ modelValue }}
      </div>
    </div>
  </div>
</template>
