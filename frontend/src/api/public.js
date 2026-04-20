import http, { unwrapResult } from './http'

export async function fetchPublicSettings() {
  return unwrapResult(await http.get('/public/settings'))
}

export async function fetchPublicCategories() {
  return unwrapResult(await http.get('/public/categories'))
}

export async function fetchPublicCategoryItems(categoryId) {
  return unwrapResult(await http.get(`/public/categories/${categoryId}/items`))
}
