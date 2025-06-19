package com.btireland.talos.ethernet.engine.util;


import com.btireland.talos.ethernet.engine.client.asset.notcom.Notifications;
import com.btireland.talos.ethernet.engine.domain.Orders;
import com.btireland.talos.ethernet.engine.domain.ParkedNotifications;
import com.btireland.talos.ethernet.engine.dto.NotificationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class NotificationFactory {

    public static String asJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(object);
    }

    public static NotificationDTO undeliverableNotificationRequest() throws IOException {
        return NotificationDTO.builder()
                .source("SIEBEL")
                .content(IOUtils.toString(NotificationFactory.class
                                .getResourceAsStream("/data/UndeliverableNotification.xml"),
                        StandardCharsets.UTF_8).getBytes())
                .type("U")
                .reference("TALOS-123-PBTDC-1")
                .build();
    }

    public static NotificationDTO acceptNotificationRequest() throws IOException {
        return NotificationDTO.builder()
                .source("SIEBEL")
                .content(IOUtils.toString(NotificationFactory.class
                                .getResourceAsStream("/data/AcceptNotification.xml"),
                        StandardCharsets.UTF_8).getBytes())
                .type("A")
                .reference("TALOS-123-PBTDC-1")
                .build();
    }

    public static Notifications undeliverableNotcomNotification() throws IOException {
        return Notifications.builder()
                .source("TALOS")
                .content(Base64.getDecoder().decode("eyJvcmRlcklkIjoxMjMsInRhbG9zT3JkZXJJZCI6bnVsbCwib2FvIjoic2t5IiwiYWdlbnQiOm51bGwsImRhdGFDb250cmFjdCI6IldBRyIsIm9yaWdpbmF0b3JDb2RlIjoiRVhUIiwicmVzZWxsZXJUcmFuc2FjdGlvbklkIjoiMTAwIiwicmVzZWxsZXJPcmRlclJlcXVlc3REYXRlVGltZSI6IjAzLzAyLzIwMjIgMTA6NDM6NTUiLCJvcGVyYXRvck5hbWUiOiJTS1kiLCJvcGVyYXRvckNvZGUiOiI4Mzc1OCIsIm9yZGVyTnVtYmVyIjoiQlQtUEJUREMtMTIzIiwiY3JlYXRlZEF0IjoiMjAyMi0wMi0wM1QxNToxMDo1NiIsInNlcnZpY2VUeXBlIjoiUEJUREMiLCJzZXJ2aWNlQ2xhc3MiOm51bGwsInNlcnZpY2VMb2NhdGlvbkFkZHJlc3NJZCI6bnVsbCwibG9jYXRpb25EZXNjcmlwdGlvbiI6bnVsbCwib3JkZXJUeXBlIjpudWxsLCJ0ZWNobmljaWFuTm90ZXMiOm51bGwsImludGVybmFsVHJhY2tpbmdPcmRlclJlZmVyZW5jZSI6bnVsbCwic3VwcGxpZXJPcmRlcklkIjpudWxsLCJhY2NvdW50TnVtYmVyIjpudWxsLCJtYWpvckFjY291bnROdW1iZXIiOm51bGwsImR1ZUNvbXBsZXRpb25EYXRlIjpudWxsLCJlc3RpbWF0ZWRDb21wbGV0aW9uRGF0ZSI6bnVsbCwibm9uU3RhbmRhcmRGbGFnIjpudWxsLCJub25TdGFuZGFyZFJlYXNvbiI6bnVsbCwiY29uZmlybWF0aW9uUmVzdWx0IjpudWxsLCJyZWZvcmVjYXN0RHVlRGF0ZSI6bnVsbCwib3JkZXJGaWxlTmFtZSI6bnVsbCwibW9kZSI6bnVsbCwibGFzdE5vdGlmaWNhdGlvblR5cGUiOiJVIiwidW5pcXVlSWQiOm51bGwsIndhc3NldCI6bnVsbCwicHJvZHVjdEdyb3VwIjpudWxsLCJvcmRlclN0YXR1cyI6bnVsbCwic3RhdHVzTm90ZXMiOm51bGwsInN1cHBsaWVyTm90aWZpY2F0aW9uSWQiOm51bGwsInJlamVjdGlvbkRldGFpbHMiOnsicmVqZWN0Q29kZSI6IjEwNSIsInJlamVjdFJlYXNvbiI6IkNhbmNlbGxhdGlvbiJ9LCJjb250YWN0RGV0YWlscyI6bnVsbCwib3JpZ2luYWxPcmRlclJlZiI6bnVsbCwiY3VzdG9tZXJEZWxheSI6bnVsbCwicGJ0ZGMiOm51bGx9"))
                .type("U")
                .reference("BT-PBTDC-123")
                .build();
    }

    public static Notifications acceptNotcomNotification() throws IOException {
        return Notifications.builder()
                .source("TALOS")
                .content(Base64.getDecoder().decode("eyJvcmRlcklkIjoxMjMsInRhbG9zT3JkZXJJZCI6bnVsbCwib2FvIjoic2t5IiwiYWdlbnQiOm51bGwsImRhdGFDb250cmFjdCI6IldBRyIsIm9yaWdpbmF0b3JDb2RlIjoiRVhUIiwicmVzZWxsZXJUcmFuc2FjdGlvbklkIjoiMTAwIiwicmVzZWxsZXJPcmRlclJlcXVlc3REYXRlVGltZSI6IjAzLzAyLzIwMjIgMTA6NDM6NTUiLCJvcGVyYXRvck5hbWUiOiJTS1kiLCJvcGVyYXRvckNvZGUiOiI4Mzc1OCIsIm9yZGVyTnVtYmVyIjoiQlQtUEJUREMtMTIzIiwiY3JlYXRlZEF0IjoiMjAyMi0wMi0wM1QxNToxMDo1NiIsInNlcnZpY2VUeXBlIjoiUEJUREMiLCJzZXJ2aWNlQ2xhc3MiOm51bGwsInNlcnZpY2VMb2NhdGlvbkFkZHJlc3NJZCI6bnVsbCwibG9jYXRpb25EZXNjcmlwdGlvbiI6bnVsbCwib3JkZXJUeXBlIjpudWxsLCJ0ZWNobmljaWFuTm90ZXMiOm51bGwsImludGVybmFsVHJhY2tpbmdPcmRlclJlZmVyZW5jZSI6bnVsbCwic3VwcGxpZXJPcmRlcklkIjpudWxsLCJhY2NvdW50TnVtYmVyIjpudWxsLCJtYWpvckFjY291bnROdW1iZXIiOm51bGwsImR1ZUNvbXBsZXRpb25EYXRlIjpudWxsLCJlc3RpbWF0ZWRDb21wbGV0aW9uRGF0ZSI6bnVsbCwibm9uU3RhbmRhcmRGbGFnIjpudWxsLCJub25TdGFuZGFyZFJlYXNvbiI6bnVsbCwiY29uZmlybWF0aW9uUmVzdWx0IjpudWxsLCJyZWZvcmVjYXN0RHVlRGF0ZSI6bnVsbCwib3JkZXJGaWxlTmFtZSI6bnVsbCwibW9kZSI6bnVsbCwibGFzdE5vdGlmaWNhdGlvblR5cGUiOiJVIiwidW5pcXVlSWQiOm51bGwsIndhc3NldCI6bnVsbCwicHJvZHVjdEdyb3VwIjpudWxsLCJvcmRlclN0YXR1cyI6bnVsbCwic3RhdHVzTm90ZXMiOm51bGwsInN1cHBsaWVyTm90aWZpY2F0aW9uSWQiOm51bGwsInJlamVjdGlvbkRldGFpbHMiOm51bGwsImNvbnRhY3REZXRhaWxzIjpudWxsLCJvcmlnaW5hbE9yZGVyUmVmIjpudWxsLCJjdXN0b21lckRlbGF5IjpudWxsLCAicGJ0ZGMiOiB7ICJvcmRlck1hbmFnZXJDb250YWN0Ijp7ImZpcnN0TmFtZSI6ICJGaXJzdCIsImxhc3ROYW1lIjoiTGFzdCIsImVtYWlsIjogInRlc3RAdGVzdC5jb20ifX19"))
                .type("A")
                .reference("BT-PBTDC-123")
                .build();
    }

    public static Notifications delayEndNotcomNotification() throws IOException {
        return Notifications.builder()
                .source("TALOS")
                .content(Base64.getDecoder().decode("eyJvcmRlcklkIjoxMjMsInRhbG9zT3JkZXJJZCI6bnVsbCwib2FvIjoic2t5IiwiYWdlbnQiOm51bGwsImRhdGFDb250cmFjdCI6IldBRyIsIm9yaWdpbmF0b3JDb2RlIjoiRVhUIiwicmVzZWxsZXJUcmFuc2FjdGlvbklkIjoiMTAwIiwicmVzZWxsZXJPcmRlclJlcXVlc3REYXRlVGltZSI6IjAzLzAyLzIwMjIgMTA6NDM6NTUiLCJvcGVyYXRvck5hbWUiOiJTS1kiLCJvcGVyYXRvckNvZGUiOiI4Mzc1OCIsIm9yZGVyTnVtYmVyIjoiQlQtUEJUREMtMTIzIiwiY3JlYXRlZEF0IjoiMjAyMi0wMi0wM1QxNToxMDo1NiIsInNlcnZpY2VUeXBlIjoiUEJUREMiLCJzZXJ2aWNlQ2xhc3MiOm51bGwsInNlcnZpY2VMb2NhdGlvbkFkZHJlc3NJZCI6bnVsbCwibG9jYXRpb25EZXNjcmlwdGlvbiI6bnVsbCwib3JkZXJUeXBlIjpudWxsLCJ0ZWNobmljaWFuTm90ZXMiOm51bGwsImludGVybmFsVHJhY2tpbmdPcmRlclJlZmVyZW5jZSI6bnVsbCwic3VwcGxpZXJPcmRlcklkIjpudWxsLCJhY2NvdW50TnVtYmVyIjpudWxsLCJtYWpvckFjY291bnROdW1iZXIiOm51bGwsImR1ZUNvbXBsZXRpb25EYXRlIjpudWxsLCJlc3RpbWF0ZWRDb21wbGV0aW9uRGF0ZSI6bnVsbCwibm9uU3RhbmRhcmRGbGFnIjpudWxsLCJub25TdGFuZGFyZFJlYXNvbiI6bnVsbCwiY29uZmlybWF0aW9uUmVzdWx0IjpudWxsLCJyZWZvcmVjYXN0RHVlRGF0ZSI6bnVsbCwib3JkZXJGaWxlTmFtZSI6bnVsbCwibW9kZSI6bnVsbCwibGFzdE5vdGlmaWNhdGlvblR5cGUiOiJVIiwidW5pcXVlSWQiOm51bGwsIndhc3NldCI6bnVsbCwicHJvZHVjdEdyb3VwIjpudWxsLCJvcmRlclN0YXR1cyI6bnVsbCwic3RhdHVzTm90ZXMiOm51bGwsInN1cHBsaWVyTm90aWZpY2F0aW9uSWQiOm51bGwsInJlamVjdGlvbkRldGFpbHMiOm51bGwsImNvbnRhY3REZXRhaWxzIjpudWxsLCJvcmlnaW5hbE9yZGVyUmVmIjpudWxsLCJjdXN0b21lckRlbGF5Ijp7InN0YXJ0RGF0ZSI6IjExLzExLzIwMjAgMDk6NDQ6MTAiLCJlbmREYXRlIjoiMTIvMTEvMjAyMCAwOTo0NDoxMCIsInJlYXNvbiI6IlRlc3QgUmVhc29uIn19"))
                .type("DE")
                .reference("BT-PBTDC-123")
                .build();
    }

    public static NotificationDTO categorizationNotificationRequest() throws IOException {
        return NotificationDTO.builder()
                .source("X")
                .content(IOUtils.toString(NotificationFactory.class
                                .getResourceAsStream("/data/UndeliverableNotification.xml"),
                        StandardCharsets.UTF_8).getBytes())
                .type("CT")
                .reference("WAG-123-PBTDC-1")
                .build();
    }


    public static ParkedNotifications defaultParkedNotifications() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return ParkedNotifications.builder()
                .orders(OrderFactory.defaultPbtdcOrders())
                .notificationType("U")
                .processedStatus(NotificationProcessedStatus.UNPROCESSED.getValue())
                .supplierNotification(mapper.writeValueAsBytes(undeliverableNotificationRequest()))
                .build();
    }

    public static List<ParkedNotifications> parkerNotificationsList() throws IOException {
        List<ParkedNotifications> parkedNotifications = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        ParkedNotifications parkedNotifications1 = ParkedNotifications.builder()
                .orders(Orders.builder().id(2L).build())
                .notificationType("U")
                .processedStatus(NotificationProcessedStatus.UNPROCESSED.getValue())
                .supplierNotification(mapper.writeValueAsBytes(undeliverableNotificationRequest()))
                .build();
        ParkedNotifications parkedNotifications2 = ParkedNotifications.builder()
                .orders(Orders.builder().id(3L).build())
                .notificationType("U")
                .processedStatus(NotificationProcessedStatus.UNPROCESSED.getValue())
                .supplierNotification(mapper.writeValueAsBytes(undeliverableNotificationRequest()))
                .build();
        parkedNotifications.add(parkedNotifications1);
        parkedNotifications.add(parkedNotifications2);
        return parkedNotifications;


    }

    public static ParkedNotifications parkedNotificationForAccept() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return ParkedNotifications.builder()
                .orders(Orders.builder().id(1L).build())
                .notificationType("A")
                .processedStatus(NotificationProcessedStatus.UNPROCESSED.getValue())
                .supplierNotification(mapper.writeValueAsBytes(acceptNotificationRequest()))
                .build();
    }

    public static Notifications WSANotificationResponse() throws IOException {
        return Notifications.builder()
                .source("TALOS")
                .createdAt(LocalDateTime.now())
                .changedAt(LocalDateTime.now())
                .type("WSA")
                .reference("BT-PBTDC-1")
                .content(new byte[0])
                .build();
    }

    public static NotificationDTO siebelNotificationDTO(String notificationType) throws IOException {
        return NotificationDTO.builder()
                .source("SIEBEL")
                .type(notificationType)
                .reference("TALOS-1-PBTDC-1")
                .content(IOUtils.toString(NotificationFactory.class.getResourceAsStream("/data/CucumberTest/" + NotificationTypes.getMessageNameByNotificationCode(notificationType).substring(8)+"Notification.xml"),
                        StandardCharsets.UTF_8).getBytes())
                .build();

    }

    public static Notifications qbtdcCompleteNotification() throws IOException {
        return Notifications.builder()
                .source("TALOS")
                .content(Base64.getDecoder().decode("eyJvcmRlcklkIjoxMjMsInRhbG9zT3JkZXJJZCI6bnVsbCwib2FvIjoic2t5IiwiYWdlbnQiOm51bGwsImRhdGFDb250cmFjdCI6IldBRyIsIm9yaWdpbmF0b3JDb2RlIjoiRVhUIiwicmVzZWxsZXJUcmFuc2FjdGlvbklkIjoiMTAwIiwicmVzZWxsZXJPcmRlclJlcXVlc3REYXRlVGltZSI6IjAzLzAyLzIwMjIgMTA6NDM6NTUiLCJvcGVyYXRvck5hbWUiOiJTS1kiLCJvcGVyYXRvckNvZGUiOiI4Mzc1OCIsIm9yZGVyTnVtYmVyIjoiQlQtUEJUREMtMTIzIiwiY3JlYXRlZEF0IjoiMjAyMi0wMi0wM1QxNToxMDo1NiIsInNlcnZpY2VUeXBlIjoiUEJUREMiLCJzZXJ2aWNlQ2xhc3MiOm51bGwsInNlcnZpY2VMb2NhdGlvbkFkZHJlc3NJZCI6bnVsbCwibG9jYXRpb25EZXNjcmlwdGlvbiI6bnVsbCwib3JkZXJUeXBlIjpudWxsLCJ0ZWNobmljaWFuTm90ZXMiOm51bGwsImludGVybmFsVHJhY2tpbmdPcmRlclJlZmVyZW5jZSI6bnVsbCwic3VwcGxpZXJPcmRlcklkIjpudWxsLCJhY2NvdW50TnVtYmVyIjpudWxsLCJtYWpvckFjY291bnROdW1iZXIiOm51bGwsImR1ZUNvbXBsZXRpb25EYXRlIjpudWxsLCJlc3RpbWF0ZWRDb21wbGV0aW9uRGF0ZSI6bnVsbCwibm9uU3RhbmRhcmRGbGFnIjpudWxsLCJub25TdGFuZGFyZFJlYXNvbiI6bnVsbCwiY29uZmlybWF0aW9uUmVzdWx0IjpudWxsLCJyZWZvcmVjYXN0RHVlRGF0ZSI6bnVsbCwib3JkZXJGaWxlTmFtZSI6bnVsbCwibW9kZSI6bnVsbCwibGFzdE5vdGlmaWNhdGlvblR5cGUiOiJVIiwidW5pcXVlSWQiOm51bGwsIndhc3NldCI6bnVsbCwicHJvZHVjdEdyb3VwIjpudWxsLCJvcmRlclN0YXR1cyI6bnVsbCwic3RhdHVzTm90ZXMiOm51bGwsInN1cHBsaWVyTm90aWZpY2F0aW9uSWQiOm51bGwsInJlamVjdGlvbkRldGFpbHMiOm51bGwsImNvbnRhY3REZXRhaWxzIjpudWxsLCJvcmlnaW5hbE9yZGVyUmVmIjpudWxsLCJjdXN0b21lckRlbGF5Ijp7InN0YXJ0RGF0ZSI6IjExLzExLzIwMjAgMDk6NDQ6MTAiLCJlbmREYXRlIjoiMTIvMTEvMjAyMCAwOTo0NDoxMCIsInJlYXNvbiI6IlRlc3QgUmVhc29uIn19"))
                .type("C")
                .reference("BT-QBTDC-123")
                .createdAt(LocalDateTime.now())
                .transactionId(1L)
                .build();
    }

    public static Notifications qbtdcDelayedNotification1() throws IOException {
        return Notifications.builder()
                .source("TALOS")
                .content(Base64.getDecoder().decode("ewoJImRlbGF5ZWROb3RpZmljYXRpb25SZWFzb24iOiAieHl6Igp9"))
                .type("D")
                .reference("1")
                .createdAt(LocalDateTime.now())
                .transactionId(1L)
                .build();
    }

    public static Notifications qbtdcDelayedNotification() throws IOException {
        return Notifications.builder()
                .source("TALOS")
                .content(Base64.getDecoder().decode("eyJvcmRlcklkIjoxLCJ0YWxvc09yZGVySWQiOm51bGwsIm9hbyI6InNreSIsImFnZW50IjpudWxsLCJhZ2VudEVtYWlsIjpudWxsLCJvYm8iOiJza3kiLCJkYXRhQ29udHJhY3QiOiJXQUciLCJvcmlnaW5hdG9yQ29kZSI6IkVYVCIsIm9yZ2FuaXNhdGlvbk5hbWUiOm51bGwsInJlc2VsbGVyVHJhbnNhY3Rpb25JZCI6IjEwMTAxIiwicmVzZWxsZXJPcmRlclJlcXVlc3REYXRlVGltZSI6IjI1LzA1LzIwMjIgMDU6MTc6MjgiLCJvcGVyYXRvck5hbWUiOiJTS1kiLCJvcGVyYXRvckNvZGUiOiI4Mzc1OCIsIm9yZGVyTnVtYmVyIjoiMSIsImNyZWF0ZWRBdCI6IjIwMjItMTItMTVUMDc6MTU6MzQuNDM5NDcyIiwic2VydmljZVR5cGUiOiJRQlREQyIsInNlcnZpY2VDbGFzcyI6bnVsbCwic2VydmljZUxvY2F0aW9uQWRkcmVzc0lkIjpudWxsLCJsb2NhdGlvbkRlc2NyaXB0aW9uIjpudWxsLCJvcmRlclR5cGUiOm51bGwsInRlY2huaWNpYW5Ob3RlcyI6bnVsbCwiaW50ZXJuYWxUcmFja2luZ09yZGVyUmVmZXJlbmNlIjpudWxsLCJzdXBwbGllck9yZGVySWQiOm51bGwsImFjY291bnROdW1iZXIiOm51bGwsIm1ham9yQWNjb3VudE51bWJlciI6bnVsbCwiZHVlQ29tcGxldGlvbkRhdGUiOm51bGwsImVzdGltYXRlZENvbXBsZXRpb25EYXRlIjpudWxsLCJpbnRlcnZlbnRpb25EZXRhaWxzIjpudWxsLCJub25TdGFuZGFyZEZsYWciOm51bGwsIm5vblN0YW5kYXJkUmVhc29uIjpudWxsLCJjb25maXJtYXRpb25SZXN1bHQiOm51bGwsInJlZm9yZWNhc3REdWVEYXRlIjpudWxsLCJvcmRlckZpbGVOYW1lIjpudWxsLCJtb2RlIjpudWxsLCJsYXN0Tm90aWZpY2F0aW9uVHlwZSI6bnVsbCwidW5pcXVlSWQiOm51bGwsIndhc3NldCI6bnVsbCwicHJvZHVjdEdyb3VwIjpudWxsLCJvcmRlclN0YXR1cyI6IkRlbGF5ZWQiLCJzdGF0dXNOb3RlcyI6bnVsbCwicHJvamVjdEtleSI6Inh5eiIsImRlbGF5UmVhc29uIjoieHl6Iiwic3VwcGxpZXJOb3RpZmljYXRpb25JZCI6bnVsbCwicmVqZWN0aW9uRGV0YWlscyI6bnVsbCwiY29udGFjdERldGFpbHMiOm51bGwsIm9yaWdpbmFsT3JkZXJSZWYiOm51bGwsImN1c3RvbWVyRGVsYXkiOm51bGwsInBidGRjIjpudWxsLCJxYnRkYyI6eyJyZWN1cnJpbmdGcmVxdWVuY3kiOiJBTk5VQUwiLCJxdW90ZUl0ZW1zIjpbeyJvcmRlcklkIjoxLCJub3RpZmljYXRpb25JZCI6bnVsbCwicXVvdGVJdGVtSWQiOjEsInRlcm0iOjUsInByb2R1Y3QiOiJXSUMiLCJub25SZWN1cnJpbmdQcmljZSI6IjEwMDAiLCJyZWN1cnJpbmdQcmljZSI6IjMwMCIsInN0YXR1cyI6IlJlamVjdGVkIiwibm90ZXMiOm51bGwsIm9mZmxpbmVRdW90ZWQiOiJOIiwiY3VzdG9tZXJBY2Nlc3MiOnsiaWQiOm51bGwsImNoYW5nZWRBdCI6bnVsbCwiY2lyY3VpdFJlZmVyZW5jZSI6bnVsbCwiYWN0aW9uIjoiUCIsInNlcnZpY2VDbGFzcyI6bnVsbCwiYmFuZFdpZHRoIjoiMTAiLCJzbGEiOiJFTkhBTkNFRCIsImFwcG9pbnRtZW50UmVxdWVzdFJlY2VpdmVkVGltZXN0YW1wIjpudWxsLCJ0ZXJtaW5hdGluZ0VxdWlwbWVudCI6bnVsbCwic2l0ZSI6eyJuYW1lIjpudWxsLCJjb21tc1Jvb20iOm51bGwsImxvY2F0aW9uIjp7ImlkIjoiQTAwRjAyNyIsInR5cGUiOm51bGwsImFkZHJlc3MiOnsibG9jYXRpb25MaW5lMSI6bnVsbCwibG9jYXRpb25MaW5lMiI6bnVsbCwibG9jYXRpb25MaW5lMyI6bnVsbCwiY2l0eSI6bnVsbCwiY291bnR5IjpudWxsLCJlaXJjb2RlIjpudWxsLCJmdWxsQWRkcmVzcyI6IkFkZHJlc3NMaW5lIDEsIEFkZHJlc3NMaW5lIDIsIEFkZHJlc3NMaW5lIDMifSwibmV0d29ya1N0YXR1cyI6Ik9OLU5FVCIsIm11bHRpRWlyY29kZSI6Ik4ifX0sImFjY2Vzc0luc3RhbGwiOm51bGwsImNpcmN1aXRUeXBlIjpudWxsLCJzdXBwbGllciI6Ik9OLU5FVCIsInRhcmdldEFjY2Vzc1N1cHBsaWVyIjoiT04tTkVUIn0sIndob2xlc2FsZXJBY2Nlc3MiOnsiaWQiOm51bGwsImNoYW5nZWRBdCI6bnVsbCwiY2lyY3VpdFJlZmVyZW5jZSI6bnVsbCwiYWN0aW9uIjoiQ0giLCJzZXJ2aWNlQ2xhc3MiOm51bGwsImJhbmRXaWR0aCI6bnVsbCwic2xhIjpudWxsLCJhcHBvaW50bWVudFJlcXVlc3RSZWNlaXZlZFRpbWVzdGFtcCI6bnVsbCwidGVybWluYXRpbmdFcXVpcG1lbnQiOm51bGwsInNpdGUiOnsibmFtZSI6bnVsbCwiY29tbXNSb29tIjpudWxsLCJsb2NhdGlvbiI6eyJpZCI6IklOVEVSTkVUIiwidHlwZSI6bnVsbCwiYWRkcmVzcyI6bnVsbCwibmV0d29ya1N0YXR1cyI6bnVsbCwibXVsdGlFaXJjb2RlIjpudWxsfX0sImFjY2Vzc0luc3RhbGwiOm51bGwsImNpcmN1aXRUeXBlIjpudWxsLCJzdXBwbGllciI6bnVsbCwidGFyZ2V0QWNjZXNzU3VwcGxpZXIiOm51bGx9LCJsb2dpY2FsTGluayI6eyJjaXJjdWl0UmVmZXJlbmNlIjpudWxsLCJ2bGFuIjpudWxsLCJhY3Rpb24iOiJQIiwic2VydmljZUNsYXNzIjpudWxsLCJiYW5kV2lkdGgiOiI2MDAwIiwiaXAiOm51bGwsInZvaWNlQ2hhbm5lbHMiOm51bGwsInNlcnZpY2VEZXRhaWxzIjpudWxsLCJjaXJjdWl0VHlwZSI6bnVsbCwicHJvZmlsZSI6IlBSSU1BUlkiLCJpcFJhbmdlIjoiMzEifSwicmVqZWN0aW9uRGV0YWlscyI6eyJyZWplY3RDb2RlIjpudWxsLCJyZWplY3RSZWFzb24iOm51bGx9fV19fQ=="))
                .type("D")
                .reference("BT-QBTDC-1")
                .transactionId(0L)
                .build();
    }

}
