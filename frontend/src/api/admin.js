import http, { unwrapResult } from './http'

export async function adminLogin(password) {
  return unwrapResult(await http.post('/admin/login', { password }))
}

export async function fetchAdminSettings() {
  return unwrapResult(await http.get('/admin/settings'))
}

export async function updateAdminSettings(payload) {
  return unwrapResult(await http.put('/admin/settings', payload))
}

export async function fetchAdminCategories() {
  return unwrapResult(await http.get('/admin/categories'))
}

export async function createAdminCategory(payload) {
  return unwrapResult(await http.post('/admin/categories', payload))
}

export async function updateAdminCategory(payload) {
  return unwrapResult(await http.put('/admin/categories', payload))
}

export async function deleteAdminCategory(id) {
  return unwrapResult(await http.delete(`/admin/categories/${id}`))
}

export async function fetchAdminItems(categoryId) {
  return unwrapResult(await http.get('/admin/items', { params: { categoryId } }))
}

export async function createAdminItem(payload) {
  return unwrapResult(await http.post('/admin/items', payload))
}

export async function updateAdminItem(payload) {
  return unwrapResult(await http.put('/admin/items', payload))
}

export async function deleteAdminItem(id) {
  return unwrapResult(await http.delete(`/admin/items/${id}`))
}

export async function uploadAdminFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  return unwrapResult(
    await http.post('/admin/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  )
}
