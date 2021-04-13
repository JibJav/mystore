package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MystoreApp;
import com.mycompany.myapp.domain.Ordar;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Store;
import com.mycompany.myapp.domain.Customer;
import com.mycompany.myapp.repository.OrdarRepository;
import com.mycompany.myapp.service.OrdarService;
import com.mycompany.myapp.service.dto.OrdarDTO;
import com.mycompany.myapp.service.mapper.OrdarMapper;
import com.mycompany.myapp.service.dto.OrdarCriteria;
import com.mycompany.myapp.service.OrdarQueryService;

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

import com.mycompany.myapp.domain.enumeration.OrdarStatus;
/**
 * Integration tests for the {@link OrdarResource} REST controller.
 */
@SpringBootTest(classes = MystoreApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class OrdarResourceIT {

    private static final String DEFAULT_ORDARWITHNAME = "AAAAAAAAAA";
    private static final String UPDATED_ORDARWITHNAME = "BBBBBBBBBB";

    private static final Long DEFAULT_ORDAR_ID = 1L;
    private static final Long UPDATED_ORDAR_ID = 2L;
    private static final Long SMALLER_ORDAR_ID = 1L - 1L;

    private static final OrdarStatus DEFAULT_STATUS = OrdarStatus.DELIVERED;
    private static final OrdarStatus UPDATED_STATUS = OrdarStatus.NOT_SHIPPED;

    private static final Instant DEFAULT_ORDAR_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDAR_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;
    private static final Long SMALLER_AMOUNT = 1L - 1L;

    private static final Integer DEFAULT_TOTAL_ITEMS = 1;
    private static final Integer UPDATED_TOTAL_ITEMS = 2;
    private static final Integer SMALLER_TOTAL_ITEMS = 1 - 1;

    @Autowired
    private OrdarRepository ordarRepository;

    @Mock
    private OrdarRepository ordarRepositoryMock;

    @Autowired
    private OrdarMapper ordarMapper;

    @Mock
    private OrdarService ordarServiceMock;

    @Autowired
    private OrdarService ordarService;

    @Autowired
    private OrdarQueryService ordarQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdarMockMvc;

    private Ordar ordar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ordar createEntity(EntityManager em) {
        Ordar ordar = new Ordar()
            .ordarwithname(DEFAULT_ORDARWITHNAME)
            .ordarId(DEFAULT_ORDAR_ID)
            .status(DEFAULT_STATUS)
            .ordarDate(DEFAULT_ORDAR_DATE)
            .amount(DEFAULT_AMOUNT)
            .totalItems(DEFAULT_TOTAL_ITEMS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        ordar.setUser(user);
        return ordar;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ordar createUpdatedEntity(EntityManager em) {
        Ordar ordar = new Ordar()
            .ordarwithname(UPDATED_ORDARWITHNAME)
            .ordarId(UPDATED_ORDAR_ID)
            .status(UPDATED_STATUS)
            .ordarDate(UPDATED_ORDAR_DATE)
            .amount(UPDATED_AMOUNT)
            .totalItems(UPDATED_TOTAL_ITEMS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        ordar.setUser(user);
        return ordar;
    }

    @BeforeEach
    public void initTest() {
        ordar = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrdar() throws Exception {
        int databaseSizeBeforeCreate = ordarRepository.findAll().size();
        // Create the Ordar
        OrdarDTO ordarDTO = ordarMapper.toDto(ordar);
        restOrdarMockMvc.perform(post("/api/ordars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ordarDTO)))
            .andExpect(status().isCreated());

        // Validate the Ordar in the database
        List<Ordar> ordarList = ordarRepository.findAll();
        assertThat(ordarList).hasSize(databaseSizeBeforeCreate + 1);
        Ordar testOrdar = ordarList.get(ordarList.size() - 1);
        assertThat(testOrdar.getOrdarwithname()).isEqualTo(DEFAULT_ORDARWITHNAME);
        assertThat(testOrdar.getOrdarId()).isEqualTo(DEFAULT_ORDAR_ID);
        assertThat(testOrdar.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrdar.getOrdarDate()).isEqualTo(DEFAULT_ORDAR_DATE);
        assertThat(testOrdar.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testOrdar.getTotalItems()).isEqualTo(DEFAULT_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    public void createOrdarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ordarRepository.findAll().size();

        // Create the Ordar with an existing ID
        ordar.setId(1L);
        OrdarDTO ordarDTO = ordarMapper.toDto(ordar);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdarMockMvc.perform(post("/api/ordars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ordarDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ordar in the database
        List<Ordar> ordarList = ordarRepository.findAll();
        assertThat(ordarList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkOrdarwithnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordarRepository.findAll().size();
        // set the field null
        ordar.setOrdarwithname(null);

        // Create the Ordar, which fails.
        OrdarDTO ordarDTO = ordarMapper.toDto(ordar);


        restOrdarMockMvc.perform(post("/api/ordars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ordarDTO)))
            .andExpect(status().isBadRequest());

        List<Ordar> ordarList = ordarRepository.findAll();
        assertThat(ordarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrdarIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordarRepository.findAll().size();
        // set the field null
        ordar.setOrdarId(null);

        // Create the Ordar, which fails.
        OrdarDTO ordarDTO = ordarMapper.toDto(ordar);


        restOrdarMockMvc.perform(post("/api/ordars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ordarDTO)))
            .andExpect(status().isBadRequest());

        List<Ordar> ordarList = ordarRepository.findAll();
        assertThat(ordarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrdars() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList
        restOrdarMockMvc.perform(get("/api/ordars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordar.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordarwithname").value(hasItem(DEFAULT_ORDARWITHNAME)))
            .andExpect(jsonPath("$.[*].ordarId").value(hasItem(DEFAULT_ORDAR_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].ordarDate").value(hasItem(DEFAULT_ORDAR_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].totalItems").value(hasItem(DEFAULT_TOTAL_ITEMS)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllOrdarsWithEagerRelationshipsIsEnabled() throws Exception {
        when(ordarServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrdarMockMvc.perform(get("/api/ordars?eagerload=true"))
            .andExpect(status().isOk());

        verify(ordarServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllOrdarsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ordarServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrdarMockMvc.perform(get("/api/ordars?eagerload=true"))
            .andExpect(status().isOk());

        verify(ordarServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getOrdar() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get the ordar
        restOrdarMockMvc.perform(get("/api/ordars/{id}", ordar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordar.getId().intValue()))
            .andExpect(jsonPath("$.ordarwithname").value(DEFAULT_ORDARWITHNAME))
            .andExpect(jsonPath("$.ordarId").value(DEFAULT_ORDAR_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.ordarDate").value(DEFAULT_ORDAR_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.totalItems").value(DEFAULT_TOTAL_ITEMS));
    }


    @Test
    @Transactional
    public void getOrdarsByIdFiltering() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        Long id = ordar.getId();

        defaultOrdarShouldBeFound("id.equals=" + id);
        defaultOrdarShouldNotBeFound("id.notEquals=" + id);

        defaultOrdarShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrdarShouldNotBeFound("id.greaterThan=" + id);

        defaultOrdarShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrdarShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOrdarsByOrdarwithnameIsEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarwithname equals to DEFAULT_ORDARWITHNAME
        defaultOrdarShouldBeFound("ordarwithname.equals=" + DEFAULT_ORDARWITHNAME);

        // Get all the ordarList where ordarwithname equals to UPDATED_ORDARWITHNAME
        defaultOrdarShouldNotBeFound("ordarwithname.equals=" + UPDATED_ORDARWITHNAME);
    }

    @Test
    @Transactional
    public void getAllOrdarsByOrdarwithnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarwithname not equals to DEFAULT_ORDARWITHNAME
        defaultOrdarShouldNotBeFound("ordarwithname.notEquals=" + DEFAULT_ORDARWITHNAME);

        // Get all the ordarList where ordarwithname not equals to UPDATED_ORDARWITHNAME
        defaultOrdarShouldBeFound("ordarwithname.notEquals=" + UPDATED_ORDARWITHNAME);
    }

    @Test
    @Transactional
    public void getAllOrdarsByOrdarwithnameIsInShouldWork() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarwithname in DEFAULT_ORDARWITHNAME or UPDATED_ORDARWITHNAME
        defaultOrdarShouldBeFound("ordarwithname.in=" + DEFAULT_ORDARWITHNAME + "," + UPDATED_ORDARWITHNAME);

        // Get all the ordarList where ordarwithname equals to UPDATED_ORDARWITHNAME
        defaultOrdarShouldNotBeFound("ordarwithname.in=" + UPDATED_ORDARWITHNAME);
    }

    @Test
    @Transactional
    public void getAllOrdarsByOrdarwithnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarwithname is not null
        defaultOrdarShouldBeFound("ordarwithname.specified=true");

        // Get all the ordarList where ordarwithname is null
        defaultOrdarShouldNotBeFound("ordarwithname.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrdarsByOrdarwithnameContainsSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarwithname contains DEFAULT_ORDARWITHNAME
        defaultOrdarShouldBeFound("ordarwithname.contains=" + DEFAULT_ORDARWITHNAME);

        // Get all the ordarList where ordarwithname contains UPDATED_ORDARWITHNAME
        defaultOrdarShouldNotBeFound("ordarwithname.contains=" + UPDATED_ORDARWITHNAME);
    }

    @Test
    @Transactional
    public void getAllOrdarsByOrdarwithnameNotContainsSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarwithname does not contain DEFAULT_ORDARWITHNAME
        defaultOrdarShouldNotBeFound("ordarwithname.doesNotContain=" + DEFAULT_ORDARWITHNAME);

        // Get all the ordarList where ordarwithname does not contain UPDATED_ORDARWITHNAME
        defaultOrdarShouldBeFound("ordarwithname.doesNotContain=" + UPDATED_ORDARWITHNAME);
    }


    @Test
    @Transactional
    public void getAllOrdarsByOrdarIdIsEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarId equals to DEFAULT_ORDAR_ID
        defaultOrdarShouldBeFound("ordarId.equals=" + DEFAULT_ORDAR_ID);

        // Get all the ordarList where ordarId equals to UPDATED_ORDAR_ID
        defaultOrdarShouldNotBeFound("ordarId.equals=" + UPDATED_ORDAR_ID);
    }

    @Test
    @Transactional
    public void getAllOrdarsByOrdarIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarId not equals to DEFAULT_ORDAR_ID
        defaultOrdarShouldNotBeFound("ordarId.notEquals=" + DEFAULT_ORDAR_ID);

        // Get all the ordarList where ordarId not equals to UPDATED_ORDAR_ID
        defaultOrdarShouldBeFound("ordarId.notEquals=" + UPDATED_ORDAR_ID);
    }

    @Test
    @Transactional
    public void getAllOrdarsByOrdarIdIsInShouldWork() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarId in DEFAULT_ORDAR_ID or UPDATED_ORDAR_ID
        defaultOrdarShouldBeFound("ordarId.in=" + DEFAULT_ORDAR_ID + "," + UPDATED_ORDAR_ID);

        // Get all the ordarList where ordarId equals to UPDATED_ORDAR_ID
        defaultOrdarShouldNotBeFound("ordarId.in=" + UPDATED_ORDAR_ID);
    }

    @Test
    @Transactional
    public void getAllOrdarsByOrdarIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarId is not null
        defaultOrdarShouldBeFound("ordarId.specified=true");

        // Get all the ordarList where ordarId is null
        defaultOrdarShouldNotBeFound("ordarId.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdarsByOrdarIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarId is greater than or equal to DEFAULT_ORDAR_ID
        defaultOrdarShouldBeFound("ordarId.greaterThanOrEqual=" + DEFAULT_ORDAR_ID);

        // Get all the ordarList where ordarId is greater than or equal to UPDATED_ORDAR_ID
        defaultOrdarShouldNotBeFound("ordarId.greaterThanOrEqual=" + UPDATED_ORDAR_ID);
    }

    @Test
    @Transactional
    public void getAllOrdarsByOrdarIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarId is less than or equal to DEFAULT_ORDAR_ID
        defaultOrdarShouldBeFound("ordarId.lessThanOrEqual=" + DEFAULT_ORDAR_ID);

        // Get all the ordarList where ordarId is less than or equal to SMALLER_ORDAR_ID
        defaultOrdarShouldNotBeFound("ordarId.lessThanOrEqual=" + SMALLER_ORDAR_ID);
    }

    @Test
    @Transactional
    public void getAllOrdarsByOrdarIdIsLessThanSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarId is less than DEFAULT_ORDAR_ID
        defaultOrdarShouldNotBeFound("ordarId.lessThan=" + DEFAULT_ORDAR_ID);

        // Get all the ordarList where ordarId is less than UPDATED_ORDAR_ID
        defaultOrdarShouldBeFound("ordarId.lessThan=" + UPDATED_ORDAR_ID);
    }

    @Test
    @Transactional
    public void getAllOrdarsByOrdarIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarId is greater than DEFAULT_ORDAR_ID
        defaultOrdarShouldNotBeFound("ordarId.greaterThan=" + DEFAULT_ORDAR_ID);

        // Get all the ordarList where ordarId is greater than SMALLER_ORDAR_ID
        defaultOrdarShouldBeFound("ordarId.greaterThan=" + SMALLER_ORDAR_ID);
    }


    @Test
    @Transactional
    public void getAllOrdarsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where status equals to DEFAULT_STATUS
        defaultOrdarShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the ordarList where status equals to UPDATED_STATUS
        defaultOrdarShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllOrdarsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where status not equals to DEFAULT_STATUS
        defaultOrdarShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the ordarList where status not equals to UPDATED_STATUS
        defaultOrdarShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllOrdarsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultOrdarShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the ordarList where status equals to UPDATED_STATUS
        defaultOrdarShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllOrdarsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where status is not null
        defaultOrdarShouldBeFound("status.specified=true");

        // Get all the ordarList where status is null
        defaultOrdarShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdarsByOrdarDateIsEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarDate equals to DEFAULT_ORDAR_DATE
        defaultOrdarShouldBeFound("ordarDate.equals=" + DEFAULT_ORDAR_DATE);

        // Get all the ordarList where ordarDate equals to UPDATED_ORDAR_DATE
        defaultOrdarShouldNotBeFound("ordarDate.equals=" + UPDATED_ORDAR_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdarsByOrdarDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarDate not equals to DEFAULT_ORDAR_DATE
        defaultOrdarShouldNotBeFound("ordarDate.notEquals=" + DEFAULT_ORDAR_DATE);

        // Get all the ordarList where ordarDate not equals to UPDATED_ORDAR_DATE
        defaultOrdarShouldBeFound("ordarDate.notEquals=" + UPDATED_ORDAR_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdarsByOrdarDateIsInShouldWork() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarDate in DEFAULT_ORDAR_DATE or UPDATED_ORDAR_DATE
        defaultOrdarShouldBeFound("ordarDate.in=" + DEFAULT_ORDAR_DATE + "," + UPDATED_ORDAR_DATE);

        // Get all the ordarList where ordarDate equals to UPDATED_ORDAR_DATE
        defaultOrdarShouldNotBeFound("ordarDate.in=" + UPDATED_ORDAR_DATE);
    }

    @Test
    @Transactional
    public void getAllOrdarsByOrdarDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where ordarDate is not null
        defaultOrdarShouldBeFound("ordarDate.specified=true");

        // Get all the ordarList where ordarDate is null
        defaultOrdarShouldNotBeFound("ordarDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdarsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where amount equals to DEFAULT_AMOUNT
        defaultOrdarShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the ordarList where amount equals to UPDATED_AMOUNT
        defaultOrdarShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllOrdarsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where amount not equals to DEFAULT_AMOUNT
        defaultOrdarShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the ordarList where amount not equals to UPDATED_AMOUNT
        defaultOrdarShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllOrdarsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultOrdarShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the ordarList where amount equals to UPDATED_AMOUNT
        defaultOrdarShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllOrdarsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where amount is not null
        defaultOrdarShouldBeFound("amount.specified=true");

        // Get all the ordarList where amount is null
        defaultOrdarShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdarsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultOrdarShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the ordarList where amount is greater than or equal to UPDATED_AMOUNT
        defaultOrdarShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllOrdarsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where amount is less than or equal to DEFAULT_AMOUNT
        defaultOrdarShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the ordarList where amount is less than or equal to SMALLER_AMOUNT
        defaultOrdarShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllOrdarsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where amount is less than DEFAULT_AMOUNT
        defaultOrdarShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the ordarList where amount is less than UPDATED_AMOUNT
        defaultOrdarShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllOrdarsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where amount is greater than DEFAULT_AMOUNT
        defaultOrdarShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the ordarList where amount is greater than SMALLER_AMOUNT
        defaultOrdarShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllOrdarsByTotalItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where totalItems equals to DEFAULT_TOTAL_ITEMS
        defaultOrdarShouldBeFound("totalItems.equals=" + DEFAULT_TOTAL_ITEMS);

        // Get all the ordarList where totalItems equals to UPDATED_TOTAL_ITEMS
        defaultOrdarShouldNotBeFound("totalItems.equals=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    public void getAllOrdarsByTotalItemsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where totalItems not equals to DEFAULT_TOTAL_ITEMS
        defaultOrdarShouldNotBeFound("totalItems.notEquals=" + DEFAULT_TOTAL_ITEMS);

        // Get all the ordarList where totalItems not equals to UPDATED_TOTAL_ITEMS
        defaultOrdarShouldBeFound("totalItems.notEquals=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    public void getAllOrdarsByTotalItemsIsInShouldWork() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where totalItems in DEFAULT_TOTAL_ITEMS or UPDATED_TOTAL_ITEMS
        defaultOrdarShouldBeFound("totalItems.in=" + DEFAULT_TOTAL_ITEMS + "," + UPDATED_TOTAL_ITEMS);

        // Get all the ordarList where totalItems equals to UPDATED_TOTAL_ITEMS
        defaultOrdarShouldNotBeFound("totalItems.in=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    public void getAllOrdarsByTotalItemsIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where totalItems is not null
        defaultOrdarShouldBeFound("totalItems.specified=true");

        // Get all the ordarList where totalItems is null
        defaultOrdarShouldNotBeFound("totalItems.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdarsByTotalItemsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where totalItems is greater than or equal to DEFAULT_TOTAL_ITEMS
        defaultOrdarShouldBeFound("totalItems.greaterThanOrEqual=" + DEFAULT_TOTAL_ITEMS);

        // Get all the ordarList where totalItems is greater than or equal to UPDATED_TOTAL_ITEMS
        defaultOrdarShouldNotBeFound("totalItems.greaterThanOrEqual=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    public void getAllOrdarsByTotalItemsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where totalItems is less than or equal to DEFAULT_TOTAL_ITEMS
        defaultOrdarShouldBeFound("totalItems.lessThanOrEqual=" + DEFAULT_TOTAL_ITEMS);

        // Get all the ordarList where totalItems is less than or equal to SMALLER_TOTAL_ITEMS
        defaultOrdarShouldNotBeFound("totalItems.lessThanOrEqual=" + SMALLER_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    public void getAllOrdarsByTotalItemsIsLessThanSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where totalItems is less than DEFAULT_TOTAL_ITEMS
        defaultOrdarShouldNotBeFound("totalItems.lessThan=" + DEFAULT_TOTAL_ITEMS);

        // Get all the ordarList where totalItems is less than UPDATED_TOTAL_ITEMS
        defaultOrdarShouldBeFound("totalItems.lessThan=" + UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    public void getAllOrdarsByTotalItemsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        // Get all the ordarList where totalItems is greater than DEFAULT_TOTAL_ITEMS
        defaultOrdarShouldNotBeFound("totalItems.greaterThan=" + DEFAULT_TOTAL_ITEMS);

        // Get all the ordarList where totalItems is greater than SMALLER_TOTAL_ITEMS
        defaultOrdarShouldBeFound("totalItems.greaterThan=" + SMALLER_TOTAL_ITEMS);
    }


    @Test
    @Transactional
    public void getAllOrdarsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = ordar.getUser();
        ordarRepository.saveAndFlush(ordar);
        Long userId = user.getId();

        // Get all the ordarList where user equals to userId
        defaultOrdarShouldBeFound("userId.equals=" + userId);

        // Get all the ordarList where user equals to userId + 1
        defaultOrdarShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllOrdarsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);
        Product product = ProductResourceIT.createEntity(em);
        em.persist(product);
        em.flush();
        ordar.addProduct(product);
        ordarRepository.saveAndFlush(ordar);
        Long productId = product.getId();

        // Get all the ordarList where product equals to productId
        defaultOrdarShouldBeFound("productId.equals=" + productId);

        // Get all the ordarList where product equals to productId + 1
        defaultOrdarShouldNotBeFound("productId.equals=" + (productId + 1));
    }


    @Test
    @Transactional
    public void getAllOrdarsByStoreIsEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);
        Store store = StoreResourceIT.createEntity(em);
        em.persist(store);
        em.flush();
        ordar.setStore(store);
        ordarRepository.saveAndFlush(ordar);
        Long storeId = store.getId();

        // Get all the ordarList where store equals to storeId
        defaultOrdarShouldBeFound("storeId.equals=" + storeId);

        // Get all the ordarList where store equals to storeId + 1
        defaultOrdarShouldNotBeFound("storeId.equals=" + (storeId + 1));
    }


    @Test
    @Transactional
    public void getAllOrdarsByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);
        Customer customer = CustomerResourceIT.createEntity(em);
        em.persist(customer);
        em.flush();
        ordar.setCustomer(customer);
        ordarRepository.saveAndFlush(ordar);
        Long customerId = customer.getId();

        // Get all the ordarList where customer equals to customerId
        defaultOrdarShouldBeFound("customerId.equals=" + customerId);

        // Get all the ordarList where customer equals to customerId + 1
        defaultOrdarShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrdarShouldBeFound(String filter) throws Exception {
        restOrdarMockMvc.perform(get("/api/ordars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordar.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordarwithname").value(hasItem(DEFAULT_ORDARWITHNAME)))
            .andExpect(jsonPath("$.[*].ordarId").value(hasItem(DEFAULT_ORDAR_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].ordarDate").value(hasItem(DEFAULT_ORDAR_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].totalItems").value(hasItem(DEFAULT_TOTAL_ITEMS)));

        // Check, that the count call also returns 1
        restOrdarMockMvc.perform(get("/api/ordars/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrdarShouldNotBeFound(String filter) throws Exception {
        restOrdarMockMvc.perform(get("/api/ordars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrdarMockMvc.perform(get("/api/ordars/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingOrdar() throws Exception {
        // Get the ordar
        restOrdarMockMvc.perform(get("/api/ordars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrdar() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        int databaseSizeBeforeUpdate = ordarRepository.findAll().size();

        // Update the ordar
        Ordar updatedOrdar = ordarRepository.findById(ordar.getId()).get();
        // Disconnect from session so that the updates on updatedOrdar are not directly saved in db
        em.detach(updatedOrdar);
        updatedOrdar
            .ordarwithname(UPDATED_ORDARWITHNAME)
            .ordarId(UPDATED_ORDAR_ID)
            .status(UPDATED_STATUS)
            .ordarDate(UPDATED_ORDAR_DATE)
            .amount(UPDATED_AMOUNT)
            .totalItems(UPDATED_TOTAL_ITEMS);
        OrdarDTO ordarDTO = ordarMapper.toDto(updatedOrdar);

        restOrdarMockMvc.perform(put("/api/ordars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ordarDTO)))
            .andExpect(status().isOk());

        // Validate the Ordar in the database
        List<Ordar> ordarList = ordarRepository.findAll();
        assertThat(ordarList).hasSize(databaseSizeBeforeUpdate);
        Ordar testOrdar = ordarList.get(ordarList.size() - 1);
        assertThat(testOrdar.getOrdarwithname()).isEqualTo(UPDATED_ORDARWITHNAME);
        assertThat(testOrdar.getOrdarId()).isEqualTo(UPDATED_ORDAR_ID);
        assertThat(testOrdar.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrdar.getOrdarDate()).isEqualTo(UPDATED_ORDAR_DATE);
        assertThat(testOrdar.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testOrdar.getTotalItems()).isEqualTo(UPDATED_TOTAL_ITEMS);
    }

    @Test
    @Transactional
    public void updateNonExistingOrdar() throws Exception {
        int databaseSizeBeforeUpdate = ordarRepository.findAll().size();

        // Create the Ordar
        OrdarDTO ordarDTO = ordarMapper.toDto(ordar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdarMockMvc.perform(put("/api/ordars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ordarDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ordar in the database
        List<Ordar> ordarList = ordarRepository.findAll();
        assertThat(ordarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrdar() throws Exception {
        // Initialize the database
        ordarRepository.saveAndFlush(ordar);

        int databaseSizeBeforeDelete = ordarRepository.findAll().size();

        // Delete the ordar
        restOrdarMockMvc.perform(delete("/api/ordars/{id}", ordar.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ordar> ordarList = ordarRepository.findAll();
        assertThat(ordarList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
