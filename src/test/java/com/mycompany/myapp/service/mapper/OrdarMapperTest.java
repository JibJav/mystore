package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class OrdarMapperTest {

    private OrdarMapper ordarMapper;

    @BeforeEach
    public void setUp() {
        ordarMapper = new OrdarMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(ordarMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(ordarMapper.fromId(null)).isNull();
    }
}
