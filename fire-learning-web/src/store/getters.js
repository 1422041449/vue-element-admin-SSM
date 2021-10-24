const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.userinfo.token,
  avatar: state => state.userinfo.avatar,
  name: state => state.userinfo.name
}
export default getters
