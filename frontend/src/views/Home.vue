<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useSiteData } from '../composables/useSiteData'
import { ElMessage } from 'element-plus'
import { ChatDotRound, Document, Iphone } from '@element-plus/icons-vue'

const router = useRouter()
const { settings, categories, loadSettings, loadCategories } = useSiteData()
const loading = ref(true)
const errorMessage = ref('')

const featuredCategory = computed(() => categories.value[0] || null)
const avatarImage = computed(() => settings.value?.avatarUrl || featuredCategory.value?.coverImageUrl || '')
const resumeReady = computed(() => Boolean(settings.value?.resumeUrl))
const wechatText = computed(() => normalizeText(settings.value?.wechatAccount) || '未配置')

const bioSummary = computed(() => {
  const text = normalizeText(settings.value?.bioContent)
  if (!text) {
    return '这里放一段简短的个人介绍，建议保持克制、留白，像一张电影海报。'
  }
  return text.split('\n').find(Boolean) || text
})

const quickActions = computed(() => [
  {
    label: '简历',
    value: normalizeText(settings.value?.resumeUrl) || '未配置',
    copyText: normalizeText(settings.value?.resumeUrl),
    disabled: !resumeReady.value,
    icon: Document
  },
  {
    label: '微信',
    value: wechatText.value,
    copyText: normalizeText(settings.value?.wechatAccount),
    disabled: !normalizeText(settings.value?.wechatAccount),
    icon: ChatDotRound
  },
  {
    label: '电话',
    value: normalizeText(settings.value?.phone) || '暂无',
    copyText: normalizeText(settings.value?.phone),
    disabled: !normalizeText(settings.value?.phone),
    icon: Iphone
  }
])

const backgroundStyle = computed(() => {
  const bgUrl = settings.value?.bgImageUrl
  if (bgUrl) {
    return {
      backgroundImage: `linear-gradient(135deg, rgba(235, 219, 190, 0.88), rgba(197, 171, 130, 0.72)), url(${bgUrl})`,
      backgroundSize: 'cover',
      backgroundPosition: 'center'
    }
  }
  return {
    backgroundImage:
      'radial-gradient(circle at 18% 18%, rgba(255, 255, 255, 0.78), transparent 18%), radial-gradient(circle at 82% 20%, rgba(255, 238, 205, 0.9), transparent 22%), radial-gradient(circle at 55% 82%, rgba(129, 97, 55, 0.2), transparent 20%), linear-gradient(135deg, #dfccb0, #c1a37c 42%, #8f7a61 100%)'
  }
})

function normalizeText(text) {
  return text ? String(text).trim() : ''
}

function openCategory(categoryId) {
  router.push({ name: 'category-detail', params: { id: categoryId } })
}

function openFeaturedCategory() {
  if (featuredCategory.value) {
    openCategory(featuredCategory.value.id)
  }
}

function cardToneStyle(index) {
  const tones = [
    ['rgba(84, 82, 76, 0.96)', 'rgba(38, 44, 54, 0.96)'],
    ['rgba(110, 98, 80, 0.96)', 'rgba(64, 55, 47, 0.96)'],
    ['rgba(94, 94, 93, 0.96)', 'rgba(45, 51, 58, 0.96)'],
    ['rgba(123, 109, 90, 0.96)', 'rgba(67, 59, 49, 0.96)'],
    ['rgba(101, 101, 104, 0.96)', 'rgba(48, 54, 61, 0.96)'],
    ['rgba(88, 95, 90, 0.96)', 'rgba(40, 46, 42, 0.96)']
  ]
  const [start, end] = tones[index % tones.length]
  return {
    backgroundImage: `linear-gradient(135deg, ${start}, ${end})`
  }
}

async function copyValue(item) {
  if (!item?.copyText) {
    ElMessage.warning('内容尚未配置')
    return
  }
  try {
    await navigator.clipboard.writeText(item.copyText)
    ElMessage.success(`${item.label}已复制`)
  } catch (error) {
    ElMessage.error('复制失败，请手动复制')
  }
}

onMounted(async () => {
  loading.value = true
  errorMessage.value = ''
  try {
    await Promise.all([loadSettings(), loadCategories()])
  } catch (error) {
    errorMessage.value = error?.message || '数据加载失败'
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <main class="relative min-h-screen overflow-x-hidden overflow-y-auto" :style="backgroundStyle">
    <div
      class="pointer-events-none absolute inset-0 bg-[radial-gradient(circle_at_25%_20%,rgba(255,255,255,0.48),transparent_18%),radial-gradient(circle_at_70%_26%,rgba(255,241,214,0.55),transparent_24%),radial-gradient(circle_at_50%_78%,rgba(95,73,41,0.16),transparent_22%)] opacity-80"
    ></div>
    <div class="pointer-events-none absolute inset-0 bg-[linear-gradient(90deg,rgba(255,255,255,0.12),transparent_30%,transparent_70%,rgba(86,68,39,0.14))]"></div>

    <div class="relative mx-auto flex min-h-screen w-full max-w-[1600px] flex-col gap-5 px-4 py-4 pb-28 sm:px-6 lg:min-h-[calc(100vh-5.5rem)] lg:px-8 lg:py-5">
      <header class="flex items-center justify-between gap-4 px-1 py-1 text-[#3c3125]">
        <h1 class="text-xl font-semibold text-[#2f261c] sm:text-2xl">作品集展示</h1>
        <button
          class="rounded-full border border-white/35 bg-white/20 px-4 py-2 text-xs font-medium text-[#35291e] backdrop-blur-md transition hover:bg-white/30"
          :disabled="!featuredCategory"
          @click="openFeaturedCategory"
        >
          进入首个分区
        </button>
      </header>

      <section class="grid flex-1 gap-5 lg:grid-cols-[minmax(280px,320px)_minmax(0,1fr)]">
        <aside class="warm-shell flex min-h-[520px] flex-col gap-5 p-5 text-[#2f251a] sm:p-6">
          <div class="rounded-[1.9rem] border border-white/35 bg-[#f6efe1]/78 p-3 shadow-[0_28px_70px_rgba(74,54,24,0.18)]">
            <div class="relative overflow-hidden rounded-[1.65rem] bg-[#5b554c]">
              <div class="relative aspect-[4/5] overflow-hidden">
                <img
                  v-if="avatarImage"
                  :src="avatarImage"
                  alt="头像"
                  class="h-full w-full object-cover object-center"
                />
                <div
                  v-else
                  class="flex h-full w-full items-center justify-center bg-[radial-gradient(circle_at_50%_36%,rgba(255,255,255,0.18),transparent_28%),linear-gradient(180deg,rgba(255,255,255,0.12),rgba(0,0,0,0.28))]"
                >
                  <span class="text-4xl font-black tracking-[0.35em] text-white/80">PF</span>
                </div>
                <div class="absolute inset-0 bg-[radial-gradient(circle_at_50%_30%,rgba(255,255,255,0.14),transparent_26%),linear-gradient(180deg,rgba(255,255,255,0.06),rgba(0,0,0,0.28))]"></div>
              </div>
            </div>
          </div>

          <div class="space-y-3">
            <h2 class="text-4xl font-black leading-none text-[#2e241a] sm:text-5xl">作品集</h2>
            <p class="text-xl font-semibold text-[#3b3024]">
              {{ normalizeText(settings?.bioContent) ? '作品集展示' : '个人作品集' }}
            </p>

            <p class="max-w-[28rem] text-sm leading-7 text-[#4d4234]">
              {{ bioSummary }}
            </p>

            <div class="grid grid-cols-3 gap-4">
              <button
                v-for="item in quickActions"
                :key="item.label"
                class="group flex flex-col items-center gap-2 rounded-[1rem] px-1 py-1 text-[#2f2419] transition duration-200 hover:-translate-y-0.5"
                :class="{ 'pointer-events-none opacity-55': item.disabled }"
                @click="copyValue(item)"
              >
                <span class="text-[0.72rem] font-semibold tracking-[0.42em] text-[#5f4f3d]">
                  {{ item.label }}
                </span>
                <span
                  class="flex h-11 w-11 items-center justify-center rounded-full border border-white/45 bg-white/28 text-[#3a2f23] shadow-[0_10px_24px_rgba(78,58,33,0.1)] backdrop-blur-md transition duration-200 group-hover:bg-white/40"
                >
                  <el-icon :size="18">
                    <component :is="item.icon" />
                  </el-icon>
                </span>
                <p class="max-w-full truncate text-[0.7rem] leading-none text-[#4e4132] opacity-0 transition duration-200 group-hover:opacity-100">
                  {{ item.value }}
                </p>
              </button>
            </div>

            <button
              class="paper-chip w-full justify-center !bg-[#f4ecde]/70 !text-[#2d2217]"
              :disabled="!featuredCategory"
              @click="openFeaturedCategory"
            >
              进入首个分类
            </button>
          </div>
        </aside>

        <section class="warm-shell flex min-h-[520px] flex-col gap-4 p-4 sm:p-5 lg:p-6">
          <div class="flex items-end justify-between gap-4 rounded-[1.4rem] border border-white/20 bg-[#f4e5c9]/28 px-5 py-4 text-[#2d241a]">
            <h3 class="text-2xl font-semibold text-[#2c2419]">
              {{ featuredCategory?.title || '作品分类' }}
            </h3>
          </div>

          <div class="flex flex-1 flex-col gap-4 overflow-hidden">
            <template v-if="loading">
              <div class="flex gap-4 overflow-hidden">
                <div
                  v-for="n in 6"
                  :key="n"
                  class="h-[220px] w-[190px] shrink-0 animate-pulse rounded-[1.6rem] border border-white/20 bg-white/15 sm:w-[210px] lg:w-[230px]"
                ></div>
              </div>
            </template>

            <div
              v-else-if="errorMessage"
              class="rounded-[1.5rem] border border-[#8c6d52]/20 bg-white/25 p-5 text-sm text-[#5c4735]"
            >
              {{ errorMessage }}
            </div>

            <template v-else>
              <div class="overflow-x-auto pb-2">
                <div class="flex min-h-[220px] gap-4">
                  <button
                    v-for="(category, index) in categories"
                    :key="category.id"
                    class="group relative shrink-0 overflow-hidden rounded-[1.7rem] border border-white/25 text-left shadow-[0_18px_60px_rgba(38,26,14,0.18)] transition duration-300 hover:-translate-y-1 hover:shadow-[0_26px_70px_rgba(27,20,12,0.26)]"
                    :style="[cardToneStyle(index), { width: 'clamp(190px, 18vw, 240px)' }]"
                    @click="openCategory(category.id)"
                  >
                    <div class="relative flex h-[220px] flex-col p-3">
                      <div class="flex-1 overflow-hidden rounded-[1.25rem] border border-white/20 bg-[#efe5d5]/70">
                        <img
                          v-if="category.coverImageUrl"
                          :src="category.coverImageUrl"
                          :alt="category.title"
                          class="h-full w-full object-contain p-3 transition duration-500 group-hover:scale-[1.03]"
                        />
                        <div
                          v-else
                          class="flex h-full w-full items-center justify-center bg-[radial-gradient(circle_at_50%_30%,rgba(255,255,255,0.28),transparent_28%),linear-gradient(135deg,rgba(255,255,255,0.18),rgba(0,0,0,0.08))]"
                        >
                          <span class="text-sm tracking-[0.3em] text-white/60">NO COVER</span>
                        </div>
                      </div>

                      <div class="mt-2 flex items-end justify-between gap-3 px-1 text-white">
                        <div class="min-w-0">
                          <h4 class="truncate text-sm font-semibold tracking-wide sm:text-base">
                            {{ category.title }}
                          </h4>
                        </div>
                        <span class="shrink-0 text-lg text-white/75 transition group-hover:translate-x-1">→</span>
                      </div>
                    </div>
                  </button>
                </div>
              </div>
            </template>
          </div>
        </section>
      </section>
    </div>
  </main>
</template>
