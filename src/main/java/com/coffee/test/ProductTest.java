package com.coffee.test;

import com.coffee.common.GenerateData;
import com.coffee.constant.Category;
import com.coffee.entity.Product;
import com.coffee.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class ProductTest {
    @Autowired
    private ProductRepository productRepository ;

    @Test
    public void testCreateProducts(){
        List<String> imageFileList = GenerateData.getImageFileNames();

        for(int i = 0 ; i < imageFileList.size() ; i++ ){
            // System.out.println(image);
            Product bean = new Product();
            //bean.setId(0);
            bean.setName("상품" + i);
            bean.setCategory(Category.BREAD);
            bean.setImage(imageFileList.get(i));
            bean.setPrice(10*(i+1));
            bean.setStock(100*(i+1));
            bean.setDescription("상품 설명 " + i);
            bean.setInputdate(LocalDate.now());
            productRepository.save(bean) ; // 데이터 베이스에 저장
        }
    }
}
