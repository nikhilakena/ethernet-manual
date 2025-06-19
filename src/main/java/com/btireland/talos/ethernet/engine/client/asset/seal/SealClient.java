package com.btireland.talos.ethernet.engine.client.asset.seal;

import com.btireland.talos.ethernet.engine.dto.EmailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "seal", url = "${application.talos.seal.url}")
public interface SealClient {

    @PostMapping(value = "api/v1/pbtdc-order", consumes = "application/json")
    String createOrder(EmailDTO email);

}
