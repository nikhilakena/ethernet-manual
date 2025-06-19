package com.btireland.talos.ethernet.engine.client.asset.notcom;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "notification", url = "${application.talos.notcom.url}")
public interface NotificationClient {

    @PostMapping(value = "/api/v1/notification", consumes = "application/json")
    Notifications createNotification(Notifications notComRequest);

}
