package com.coffee.controller;

import com.coffee.entity.Product;
import com.coffee.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;

// 상품 정보 요청을 처리해주는 Controller
// @RequestMapping에 여러 개의 요청을 처리하려면 중괄호 기호를 사용하면 됩니다.
@RestController
@RequestMapping({"/product", "/products"})
public class ProductController {
    private ProductService productService ;

    public ProductController(ProductService productService) {
        this.productService = productService; // 생성자 주입
    }

    // http://localhost:9000/product/list
    @GetMapping("/list") // 상품 목록 보여 주세요.
    public ResponseEntity<List<Product>> list(){
        List<Product> products = this.productService.listProducts() ;
        System.out.println("상품 개수 : " + products.size());

        // Http 응답 코드 200(성공)과 함께 상품 정보를 json으로 반환해줍니다.
        return ResponseEntity.ok(products) ;
    }

    // @PathVariable : url 경로 내의 변수 값을 파라미터로 매핑할 때 사용합니다.
    // http://localhost:9000/product/detail/상품번호
    @GetMapping("/detail/{id}")
    public ResponseEntity<Product> detail(@PathVariable Long id){
        Product product = this.productService.getProductById(id);

        if(product != null){
            return ResponseEntity.ok(product); // 200 Ok 응답
        }else{ // 404 응답
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build() ;
        }
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id){
        System.out.println("수정할 상품 번호 : " + id);
        Product product = this.productService.getProductById(id);

        if(product != null){ // 상품이 존재하면 해당 상품 반화
            return ResponseEntity.ok(product); // 200 Ok 응답

        }else{ // 없으면 404 응답과 함께 널 값을 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) ;
        }
    }

    @PostMapping("/insert") // 상품 등록
    public ResponseEntity<?> insert(@RequestBody Product product){
        String imageData = product.getImage() ; // Base64 인코딩된 문자열
        byte[] decodedImage = Base64.getDecoder().decode(imageData.split(",")[1]);

        // 데이터 베이스에 저장될 문자열 형식의 이미지 이름
        String imageFileName = "product_" + System.currentTimeMillis() + ".jpg" ;

        File imageFile = new File("c:\\boot\\images\\" + imageFileName) ;
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(decodedImage); // 바이트 파일을 해당 경로에 복사하기

            product.setImage(imageFileName);
            product.setInputdate(LocalDate.now());

            productService.save(product) ;

            return ResponseEntity.ok(Map.of("message", "Product inserted successfully", "image", imageFileName));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", e.getMessage(), "error", "error uploading file"));
        }
    }
}
