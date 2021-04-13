package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class OrdarTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ordar.class);
        Ordar ordar1 = new Ordar();
        ordar1.setId(1L);
        Ordar ordar2 = new Ordar();
        ordar2.setId(ordar1.getId());
        assertThat(ordar1).isEqualTo(ordar2);
        ordar2.setId(2L);
        assertThat(ordar1).isNotEqualTo(ordar2);
        ordar1.setId(null);
        assertThat(ordar1).isNotEqualTo(ordar2);
    }
}
