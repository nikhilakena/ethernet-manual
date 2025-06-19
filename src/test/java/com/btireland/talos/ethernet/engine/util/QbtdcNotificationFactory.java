package com.btireland.talos.ethernet.engine.util;


import com.btireland.talos.ethernet.engine.client.asset.notcom.Notifications;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.Base64;


public class QbtdcNotificationFactory {

    public static String asJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(object);
    }

    public static Notifications completeNotificationRequest() throws IOException {
        return Notifications.builder()
                .source("TALOS")
                .content(Base64.getDecoder().decode("ewoJIm9yZGVySWQiOiAyNTc1MSwKCSJ0YWxvc09yZGVySWQiOiA3OTksCgkib2FvIjogInZvZGFmb25lIiwKCSJvYm8iOiAidm9kYWZvbmUiLAoJInByb2plY3RLZXkiOiAicHJval9rZXkiLAoJImRhdGFDb250cmFjdCI6ICJXQUciLAoJIm9yaWdpbmF0b3JDb2RlIjogIkVYVCIsCgkicmVzZWxsZXJUcmFuc2FjdGlvbklkIjogIjAiLAoJInJlc2VsbGVyT3JkZXJSZXF1ZXN0RGF0ZVRpbWUiOiAiMTEvMTEvMjAyMCAwOTo0NDoxMCIsCgkib3BlcmF0b3JOYW1lIjogIioqKioiLAoJIm9wZXJhdG9yQ29kZSI6ICIqKioqIiwKCSJvcmRlck51bWJlciI6ICJCVC1RQlREQy0yNTc1MSIsCgkiYWNjb3VudE51bWJlciI6ICIxMjM0NTY3ODkwMTIzNDU2Nzg5IiwKCSJjcmVhdGVkQXQiOiAiMjAyMC0xMS0xMVQwOTo0NDoxMC43NzczMTciLAoJInNlcnZpY2VUeXBlIjogIlFCVERDIiwKCSJpbnRlcm5hbFRyYWNraW5nT3JkZXJSZWZlcmVuY2UiOiAiVEFMT1MtMjU3NTEtUUJUREMtMSIsCgkicWJ0ZGMiOiB7CgkJInJlY3VycmluZ0ZyZXF1ZW5jeSI6ICJBTk5VQUwiLAoJCSJxdW90ZUl0ZW1zIjogW3sKCQkJInN0YXR1cyI6ICJSZWplY3RlZCIsCgkJCSJwcm9kdWN0IjogIldJQyIsCgkJCSJxdW90ZUl0ZW1JZCI6IDEsCgkJCSJyZWplY3Rpb25EZXRhaWxzIjogewoJCQkJInJlamVjdENvZGUiOiAiMSIsCgkJCQkicmVqZWN0UmVhc29uIjogIlNMQSBpcyBub3QgcmVxdWlyZWQgZm9yIFByb3ZpZGUgQWNjZXNzIgoJCQl9LAoJCQkibm9uUmVjdXJyaW5nUHJpY2UiOiAiMzQiLAoJCQkicmVjdXJyaW5nUHJpY2UiOiAiNTUiLAoJCQkidGVybSI6IDUsCgoJCQkibG9naWNhbExpbmsiOiB7CgkJCQkiaXBSYW5nZSI6ICIzMSIsCgkJCQkiYWN0aW9uIjogIlAiLAoKCQkJCSJiYW5kV2lkdGgiOiAiOTAwMCIsCgkJCQkicHJvZmlsZSI6ICJQUkVNSVVNXzEwMCIKCQkJfSwKCQkJImN1c3RvbWVyQWNjZXNzIjogewoJCQkJImFjdGlvbiI6ICJQIiwKCQkJCSJzdXBwbGllciI6ICJPTi1ORVQiLAoJCQkJInRhcmdldEFjY2Vzc1N1cHBsaWVyIjogInRhcyIsCgkJCQkiYmFuZFdpZHRoIjogIjEwMCIsCgkJCQkic2xhIjogIkVOSEFOQ0VEIiwKCQkJCSJzaXRlIjogewoJCQkJCSJsb2NhdGlvbiI6IHsKCQkJCQkJImlkIjogIkFGMDAwMDMyIiwKCQkJCQkJIm5ldHdvcmtTdGF0dXMiOiAibmV0c3QiLAoJCQkJCQkibXVsdGlFaXJjb2RlIjogIlkiLAoJCQkJCQkiYWRkcmVzcyI6IHsKCQkJCQkJCSJmdWxsQWRkcmVzcyI6ICJhZGRyZXNzIgoJCQkJCQl9CgkJCQkJfQoJCQkJfQoJCQl9LAoJCQkid2hvbGVzYWxlckFjY2VzcyI6IHsKCQkJCSJhY3Rpb24iOiAiQ0giLAoJCQkJInNpdGUiOiB7CgkJCQkJImxvY2F0aW9uIjogewoJCQkJCQkiaWQiOiAiRVFVSU5JWF9EQjEiCgoJCQkJCX0KCQkJCX0KCgkJCX0KCgkJfV0KCgl9Cn0="))
                .type("C")
                .reference("BT-QBTDC-25751")
                .build();
    }
}
