package lxz.tutorial.mysql.service.impl;

import java.util.List;
import lxz.tutorial.mysql.domain.Product;
import lxz.tutorial.mysql.domain.ProductExample;
import lxz.tutorial.mysql.mapper.ProductMapper;
import lxz.tutorial.mysql.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @see ProductService
 */
@Service
public class ProductServiceImpl implements ProductService {

  private static final String LOG_COUNT = "获取产品数量";
  private static final String LOG_LIST = "获取产品列表";
  private static final String LOG_INFO = "获取产品信息";
  private static final String LOG_UPDATE = "维护产品信息";
  private static final String LOG_CREATE = "创建产品";

  @Autowired
  private ProductMapper productMapper;

  @Override
  public int getAllProductCount(ProductExample example) {
    int total = 0;
    total = productMapper.countByExample(example);
    return total;
  }

  @Override
  public List<Product> getAllProductList(ProductExample example) {
    List<Product> products = null;
    products = productMapper.selectByExample(example);
    return products;
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  @Override
  public Product getProduct(long id) {
    Product product = null;
    product = productMapper.selectByPrimaryKey(id);
    return product;
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  @Override
  public int updateProduct(Product product) {
    int result = productMapper.updateByPrimaryKeySelective(product);
    Assert.isNull(this, "throw on purpose to test transaction");
    return result;
  }

  @Override
  public int createProduct(Product product) {
    return productMapper.insertSelective(product);
  }
}
