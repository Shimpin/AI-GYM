<template>
  <div class="ai-chat-container">
    <!-- 聊天头部 -->
    <div class="chat-header">
      <div class="header-content">
        <div class="header-info">
          <h2>🐏 AI 客服助手</h2>
          <p class="header-subtitle">随时随地，智能问答，为你提供让你满意的服务😇</p>
        </div>
        <div class="header-actions">
          <el-button
              type="danger"
              size="small"
              @click="clearChat"
              class="clear-btn"
          >
            <el-icon><Delete /></el-icon>
            清空记录
          </el-button>
        </div>
      </div>
    </div>

    <!-- 聊天内容区 -->
    <div class="chat-content" ref="chatContent">
      <!-- 空状态 -->
      <div class="empty-state" v-if="chatRecords.length === 0">
        <div class="empty-content">
          <div class="empty-icon">🐏</div>
          <h2>欢迎使用刀羊健身房管理系统</h2>
          <h3>我是你的私人客服助理</h3>
          <p>有任何问题都可以向我提问❤️</p>
          <div class="suggestion-tips">
            <el-tag type="info" size="small">你可以问：</el-tag>
            <div class="suggestions">
              <el-button size="small" @click="message = '健身器材一般有哪些'">健身器材</el-button>
              <el-button size="small" @click="message = '如何积极健身'">怎样积极健身</el-button>
              <el-button size="small" @click="message = '健身的好处'">健身的好处</el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 聊天记录 -->
      <div class="chat-record" v-else>
        <div
            v-for="(item, index) in chatRecords"
            :key="index"
            :class="['chat-item', item.role === 'user' ? 'user-item' : 'ai-item']"
        >
          <div class="avatar">
            <span v-if="item.role === 'user'">🤓</span>
            <span v-else>🐏</span>
          </div>
          <div class="chat-bubble">
            <div class="chat-content2">{{ item.content }}</div>
            <div class="chat-time">{{ formatTime(item.time) }}</div>
          </div>
        </div>
      </div>

      <!-- 加载状态 -->
      <div class="loading-state" v-if="isLoading">
        <div class="ai-item">
          <div class="avatar">🐏</div>
          <div class="chat-bubble">
            <div class="typing-indicator">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 输入区 -->
    <div class="chat-input-area">
      <div class="input-container">
        <el-input
            v-model="message"
            placeholder="输入你的问题... (按 Enter 发送，Shift + Enter 换行)"
            type="textarea"
            :rows="1"
            class="input-box"
            @keydown.enter.exact.prevent="sendMessage"
            @keydown.enter.shift.exact.prevent="handleNewLine"
            resize="none"
        />
        <el-button
            type="primary"
            @click="sendMessage"
            :loading="isLoading"
            class="send-btn"
        >
          <el-icon><Promotion /></el-icon>
          发送
        </el-button>
      </div>
      <div class="input-footer">
        <span class="hint">按 Enter 发送，Shift + Enter 换行</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from "vue";
import { ElEmpty, ElMessage } from "element-plus";
import { Delete, Promotion } from "@element-plus/icons-vue";
import api from "@/util/api";
import { getJwt } from "@/api/jwt.js";

// 请求头
const headers = { Authorization: getJwt() };

// 聊天状态
const message = ref("");
const chatRecords = ref([]);
const chatContent = ref(null);
const isLoading = ref(false);

// 格式化时间
const formatTime = (time) => {
  return new Date(time).toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit"
  });
};

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (chatContent.value) {
      chatContent.value.scrollTop = chatContent.value.scrollHeight;
    }
  });
};

// 处理换行
const handleNewLine = () => {
  message.value += "\n";
};

// 发送消息
const sendMessage = async () => {
  const content = message.value.trim();
  if (!content) return;

  // 添加用户消息
  chatRecords.value.push({
    role: "user",
    content,
    time: new Date().getTime()
  });
  message.value = "";
  scrollToBottom();

  try {
    isLoading.value = true;
    const resp = await api({
      url: "/ai/chat",
      method: "get",
      params: { message: content },
      timeout: 90000,
      responseType: 'text'
    });

    // 清理空白字符，防止空字符串误判
    const aiReply = resp || "抱歉，未收到有效回复";

    // 添加AI消息
    chatRecords.value.push({
      role: "ai",
      content: aiReply,
      time: new Date().getTime()
    });

  } catch (err) {
    console.error("请求失败:", err);

    let errorMessage = "请求失败，请稍后重试";
    if (err.code === 'ECONNABORTED') {
      errorMessage = "请求超时（30秒），请稍后再试";
    } else if (err.response && err.response.status === 401) {
      errorMessage = "身份验证失败，请重新登录";
    }

    chatRecords.value.push({
      role: "ai",
      content: errorMessage,
      time: new Date().getTime()
    });
    ElMessage.error(errorMessage);
  } finally {
    isLoading.value = false;
    scrollToBottom();
  }
};

// 清空聊天记录
const clearChat = () => {
  chatRecords.value = [];
};
</script>

<style scoped>
.ai-chat-container {
  width: 100%;
  height: calc(100vh - 40px);
  max-width: 1200px;
  margin: 20px auto;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  background: #fff;
  display: flex;
  flex-direction: column;
}

/* 头部样式 */
.chat-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 30px;
}

.header-info h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
}

.header-subtitle {
  margin: 0;
  font-size: 14px;
  opacity: 0.9;
}

.clear-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  transition: all 0.3s ease;
}

.clear-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

/* 聊天内容区 */
.chat-content {
  flex: 1;
  overflow-y: auto;
  padding: 30px;
  background-color: #f8f9fa;
  scrollbar-width: thin;
  scrollbar-color: #ffffff #d31010;
}

.chat-content::-webkit-scrollbar {
  width: 8px;
}

.chat-content::-webkit-scrollbar-track {
  background: #ffffff;
}

.chat-content::-webkit-scrollbar-thumb {
  background-color: #c5c5c5;
  border-radius: 4px;
}

.chat-content::-webkit-scrollbar-thumb:hover {
  background-color: #bababa;
}

/* 空状态 */
.empty-state {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.empty-content {
  text-align: center;
  max-width: 500px;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 20px;
}

.empty-content h3 {
  font-size: 24px;
  margin-bottom: 10px;
  color: #333;
}

.empty-content p {
  font-size: 16px;
  color: #666;
  margin-bottom: 30px;
}

.suggestion-tips {
  text-align: left;
}

.suggestions {
  margin-top: 15px;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.suggestions .el-button {
  background: #f0f2f5;
  border: none;
  color: #666;
}

.suggestions .el-button:hover {
  background: #e0e5ec;
  color: #333;
}

/* 聊天记录 */
.chat-record {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.chat-item {
  display: flex;
  max-width: 85%;
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.user-item {
  margin-left: auto;
  flex-direction: row-reverse;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
  margin: 0 15px;
  background: #f0f2f5;
}

.user-item .avatar {
  background: #007bff;
  color: #1e1e1e;
}

.ai-item .avatar {
  background: #28a745;
  color: white;
}

.chat-bubble {
  padding: 16px 20px;
  border-radius: 18px;
  position: relative;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  max-width: 100%;
}

.chat-content2{
  background-color: #f8f9fa;
}

/* 用户消息样式修改 */
.user-item .chat-bubble {
  background-color: #FFFFFF; /* 改为蓝色背景 */
  color: #121212; /* 文字 */
  border-bottom-right-radius: 6px;
  border: 1px solid #a4a4a6; /* 添加深蓝色边框 */
  box-shadow: 0 2px 8px rgba(0, 123, 255, 0.3); /* 添加阴影效果 */
}

/* AI消息样式保持不变 */
.ai-item .chat-bubble {
  background-color: #fff;
  color: #333;
  border-bottom-left-radius: 6px;
  border: 1px solid #e9ecef;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.chat-content {
  line-height: 1.6;
  font-size: 15px;
  white-space: pre-wrap;
  word-break: break-word;
}

.chat-time {
  font-size: 12px;
  margin-top: 8px;
  opacity: 0.7;
  text-align: right;
}

/* 加载状态 */
.loading-state {
  padding: 10px 30px 30px;
}

.typing-indicator {
  display: flex;
  gap: 6px;
  padding: 8px 0;
}

.typing-indicator span {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #ccc;
  animation: typing 1.4s infinite ease-in-out;
}

.typing-indicator span:nth-child(1) { animation-delay: -0.32s; }
.typing-indicator span:nth-child(2) { animation-delay: -0.16s; }

@keyframes typing {
  0%, 80%, 100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

/* 输入区 */
.chat-input-area {
  padding: 20px 30px;
  background: white;
  border-top: 1px solid #e9ecef;
}

.input-container {
  display: flex;
  gap: 12px;
  align-items: flex-end;
  margin-bottom: 8px;
}

.input-box {
  flex: 1;
}

.input-box :deep(.el-textarea__inner) {
  border-radius: 24px !important;
  padding: 14px 20px;
  border-color: #e0e0e0;
  resize: none;
  font-size: 15px;
  line-height: 1.5;
  min-height: 50px !important;
}

.send-btn {
  border-radius: 24px !important;
  padding: 12px 24px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  font-weight: 500;
  box-shadow: 0 2px 8px rgba(0, 123, 255, 0.3);
  transition: all 0.3s ease;
}

.send-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 123, 255, 0.4);
}

.input-footer {
  text-align: right;
}

.hint {
  font-size: 12px;
  color: #999;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .ai-chat-container {
    height: calc(100vh - 20px);
    margin: 10px;
    border-radius: 12px;
  }

  .header-content {
    padding: 15px 20px;
  }

  .chat-content {
    padding: 15px;
  }

  .chat-item {
    max-width: 90%;
  }

  .input-container {
    flex-direction: column;
  }

  .send-btn {
    width: 100%;
    justify-content: center;
  }
}
</style>
