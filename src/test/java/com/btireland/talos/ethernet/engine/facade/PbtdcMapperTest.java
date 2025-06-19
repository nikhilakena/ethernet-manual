package com.btireland.talos.ethernet.engine.facade;

import com.btireland.talos.core.common.test.tag.UnitTest;
import com.btireland.talos.ethernet.engine.domain.Pbtdc;
import com.btireland.talos.ethernet.engine.dto.PbtdcDTO;
import com.btireland.talos.ethernet.engine.util.PbtdcDTOFactory;
import com.btireland.talos.ethernet.engine.util.PbtdcFactory;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

@UnitTest
@Slf4j
class PbtdcMapperTest {
    private static final PbtdcMapper mapper = Mappers.getMapper(PbtdcMapper.class);

    @Test
    void toPbtdcDTO() {

        Pbtdc pbtdc = PbtdcFactory.defaultPbtdc();

        PbtdcDTO expected = PbtdcDTOFactory.defaultPbtdcDTO();

        PbtdcDTO actual = mapper.toPbtdcDTO(pbtdc);

        Assertions.assertThat(actual).isEqualTo(expected);

    }

}