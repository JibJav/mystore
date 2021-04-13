package com.mycompany.myapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class OrdarDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrdarDTO.class);
        OrdarDTO ordarDTO1 = new OrdarDTO();
        ordarDTO1.setId(1L);
        OrdarDTO ordarDTO2 = new OrdarDTO();
        assertThat(ordarDTO1).isNotEqualTo(ordarDTO2);
        ordarDTO2.setId(ordarDTO1.getId());
        assertThat(ordarDTO1).isEqualTo(ordarDTO2);
        ordarDTO2.setId(2L);
        assertThat(ordarDTO1).isNotEqualTo(ordarDTO2);
        ordarDTO1.setId(null);
        assertThat(ordarDTO1).isNotEqualTo(ordarDTO2);
    }
}
