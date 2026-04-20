<script setup>
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useSiteData } from './composables/useSiteData'
import FloatingMusicPlayer from './components/FloatingMusicPlayer.vue'

const { settings, loadSettings } = useSiteData()
const route = useRoute()

onMounted(() => {
  if (!route.path.startsWith('/admin')) {
    loadSettings().catch(() => {})
  }
})

const musicUrl = computed(() => settings.value?.bgMusicUrl || '')
const hidePlayer = computed(() => route.meta.hideMusicPlayer || route.path.startsWith('/admin'))
</script>

<template>
  <div class="min-h-screen bg-slate-950 text-slate-100">
    <router-view />
    <FloatingMusicPlayer v-if="!hidePlayer" :music-url="musicUrl" />
  </div>
</template>
