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
      backgroundImage: `linear-gradient(135deg, rgba(235, 219, 190, 0.92), rgba(192, 164, 126, 0.78)), url(${bgUrl})`,
      backgroundSize: 'cover',
      backgroundPosition: 'center'
    }
  }
  return {
    backgroundImage:
      'radial-gradient(circle at 18% 18%, rgba(255, 255, 255, 0.72), transparent 18%), radial-gradient(circle at 82% 24%, rgba(255, 240, 210, 0.82), transparent 22%), radial-gradient(circle at 58% 80%, rgba(88, 68, 39, 0.16), transparent 20%), linear-gradient(135deg, #deccb0, #c4a47d 44%, #927c61 100%)'
  }
})

function openItem(item) {
  selectedItem.value = item
  showModal.value = true
}

function closeModal() {
  showModal.value = false
}

function itemBackgroundStyle(index) {
  const tones = [
    ['#6b7580', '#373f46', '#171b20'],
    ['#90866f', '#5c5345', '#2a241c'],
    ['#5f6770', '#3d474f', '#21262c'],
    ['#947c60', '#5c4f3d', '#2c2419'],
    ['#7b726c', '#474d53', '#1e2329'],
    ['#65716a', '#39413d', '#171c1a']
  ]
  const [start, mid, end] = tones[index % tones.length]
  return {
    backgroundImage: `linear-gradient(135deg, ${start}, ${mid} 52%, ${end})`
  }
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
    <div
      class="pointer-events-none absolute inset-0 bg-[radial-gradient(circle_at_22%_16%,rgba(255,255,255,0.52),transparent_18%),radial-gradient(circle_at_76%_24%,rgba(255,244,220,0.62),transparent_24%),radial-gradient(circle_at_55%_82%,rgba(92,73,46,0.15),transparent_20%)] opacity-90"
    ></div>
    <div class="pointer-events-none absolute inset-0 bg-[linear-gradient(90deg,rgba(255,255,255,0.12),transparent_35%,transparent_70%,rgba(89,69,38,0.12))]"></div>

    <div class="relative mx-auto min-h-screen w-full max-w-[1600px] px-4 py-4 sm:px-6 lg:px-8 lg:py-6">
      <section class="warm-shell p-4 sm:p-5 lg:p-6 xl:p-7">
        <div class="grid gap-6 xl:grid-cols-[minmax(260px,300px)_minmax(0,1fr)]">
          <aside class="relative flex min-h-[260px] flex-col justify-between overflow-hidden rounded-[1.8rem] border border-white/25 bg-[#f3e4c7]/46 p-5 text-[#2d2419]">
            <div class="pointer-events-none absolute inset-y-0 right-0 hidden items-center xl:flex">
              <span class="mirror-rail translate-x-1/2 text-[#ffffff]/20">FRAME</span>
            </div>

            <div>
              <button
                class="paper-chip !border-white/25 !bg-white/25 !text-[#2c2419]"
                @click="router.push({ name: 'home' })"
              >
                ← 返回首页
              </button>

              <h1 class="mt-4 text-3xl font-black leading-tight text-[#2d2419] sm:text-4xl">
                {{ currentCategory?.title || `分类 #${categoryId}` }}
              </h1>
            </div>

            <div class="space-y-3">
              <div class="paper-line bg-gradient-to-r from-transparent via-[#8a775f]/55 to-transparent"></div>
              <div class="grid grid-cols-3 gap-3 text-xs text-[#5b4a3a]">
                <div>
                  <p class="font-semibold">作品</p>
                  <p class="mt-1">{{ items.length }}</p>
                </div>
                <div>
                  <p class="font-semibold">背景</p>
                  <p class="mt-1">{{ settings?.bgImageUrl ? '已配置' : '默认' }}</p>
                </div>
                <div>
                  <p class="font-semibold">分类</p>
                  <p class="mt-1">{{ currentCategory?.title || '未命名' }}</p>
                </div>
              </div>
            </div>
          </aside>

          <div>
            <div class="flex flex-col gap-4 border-b border-white/20 pb-4 text-[#2d2419] md:flex-row md:items-end md:justify-between">
              <div>
                <h2 class="text-2xl font-semibold sm:text-3xl">作品缩略图</h2>
              </div>
              <div class="paper-chip self-start !border-white/25 !bg-white/25 !text-[#2c2419]">
                共 {{ items.length }} 个作品
              </div>
            </div>

            <div v-if="loading" class="mt-6 grid gap-5 sm:grid-cols-2 xl:grid-cols-3">
              <div
                v-for="n in 8"
                :key="n"
                class="h-56 animate-pulse rounded-[1.75rem] border border-white/20 bg-white/20"
              ></div>
            </div>

            <div
              v-else-if="errorMessage"
              class="mt-6 rounded-[1.75rem] border border-[#8c6d52]/20 bg-white/25 p-5 text-sm text-[#5c4735]"
            >
              {{ errorMessage }}
            </div>

            <div
              v-else-if="items.length === 0"
              class="mt-6 rounded-[1.75rem] border border-white/25 bg-white/20 p-10 text-center text-sm text-[#5a4a3c]"
            >
              该分类下暂无作品
            </div>

            <div v-else class="mt-6 grid auto-rows-[220px] gap-5 sm:grid-cols-2 xl:grid-cols-3">
              <button
                v-for="(item, index) in items"
                :key="item.id"
                class="film-card group relative flex h-full flex-col overflow-hidden transition duration-300 hover:-translate-y-1 hover:shadow-[0_24px_70px_rgba(27,20,12,0.32)]"
                :style="itemBackgroundStyle(index)"
                @click="openItem(item)"
              >
                <div class="relative flex-1 overflow-hidden">
                  <img
                    :src="item.mediaUrl"
                    :alt="item.description || '作品图片'"
                    class="h-full w-full object-contain p-4 opacity-90 transition duration-500 group-hover:scale-[1.02]"
                  />
                  <div
                    class="absolute inset-0 bg-[radial-gradient(circle_at_50%_35%,rgba(255,255,255,0.12),transparent_34%),linear-gradient(180deg,rgba(255,255,255,0.05),rgba(0,0,0,0.3))]"
                  ></div>
                  <div class="absolute left-4 top-4 text-white/20">
                    <p class="text-6xl font-black leading-none">{{ String(index + 1).padStart(2, '0') }}</p>
                  </div>
                  <div class="absolute right-4 top-4 rounded-full border border-white/20 bg-white/10 px-3 py-1 text-[0.62rem] font-semibold tracking-[0.36em] text-white/75">
                    OPEN
                  </div>
                </div>

                <div class="border-t border-white/12 bg-black/10 px-4 py-4 text-white">
                  <p class="film-label">Artwork</p>
                  <p class="mt-2 max-h-14 overflow-hidden text-sm leading-6 text-white/85">
                    {{ item.description || '暂无简介' }}
                  </p>
                  <div class="mt-4 flex items-center justify-between">
                    <span class="text-xs tracking-[0.32em] text-white/55">
                      {{ String(index + 1).padStart(2, '0') }}
                    </span>
                    <span class="text-sm text-white/70 transition group-hover:translate-x-1">→</span>
                  </div>
                </div>
              </button>
            </div>
          </div>
        </div>
      </section>

      <ArtworkModal :show="showModal" :item="selectedItem" @close="closeModal" />
    </div>
  </main>
</template>
