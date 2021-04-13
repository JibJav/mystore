package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MystoreApp;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Customer;
import com.mycompany.myapp.domain.Ordar;
import com.mycompany.myapp.repository.ProductRepository;
import com.mycompany.myapp.service.ProductService;
import com.mycompany.myapp.service.dto.ProductDTO;
import com.mycompany.myapp.service.mapper.ProductMapper;
import com.mycompany.myapp.service.dto.ProductCriteria;
import com.mycompany.myapp.service.ProductQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.AvailableStatus;
/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@SpringBootTest(classes = MystoreApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProductResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final AvailableStatus DEFAULT_STATUS = AvailableStatus.AVAILABLE;
    private static final AvailableStatus UPDATED_STATUS = AvailableStatus.NOT_AVAILABLE;

    private static final Instant DEFAULT_ORDAR_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDAR_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_PRICE = 1.0;
    private static final Double UPDATED_PRICE = 2.0;
    private static final Double SMALLER_PRICE = 1.0 - 1.0;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductQueryService productQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .name(DEFAULT_NAME)
            .brand(DEFAULT_BRAND)
            .status(DEFAULT_STATUS)
            .ordarDate(DEFAULT_ORDAR_DATE)
            .price(DEFAULT_PRICE)
            .quantity(DEFAULT_QUANTITY);
        return product;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .name(UPDATED_NAME)
            .brand(UPDATED_BRAND)
            .status(UPDATED_STATUS)
            .ordarDate(UPDATED_ORDAR_DATE)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY);
        return product;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testProduct.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProduct.getOrdarDate()).isEqualTo(DEFAULT_ORDAR_DATE);
        assertThat(testProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);


        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBrandIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setBrand(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);


        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].ordarDate").value(hasItem(DEFAULT_ORDAR_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }
    
    @Test
    @Transactional
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.ordarDate").value(DEFAULT_ORDAR_DATE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }


    @Test
    @Transactional
    public void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductShouldBeFound("id.equals=" + id);
        defaultProductShouldNotBeFound("id.notEquals=" + id);

        defaultProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.greaterThan=" + id);

        defaultProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name equals to DEFAULT_NAME
        defaultProductShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name not equals to DEFAULT_NAME
        defaultProductShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the productList where name not equals to UPDATED_NAME
        defaultProductShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name is not null
        defaultProductShouldBeFound("name.specified=true");

        // Get all the productList where name is null
        defaultProductShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductsByNameContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name contains DEFAULT_NAME
        defaultProductShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the productList where name contains UPDATED_NAME
        defaultProductShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name does not contain DEFAULT_NAME
        defaultProductShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the productList where name does not contain UPDATED_NAME
        defaultProductShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllProductsByBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where brand equals to DEFAULT_BRAND
        defaultProductShouldBeFound("brand.equals=" + DEFAULT_BRAND);

        // Get all the productList where brand equals to UPDATED_BRAND
        defaultProductShouldNotBeFound("brand.equals=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    public void getAllProductsByBrandIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where brand not equals to DEFAULT_BRAND
        defaultProductShouldNotBeFound("brand.notEquals=" + DEFAULT_BRAND);

        // Get all the productList where brand not equals to UPDATED_BRAND
        defaultProductShouldBeFound("brand.notEquals=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    public void getAllProductsByBrandIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where brand in DEFAULT_BRAND or UPDATED_BRAND
        defaultProductShouldBeFound("brand.in=" + DEFAULT_BRAND + "," + UPDATED_BRAND);

        // Get all the productList where brand equals to UPDATED_BRAND
        defaultProductShouldNotBeFound("brand.in=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    public void getAllProductsByBrandIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where brand is not null
        defaultProductShouldBeFound("brand.specified=true");

        // Get all the productList where brand is null
        defaultProductShouldNotBeFound("brand.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductsByBrandContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where brand contains DEFAULT_BRAND
        defaultProductShouldBeFound("brand.contains=" + DEFAULT_BRAND);

        // Get all the productList where brand contains UPDATED_BRAND
        defaultProductShouldNotBeFound("brand.contains=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    public void getAllProductsByBrandNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where brand does not contain DEFAULT_BRAND
        defaultProductShouldNotBeFound("brand.doesNotContain=" + DEFAULT_BRAND);

        // Get all the productList where brand does not contain UPDATED_BRAND
        defaultProductShouldBeFound("brand.doesNotContain=" + UPDATED_BRAND);
    }


    @Test
    @Transactional
    public void getAllProductsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where status equals to DEFAULT_STATUS
        defaultProductShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the productList where status equals to UPDATED_STATUS
        defaultProductShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllProductsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where status not equals to DEFAULT_STATUS
        defaultProductShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the productList where status not equals to UPDATED_STATUS
        defaultProductShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllProductsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultProductShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the productList where status equals to UPDATED_STATUS
        defaultProductShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllProductsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where status is not null
        defaultProductShouldBeFound("status.specified=true");

        // Get all the productList where status is null
        defaultProductShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByOrdarDateIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where ordarDate equals to DEFAULT_ORDAR_DATE
        defaultProductShouldBeFound("ordarDate.equals=" + DEFAULT_ORDAR_DATE);

        // Get all the productList where ordarDate equals to UPDATED_ORDAR_DATE
        defaultProductShouldNotBeFound("ordarDate.equals=" + UPDATED_ORDAR_DATE);
    }

    @Test
    @Transactional
    public void getAllProductsByOrdarDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where ordarDate not equals to DEFAULT_ORDAR_DATE
        defaultProductShouldNotBeFound("ordarDate.notEquals=" + DEFAULT_ORDAR_DATE);

        // Get all the productList where ordarDate not equals to UPDATED_ORDAR_DATE
        defaultProductShouldBeFound("ordarDate.notEquals=" + UPDATED_ORDAR_DATE);
    }

    @Test
    @Transactional
    public void getAllProductsByOrdarDateIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where ordarDate in DEFAULT_ORDAR_DATE or UPDATED_ORDAR_DATE
        defaultProductShouldBeFound("ordarDate.in=" + DEFAULT_ORDAR_DATE + "," + UPDATED_ORDAR_DATE);

        // Get all the productList where ordarDate equals to UPDATED_ORDAR_DATE
        defaultProductShouldNotBeFound("ordarDate.in=" + UPDATED_ORDAR_DATE);
    }

    @Test
    @Transactional
    public void getAllProductsByOrdarDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where ordarDate is not null
        defaultProductShouldBeFound("ordarDate.specified=true");

        // Get all the productList where ordarDate is null
        defaultProductShouldNotBeFound("ordarDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price equals to DEFAULT_PRICE
        defaultProductShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price not equals to DEFAULT_PRICE
        defaultProductShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the productList where price not equals to UPDATED_PRICE
        defaultProductShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultProductShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is not null
        defaultProductShouldBeFound("price.specified=true");

        // Get all the productList where price is null
        defaultProductShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is greater than or equal to DEFAULT_PRICE
        defaultProductShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the productList where price is greater than or equal to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is less than or equal to DEFAULT_PRICE
        defaultProductShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the productList where price is less than or equal to SMALLER_PRICE
        defaultProductShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is less than DEFAULT_PRICE
        defaultProductShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the productList where price is less than UPDATED_PRICE
        defaultProductShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is greater than DEFAULT_PRICE
        defaultProductShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the productList where price is greater than SMALLER_PRICE
        defaultProductShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }


    @Test
    @Transactional
    public void getAllProductsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where quantity equals to DEFAULT_QUANTITY
        defaultProductShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the productList where quantity equals to UPDATED_QUANTITY
        defaultProductShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllProductsByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where quantity not equals to DEFAULT_QUANTITY
        defaultProductShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the productList where quantity not equals to UPDATED_QUANTITY
        defaultProductShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllProductsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultProductShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the productList where quantity equals to UPDATED_QUANTITY
        defaultProductShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllProductsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where quantity is not null
        defaultProductShouldBeFound("quantity.specified=true");

        // Get all the productList where quantity is null
        defaultProductShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultProductShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the productList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultProductShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllProductsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultProductShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the productList where quantity is less than or equal to SMALLER_QUANTITY
        defaultProductShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllProductsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where quantity is less than DEFAULT_QUANTITY
        defaultProductShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the productList where quantity is less than UPDATED_QUANTITY
        defaultProductShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllProductsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where quantity is greater than DEFAULT_QUANTITY
        defaultProductShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the productList where quantity is greater than SMALLER_QUANTITY
        defaultProductShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllProductsByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        Customer customer = CustomerResourceIT.createEntity(em);
        em.persist(customer);
        em.flush();
        product.addCustomer(customer);
        productRepository.saveAndFlush(product);
        Long customerId = customer.getId();

        // Get all the productList where customer equals to customerId
        defaultProductShouldBeFound("customerId.equals=" + customerId);

        // Get all the productList where customer equals to customerId + 1
        defaultProductShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }


    @Test
    @Transactional
    public void getAllProductsByOrdarIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        Ordar ordar = OrdarResourceIT.createEntity(em);
        em.persist(ordar);
        em.flush();
        product.addOrdar(ordar);
        productRepository.saveAndFlush(product);
        Long ordarId = ordar.getId();

        // Get all the productList where ordar equals to ordarId
        defaultProductShouldBeFound("ordarId.equals=" + ordarId);

        // Get all the productList where ordar equals to ordarId + 1
        defaultProductShouldNotBeFound("ordarId.equals=" + (ordarId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].ordarDate").value(hasItem(DEFAULT_ORDAR_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));

        // Check, that the count call also returns 1
        restProductMockMvc.perform(get("/api/products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc.perform(get("/api/products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .name(UPDATED_NAME)
            .brand(UPDATED_BRAND)
            .status(UPDATED_STATUS)
            .ordarDate(UPDATED_ORDAR_DATE)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc.perform(put("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testProduct.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProduct.getOrdarDate()).isEqualTo(UPDATED_ORDAR_DATE);
        assertThat(testProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProduct.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc.perform(put("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc.perform(delete("/api/products/{id}", product.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
