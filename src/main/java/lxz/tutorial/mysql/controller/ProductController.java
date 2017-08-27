package lxz.tutorial.mysql.controller;

import lxz.tutorial.mysql.domain.Product;
import lxz.tutorial.mysql.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping("/{id}")
  public Product getProductInfo(
      @PathVariable("id")
          Long productId) {
    return productService.getProduct(productId);
  }

  @PutMapping("/{id}")
  public Product updateProductInfo(
      @PathVariable("id")
          Long productId,
      @RequestBody
          Product newProduct) {
    newProduct.setId(productId);
    int result = productService.updateProduct(newProduct);
    return productService.getProduct(productId);
  }
}
