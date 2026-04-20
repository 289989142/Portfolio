<script setup>
import { computed, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'

const props = defineProps({
  musicUrl: {
    type: String,
    default: ''
  }
})

const audioRef = ref(null)
const isPlaying = ref(false)
const isMuted = ref(false)
const statusText = ref('背景音乐')

const hasMusic = computed(() => Boolean(props.musicUrl))

async function tryPlay() {
  await nextTick()
  const audio = audioRef.value
  if (!audio || !props.musicUrl) {
    isPlaying.value = false
    statusText.value = '未配置音乐'
    return
  }

  audio.src = props.musicUrl
  audio.muted = isMuted.value
  try {
    await audio.play()
    isPlaying.value = true
    statusText.value = isMuted.value ? '已静音播放' : '正在播放'
  } catch (error) {
    isPlaying.value = false
    statusText.value = '点击播放'
  }
}

async function togglePlay() {
  const audio = audioRef.value
  if (!audio || !props.musicUrl) {
    return
  }

  if (isPlaying.value) {
    audio.pause()
    isPlaying.value = false
    statusText.value = '已暂停'
    return
  }

  try {
    await audio.play()
    isPlaying.value = true
    statusText.value = isMuted.value ? '已静音播放' : '正在播放'
  } catch (error) {
    statusText.value = '浏览器阻止自动播放'
  }
}

function toggleMute() {
  const audio = audioRef.value
  isMuted.value = !isMuted.value
  if (audio) {
    audio.muted = isMuted.value
  }
  if (isPlaying.value) {
    statusText.value = isMuted.value ? '已静音播放' : '正在播放'
  }
}

watch(
  () => props.musicUrl,
  () => {
    tryPlay().catch(() => {})
  }
)

onMounted(() => {
  tryPlay().catch(() => {})
})

onBeforeUnmount(() => {
  const audio = audioRef.value
  if (audio) {
    audio.pause()
  }
})
</script>

<template>
  <div
    class="fixed bottom-4 right-4 z-50 w-[min(92vw,20rem)] rounded-2xl border border-white/10 bg-slate-950/70 p-3 text-sm text-slate-100 shadow-glow backdrop-blur-xl"
  >
    <audio ref="audioRef" loop />
    <div class="flex items-center gap-3">
      <div class="grid h-11 w-11 place-items-center rounded-xl bg-cyan-400/15 text-cyan-200 ring-1 ring-cyan-300/20">
        ♪
      </div>
      <div class="min-w-0 flex-1">
        <p class="truncate font-medium">背景音乐</p>
        <p class="truncate text-xs text-slate-400">{{ statusText }}</p>
      </div>
    </div>
    <div class="mt-3 flex gap-2">
      <button
        class="flex-1 rounded-xl bg-white/10 px-3 py-2 text-xs font-medium transition hover:bg-white/15 disabled:cursor-not-allowed disabled:opacity-50"
        :disabled="!hasMusic"
        @click="togglePlay"
      >
        {{ isPlaying ? '暂停' : '播放' }}
      </button>
      <button
        class="flex-1 rounded-xl bg-white/10 px-3 py-2 text-xs font-medium transition hover:bg-white/15 disabled:cursor-not-allowed disabled:opacity-50"
        :disabled="!hasMusic"
        @click="toggleMute"
      >
        {{ isMuted ? '取消静音' : '静音' }}
      </button>
    </div>
  </div>
</template>
