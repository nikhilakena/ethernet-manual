package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.client.asset.ordermanager.AgentDTO;

import java.util.Optional;


public class AgentsDTOFactory {

    public static Optional<AgentDTO> defaultAgentDTO() {
        return Optional.of(AgentDTO.builder()
                .name("Test Agent")
                .email("test@test.com").build());
    }
}
