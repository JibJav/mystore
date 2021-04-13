package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MystoreApp;
import com.mycompany.myapp.domain.Customer;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.Ordar;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.repository.CustomerRepository;
import com.mycompany.myapp.service.CustomerService;
import com.mycompany.myapp.service.dto.CustomerDTO;
import com.mycompany.myapp.service.mapper.CustomerMapper;
import com.mycompany.myapp.service.dto.CustomerCriteria;
import com.mycompany.myapp.service.CustomerQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CustomerResource} REST controller.
 */
@SpringBootTest(classes = MystoreApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CustomerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CustomerRepository customerRepository;

    @Mock
    private CustomerRepository customerRepositoryMock;

    @Autowired
    private CustomerMapper customerMapper;

    @Mock
    private CustomerService customerServiceMock;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerQueryService customerQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerMockMvc;

    private Customer customer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .timestamp(DEFAULT_TIMESTAMP);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        customer.setUser(user);
        return customer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity(EntityManager em) {
        Customer customer = new Customer()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .timestamp(UPDATED_TIMESTAMP);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        customer.setUser(user);
        return customer;
    }

    @BeforeEach
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();
        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomer.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCustomer.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createCustomerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer with an existing ID
        customer.setId(1L);
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setName(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);


        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setAddress(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);


        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setTimestamp(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);


        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCustomersWithEagerRelationshipsIsEnabled() throws Exception {
        when(customerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerMockMvc.perform(get("/api/customers?eagerload=true"))
            .andExpect(status().isOk());

        verify(customerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCustomersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(customerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerMockMvc.perform(get("/api/customers?eagerload=true"))
            .andExpect(status().isOk());

        verify(customerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
    }


    @Test
    @Transactional
    public void getCustomersByIdFiltering() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        Long id = customer.getId();

        defaultCustomerShouldBeFound("id.equals=" + id);
        defaultCustomerShouldNotBeFound("id.notEquals=" + id);

        defaultCustomerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomerShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomerShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCustomersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where name equals to DEFAULT_NAME
        defaultCustomerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the customerList where name equals to UPDATED_NAME
        defaultCustomerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where name not equals to DEFAULT_NAME
        defaultCustomerShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the customerList where name not equals to UPDATED_NAME
        defaultCustomerShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCustomerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the customerList where name equals to UPDATED_NAME
        defaultCustomerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where name is not null
        defaultCustomerShouldBeFound("name.specified=true");

        // Get all the customerList where name is null
        defaultCustomerShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllCustomersByNameContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where name contains DEFAULT_NAME
        defaultCustomerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the customerList where name contains UPDATED_NAME
        defaultCustomerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where name does not contain DEFAULT_NAME
        defaultCustomerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the customerList where name does not contain UPDATED_NAME
        defaultCustomerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllCustomersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address equals to DEFAULT_ADDRESS
        defaultCustomerShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the customerList where address equals to UPDATED_ADDRESS
        defaultCustomerShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address not equals to DEFAULT_ADDRESS
        defaultCustomerShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the customerList where address not equals to UPDATED_ADDRESS
        defaultCustomerShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultCustomerShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the customerList where address equals to UPDATED_ADDRESS
        defaultCustomerShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address is not null
        defaultCustomerShouldBeFound("address.specified=true");

        // Get all the customerList where address is null
        defaultCustomerShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllCustomersByAddressContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address contains DEFAULT_ADDRESS
        defaultCustomerShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the customerList where address contains UPDATED_ADDRESS
        defaultCustomerShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where address does not contain DEFAULT_ADDRESS
        defaultCustomerShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the customerList where address does not contain UPDATED_ADDRESS
        defaultCustomerShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllCustomersByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where timestamp equals to DEFAULT_TIMESTAMP
        defaultCustomerShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the customerList where timestamp equals to UPDATED_TIMESTAMP
        defaultCustomerShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllCustomersByTimestampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where timestamp not equals to DEFAULT_TIMESTAMP
        defaultCustomerShouldNotBeFound("timestamp.notEquals=" + DEFAULT_TIMESTAMP);

        // Get all the customerList where timestamp not equals to UPDATED_TIMESTAMP
        defaultCustomerShouldBeFound("timestamp.notEquals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllCustomersByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultCustomerShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the customerList where timestamp equals to UPDATED_TIMESTAMP
        defaultCustomerShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllCustomersByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where timestamp is not null
        defaultCustomerShouldBeFound("timestamp.specified=true");

        // Get all the customerList where timestamp is null
        defaultCustomerShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = customer.getUser();
        customerRepository.saveAndFlush(customer);
        Long userId = user.getId();

        // Get all the customerList where user equals to userId
        defaultCustomerShouldBeFound("userId.equals=" + userId);

        // Get all the customerList where user equals to userId + 1
        defaultCustomerShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllCustomersByOrdarIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        Ordar ordar = OrdarResourceIT.createEntity(em);
        em.persist(ordar);
        em.flush();
        customer.addOrdar(ordar);
        customerRepository.saveAndFlush(customer);
        Long ordarId = ordar.getId();

        // Get all the customerList where ordar equals to ordarId
        defaultCustomerShouldBeFound("ordarId.equals=" + ordarId);

        // Get all the customerList where ordar equals to ordarId + 1
        defaultCustomerShouldNotBeFound("ordarId.equals=" + (ordarId + 1));
    }


    @Test
    @Transactional
    public void getAllCustomersByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        Product product = ProductResourceIT.createEntity(em);
        em.persist(product);
        em.flush();
        customer.addProduct(product);
        customerRepository.saveAndFlush(customer);
        Long productId = product.getId();

        // Get all the customerList where product equals to productId
        defaultCustomerShouldBeFound("productId.equals=" + productId);

        // Get all the customerList where product equals to productId + 1
        defaultCustomerShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerShouldBeFound(String filter) throws Exception {
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));

        // Check, that the count call also returns 1
        restCustomerMockMvc.perform(get("/api/customers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerShouldNotBeFound(String filter) throws Exception {
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerMockMvc.perform(get("/api/customers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .timestamp(UPDATED_TIMESTAMP);
        CustomerDTO customerDTO = customerMapper.toDto(updatedCustomer);

        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomer.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCustomer.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Delete the customer
        restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
