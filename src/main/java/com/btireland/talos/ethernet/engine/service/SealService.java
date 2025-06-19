package com.btireland.talos.ethernet.engine.service;


import com.btireland.talos.core.common.rest.exception.BadRequestException;
import com.btireland.talos.ethernet.engine.client.asset.seal.SealClient;
import com.btireland.talos.ethernet.engine.dto.EmailDTO;
import com.btireland.talos.ethernet.engine.facade.PbtdcMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SealService {

    private SealClient sealClient;

    private PbtdcMapper pbtdcMapper;

    public SealService(SealClient sealClient, PbtdcMapper pbtdcMapper) {
        this.sealClient = sealClient;
        this.pbtdcMapper = pbtdcMapper;
    }

    /**
     *
     * @param order
     * @return
     * @throws BadRequestException if the request is invalid
     */
    public String createPbtdcOrderForSeal(EmailDTO email) {
        return sealClient.createOrder(email);
    }

}
