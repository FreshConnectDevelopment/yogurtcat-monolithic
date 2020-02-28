const state = {
  authType: ''
}

const mutations = {
  SET_AUTH_TYPE: (state, authType) => {
    state.authType = authType
  }
}

export default {
  namespaced: true,
  state,
  mutations
}
