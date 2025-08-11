<template>
  <div class="search-container">
    <!-- 简洁搜索栏（首页样式） -->
    <div v-if="type === 'simple'" class="search-section">
      <div class="search-bar" @click="handleClick">
        <i class="ri-search-line"></i>
        <span>{{ placeholder }}</span>
      </div>
      <div class="chat-icon" v-if="showChatIcon">
        <i class="ri-chat-3-line"></i>
      </div>
    </div>
    
    <!-- 功能完整搜索栏（分类和话题页样式） -->
    <van-search 
      v-else
      v-model="searchValue" 
      :placeholder="placeholder"
      :show-action="showAction"
      shape="round"
      background="transparent" 
      class="functional-search"
      @search="handleSearch"
    >
      <template #action v-if="showAction">
        <div class="search-btn" @click="handleSearch">{{ actionText }}</div>
      </template>
    </van-search>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits, watch } from 'vue';

// 组件属性
const props = defineProps({
  // 搜索框类型：'simple' | 'functional'
  type: {
    type: String,
    default: 'simple'
  },
  // 占位符文本
  placeholder: {
    type: String,
    default: '搜索'
  },
  // 是否显示聊天图标（仅简洁模式）
  showChatIcon: {
    type: Boolean,
    default: true
  },
  // 是否显示搜索按钮（仅功能模式）
  showAction: {
    type: Boolean,
    default: true
  },
  // 搜索按钮文字
  actionText: {
    type: String,
    default: '搜索'
  },
  // 搜索值（双向绑定）
  modelValue: {
    type: String,
    default: ''
  }
});

// 组件事件
const emit = defineEmits(['update:modelValue', 'search', 'click']);

// 搜索值
const searchValue = ref(props.modelValue);

// 处理点击事件（简洁模式）
const handleClick = () => {
  emit('click');
};

// 处理搜索事件（功能模式）
const handleSearch = () => {
  emit('update:modelValue', searchValue.value);
  emit('search', searchValue.value);
};

// 监听外部值变化
watch(() => props.modelValue, (newValue) => {
  searchValue.value = newValue;
});

// 监听内部值变化
watch(searchValue, (newValue) => {
  emit('update:modelValue', newValue);
});
</script>

<style scoped>
.search-container {
  width: 100%;
}

/* 简洁搜索栏样式（首页风格） */
.search-section {
  display: flex;
  align-items: center;
  padding: 0 var(--spacing-md);
}

.search-bar {
  flex: 1;
  background-color: #F7FAFC;
  border-radius: 20px;
  padding: var(--spacing-sm) var(--spacing-md);
  display: flex;
  align-items: center;
  transition: transform var(--transition-fast);
  cursor: pointer;
}

.search-bar:active {
  transform: scale(0.98);
}

.search-bar i {
  margin-right: var(--spacing-sm);
  color: #A0AEC0;
  font-size: 18px;
}

.search-bar span {
  color: #A0AEC0;
  font-size: var(--font-size-sm);
}

.chat-icon {
  margin-left: var(--spacing-md);
  width: 40px;
  height: 40px;
  background-color: #F7FAFC;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color var(--transition-fast);
}

.chat-icon:hover {
  background-color: #EDF2F7;
}

.chat-icon i {
  color: #4A5568;
  font-size: 20px;
}

/* 功能完整搜索栏样式（分类和话题页风格） */
.functional-search :deep(.van-search) {
  padding: 0;
  background: transparent;
}

.functional-search :deep(.van-search__content) {
  background-color: #F7FAFC;
  border-radius: 50px;
  box-shadow: none;
  border: 1px solid #E2E8F0;
}

.search-btn {
  color: #64748B;
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-medium);
  padding-left: var(--spacing-md);
  cursor: pointer;
}
</style> 