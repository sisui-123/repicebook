// 话题相关API
import { get, post, put, del } from './http';

/**
 * 获取单个话题详情
 * @param {string} topicId - 话题ID
 * @returns {Promise} 话题详情，包含评论
 */
export function getTopicById(topicId) {
  return get(`/topics/${topicId}`);
}

/**
 * 获取所有话题
 * @returns {Promise} 话题列表
 */
export function getAllTopics() {
  return get('/topics');
}

/**
 * 获取指定用户的话题
 * @param {string} userId - 用户ID
 * @returns {Promise} 该用户的话题列表
 */
export function getUserTopics(userId) {
  return get(`/topics/user/${userId}`);
}

/**
 * 搜索话题
 * @param {string} keyword - 搜索关键词
 * @returns {Promise} 搜索结果话题列表
 */
export function searchTopics(keyword) {
  return get('/topics/search', { keyword });
}

/**
 * 获取最新话题
 * @param {number} limit - 返回数量限制，默认为10
 * @returns {Promise} 最新的话题列表
 */
export function getLatestTopics(limit = 10) {
  return get('/topics/latest', { limit });
}

/**
 * 创建新话题
 * @param {Object} topicData - 话题数据
 * @returns {Promise} 创建的话题详情
 */
export function createTopic(topicData) {
  return post('/topics', topicData);
}

/**
 * 更新话题
 * @param {string} topicId - 话题ID
 * @param {Object} topicData - 更新的话题数据
 * @returns {Promise} 更新后的话题详情
 */
export function updateTopic(topicId, topicData) {
  return put(`/topics/${topicId}`, topicData);
}

/**
 * 删除话题
 * @param {string} topicId - 要删除的话题ID
 * @param {string} userId - 用户ID，用于验证权限
 * @returns {Promise} 删除结果
 */
export function deleteTopic(topicId, userId) {
  return del(`/topics/${topicId}`, { userId });
}

/**
 * 添加评论
 * @param {string} topicId - 话题ID
 * @param {Object} commentData - 评论数据
 * @returns {Promise} 更新后的话题，包含新评论
 */
export function addComment(topicId, commentData) {
  return post(`/topics/${topicId}/comments`, commentData);
}

/**
 * 删除评论
 * @param {string} commentId - 评论ID
 * @param {string} userId - 用户ID，用于验证权限
 * @returns {Promise} 删除结果
 */
export function deleteComment(commentId, userId) {
  return del(`/topics/comments/${commentId}`, { userId });
}

export default {
  getTopicById,
  getAllTopics,
  getUserTopics,
  searchTopics,
  getLatestTopics,
  createTopic,
  updateTopic,
  deleteTopic,
  addComment,
  deleteComment
};
