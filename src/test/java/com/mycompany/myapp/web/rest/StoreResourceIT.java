package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MystoreApp;
import com.mycompany.myapp.domain.Store;
import com.mycompany.myapp.domain.Ordar;
import com.mycompany.myapp.repository.StoreRepository;
import com.mycompany.myapp.service.StoreService;
import com.mycompany.myapp.service.dto.StoreDTO;
import com.mycompany.myapp.service.mapper.StoreMapper;
import com.mycompany.myapp.service.dto.StoreCriteria;
import com.mycompany.myapp.service.StoreQueryService;

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

/**
 * Integration tests for the {@link StoreResource} REST controller.
 */
@SpringBootTest(classes = MystoreApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class StoreResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreQueryService storeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStoreMockMvc;

    private Store store;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Store createEntity(EntityManager em) {
        Store store = new Store()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .timestamp(DEFAULT_TIMESTAMP);
        return store;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Store createUpdatedEntity(EntityManager em) {
        Store store = new Store()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .timestamp(UPDATED_TIMESTAMP);
        return store;
    }

    @BeforeEach
    public void initTest() {
        store = createEntity(em);
    }

    @Test
    @Transactional
    public void createStore() throws Exception {
        int databaseSizeBeforeCreate = storeRepository.findAll().size();
        // Create the Store
        StoreDTO storeDTO = storeMapper.toDto(store);
        restStoreMockMvc.perform(post("/api/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isCreated());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeCreate + 1);
        Store testStore = storeList.get(storeList.size() - 1);
        assertThat(testStore.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStore.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testStore.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createStoreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storeRepository.findAll().size();

        // Create the Store with an existing ID
        store.setId(1L);
        StoreDTO storeDTO = storeMapper.toDto(store);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoreMockMvc.perform(post("/api/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeRepository.findAll().size();
        // set the field null
        store.setName(null);

        // Create the Store, which fails.
        StoreDTO storeDTO = storeMapper.toDto(store);


        restStoreMockMvc.perform(post("/api/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeRepository.findAll().size();
        // set the field null
        store.setAddress(null);

        // Create the Store, which fails.
        StoreDTO storeDTO = storeMapper.toDto(store);


        restStoreMockMvc.perform(post("/api/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeRepository.findAll().size();
        // set the field null
        store.setTimestamp(null);

        // Create the Store, which fails.
        StoreDTO storeDTO = storeMapper.toDto(store);


        restStoreMockMvc.perform(post("/api/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStores() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList
        restStoreMockMvc.perform(get("/api/stores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(store.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }
    
    @Test
    @Transactional
    public void getStore() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get the store
        restStoreMockMvc.perform(get("/api/stores/{id}", store.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(store.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
    }


    @Test
    @Transactional
    public void getStoresByIdFiltering() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        Long id = store.getId();

        defaultStoreShouldBeFound("id.equals=" + id);
        defaultStoreShouldNotBeFound("id.notEquals=" + id);

        defaultStoreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStoreShouldNotBeFound("id.greaterThan=" + id);

        defaultStoreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStoreShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllStoresByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where name equals to DEFAULT_NAME
        defaultStoreShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the storeList where name equals to UPDATED_NAME
        defaultStoreShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStoresByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where name not equals to DEFAULT_NAME
        defaultStoreShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the storeList where name not equals to UPDATED_NAME
        defaultStoreShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStoresByNameIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultStoreShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the storeList where name equals to UPDATED_NAME
        defaultStoreShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStoresByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where name is not null
        defaultStoreShouldBeFound("name.specified=true");

        // Get all the storeList where name is null
        defaultStoreShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllStoresByNameContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where name contains DEFAULT_NAME
        defaultStoreShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the storeList where name contains UPDATED_NAME
        defaultStoreShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStoresByNameNotContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where name does not contain DEFAULT_NAME
        defaultStoreShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the storeList where name does not contain UPDATED_NAME
        defaultStoreShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllStoresByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where address equals to DEFAULT_ADDRESS
        defaultStoreShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the storeList where address equals to UPDATED_ADDRESS
        defaultStoreShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllStoresByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where address not equals to DEFAULT_ADDRESS
        defaultStoreShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the storeList where address not equals to UPDATED_ADDRESS
        defaultStoreShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllStoresByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultStoreShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the storeList where address equals to UPDATED_ADDRESS
        defaultStoreShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllStoresByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where address is not null
        defaultStoreShouldBeFound("address.specified=true");

        // Get all the storeList where address is null
        defaultStoreShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllStoresByAddressContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where address contains DEFAULT_ADDRESS
        defaultStoreShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the storeList where address contains UPDATED_ADDRESS
        defaultStoreShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllStoresByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where address does not contain DEFAULT_ADDRESS
        defaultStoreShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the storeList where address does not contain UPDATED_ADDRESS
        defaultStoreShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllStoresByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where timestamp equals to DEFAULT_TIMESTAMP
        defaultStoreShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the storeList where timestamp equals to UPDATED_TIMESTAMP
        defaultStoreShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllStoresByTimestampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where timestamp not equals to DEFAULT_TIMESTAMP
        defaultStoreShouldNotBeFound("timestamp.notEquals=" + DEFAULT_TIMESTAMP);

        // Get all the storeList where timestamp not equals to UPDATED_TIMESTAMP
        defaultStoreShouldBeFound("timestamp.notEquals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllStoresByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultStoreShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the storeList where timestamp equals to UPDATED_TIMESTAMP
        defaultStoreShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllStoresByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        // Get all the storeList where timestamp is not null
        defaultStoreShouldBeFound("timestamp.specified=true");

        // Get all the storeList where timestamp is null
        defaultStoreShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllStoresByOrdarIsEqualToSomething() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);
        Ordar ordar = OrdarResourceIT.createEntity(em);
        em.persist(ordar);
        em.flush();
        store.addOrdar(ordar);
        storeRepository.saveAndFlush(store);
        Long ordarId = ordar.getId();

        // Get all the storeList where ordar equals to ordarId
        defaultStoreShouldBeFound("ordarId.equals=" + ordarId);

        // Get all the storeList where ordar equals to ordarId + 1
        defaultStoreShouldNotBeFound("ordarId.equals=" + (ordarId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStoreShouldBeFound(String filter) throws Exception {
        restStoreMockMvc.perform(get("/api/stores?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(store.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));

        // Check, that the count call also returns 1
        restStoreMockMvc.perform(get("/api/stores/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStoreShouldNotBeFound(String filter) throws Exception {
        restStoreMockMvc.perform(get("/api/stores?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStoreMockMvc.perform(get("/api/stores/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingStore() throws Exception {
        // Get the store
        restStoreMockMvc.perform(get("/api/stores/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStore() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        int databaseSizeBeforeUpdate = storeRepository.findAll().size();

        // Update the store
        Store updatedStore = storeRepository.findById(store.getId()).get();
        // Disconnect from session so that the updates on updatedStore are not directly saved in db
        em.detach(updatedStore);
        updatedStore
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .timestamp(UPDATED_TIMESTAMP);
        StoreDTO storeDTO = storeMapper.toDto(updatedStore);

        restStoreMockMvc.perform(put("/api/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isOk());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);
        Store testStore = storeList.get(storeList.size() - 1);
        assertThat(testStore.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStore.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testStore.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void updateNonExistingStore() throws Exception {
        int databaseSizeBeforeUpdate = storeRepository.findAll().size();

        // Create the Store
        StoreDTO storeDTO = storeMapper.toDto(store);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoreMockMvc.perform(put("/api/stores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(storeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Store in the database
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStore() throws Exception {
        // Initialize the database
        storeRepository.saveAndFlush(store);

        int databaseSizeBeforeDelete = storeRepository.findAll().size();

        // Delete the store
        restStoreMockMvc.perform(delete("/api/stores/{id}", store.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Store> storeList = storeRepository.findAll();
        assertThat(storeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
