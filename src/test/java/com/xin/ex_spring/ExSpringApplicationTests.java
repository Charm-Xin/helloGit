package com.xin.ex_spring;

import com.xin.ex_spring.dao.ProductDao;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ExSpringApplicationTests {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private ProductDao productDao;

    /**
     * 创建索引，系统初始化会自动创建索引
     */
    @Test
    public void createIndex() {
        System.out.println("创建索引");
    }

    /**
     * 删除索引
     */
    @Test
    public void deleteIndex() {
        boolean delete = elasticsearchRestTemplate.indexOps(Product.class).delete();
        System.out.println("删除索引：" + delete);
    }

    /**
     * 文档新增
     */
    @Test
    public void save(){
        Product product = new Product();
        product.setId(2L);
        product.setTitle("华为手机");
        product.setCategory("手机");
        product.setPrice(2999.0);
        product.setImages("https://www.baidu.com");
        productDao.save(product);
    }

    /**
     * 文档修改 id要相同
     */
    @Test
    public void update(){
        Product product = new Product();
        product.setId(2L);
        product.setTitle("小米手机");
        product.setCategory("手机");
        product.setPrice(4999.0);
        product.setImages("https://www.baidu.com");
        productDao.save(product);
    }

    /**
     * 文档查询 根据id
     */
    @Test
    public void findById(){
        Product product = productDao.findById(2L).get();
        System.out.println(product);
    }

    /**
     * 文档查询 全部信息
     */
    @Test
    public void findAll(){
        productDao.findAll().forEach(System.out::println);
    }

    /**
     * 文档删除
     */
    @Test
    public void delete(){
        Product product = new Product();
        product.setId(1L);
        productDao.delete(product);
    }

    /**
     * 文档批量新增
     */
    @Test
    public void saveAll(){
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setId(Long.valueOf(i));
            product.setTitle("["+i+"]小米手机");
            product.setCategory("手机");
            product.setPrice(1999.0 + i);
            product.setImages("http://www.atguigu/xm.jpg");
            productList.add(product);
        }
        productDao.saveAll(productList);
    }

    /**
     * 文档分页查询
     */
    @Test
    public void findByPageable(){
        //设置排序(排序方式，正序还是倒序，排序的 id)
        Sort sort = Sort.by(Sort.Direction.ASC,"id");
        int currentPage=0;//当前页，第一页从 0 开始， 1 表示第二页
        int pageSize = 5;//每页显示多少条
        //设置查询分页
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize,sort);
        //分页查询
        productDao.findAll(pageRequest).getContent().forEach(System.out::println);
    }

    /**
     * 文档 term 查询
     */
    @Test
    public void termQuery(){
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("category","手机"));
        nativeSearchQueryBuilder.withQuery(boolQuery);
    }


}
