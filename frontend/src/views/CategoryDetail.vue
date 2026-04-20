<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import ArtworkModal from '../components/ArtworkModal.vue'
import { useSiteData } from '../composables/useSiteData'

const route = useRoute()
const router = useRouter()
const { settings, categories, loadSettings, loadCategories, loadCategoryItems } = useSiteData()

const loading = ref(true)
const errorMessage = ref('')
const items = ref([])
const selectedItem = ref(null)
const showModal = ref(false)

const categoryId = computed(() => Number(route.params.id))
const currentCategory = computed(() => {
  return categories.value.find((item) => Number(item.id) === categoryId.value) || null
})

const backgroundStyle = computed(() => {
  const bgUrl = settings.value?.bgImageUrl
  if (bgUrl) {
    return {
      backgroundImage: `linear-gradient(180deg, rgba(2, 6, 23, 0.5), rgba(2, 6, 23, 0.95)), url(${bgUrl})`
    }
  }
  return {
    backgroundImage:
      'radial-gradient(circle at top left, rgba(56, 189, 248, 0.16), transparent 30%), radial-gradient(circle at bottom right, rgba(14, 165, 233, 0.16), transparent 26%), linear-gradient(135deg, #020617, #091223 55%, #030712)'
  }
})

function openItem(item) {
  selectedItem.value = item
  showModal.value = true
}

function closeModal() {
  showModal.value = false
}

async function loadPage() {
  loading.value = true
  errorMessage.value = ''
  try {
    await Promise.all([loadSettings(), loadCategories()])
    items.value = await loadCategoryItems(categoryId.value)
  } catch (error) {
    errorMessage.value = error?.message || '分类作品加载失败'
  } finally {
    loading.value = false
  }
}

onMounted(loadPage)

watch(
  () => route.params.id,
  () => {
    loadPage()
    closeModal()
  }
)
</script>

<template>
  <main class="relative min-h-screen overflow-hidden" :style="backgroundStyle">
    <div class="absolute inset-0 bg-[radial-gradient(circle_at_20%_15%,rgba(56,189,248,0.12),transparent_28%),radial-gradient(circle_at_80%_25%,rgba(59,130,246,0.12),transparent_24%),linear-gradient(180deg,rgba(2,6,23,0.2),rgba(2,6,23,0.9))]"></div>

    <div class="relative mx-auto min-h-screen w-full max-w-7xl px-4 py-5 sm:px-6 lg:px-8 lg:py-8">
      <section class="glass-panel rounded-3xl p-5 sm:p-6">
        <div class="flex flex-col gap-4 md:flex-row md:items-end md:justify-between">
          <div>
            <button
              class="inline-flex items-center gap-2 rounded-full border border-white/10 bg-white/5 px-3 py-1 text-xs text-slate-200 transition hover:bg-white/10"
              @click="router.push({ name: 'home' })"
            >
              <span>←</span>
              <span>返回首页</span>
            </button>
            <h1 class="mt-3 text-2xl font-semibold text-white sm:text-3xl">
              {{ currentCategory?.title || `分类 #${categoryId}` }}
            </h1>
            <p class="mt-2 text-sm text-slate-300">
              点击任意作品缩略图，查看居中放大的详情弹窗。
            </p>
          </div>
          <div class="rounded-2xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-slate-300">
            共 {{ items.length }} 个作品
          </div>
        </div>

        <div v-if="loading" class="mt-6 grid gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
          <div
            v-for="n in 8"
            :key="n"
            class="h-48 animate-pulse rounded-3xl border border-white/10 bg-white/5"
          ></div>
        </div>

        <div v-else-if="errorMessage" class="mt-6 rounded-3xl border border-rose-400/20 bg-rose-400/10 p-4 text-sm text-rose-100">
          {{ errorMessage }}
        </div>

        <div
          v-else-if="items.length === 0"
          class="mt-6 rounded-3xl border border-white/10 bg-white/5 p-10 text-center text-sm text-slate-300"
        >
          该分类下暂无作品
        </div>

        <div v-else class="mt-6 grid gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
          <button
            v-for="item in items"
            :key="item.id"
            class="group overflow-hidden rounded-3xl border border-white/10 bg-white/5 text-left transition duration-300 hover:-translate-y-1 hover:border-cyan-300/40 hover:bg-white/[0.08]"
            @click="openItem(item)"
          >
            <div class="relative aspect-[4/3] overflow-hidden">
              <img
                :src="item.mediaUrl"
                :alt="item.description || '作品图片'"
                class="h-full w-full object-cover transition duration-500 group-hover:scale-105"
              />
              <div class="absolute inset-0 bg-gradient-to-t from-slate-950/90 via-slate-950/10 to-transparent opacity-0 transition duration-300 group-hover:opacity-100"></div>
            </div>
            <div class="p-4">
              <p class="max-h-12 overflow-hidden text-sm leading-6 text-slate-200">
                {{ item.description || '暂无简介' }}
              </p>
            </div>
          </button>
        </div>
      </section>

      <ArtworkModal :show="showModal" :item="selectedItem" @close="closeModal" />
    </div>
  </main>
</template>
