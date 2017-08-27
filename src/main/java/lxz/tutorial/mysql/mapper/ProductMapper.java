package lxz.tutorial.mysql.mapper;

import java.util.List;
import lxz.tutorial.mysql.domain.Product;
import lxz.tutorial.mysql.domain.ProductExample;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    int countByExample(ProductExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Product record);

    int insertSelective(Product record);

    List<Product> selectByExample(ProductExample example);

    Product selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
}