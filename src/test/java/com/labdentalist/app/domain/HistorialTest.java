package com.labdentalist.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.labdentalist.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistorialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Historial.class);
        Historial historial1 = new Historial();
        historial1.setId(1L);
        Historial historial2 = new Historial();
        historial2.setId(historial1.getId());
        assertThat(historial1).isEqualTo(historial2);
        historial2.setId(2L);
        assertThat(historial1).isNotEqualTo(historial2);
        historial1.setId(null);
        assertThat(historial1).isNotEqualTo(historial2);
    }
}
