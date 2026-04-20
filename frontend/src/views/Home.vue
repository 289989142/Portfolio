<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useSiteData } from '../composables/useSiteData'

const router = useRouter()
const { settings, categories, loadSettings, loadCategories } = useSiteData()
const loading = ref(true)
const errorMessage = ref('')

const backgroundStyle = computed(() => {
  const bgUrl = settings.value?.bgImageUrl
  if (bgUrl) {
    return {
      backgroundImage: `linear-gradient(180deg, rgba(2, 6, 23, 0.4), rgba(2, 6, 23, 0.92)), url(${bgUrl})`
    }
  }
  return {
    backgroundImage:
      'radial-gradient(circle at top left, rgba(14, 165, 233, 0.22), transparent 32%), radial-gradient(circle at bottom right, rgba(59, 130, 246, 0.18), transparent 28%), linear-gradient(135deg, #050816, #091223 55%, #030712)'
  }
})

function normalizeText(text) {
  return text ? String(text).trim() : ''
}

function openCategory(categoryId) {
  router.push({ name: 'category-detail', params: { id: categoryId } })
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
  <main class="relative min-h-screen overflow-hidden" :style="backgroundStyle">
    <div class="absolute inset-0 bg-[radial-gradient(circle_at_20%_20%,rgba(56,189,248,0.15),transparent_28%),radial-gradient(circle_at_80%_25%,rgba(59,130,246,0.14),transparent_24%),linear-gradient(180deg,rgba(2,6,23,0.2),rgba(2,6,23,0.85))]"></div>

    <div class="relative mx-auto flex min-h-screen w-full max-w-7xl flex-col gap-6 px-4 py-5 sm:px-6 lg:px-8 lg:py-8">
      <header class="glass-panel rounded-3xl px-5 py-4">
        <div class="flex flex-col gap-2 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <p class="text-xs uppercase tracking-[0.35em] text-cyan-200/80">Portfolio Gallery</p>
            <h1 class="mt-2 text-2xl font-semibold tracking-wide text-white sm:text-3xl">
              个人作品展示
            </h1>
          </div>
          <p class="text-sm text-slate-300/80">
            作品分类与展示内容来自后端 `/api/public/**`
          </p>
        </div>
      </header>

      <section class="grid flex-1 gap-6 lg:grid-cols-[360px_minmax(0,1fr)]">
        <aside class="glass-panel rounded-3xl p-5 sm:p-6">
          <div class="flex items-center gap-4">
            <div class="relative">
              <div class="h-20 w-20 rounded-full bg-gradient-to-br from-cyan-400 via-sky-500 to-blue-700 p-[2px] shadow-glow">
                <div class="grid h-full w-full place-items-center rounded-full bg-slate-950 text-2xl font-semibold text-cyan-100">
                  PF
                </div>
              </div>
              <div class="absolute -right-1 bottom-0 h-4 w-4 rounded-full bg-emerald-400 ring-4 ring-slate-950"></div>
            </div>
            <div class="min-w-0">
              <p class="text-sm text-slate-300">个人简介</p>
              <h2 class="truncate text-xl font-semibold text-white">Portfolio</h2>
              <p class="mt-1 text-xs tracking-[0.24em] text-cyan-200/80">Creative backend & front-end showcase</p>
            </div>
          </div>

          <div class="mt-5 space-y-5">
            <div class="rounded-2xl border border-white/10 bg-white/5 p-4">
              <p class="text-xs uppercase tracking-[0.28em] text-slate-400">Bio</p>
              <p class="mt-3 whitespace-pre-line text-sm leading-7 text-slate-200/90">
                {{ normalizeText(settings?.bioContent) || '这里是站点简介内容，请在后台配置 bio_content。' }}
              </p>
            </div>

            <div class="grid gap-4 sm:grid-cols-2 lg:grid-cols-1">
              <div class="rounded-2xl border border-white/10 bg-white/5 p-4">
                <p class="text-xs uppercase tracking-[0.28em] text-slate-400">电话</p>
                <p class="mt-3 text-base font-medium text-white">
                  {{ normalizeText(settings?.phone) || '暂无联系电话' }}
                </p>
              </div>

              <div class="rounded-2xl border border-white/10 bg-white/5 p-4">
                <p class="text-xs uppercase tracking-[0.28em] text-slate-400">微信二维码</p>
                <div class="mt-3 flex items-center gap-3">
                  <img
                    v-if="settings?.wechatQrUrl"
                    :src="settings.wechatQrUrl"
                    alt="微信二维码"
                    class="h-24 w-24 rounded-2xl bg-white object-cover p-1"
                  />
                  <div
                    v-else
                    class="grid h-24 w-24 place-items-center rounded-2xl border border-dashed border-white/20 text-xs text-slate-400"
                  >
                    暂无二维码
                  </div>
                  <p class="text-sm leading-6 text-slate-300">
                    扫码联系，支持咨询、合作与定制需求。
                  </p>
                </div>
              </div>
            </div>

            <a
              :href="settings?.resumeUrl || '#'"
              :download="settings?.resumeUrl ? '' : null"
              :target="settings?.resumeUrl ? '_blank' : null"
              rel="noreferrer"
              class="inline-flex w-full items-center justify-center rounded-2xl bg-cyan-400 px-4 py-3 text-sm font-semibold text-slate-950 transition hover:bg-cyan-300 disabled:cursor-not-allowed disabled:opacity-50"
              :class="{ 'pointer-events-none opacity-50': !settings?.resumeUrl }"
            >
              简历下载
            </a>
          </div>
        </aside>

        <section class="glass-panel rounded-3xl p-5 sm:p-6">
          <div class="flex items-center justify-between gap-4">
            <div>
              <p class="section-title">作品集分类</p>
              <p class="muted-text mt-1">点击卡片进入分类详情页</p>
            </div>
            <div class="rounded-full border border-white/10 bg-white/5 px-3 py-1 text-xs text-slate-300">
              {{ categories.length }} 个分类
            </div>
          </div>

          <div v-if="loading" class="mt-6 grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
            <div
              v-for="n in 6"
              :key="n"
              class="h-56 animate-pulse rounded-3xl border border-white/10 bg-white/5"
            ></div>
          </div>

          <div v-else-if="errorMessage" class="mt-6 rounded-3xl border border-rose-400/20 bg-rose-400/10 p-4 text-sm text-rose-100">
            {{ errorMessage }}
          </div>

          <div v-else class="mt-6 grid gap-4 sm:grid-cols-2 xl:grid-cols-3">
            <button
              v-for="category in categories"
              :key="category.id"
              class="group overflow-hidden rounded-3xl border border-white/10 bg-white/5 text-left transition duration-300 hover:-translate-y-1 hover:border-cyan-300/40 hover:bg-white/[0.08]"
              @click="openCategory(category.id)"
            >
              <div class="relative aspect-[4/3] overflow-hidden">
                <img
                  v-if="category.coverImageUrl"
                  :src="category.coverImageUrl"
                  :alt="category.title"
                  class="h-full w-full object-cover transition duration-500 group-hover:scale-105"
                />
                <div
                  v-else
                  class="flex h-full w-full items-center justify-center bg-gradient-to-br from-slate-800 via-slate-900 to-slate-950 text-sm text-slate-400"
                >
                  暂无封面图
                </div>
                <div class="absolute inset-0 bg-gradient-to-t from-slate-950/90 via-slate-950/15 to-transparent"></div>
                <div class="absolute bottom-3 left-3 right-3">
                  <p class="text-xs uppercase tracking-[0.28em] text-cyan-200/80">
                    Category {{ category.sortOrder ?? 0 }}
                  </p>
                  <h3 class="mt-1 text-lg font-semibold text-white">
                    {{ category.title }}
                  </h3>
                </div>
              </div>
              <div class="flex items-center justify-between px-4 py-3">
                <span class="text-sm text-slate-300">进入查看</span>
                <span class="text-cyan-200 transition group-hover:translate-x-1">→</span>
              </div>
            </button>
          </div>
        </section>
      </section>
    </div>
  </main>
</template>
