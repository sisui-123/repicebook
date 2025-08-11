import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { 
  Button, 
  Tabbar, 
  TabbarItem, 
  Swipe, 
  SwipeItem, 
  Card, 
  Image as VanImage, 
  Tag, 
  List, 
  Cell, 
  Search,
  Tab,
  Tabs,
  Field,
  CellGroup,
  Loading,
  Rate,
  Icon,
  Popup,
  Empty,
  Form,
  Dialog
} from 'vant'
// 导入Vant样式
import 'vant/lib/index.css'

const app = createApp(App)

// 注册路由
app.use(router)

// 注册Vant组件
app.use(Button)
app.use(Tabbar)
app.use(TabbarItem)
app.use(Swipe)
app.use(SwipeItem)
app.use(Card)
app.use(VanImage)
app.use(Tag)
app.use(List)
app.use(Cell)
app.use(Search)
app.use(Tab)
app.use(Tabs)
app.use(Field)
app.use(CellGroup)
app.use(Loading)
app.use(Rate)
app.use(Icon)
app.use(Popup)
app.use(Empty)
app.use(Form)
app.use(Dialog)

app.mount('#app')
