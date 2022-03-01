package com.labdentalist.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.labdentalist.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntervencionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Intervencion.class);
        Intervencion intervencion1 = new Intervencion();
        intervencion1.setId(1L);
        Intervencion intervencion2 = new Intervencion();
        intervencion2.setId(intervencion1.getId());
        assertThat(intervencion1).isEqualTo(intervencion2);
        intervencion2.setId(2L);
        assertThat(intervencion1).isNotEqualTo(intervencion2);
        intervencion1.setId(null);
        assertThat(intervencion1).isNotEqualTo(intervencion2);
    }
}
