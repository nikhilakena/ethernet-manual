package com.btireland.talos.ethernet.engine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamSource;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailAttachmentDTO {

    InputStreamSource fileContent;
    String contentType;
    String fileName;
}
