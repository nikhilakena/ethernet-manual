package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.config.ApplicationConfiguration;
import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Service;

@Service
public class OaoUtils {

    private ApplicationConfiguration applicationConfiguration;

    public OaoUtils(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    public String translateOaoName(String oao){
        if(applicationConfiguration.getOaoDetails().containsKey(oao)){
            return applicationConfiguration.getOaoDetails().get(oao).getName();
        }else{
            return WordUtils.capitalize(oao);
        }
    }
}
