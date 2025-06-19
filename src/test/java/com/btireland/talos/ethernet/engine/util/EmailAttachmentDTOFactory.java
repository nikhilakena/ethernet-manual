package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.dto.EmailAttachmentDTO;
import org.springframework.core.io.ByteArrayResource;

import java.util.ArrayList;
import java.util.List;


public class EmailAttachmentDTOFactory {

    public static List<EmailAttachmentDTO> defaultEmailAttachmentDTOList() {
        List<EmailAttachmentDTO> attachmentDTOList = new ArrayList<>();
        byte[] excelReport ="This is an excel report".getBytes();
        byte[] pdfReport = "This is a pdf report".getBytes();

        attachmentDTOList.add(EmailAttachmentDTO.builder()
                .fileContent(new ByteArrayResource(pdfReport))
                .fileName("sky-2022-01-08" + ".pdf")
                .contentType("application/pdf")
                .build());

        attachmentDTOList.add(EmailAttachmentDTO.builder()
                .fileContent(new ByteArrayResource(excelReport))
                .fileName("sky-2022-01-08" + ".xlsx")
                .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .build());
        return attachmentDTOList;
    }
}
