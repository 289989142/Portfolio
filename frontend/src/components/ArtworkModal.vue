<script setup>
import { onBeforeUnmount, onMounted } from 'vue'

const props = defineProps({
  show: {
    type: Boolean,
    default: false
  },
  item: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['close'])

function close() {
  emit('close')
}

function onKeydown(event) {
  if (event.key === 'Escape') {
    close()
  }
}

onMounted(() => {
  window.addEventListener('keydown', onKeydown)
})

onBeforeUnmount(() => {
  window.removeEventListener('keydown', onKeydown)
})
</script>

<template>
  <Teleport to="body">
    <Transition name="fade">
      <div
        v-if="show"
        class="fixed inset-0 z-[60] flex items-center justify-center bg-slate-950/80 p-4 backdrop-blur-md"
        @click.self="close"
      >
        <Transition name="zoom" appear>
          <div
            class="w-full max-w-4xl overflow-hidden rounded-3xl border border-white/10 bg-slate-950/90 shadow-glow"
          >
            <div class="flex items-center justify-between border-b border-white/10 px-4 py-3">
              <div>
                <p class="text-sm text-slate-400">作品详情</p>
                <h3 class="text-base font-semibold text-white">
                  {{ item?.description ? '作品预览' : '作品展示' }}
                </h3>
              </div>
              <button
                class="rounded-xl bg-white/10 px-3 py-2 text-xs font-medium text-white transition hover:bg-white/15"
                @click="close"
              >
                关闭
              </button>
            </div>
            <div class="grid gap-0 lg:grid-cols-[minmax(0,1.3fr)_minmax(0,0.7fr)]">
              <div class="bg-black/30 p-3 sm:p-4">
                <div class="flex min-h-[52vh] items-center justify-center rounded-2xl bg-black/20 p-2">
                  <img
                    v-if="item?.mediaUrl"
                    :src="item.mediaUrl"
                    :alt="item?.description || '作品图片'"
                    class="max-h-[72vh] w-full rounded-2xl object-contain"
                  />
                  <div v-else class="text-sm text-slate-400">暂无作品图片</div>
                </div>
              </div>
              <div class="border-t border-white/10 bg-slate-950/50 p-4 lg:border-l lg:border-t-0">
                <p class="text-sm font-medium text-cyan-200">简介</p>
                <p class="mt-3 whitespace-pre-line text-sm leading-7 text-slate-200">
                  {{ item?.description || '暂无简介' }}
                </p>
              </div>
            </div>
          </div>
        </Transition>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.22s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.zoom-enter-active,
.zoom-leave-active {
  transition: transform 0.22s ease, opacity 0.22s ease;
}

.zoom-enter-from,
.zoom-leave-to {
  transform: scale(0.96);
  opacity: 0;
}
</style>
