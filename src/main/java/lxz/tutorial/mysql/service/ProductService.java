package lxz.tutorial.mysql.service;

import java.util.List;

import lxz.tutorial.mysql.domain.Product;
import lxz.tutorial.mysql.domain.ProductExample;

/**
 * 产品管理
 */
public interface ProductService {

	/**
	 * 获取产品数量
	 * 
	 * @return
	 * @
	 */
	int getAllProductCount(ProductExample example);

	/**
	 * 获取产品分页列表
	 * 
	 */
	List<Product> getAllProductList(ProductExample example);

	/**
	 * 获取产品基本信息
	 * 
	 * @param id
	 * 
	 */
	Product getProduct(long id);

	/**
	 * 修改产品基本信息
	 * 
	 * @param hospital
	 * @return
	 * @
	 */
	int updateProduct(Product product);

	/**
	 * 创建新的产品
	 * 
	 * @param hospital
	 * @
	 */
	int createProduct(Product product);

}
