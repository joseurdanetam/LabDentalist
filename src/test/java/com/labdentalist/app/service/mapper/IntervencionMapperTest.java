package com.labdentalist.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntervencionMapperTest {

    private IntervencionMapper intervencionMapper;

    @BeforeEach
    public void setUp() {
        intervencionMapper = new IntervencionMapperImpl();
    }
}
