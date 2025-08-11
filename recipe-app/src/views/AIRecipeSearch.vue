<template>
  <div class="ai-recipe-search-container">
    <h3>菜谱助手</h3>
    <div class="chat-container" ref="chatContainer">
      <!-- 初始欢迎消息 -->
      <div v-if="welcomeMessage" class="message-item">
        <div class="ai-message" v-html="renderMarkdown(welcomeMessage)"></div>
      </div>
      
      <!-- 聊天历史记录 -->
      <div v-for="(message, index) in messages" :key="index" class="message-item">
        <!-- 用户消息 -->
        <div v-if="message.role === 'user'" class="user-message">
          {{ message.content }}
        </div>
        <!-- AI回复 -->
        <div v-else class="ai-message" v-html="renderMarkdown(message.content)"></div>
      </div>
      
      <!-- 流式回复中的消息 -->
      <div v-if="streamingResponse" class="message-item">
        <div class="ai-message" v-html="renderMarkdown(streamingResponse)"></div>
      </div>
      
      <!-- 错误消息 -->
      <div v-if="error" class="error-message">
        <p>{{ error }}</p>
      </div>
    </div>
    <div class="input-area">
      <textarea 
        v-model="query" 
        placeholder="请输入您想查询的菜谱，例如：如何制作宫保鸡丁？" 
        rows="2"
        @keydown.enter.ctrl="searchRecipe"
      ></textarea>
      <button @click="searchRecipe" :disabled="loading" class="send-button">
        <i class="ri-send-plane-fill"></i>
        {{ loading ? '等待回复中...' : '发送' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue';
import { marked } from 'marked'; // 需要安装: npm install marked

// 直接硬编码 API Key（注意：在实际生产环境中，应该通过后端服务来处理API请求，避免暴露API Key）
const apiKey = 'sk-caf9aa2790fd42f293388f5718ee9b9c'; 
const query = ref('');
const loading = ref(false);
const error = ref(null);
const messages = ref([]);
const welcomeMessage = ref('你好！我是AI菜谱助手。请告诉我你想了解什么菜谱，我会尽力提供详细的做法和建议。');
const streamingResponse = ref('');
const chatContainer = ref(null);

// Markdown渲染函数
const renderMarkdown = (text) => {
  if (!text) return '';
  try {
    return marked.parse(text);
  } catch (e) {
    console.error('Markdown渲染错误:', e);
    return text;
  }
};

// 滚动到底部
const scrollToBottom = async () => {
  await nextTick();
  if (chatContainer.value) {
    chatContainer.value.scrollTop = chatContainer.value.scrollHeight;
  }
};

// 初始化
onMounted(() => {
  scrollToBottom();
});

const searchRecipe = async () => {
  if (!query.value.trim()) {
    error.value = '请输入查询内容';
    return;
  }

  loading.value = true;
  error.value = null;
  
  // 保存用户的查询
  const userQuery = query.value;
  // 清空输入框
  query.value = '';
  
  // 添加用户消息到聊天记录
  messages.value.push({
    role: 'user',
    content: userQuery
  });
  
  // 滚动到底部
  await scrollToBottom();

  try {
    // 初始化流式回复
    streamingResponse.value = '';
    
    // 准备请求体
    const requestBody = {
      model: 'deepseek-chat', // <mcreference link="https://api-docs.deepseek.com/" index="1">1</mcreference>
      messages: [
        { role: 'system', content: '你是一个专业的中文菜谱助手，请根据用户的问题提供详细的菜谱做法，包括所需食材、步骤和烹饪技巧。回答要详细、专业且易于理解。需要时可以使用Markdown格式来美化你的回答，例如使用标题、列表、加粗等。可以有适当的表情包来丰富你的文字，如果用户询问问的不是需要详细回答的问题，你可以简略回答' },
        ...messages.value.map(msg => ({ role: msg.role, content: msg.content }))
      ],
      stream: true // 启用流式回复
    };
    
    // 发送请求
    const response = await fetch('https://api.deepseek.com/chat/completions', { // <mcreference link="https://api-docs.deepseek.com/" index="1">1</mcreference>
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${apiKey}` // 使用硬编码的API Key
      },
      body: JSON.stringify(requestBody)
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.error?.message || `HTTP error! status: ${response.status}`);
    }

    // 处理流式回复
    const reader = response.body.getReader();
    const decoder = new TextDecoder('utf-8');
    let fullContent = '';
    
    while (true) {
      const { done, value } = await reader.read();
      if (done) break;
      
      // 解码二进制数据
      const chunk = decoder.decode(value, { stream: true });
      
      // 处理数据块
      const lines = chunk.split('\n').filter(line => line.trim() !== '');
      
      for (const line of lines) {
        if (line.startsWith('data: ')) {
          const data = line.substring(6);
          if (data === '[DONE]') continue;
          
          try {
            const parsedData = JSON.parse(data);
            if (parsedData.choices && parsedData.choices.length > 0) {
              const content = parsedData.choices[0].delta?.content || '';
              fullContent += content;
              streamingResponse.value = fullContent;
              await scrollToBottom();
            }
          } catch (e) {
            console.error('解析流数据错误:', e);
          }
        }
      }
    }
    
    // 流式回复完成，添加到消息列表
    if (fullContent) {
      messages.value.push({
        role: 'assistant',
        content: fullContent
      });
      streamingResponse.value = ''; // 清空流式回复
    } else {
      throw new Error('未能获取到菜谱信息');
    }
  } catch (e) {
    console.error('API请求失败:', e);
    error.value = e.message || '查询菜谱失败，请检查网络或API Key。';
    
    // 添加错误消息到聊天记录
    messages.value.push({
      role: 'assistant',
      content: `❌ 出错了: ${error.value}`
    });
    streamingResponse.value = '';
  } finally {
    loading.value = false;
    await scrollToBottom();
  }
};
</script>

<style scoped>
.ai-recipe-search-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  height: calc(100vh - 120px);
}

h3 {
  text-align: center;
  color: #38A169;
  margin-bottom: 10px;
  font-weight: bold;
}

.chat-container {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 8px;
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
}

.message-item {
  margin-bottom: 15px;
  display: flex;
  flex-direction: column;
  animation: fadeIn 0.3s ease-in-out;
}

.message-item > div {
  max-width: 85%;
  width: fit-content;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* AI消息样式 */
.ai-message {
  background-color: #e9f5ff;
  padding: 15px;
  border-radius: 8px;
  line-height: 1.5;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  align-self: flex-start;
}

/* 用户消息样式 */
.user-message {
  background-color: #dcf8c6;
  padding: 15px;
  border-radius: 8px;
  line-height: 1.5;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  align-self: flex-end;
  text-align: left;
  margin-left: auto;
}

/* Markdown样式 */
.ai-message :deep(h1),
.ai-message :deep(h2),
.ai-message :deep(h3),
.ai-message :deep(h4) {
  margin-top: 0.5em;
  margin-bottom: 0.5em;
  color: #333;
}

.ai-message :deep(ul),
.ai-message :deep(ol) {
  padding-left: 1.5em;
  margin-bottom: 1em;
}

.ai-message :deep(li) {
  margin-bottom: 0.5em;
}

.ai-message :deep(p) {
  margin-bottom: 1em;
}

.ai-message :deep(code) {
  background-color: #f0f0f0;
  padding: 0.2em 0.4em;
  border-radius: 3px;
  font-family: monospace;
}

.ai-message :deep(pre) {
  background-color: #f0f0f0;
  padding: 1em;
  border-radius: 5px;
  overflow-x: auto;
  margin-bottom: 1em;
}

.ai-message :deep(blockquote) {
  border-left: 4px solid #ccc;
  padding-left: 1em;
  margin-left: 0;
  color: #666;
}

.ai-message :deep(table) {
  border-collapse: collapse;
  width: 100%;
  margin-bottom: 1em;
}

.ai-message :deep(th),
.ai-message :deep(td) {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.ai-message :deep(th) {
  background-color: #f2f2f2;
}

.error-message {
  color: #f44336;
  margin-bottom: 15px;
  padding: 10px;
  background-color: #ffebee;
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.input-area {
  display: flex;
  gap: 5px;
  margin-top: auto;
  position: relative;
}

textarea {
  flex: 1;
  padding: 1px;
  border: 1px solid rgb(77, 223, 154);
  border-radius: 8px;
  resize: none;
  font-size: 15px;
  font-family: inherit;
  transition: border-color 0.3s;
}

textarea:focus {
  outline: none;
  border-color: #4CAF50;
  box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
}

.send-button {
  padding: 0 26px;
  background: linear-gradient(90deg,rgb(111, 217, 114) 40%,rgb(104, 168, 236) 100%);
  color: white;
  border: none;
  border-radius: 999px;
  cursor: pointer;
  font-size: 1.08rem;
  font-weight: 600;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 7px;
  box-shadow: 0 1px 4px rgba(45,140,240,0.08);
  height: 44px;
}

.send-button:hover {
  background-color: #45a049;
  transform: translateY(-2px);
}

.send-button:active {
  transform: translateY(0);
}

.send-button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
  transform: none;
}

/* 添加响应式设计 */
@media (max-width: 600px) {
  .ai-recipe-search-container {
    padding: 10px;
    height: calc(100vh - 80px);
  }
  
  .input-area {
    flex-direction: column;
  }
  
  .send-button {
    width: 100%;
    padding: 12px;
  }
  
  .message-item {
    max-width: 95%;
  }
}
</style>