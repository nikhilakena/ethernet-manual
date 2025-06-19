package com.btireland.talos.ethernet.engine.util;

import com.btireland.talos.ethernet.engine.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class EmailDTOFactory {

    public static String asJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(object);
    }

    public static EmailDTO defaultEmailDTO() {

        return EmailDTO.builder().emailText("<!DOCTYPE html>\n" +
                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div style=\"color:#6600cc; font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Order Details</div>\n" +
                "<table>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Customer</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">sky</td></tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Agent</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Test Agent</td></tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Email</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">test@test.com</td></tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">BT Quote Reference</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">BT-QBTDC-328917/1067</td></tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">BT Order Reference</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">BT-PBTDC-328953</td></tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">BT Group Ref</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">BT-PBTDC-328953-G1</td></tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">COFE Order Id</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">TALOS-328953-PBTDC-1</td></tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Customer Reference</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">NUA_C_1404</td></tr>\n" +
                "</table>\n" +
                "<hr>\n" +
                "<div style=\"color:#6600cc; font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Circuit Details</div>\n" +
                "<table>\n" +
                "    <tr>\n" +
                "        <td rowspan=\"2\" style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Product</td>\n" +
                "        <td rowspan=\"2\" style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Connection Type</td>\n" +
                "        <td colspan=\"4\" style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">A-End (End Customer)</td>\n" +
                "        <td colspan=\"3\" style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">B-End (PoP)</td>\n" +
                "        \n" +
                "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Logical</td>\n" +
                "        <td colspan=\"2\" style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Price</td>\n" +
                "    </tr>\n" +
                "    <tr style=\"background: #929194; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left;\">\n" +
                "        <td>Eircode</td>\n" +
                "        <td>Access</td>\n" +
                "        <td>Delivery</td>\n" +
                "        <td>Presentation</td>\n" +
                "        <td>Handover</td>\n" +
                "        <td>NNI ID</td>\n" +
                "        <td>VLAN</td>\n" +
                "        <td>Access</td>\n" +
                "        <td>NRC</td>\n" +
                "        <td>RC</td>\n" +
                "    </tr>\n" +
                "    <tr style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left;\">\n" +
                "        <td rowspan=\"3\"><div><span>WEC</span><span> /31</span></div><div><span>1</span><span> Year</span></div></td>\n" +
                "        <td rowspan=\"3\">ETHERWAY_DIVERSE</td>\n" +
                "        <td>A4000032</td>\n" +
                "        <td><span>1</span><span> Gb</span><div>Standard</div></td>\n" +
                "        <td>3rd Party Supplier</td>\n" +
                "        <td>LC 1310 Single Mode Fibre</td>\n" +
                "        <td rowspan=\"3\">BT Citywest</td>\n" +
                "        <td rowspan=\"3\">tst10672</td>\n" +
                "        <td rowspan=\"3\">1067</td>\n" +
                "        <td rowspan=\"3\"><span>900</span><span> Mb</span><div>Premium 100 (100% AF)</div></td>\n" +
                "        <td rowspan=\"3\"><span>&euro; </span><span>1,000</span></td>\n" +
                "        <td rowspan=\"3\"><span>&euro; </span><span>8,354</span><div>Annual</div></td>\n" +
                "    </tr>\n" +
                "    <tr style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left;\">\n" +
                "        <td colspan=\"4\">3 Mourne View, Dublin Road, Dundalk, Co. Louth</td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "\n" +
                "<hr>\n" +
                "<div style=\"color:#6600cc; font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Customer Details</div>\n" +
                "<table>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Organisation Name</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Test Organisation</td></tr>\n" +
                "</table>\n" +
                "\n" +
                "<hr>\n" +
                "<div style=\"color:#6600cc; font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Site Details</div>\n" +
                "<div style=\"font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">First Site Contact Details</div>\n" +
                "<table>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">First Name</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">John</td></tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Last Name</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Doe</td></tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Contact Number</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">9999999999</td></tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Email</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">test1@test.com</td></tr>\n" +
                "</table>\n" +
                "<div style=\"font-weight:bold; font-family: Arial; font-size:16px;\">Second Site Contact Details</div>\n" +
                "<table>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">First Name</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">First Name</td></tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Last Name</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Last Name</td></tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Contact Number</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">8888888888</td></tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Email</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">test2@test.com</td></tr>\n" +
                "</table>\n" +
                "\n" +
                "<hr>\n" +
                "<div style=\"color:#6600cc; font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Site Readiness</div>\n" +
                "<table>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Is a method statement or insurance certificate required?</td>\n" +
                "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\"><span>Yes</span></td>\n" +
                "    </tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Are attending technicians required to complete any site induction?</td>\n" +
                "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\"><span>No</span></td>\n" +
                "    </tr>\n" +
                "    \n" +
                "</table>\n" +
                "\n" +
                "<div style=\"font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Landlord Details</div>\n" +
                "<table>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Is site leased from a landlord?</td>\n" +
                "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\"><span>Yes</span></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">First Name</td>\n" +
                "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Landlord First Name</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Last Name</td>\n" +
                "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Landlord Last Name</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Contact Number</td>\n" +
                "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">7777777777</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Email</td>\n" +
                "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">test3@test.com</td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "\n" +
                "<div style=\"font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Building Manager Details</div>\n" +
                "<table>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Is the building multi-tenant?</td>\n" +
                "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\"><span>Yes</span></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">First Name</td>\n" +
                "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">BM First Name</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Last Name</td>\n" +
                "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">BM Last Name</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Contact Number</td>\n" +
                "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">6666666666</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Email</td>\n" +
                "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">test4@test.com</td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "\n" +
                "<hr>\n" +
                "<div style=\"color:#6600cc; font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Comms Room</div>\n" +
                "<table>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Comms Room Details</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">tst1_180422_1067_CR</td></tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Is the Comms Room ready for delivery?</td>\n" +
                "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\"><span>No</span></td>\n" +
                "    </tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Power Socket Type</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">3-PIN</td></tr>\n" +
                "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Patch Panel / Cable Manager required?</td>\n" +
                "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\"><span>Yes</span></td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "\n" +
                "<hr>\n" +
                "<div style=\"color:#6600cc; font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Notes</div>\n" +
                "<table>\n" +
                "    <tr><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">This is a test Note</td></tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>")
                .emailSubject("Order - sky - Wholesale Ethernet Connect - BT-PBTDC-328953").build();
    }

    public static EmailDTO defaultEmailDTOForWIC() {
        return EmailDTO.builder()
                .emailText("<!DOCTYPE html>\n" +
                        "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                        "<head>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<div style=\"color:#6600cc; font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Order Details</div>\n" +
                        "<table>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Customer</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">sky</td></tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Agent</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Test Agent</td></tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Email</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">test@test.com</td></tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">BT Quote Reference</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">BT-QBTDC-328917/1067</td></tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">BT Order Reference</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">BT-PBTDC-328953</td></tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">BT Group Ref</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">BT-PBTDC-328953-G1</td></tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">COFE Order Id</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">TALOS-328953-PBTDC-1</td></tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Customer Reference</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">NUA_C_1404</td></tr>\n" +
                        "</table>\n" +
                        "<hr>\n" +
                        "<div style=\"color:#6600cc; font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Circuit Details</div>\n" +
                        "<table>\n" +
                        "    <tr>\n" +
                        "        <td rowspan=\"2\" style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Product</td>\n" +
                        "        <td rowspan=\"2\" style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Connection Type</td>\n" +
                        "        <td colspan=\"4\" style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">A-End (End Customer)</td>\n" +
                        "        \n" +
                        "        <td colspan=\"1\" style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">B-End (PoP)</td>\n" +
                        "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Logical</td>\n" +
                        "        <td colspan=\"2\" style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Price</td>\n" +
                        "    </tr>\n" +
                        "    <tr style=\"background: #929194; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left;\">\n" +
                        "        <td>Eircode</td>\n" +
                        "        <td>Access</td>\n" +
                        "        <td>Delivery</td>\n" +
                        "        <td>Presentation</td>\n" +
                        "        <td>Handover</td>\n" +
                        "        \n" +
                        "        \n" +
                        "        <td>Access</td>\n" +
                        "        <td>NRC</td>\n" +
                        "        <td>RC</td>\n" +
                        "    </tr>\n" +
                        "    <tr style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left;\">\n" +
                        "        <td rowspan=\"3\"><div><span>WIC</span><span> /31</span></div><div><span>1</span><span> Year</span></div></td>\n" +
                        "        <td rowspan=\"3\">ETHERWAY_DIVERSE</td>\n" +
                        "        <td>A4000032</td>\n" +
                        "        <td><span>Existing</span></td>\n" +
                        "        <td>3rd Party Supplier</td>\n" +
                        "        <td>LC 1310 Single Mode Fibre</td>\n" +
                        "        <td rowspan=\"3\">BT Citywest</td>\n" +
                        "        \n" +
                        "        \n" +
                        "        <td rowspan=\"3\"><span>900</span><span> Mb</span><div>Premium 100 (100% AF)</div></td>\n" +
                        "        <td rowspan=\"3\"><span>&euro; </span><span>1,000</span></td>\n" +
                        "        <td rowspan=\"3\"><span>&euro; </span><span>8,354</span><div>Annual</div></td>\n" +
                        "    </tr>\n" +
                        "    <tr style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left;\">\n" +
                        "        <td colspan=\"4\">3 Mourne View, Dublin Road, Dundalk, Co. Louth</td>\n" +
                        "    </tr>\n" +
                        "</table>\n" +
                        "\n" +
                        "<hr>\n" +
                        "<div style=\"color:#6600cc; font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Customer Details</div>\n" +
                        "<table>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Organisation Name</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Test Organisation</td></tr>\n" +
                        "</table>\n" +
                        "\n" +
                        "<hr>\n" +
                        "<div style=\"color:#6600cc; font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Site Details</div>\n" +
                        "<div style=\"font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">First Site Contact Details</div>\n" +
                        "<table>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">First Name</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">John</td></tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Last Name</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Doe</td></tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Contact Number</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">9999999999</td></tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Email</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">test1@test.com</td></tr>\n" +
                        "</table>\n" +
                        "<div style=\"font-weight:bold; font-family: Arial; font-size:16px;\">Second Site Contact Details</div>\n" +
                        "<table>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">First Name</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">First Name</td></tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Last Name</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Last Name</td></tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Contact Number</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">8888888888</td></tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Email</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">test2@test.com</td></tr>\n" +
                        "</table>\n" +
                        "\n" +
                        "<hr>\n" +
                        "<div style=\"color:#6600cc; font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Site Readiness</div>\n" +
                        "<table>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Is a method statement or insurance certificate required?</td>\n" +
                        "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\"><span>Yes</span></td>\n" +
                        "    </tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Are attending technicians required to complete any site induction?</td>\n" +
                        "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\"><span>No</span></td>\n" +
                        "    </tr>\n" +
                        "    \n" +
                        "</table>\n" +
                        "\n" +
                        "<div style=\"font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Landlord Details</div>\n" +
                        "<table>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Is site leased from a landlord?</td>\n" +
                        "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\"><span>Yes</span></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">First Name</td>\n" +
                        "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Landlord First Name</td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Last Name</td>\n" +
                        "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Landlord Last Name</td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Contact Number</td>\n" +
                        "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">7777777777</td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Email</td>\n" +
                        "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">test3@test.com</td>\n" +
                        "    </tr>\n" +
                        "</table>\n" +
                        "\n" +
                        "<div style=\"font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Building Manager Details</div>\n" +
                        "<table>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Is the building multi-tenant?</td>\n" +
                        "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\"><span>Yes</span></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">First Name</td>\n" +
                        "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">BM First Name</td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Last Name</td>\n" +
                        "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">BM Last Name</td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Contact Number</td>\n" +
                        "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">6666666666</td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Email</td>\n" +
                        "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">test4@test.com</td>\n" +
                        "    </tr>\n" +
                        "</table>\n" +
                        "\n" +
                        "<hr>\n" +
                        "<div style=\"color:#6600cc; font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Comms Room</div>\n" +
                        "<table>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Comms Room Details</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">tst1_180422_1067_CR</td></tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Is the Comms Room ready for delivery?</td>\n" +
                        "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\"><span>No</span></td>\n" +
                        "    </tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Power Socket Type</td><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">3-PIN</td></tr>\n" +
                        "    <tr><td style=\"background: #5514B4; color: white; font-weight: bold; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">Patch Panel / Cable Manager required?</td>\n" +
                        "        <td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\"><span>Yes</span></td>\n" +
                        "    </tr>\n" +
                        "</table>\n" +
                        "\r\n" +
                        "<hr>\n" +
                        "<div style=\"color:#6600cc; font-weight:bold; font-family: Arial; font-size:16px; padding-left: 5px; padding-top: 15px; padding-bottom: 15px;\">Notes</div>\n" +
                        "<table>\n" +
                        "    <tr><td style=\"background: #e3dfde; color: black; font-weight: normal; font-family: Arial; padding: 6px; border: 1px solid #ccc; text-align: left; min-width:200px;\">This is a test Note</td></tr>\n" +
                        "</table>\n" +
                        "</body>\n" +
                        "</html>")
                .emailSubject("Order - sky - Wholesale Internet Connect - BT-PBTDC-328953").build();
    }

    public static String emailDTOJson() throws JsonProcessingException {
        return asJson(defaultEmailDTO());
    }
}
