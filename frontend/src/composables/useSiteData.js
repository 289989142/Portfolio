import { ref } from 'vue'
import { fetchPublicCategories, fetchPublicCategoryItems, fetchPublicSettings } from '../api/public'

const settings = ref(null)
const categories = ref([])
let settingsPromise = null
let categoriesPromise = null

export function useSiteData() {
  async function loadSettings() {
    if (settings.value) {
      return settings.value
    }
    if (!settingsPromise) {
      settingsPromise = fetchPublicSettings()
        .then((data) => {
          settings.value = data || {}
          return settings.value
        })
        .finally(() => {
          settingsPromise = null
        })
    }
    return settingsPromise
  }

  async function loadCategories() {
    if (categories.value.length > 0) {
      return categories.value
    }
    if (!categoriesPromise) {
      categoriesPromise = fetchPublicCategories()
        .then((data) => {
          categories.value = Array.isArray(data) ? data : []
          return categories.value
        })
        .finally(() => {
          categoriesPromise = null
        })
    }
    return categoriesPromise
  }

  async function loadCategoryItems(categoryId) {
    return fetchPublicCategoryItems(categoryId)
  }

  return {
    settings,
    categories,
    loadSettings,
    loadCategories,
    loadCategoryItems
  }
}
