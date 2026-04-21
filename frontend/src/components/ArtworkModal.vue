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
        class="fixed inset-0 z-[60] flex items-center justify-center bg-[#241a12]/78 p-4 backdrop-blur-xl"
        @click.self="close"
      >
        <Transition name="zoom" appear>
          <div
            class="w-full overflow-hidden rounded-[2rem] border border-white/20 bg-[#f5ebde]/96 text-[#2d2419] shadow-[0_35px_90px_rgba(24,16,10,0.35)]"
            style="max-width: min(92vw, 980px)"
          >
            <div class="flex items-center justify-between border-b border-[#cdbca5] px-5 py-4">
              <div>
                <h3 class="text-base font-semibold text-[#2d2419]">
                  作品预览
                </h3>
              </div>
              <button
                class="rounded-full border border-[#bca88e] bg-white/60 px-4 py-2 text-xs font-medium text-[#2d2419] transition hover:bg-white/85"
                @click="close"
              >
                关闭
                </button>
              </div>
            <div class="p-4 sm:p-5">
              <div class="flex justify-center">
                <div
                  class="flex max-h-[70vh] items-center justify-center rounded-[1.5rem] border border-white/40 bg-[#201914]/10 p-3"
                  style="max-width: min(88vw, 920px)"
                >
                  <img
                    v-if="item?.mediaUrl"
                    :src="item.mediaUrl"
                    :alt="item?.description || '作品图片'"
                    class="max-h-[66vh] w-auto max-w-full rounded-[1.25rem] object-contain shadow-[0_16px_40px_rgba(21,16,11,0.18)]"
                  />
                  <div v-else class="text-sm text-[#6b5a49]">暂无作品图片</div>
                </div>
              </div>
              <div class="mt-4 rounded-[1.2rem] border border-white/40 bg-white/35 px-4 py-3 text-sm leading-7 text-[#3b3025]">
                {{ item?.description || '暂无简介' }}
              </div>
              <div class="mt-3 text-center text-xs leading-6 text-[#6d5a47]">
                点击背景空白处或按 `Esc` 返回
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
