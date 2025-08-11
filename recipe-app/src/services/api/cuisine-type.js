// 烹饪类型相关API
import { get } from './http';

/**
 * 获取单个烹饪类型详情
 * @param {string} cuisineTypeId - 烹饪类型ID
 * @returns {Promise} 烹饪类型详情
 */
export function getCuisineTypeById(cuisineTypeId) {
  return get(`/cuisine-types/${cuisineTypeId}`);
}

/**
 * 获取所有烹饪类型
 * @returns {Promise} 所有烹饪类型列表
 */
export function getAllCuisineTypes() {
  return get('/cuisine-types');
}

export default {
  getCuisineTypeById,
  getAllCuisineTypes
};
