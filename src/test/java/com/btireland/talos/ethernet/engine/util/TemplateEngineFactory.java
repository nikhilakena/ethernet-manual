package com.btireland.talos.ethernet.engine.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TemplateEngineFactory {

    public static String getQbtdcCompleteEmailTextForSingleLine() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "    <style>\n" +
                "\t\tbody,\n" +
                "\n" +
                "\t\t.Heading {\n" +
                "\t\t    color:#6600cc;\n" +
                "\t\t    font-weight:bold;\n" +
                "\t\t    font-family: Arial;\n" +
                "\t\t    font-size:16px;\n" +
                "\t\t    padding-left: 5px;\n" +
                "\t\t    padding-top: 15px;\n" +
                "\t\t    padding-bottom: 15px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.p1 {\n" +
                "\t\t    font: 13px sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t\tfont-weight: 100;\n" +
                "\t\t\tcolor: black;\n" +
                "\t    }\n" +
                "\n" +
                "\t\tp {\n" +
                "\t\t    font: 14px Arial, Helvetica, sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t\tcolor: black;\n" +
                "\t    }\n" +
                "\n" +
                "\t\ttable {\n" +
                "\t\t\tborder-collapse: separate;\n" +
                "\t\t\tborder-spacing: 2px;\n" +
                "\t\t\tborder-style: none;\n" +
                "\t\t\tmso-line-height-rule: exactly;\n" +
                "\t\t\ttext-indent: initial;\n" +
                "\t\t\tpadding-top: 10px;\n" +
                "\t\t\tfont: 14px Arial, Helvetica, sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable td {\n" +
                "\t\t\tpadding: 10px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable#CircuitsTable {\n" +
                "\t\t\twidth: 100%;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable#OrderTable tr td {\n" +
                "\t\t    min-width:200px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableHeader {\n" +
                "\t\t\tbackground: #5514B4;\n" +
                "\t\t\tpadding: 2px;\n" +
                "\t\t\tcolor: #FFF;\n" +
                "\t\t\tfont-weight: bold;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableSubHeader {\n" +
                "\t\t\tbackground-color: #888;\n" +
                "\t\t\tcolor: #FFF;\n" +
                "\t\t\tpadding: 2px;\n" +
                "\t\t\tfont-weight: bold;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableElement {\n" +
                "\t\t    background: #e3dfde;\n" +
                "\t\t    color: black;\n" +
                "\t\t    font-weight: normal;\n" +
                "\t\t    font-family: Arial;\n" +
                "\t\t    padding: 6px;\n" +
                "\t\t    border: 1px solid #ccc;\n" +
                "\t\t    text-align: left;\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"Heading\">Order Details</div>\n" +
                "<table id=\"OrderTable\">\n" +
                "    <tr><td class=\"TableHeader\">Customer</td><td class=\"TableElement\">sky</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">BT Reference</td><td class=\"TableElement\">BT-QBTDC-1</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">Quote Initiated Date</td><td class=\"TableElement\">25/05/2022</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">Quote Date</td><td class=\"TableElement\">25/05/2022</td></tr>\n" +
                "</table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<p>Please see below for your requested quotation.</p>\n" +
                "\n" +
                "<p>Kind Regards,</p>\n" +
                "<p>BT Ireland Wholesale Pricing Team</p>\n" +
                "\n" +
                "\n" +
                "<div class=\"Heading\">Circuit Details</div>\n" +
                "<table id=\"CircuitsTable\">\n" +
                "    <tr class=\"TableHeader\">\n" +
                "        <td rowspan=\"2\" >Quote ID</td>\n" +
                "        <td rowspan=\"2\">Status</td>\n" +
                "        <td rowspan=\"2\">Product</td>\n" +
                "        <td rowspan=\"2\">Connection Type</td>\n" +
                "        <td colspan=\"3\">A-End (End Customer)</td>\n" +
                "        <td>B-End (PoP)</td>\n" +
                "        <td>Logical</td>\n" +
                "        <td colspan=\"2\">Price</td>\n" +
                "    </tr>\n" +
                "    <tr class=\"TableSubHeader\">\n" +
                "        <td>Eircode</td>\n" +
                "        <td>Access</td>\n" +
                "        <td>Delivery</td>\n" +
                "        <td>Handoff</td>\n" +
                "        <td>Access</td>\n" +
                "        <td>NRC</td>\n" +
                "        <td>RC</td>\n" +
                "    </tr>\n" +
                "    \n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td rowspan=\"3\"><span>1</span></td>\n" +
                "            <td rowspan=\"3\"><span>Quoted</span></td>\n" +
                "            <td rowspan=\"3\"><div><span>WEC</span><span> /31</span></div><div><span>5 Years</span></div></td>\n" +
                "            <td rowspan=\"3\"><span>ETHERWAY_STANDARD</span></td>\n" +
                "            <td><span>A00F027</span></td>\n" +
                "            <td><span>10 Gb</span><div>Enhanced SLA</div></td>\n" +
                "            <td>BT On-Net</td>\n" +
                "            <td>Equinix DB1 - Unit 4027 Citywest</td>\n" +
                "            <td><span>500 Mb</span><div>Premium 100 (100% AF)</div></td>\n" +
                "            <td rowspan=\"3\"><span>&euro; </span><span>2000</span></td>\n" +
                "            <td rowspan=\"3\"><span>&euro; </span><span>100</span><div>Annual</div></td>\n" +
                "        </tr>\n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td colspan=\"5\">DUNBEGGAN, AUGHNACLIFFE, LONGFORD</td>\n" +
                "        </tr>\n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td colspan=\"5\">Quoted Successfully</td>\n" +
                "        </tr>\n" +
                "    \n" +
                "\n" +
                "</table>\n" +
                "\n" +
                "<div>\n" +
                "    <p class=\"p1\">Quotes are in Euro, exclude VAT, and can change subject to survey, specified contract " +
                "minimum" +
                " term, site establishment costs, and capacity check. Quotes are valid for 90 days. All pricing is " +
                "subject to a final engineering survey which may result in additional customer costs. Pricing " +
                "excludes customer site costs. Additional network build costs may be notified in accordance with Clause 2.2 of the General Service Schedule. For services that reside at a customer contracted third party site (including BT and non-BT Data Centre locations), BT will deliver to the agreed demarcation. For a Data Centre, this will be the Meet-Me Room (MMR). The customer is responsible for cabling from the MMR to the customer cabinet. BT is not responsible for cable routing within third-party locations (including Data Centres). BT applies an uplift for third party charges it incurs such as ISM, DAA etc.</p>\n"+
                "    <p class=\"p1\">A standard install applies on all new installations of € 1,000 per Data Centre service " +
                "instance; and € 2,400 otherwise. If the quote is for a multi-year term, the standard install" +
                " is often discounted to € 0. Orders that are in Customer Delay for greater than an aggregate of 60 " +
                "working days will be deemed to be cancelled unless a revised due date can be mutually agreed. If an " +
                "order is cancelled after being in Customer Delay for an aggregate of 60 working days, the full connection charge will be billed. This will be the standard install (€ 1,000 or € 2,400 dependent on location), any related third-party charges incurred by BT and any exceptional costs previously agreed with the customer. For Etherflow pricing, the initial 1/10 Gig Etherway will need to be in place at your PoP.</p>\n"+
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }

    public static String getQbtdcCompleteEmailText() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "    <style>\n" +
                "\t\tbody,\n" +
                "\n" +
                "\t\t.Heading {\n" +
                "\t\t    color:#6600cc;\n" +
                "\t\t    font-weight:bold;\n" +
                "\t\t    font-family: Arial;\n" +
                "\t\t    font-size:16px;\n" +
                "\t\t    padding-left: 5px;\n" +
                "\t\t    padding-top: 15px;\n" +
                "\t\t    padding-bottom: 15px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.p1 {\n" +
                "\t\t    font: 13px sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t\tfont-weight: 100;\n" +
                "\t\t\tcolor: black;\n" +
                "\t    }\n" +
                "\n" +
                "\t\tp {\n" +
                "\t\t    font: 14px Arial, Helvetica, sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t\tcolor: black;\n" +
                "\t    }\n" +
                "\n" +
                "\t\ttable {\n" +
                "\t\t\tborder-collapse: separate;\n" +
                "\t\t\tborder-spacing: 2px;\n" +
                "\t\t\tborder-style: none;\n" +
                "\t\t\tmso-line-height-rule: exactly;\n" +
                "\t\t\ttext-indent: initial;\n" +
                "\t\t\tpadding-top: 10px;\n" +
                "\t\t\tfont: 14px Arial, Helvetica, sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable td {\n" +
                "\t\t\tpadding: 10px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable#CircuitsTable {\n" +
                "\t\t\twidth: 100%;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable#OrderTable tr td {\n" +
                "\t\t    min-width:200px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableHeader {\n" +
                "\t\t\tbackground: #5514B4;\n" +
                "\t\t\tpadding: 2px;\n" +
                "\t\t\tcolor: #FFF;\n" +
                "\t\t\tfont-weight: bold;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableSubHeader {\n" +
                "\t\t\tbackground-color: #888;\n" +
                "\t\t\tcolor: #FFF;\n" +
                "\t\t\tpadding: 2px;\n" +
                "\t\t\tfont-weight: bold;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableElement {\n" +
                "\t\t    background: #e3dfde;\n" +
                "\t\t    color: black;\n" +
                "\t\t    font-weight: normal;\n" +
                "\t\t    font-family: Arial;\n" +
                "\t\t    padding: 6px;\n" +
                "\t\t    border: 1px solid #ccc;\n" +
                "\t\t    text-align: left;\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"Heading\">Order Details</div>\n" +
                "<table id=\"OrderTable\">\n" +
                "    <tr><td class=\"TableHeader\">Customer</td><td class=\"TableElement\">sky</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">BT Reference</td><td class=\"TableElement\">BT-QBTDC-1</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">Quote Initiated Date</td><td class=\"TableElement\">25/05/2022</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">Quote Date</td><td class=\"TableElement\">25/05/2022</td></tr>\n" +
                "</table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<p>Please see below for your requested quotation.</p>\n" +
                "\n" +
                "<p>Kind Regards,</p>\n" +
                "<p>BT Ireland Wholesale Pricing Team</p>\n" +
                "\n" +
                "\n" +
                "<div class=\"Heading\">Circuit Details</div>\n" +
                "<table id=\"CircuitsTable\">\n" +
                "    <tr class=\"TableHeader\">\n" +
                "        <td rowspan=\"2\" >Quote ID</td>\n" +
                "        <td rowspan=\"2\">Group</td>\n" +
                "        <td rowspan=\"2\">Status</td>\n" +
                "        <td rowspan=\"2\">Product</td>\n" +
                "        <td rowspan=\"2\">Connection Type</td>\n" +
                "        <td colspan=\"3\">A-End (End Customer)</td>\n" +
                "        <td>B-End (PoP)</td>\n" +
                "        <td>Logical</td>\n" +
                "        <td colspan=\"2\">Price</td>\n" +
                "    </tr>\n" +
                "    <tr class=\"TableSubHeader\">\n" +
                "        <td>Eircode</td>\n" +
                "        <td>Access</td>\n" +
                "        <td>Delivery</td>\n" +
                "        <td>Handoff</td>\n" +
                "        <td>Access</td>\n" +
                "        <td>NRC</td>\n" +
                "        <td>RC</td>\n" +
                "    </tr>\n" +
                "    \n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td rowspan=\"3\"><span>1</span></td>\n" +
                "            <td rowspan=\"3\"><span>G1</span></td>\n" +
                "            <td rowspan=\"3\"><span>Quoted</span></td>\n" +
                "            <td rowspan=\"3\"><div><span>WEC</span><span> /31</span></div><div><span>5 Years</span></div></td>\n" +
                "            <td rowspan=\"3\"><span>ETHERWAY_DIVERSE</span></td>\n" +
                "            <td><span>A00F027</span></td>\n" +
                "            <td><span>10 Gb</span><div>Enhanced SLA</div></td>\n" +
                "            <td>BT On-Net</td>\n" +
                "            <td>Equinix DB1 - Unit 4027 Citywest</td>\n" +
                "            <td><span>500 Mb</span><div>Premium 100 (100% AF)</div></td>\n" +
                "            <td rowspan=\"3\"><span>&euro; </span><span>2000</span></td>\n" +
                "            <td rowspan=\"3\"><span>&euro; </span><span>100</span><div>Annual</div></td>\n" +
                "        </tr>\n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td colspan=\"5\">DUNBEGGAN, AUGHNACLIFFE, LONGFORD</td>\n" +
                "        </tr>\n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td colspan=\"5\">Quoted Successfully</td>\n" +
                "        </tr>\n" +
                "    \n" +
                "\n" +
                "</table>\n" +
                "\n" +
                "<div>\n" +
                "    <p class=\"p1\">Quotes are in Euro, exclude VAT, and can change subject to survey, specified contract " +
                "minimum" +
                " term, site establishment costs, and capacity check. Quotes are valid for 90 days. All pricing is " +
                "subject to a final engineering survey which may result in additional customer costs. Pricing " +
                "excludes customer site costs. Additional network build costs may be notified in accordance with Clause 2.2 of the General Service Schedule. For services that reside at a customer contracted third party site (including BT and non-BT Data Centre locations), BT will deliver to the agreed demarcation. For a Data Centre, this will be the Meet-Me Room (MMR). The customer is responsible for cabling from the MMR to the customer cabinet. BT is not responsible for cable routing within third-party locations (including Data Centres). BT applies an uplift for third party charges it incurs such as ISM, DAA etc.</p>\n"+
                "    <p class=\"p1\">A standard install applies on all new installations of € 1,000 per Data Centre service " +
                        "instance; and € 2,400 otherwise. If the quote is for a multi-year term, the standard install" +
                " is often discounted to € 0. Orders that are in Customer Delay for greater than an aggregate of 60 " +
                "working days will be deemed to be cancelled unless a revised due date can be mutually agreed. If an " +
                "order is cancelled after being in Customer Delay for an aggregate of 60 working days, the full connection charge will be billed. This will be the standard install (€ 1,000 or € 2,400 dependent on location), any related third-party charges incurred by BT and any exceptional costs previously agreed with the customer. For Etherflow pricing, the initial 1/10 Gig Etherway will need to be in place at your PoP.</p>\n"+
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }

    public static String getQbtdcCompleteEmailTextBDDScenario() {
        DateTimeFormatter SLASH_DAY_FIRST_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime dateTime = LocalDateTime.now();
        String date = dateTime.format(SLASH_DAY_FIRST_FORMAT);
        return "\n" +
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "    <style>\n" +
                "\t\tbody,\n" +
                "\n" +
                "\t\t.Heading {\n" +
                "\t\t    color:#6600cc;\n" +
                "\t\t    font-weight:bold;\n" +
                "\t\t    font-family: Arial;\n" +
                "\t\t    font-size:16px;\n" +
                "\t\t    padding-left: 5px;\n" +
                "\t\t    padding-top: 15px;\n" +
                "\t\t    padding-bottom: 15px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.p1 {\n" +
                "\t\t    font: 13px sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t\tfont-weight: 100;\n" +
                "\t\t\tcolor: black;\n" +
                "\t    }\n" +
                "\n" +
                "\t\tp {\n" +
                "\t\t    font: 14px Arial, Helvetica, sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t\tcolor: black;\n" +
                "\t    }\n" +
                "\n" +
                "\t\ttable {\n" +
                "\t\t\tborder-collapse: separate;\n" +
                "\t\t\tborder-spacing: 2px;\n" +
                "\t\t\tborder-style: none;\n" +
                "\t\t\tmso-line-height-rule: exactly;\n" +
                "\t\t\ttext-indent: initial;\n" +
                "\t\t\tpadding-top: 10px;\n" +
                "\t\t\tfont: 14px Arial, Helvetica, sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable td {\n" +
                "\t\t\tpadding: 10px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable#CircuitsTable {\n" +
                "\t\t\twidth: 100%;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable#OrderTable tr td {\n" +
                "\t\t    min-width:200px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableHeader {\n" +
                "\t\t\tbackground: #5514B4;\n" +
                "\t\t\tpadding: 2px;\n" +
                "\t\t\tcolor: #FFF;\n" +
                "\t\t\tfont-weight: bold;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableSubHeader {\n" +
                "\t\t\tbackground-color: #888;\n" +
                "\t\t\tcolor: #FFF;\n" +
                "\t\t\tpadding: 2px;\n" +
                "\t\t\tfont-weight: bold;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableElement {\n" +
                "\t\t    background: #e3dfde;\n" +
                "\t\t    color: black;\n" +
                "\t\t    font-weight: normal;\n" +
                "\t\t    font-family: Arial;\n" +
                "\t\t    padding: 6px;\n" +
                "\t\t    border: 1px solid #ccc;\n" +
                "\t\t    text-align: left;\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"Heading\">Order Details</div>\n" +
                "<table id=\"OrderTable\">\n" +
                "    <tr><td class=\"TableHeader\">Customer</td><td class=\"TableElement\">sky</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">BT Reference</td><td class=\"TableElement\">1</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">Quote Initiated Date</td><td class=\"TableElement\">"+date+"</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">Quote Date</td><td class=\"TableElement\">"+date+"</td></tr>\n" +
                "</table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<p>Please see below for your requested quotation.</p>\n" +
                "\n" +
                "<p>Kind Regards,</p>\n" +
                "<p>BT Ireland Wholesale Pricing Team</p>\n" +
                "\n" +
                "\n" +
                "<div class=\"Heading\">Circuit Details</div>\n" +
                "<table id=\"CircuitsTable\">\n" +
                "    <tr class=\"TableHeader\">\n" +
                "        <td rowspan=\"2\" >Quote ID</td>\n" +
                "        <td rowspan=\"2\">Status</td>\n" +
                "        <td rowspan=\"2\">Product</td>\n" +
                "        <td rowspan=\"2\">Connection Type</td>\n" +
                "        <td colspan=\"3\">A-End (End Customer)</td>\n" +
                "        <td>B-End (PoP)</td>\n" +
                "        <td>Logical</td>\n" +
                "        <td colspan=\"2\">Price</td>\n" +
                "    </tr>\n" +
                "    <tr class=\"TableSubHeader\">\n" +
                "        <td>Eircode</td>\n" +
                "        <td>Access</td>\n" +
                "        <td>Delivery</td>\n" +
                "        <td>Handoff</td>\n" +
                "        <td>Access</td>\n" +
                "        <td>NRC</td>\n" +
                "        <td>RC</td>\n" +
                "    </tr>\n" +
                "    \n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td rowspan=\"3\"><span>1</span></td>\n" +
                "            <td rowspan=\"3\"><span>Quoted</span></td>\n" +
                "            <td rowspan=\"3\"><div><span>WIC</span><span> /31</span></div><div><span>5 Years</span></div></td>\n" +
                "            <td><span>A00F027</span></td>\n" +
                "            <td><span>10 Gb</span><div>Enhanced SLA</div></td>\n" +
                "            <td>BT On-Net</td>\n" +
                "            <td>Internet</td>\n" +
                "            <td><span>6000 Mb</span><div>Primary (0% AF)</div></td>\n" +
                "            <td rowspan=\"3\"><span>&euro; </span><span>300</span></td>\n" +
                "            <td rowspan=\"3\"><span>&euro; </span><span>40</span><div>Annual</div></td>\n" +
                "        </tr>\n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td colspan=\"5\"></td>\n" +
                "        </tr>\n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td colspan=\"5\">This is test notes</td>\n" +
                "        </tr>\n" +
                "    \n" +
                "\n" +
                "</table>\n" +
                "\n" +
                "<div>\n" +
                "    <p class=\"p1\">Quotes are in Euro, exclude VAT, and can change subject to survey, specified contract " +
                "minimum" +
                " term, site establishment costs, and capacity check. Quotes are valid for 90 days. All pricing is " +
                "subject to a final engineering survey which may result in additional customer costs. Pricing " +
                "excludes customer site costs. Additional network build costs may be notified in accordance with Clause 2.2 of the General Service Schedule. For services that reside at a customer contracted third party site (including BT and non-BT Data Centre locations), BT will deliver to the agreed demarcation. For a Data Centre, this will be the Meet-Me Room (MMR). The customer is responsible for cabling from the MMR to the customer cabinet. BT is not responsible for cable routing within third-party locations (including Data Centres). BT applies an uplift for third party charges it incurs such as ISM, DAA etc.</p>\n"+
                "    <p class=\"p1\">A standard install applies on all new installations of € 1,000 per Data Centre " +
                "service " +
                "instance; and € 2,400 otherwise. If the quote is for a multi-year term, the standard install" +
                " is often discounted to € 0. Orders that are in Customer Delay for greater than an aggregate of 60 " +
                "working days will be deemed to be cancelled unless a revised due date can be mutually agreed. If an " +
                "order is cancelled after being in Customer Delay for an aggregate of 60 working days, the full connection charge will be billed. This will be the standard install (€ 1,000 or € 2,400 dependent on location), any related third-party charges incurred by BT and any exceptional costs previously agreed with the customer. For Etherflow pricing, the initial 1/10 Gig Etherway will need to be in place at your PoP.</p>\n"+
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }

    public static String getQbtdcCompleteEmailTextRejectBDDScenario() {
        DateTimeFormatter SLASH_DAY_FIRST_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime dateTime = LocalDateTime.now();
        String date = dateTime.format(SLASH_DAY_FIRST_FORMAT);

        return "\n" +
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "    <style>\n" +
                "\t\tbody,\n" +
                "\n" +
                "\t\t.Heading {\n" +
                "\t\t    color:#6600cc;\n" +
                "\t\t    font-weight:bold;\n" +
                "\t\t    font-family: Arial;\n" +
                "\t\t    font-size:16px;\n" +
                "\t\t    padding-left: 5px;\n" +
                "\t\t    padding-top: 15px;\n" +
                "\t\t    padding-bottom: 15px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.p1 {\n" +
                "\t\t    font: 13px sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t\tfont-weight: 100;\n" +
                "\t\t\tcolor: black;\n" +
                "\t    }\n" +
                "\n" +
                "\t\tp {\n" +
                "\t\t    font: 14px Arial, Helvetica, sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t\tcolor: black;\n" +
                "\t    }\n" +
                "\n" +
                "\t\ttable {\n" +
                "\t\t\tborder-collapse: separate;\n" +
                "\t\t\tborder-spacing: 2px;\n" +
                "\t\t\tborder-style: none;\n" +
                "\t\t\tmso-line-height-rule: exactly;\n" +
                "\t\t\ttext-indent: initial;\n" +
                "\t\t\tpadding-top: 10px;\n" +
                "\t\t\tfont: 14px Arial, Helvetica, sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable td {\n" +
                "\t\t\tpadding: 10px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable#CircuitsTable {\n" +
                "\t\t\twidth: 100%;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable#OrderTable tr td {\n" +
                "\t\t    min-width:200px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableHeader {\n" +
                "\t\t\tbackground: #5514B4;\n" +
                "\t\t\tpadding: 2px;\n" +
                "\t\t\tcolor: #FFF;\n" +
                "\t\t\tfont-weight: bold;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableSubHeader {\n" +
                "\t\t\tbackground-color: #888;\n" +
                "\t\t\tcolor: #FFF;\n" +
                "\t\t\tpadding: 2px;\n" +
                "\t\t\tfont-weight: bold;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableElement {\n" +
                "\t\t    background: #e3dfde;\n" +
                "\t\t    color: black;\n" +
                "\t\t    font-weight: normal;\n" +
                "\t\t    font-family: Arial;\n" +
                "\t\t    padding: 6px;\n" +
                "\t\t    border: 1px solid #ccc;\n" +
                "\t\t    text-align: left;\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"Heading\">Order Details</div>\n" +
                "<table id=\"OrderTable\">\n" +
                "    <tr><td class=\"TableHeader\">Customer</td><td class=\"TableElement\">sky</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">BT Reference</td><td class=\"TableElement\">1</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">Quote Initiated Date</td><td class=\"TableElement\">" + date + "</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">Quote Date</td><td class=\"TableElement\">"+ date + "</td></tr>\n" +
                "</table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<p>Please see below for your requested quotation.</p>\n" +
                "\n" +
                "<p>Kind Regards,</p>\n" +
                "<p>BT Ireland Wholesale Pricing Team</p>\n" +
                "\n" +
                "\n" +
                "<div class=\"Heading\">Circuit Details</div>\n" +
                "<table id=\"CircuitsTable\">\n" +
                "    <tr class=\"TableHeader\">\n" +
                "        <td rowspan=\"2\" >Quote ID</td>\n" +
                "        <td rowspan=\"2\">Status</td>\n" +
                "        <td rowspan=\"2\">Product</td>\n" +
                "        <td rowspan=\"2\">Connection Type</td>\n" +
                "        <td colspan=\"3\">A-End (End Customer)</td>\n" +
                "        <td>B-End (PoP)</td>\n" +
                "        <td>Logical</td>\n" +
                "        <td colspan=\"2\">Price</td>\n" +
                "    </tr>\n" +
                "    <tr class=\"TableSubHeader\">\n" +
                "        <td>Eircode</td>\n" +
                "        <td>Access</td>\n" +
                "        <td>Delivery</td>\n" +
                "        <td>Handoff</td>\n" +
                "        <td>Access</td>\n" +
                "        <td>NRC</td>\n" +
                "        <td>RC</td>\n" +
                "    </tr>\n" +
                "    \n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td rowspan=\"3\"><span>1</span></td>\n" +
                "            <td rowspan=\"3\"><span>Quoted</span></td>\n" +
                "            <td rowspan=\"3\"><div><span>WIC</span><span> /31</span></div><div><span>5 Years</span></div></td>\n" +
                "            <td><span>A00F027</span></td>\n" +
                "            <td><span>10 Gb</span><div>Enhanced SLA</div></td>\n" +
                "            <td>BT On-Net</td>\n" +
                "            <td>Internet</td>\n" +
                "            <td><span>6000 Mb</span><div>Primary (0% AF)</div></td>\n" +
                "            <td rowspan=\"3\"><span>&euro; </span><span>300</span></td>\n" +
                "            <td rowspan=\"3\"><span>&euro; </span><span>40</span><div>Annual</div></td>\n" +
                "        </tr>\n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td colspan=\"5\"></td>\n" +
                "        </tr>\n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td colspan=\"5\">This is test notes</td>\n" +
                "        </tr>\n" +
                "    \n" +
                "\n" +
                "</table>\n" +
                "\n" +
                "<div>\n" +
                "    <p class=\"p1\">Quotes are in Euro, exclude VAT, and can change subject to survey, specified contract " +
                "minimum" +
                " term, site establishment costs, and capacity check. Quotes are valid for 90 days. All pricing is " +
                "subject to a final engineering survey which may result in additional customer costs. Pricing " +
                "excludes customer site costs. Additional network build costs may be notified in accordance with Clause 2.2 of the General Service Schedule. For services that reside at a customer contracted third party site (including BT and non-BT Data Centre locations), BT will deliver to the agreed demarcation. For a Data Centre, this will be the Meet-Me Room (MMR). The customer is responsible for cabling from the MMR to the customer cabinet. BT is not responsible for cable routing within third-party locations (including Data Centres). BT applies an uplift for third party charges it incurs such as ISM, DAA etc.</p>\n"+
                "    <p class=\"p1\">A standard install applies on all new installations of € 1,000 per Data Centre service " +
                "instance; and € 2,400 otherwise. If the quote is for a multi-year term, the standard install" +
                " is often discounted to € 0. Orders that are in Customer Delay for greater than an aggregate of 60 " +
                "working days will be deemed to be cancelled unless a revised due date can be mutually agreed. If an " +
                "order is cancelled after being in Customer Delay for an aggregate of 60 working days, the full connection charge will be billed. This will be the standard install (€ 1,000 or € 2,400 dependent on location), any related third-party charges incurred by BT and any exceptional costs previously agreed with the customer. For Etherflow pricing, the initial 1/10 Gig Etherway will need to be in place at your PoP.</p>\n"+
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }

    public static String getQbtdcCompleteEmailTextNullMailBodyProps() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "    <style>\n" +
                "\t\tbody,\n" +
                "\n" +
                "\t\t.Heading {\n" +
                "\t\t    color:#6600cc;\n" +
                "\t\t    font-weight:bold;\n" +
                "\t\t    font-family: Arial;\n" +
                "\t\t    font-size:16px;\n" +
                "\t\t    padding-left: 5px;\n" +
                "\t\t    padding-top: 15px;\n" +
                "\t\t    padding-bottom: 15px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.p1 {\n" +
                "\t\t    font: 13px sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t\tfont-weight: 100;\n" +
                "\t\t\tcolor: black;\n" +
                "\t    }\n" +
                "\n" +
                "\t\tp {\n" +
                "\t\t    font: 14px Arial, Helvetica, sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t\tcolor: black;\n" +
                "\t    }\n" +
                "\n" +
                "\t\ttable {\n" +
                "\t\t\tborder-collapse: separate;\n" +
                "\t\t\tborder-spacing: 2px;\n" +
                "\t\t\tborder-style: none;\n" +
                "\t\t\tmso-line-height-rule: exactly;\n" +
                "\t\t\ttext-indent: initial;\n" +
                "\t\t\tpadding-top: 10px;\n" +
                "\t\t\tfont: 14px Arial, Helvetica, sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable td {\n" +
                "\t\t\tpadding: 10px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable#CircuitsTable {\n" +
                "\t\t\twidth: 100%;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable#OrderTable tr td {\n" +
                "\t\t    min-width:200px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableHeader {\n" +
                "\t\t\tbackground: #5514B4;\n" +
                "\t\t\tpadding: 2px;\n" +
                "\t\t\tcolor: #FFF;\n" +
                "\t\t\tfont-weight: bold;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableSubHeader {\n" +
                "\t\t\tbackground-color: #888;\n" +
                "\t\t\tcolor: #FFF;\n" +
                "\t\t\tpadding: 2px;\n" +
                "\t\t\tfont-weight: bold;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableElement {\n" +
                "\t\t    background: #e3dfde;\n" +
                "\t\t    color: black;\n" +
                "\t\t    font-weight: normal;\n" +
                "\t\t    font-family: Arial;\n" +
                "\t\t    padding: 6px;\n" +
                "\t\t    border: 1px solid #ccc;\n" +
                "\t\t    text-align: left;\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"Heading\">Order Details</div>\n" +
                "<table id=\"OrderTable\">\n" +
                "    <tr><td class=\"TableHeader\">Customer</td><td class=\"TableElement\">sky</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">BT Reference</td><td class=\"TableElement\">BT-QBTDC-1</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">Quote Initiated Date</td><td class=\"TableElement\">25/05/2022</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">Quote Date</td><td class=\"TableElement\">25/05/2022</td></tr>\n" +
                "</table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<p>Please see below for your requested quotation.</p>\n" +
                "\n" +
                "<p>Kind Regards,</p>\n" +
                "<p>BT Ireland Wholesale Pricing Team</p>\n" +
                "\n" +
                "\n" +
                "<div class=\"Heading\">Circuit Details</div>\n" +
                "<table id=\"CircuitsTable\">\n" +
                "    <tr class=\"TableHeader\">\n" +
                "        <td rowspan=\"2\" >Quote ID</td>\n" +
                "        <td rowspan=\"2\">Group</td>\n" +
                "        <td rowspan=\"2\">Status</td>\n" +
                "        <td rowspan=\"2\">Product</td>\n" +
                "        <td rowspan=\"2\">Connection Type</td>\n" +
                "        <td colspan=\"3\">A-End (End Customer)</td>\n" +
                "        <td>B-End (PoP)</td>\n" +
                "        <td>Logical</td>\n" +
                "        <td colspan=\"2\">Price</td>\n" +
                "    </tr>\n" +
                "    <tr class=\"TableSubHeader\">\n" +
                "        <td>Eircode</td>\n" +
                "        <td>Access</td>\n" +
                "        <td>Delivery</td>\n" +
                "        <td>Handoff</td>\n" +
                "        <td>Access</td>\n" +
                "        <td>NRC</td>\n" +
                "        <td>RC</td>\n" +
                "    </tr>\n" +
                "    \n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td rowspan=\"3\"><span>1</span></td>\n" +
                "            <td rowspan=\"3\"><span>G1</span></td>\n" +
                "            <td rowspan=\"3\"><span>Quoted</span></td>\n" +
                "            <td rowspan=\"3\"><div><span>WEC</span><span> /31</span></div><div><span>1 Year</span></div></td>\n" +
                "            <td rowspan=\"3\"><span>ETHERWAY_DIVERSE</span></td>\n" +
                "            <td><span>A00F027</span></td>\n" +
                "            <td><span>Existing</span><div></div></td>\n" +
                "            <td>BT On-Net</td>\n" +
                "            <td>Equinix DB1 - Unit 4027 Citywest</td>\n" +
                "            <td><span></span><div></div></td>\n" +
                "            <td rowspan=\"3\"><span>&euro; </span><span>2000</span></td>\n" +
                "            <td rowspan=\"3\"><span>&euro; </span><span>100</span><div></div></td>\n" +
                "        </tr>\n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td colspan=\"5\">DUNBEGGAN, AUGHNACLIFFE, LONGFORD</td>\n" +
                "        </tr>\n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td colspan=\"5\">Quoted Successfully</td>\n" +
                "        </tr>\n" +
                "    \n" +
                "\n" +
                "</table>\n" +
                "\n" +
                "<div>\n" +
                "    <p class=\"p1\">Quotes are in Euro, exclude VAT, and can change subject to survey, specified contract " +
                "minimum" +
                " term, site establishment costs, and capacity check. Quotes are valid for 90 days. All pricing is " +
                "subject to a final engineering survey which may result in additional customer costs. Pricing " +
                "excludes customer site costs. Additional network build costs may be notified in accordance with Clause 2.2 of the General Service Schedule. For services that reside at a customer contracted third party site (including BT and non-BT Data Centre locations), BT will deliver to the agreed demarcation. For a Data Centre, this will be the Meet-Me Room (MMR). The customer is responsible for cabling from the MMR to the customer cabinet. BT is not responsible for cable routing within third-party locations (including Data Centres). BT applies an uplift for third party charges it incurs such as ISM, DAA etc.</p>\n"+
                "    <p class=\"p1\">A standard install applies on all new installations of € 1,000 per Data Centre service " +
                "instance; and € 2,400 otherwise. If the quote is for a multi-year term, the standard install" +
                " is often discounted to € 0. Orders that are in Customer Delay for greater than an aggregate of 60 " +
                "working days will be deemed to be cancelled unless a revised due date can be mutually agreed. If an " +
                "order is cancelled after being in Customer Delay for an aggregate of 60 working days, the full connection charge will be billed. This will be the standard install (€ 1,000 or € 2,400 dependent on location), any related third-party charges incurred by BT and any exceptional costs previously agreed with the customer. For Etherflow pricing, the initial 1/10 Gig Etherway will need to be in place at your PoP.</p>\n"+
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }

    public static String getQbtdcDelayEmailText() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "    <style>\n" +
                "\t\tbody,\n" +
                "\n" +
                "\t\t.Heading {\n" +
                "\t\t    color:#6600cc;\n" +
                "\t\t    font-weight:bold;\n" +
                "\t\t    font-family: Arial;\n" +
                "\t\t    font-size:16px;\n" +
                "\t\t    padding-left: 5px;\n" +
                "\t\t    padding-top: 15px;\n" +
                "\t\t    padding-bottom: 15px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.p1 {\n" +
                "\t\t    font: 13px sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t\tfont-weight: 100;\n" +
                "\t\t\tcolor: black;\n" +
                "\t    }\n" +
                "\n" +
                "\t\tp {\n" +
                "\t\t    font: 14px Arial, Helvetica, sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t\tcolor: black;\n" +
                "\t    }\n" +
                "\n" +
                "\t\ttable {\n" +
                "\t\t\tborder-collapse: separate;\n" +
                "\t\t\tborder-spacing: 2px;\n" +
                "\t\t\tborder-style: none;\n" +
                "\t\t\tmso-line-height-rule: exactly;\n" +
                "\t\t\ttext-indent: initial;\n" +
                "\t\t\tpadding-top: 10px;\n" +
                "\t\t\tfont: 14px Arial, Helvetica, sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable td {\n" +
                "\t\t\tpadding: 10px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable#CircuitsTable {\n" +
                "\t\t\twidth: 100%;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable#OrderTable tr td {\n" +
                "\t\t    min-width:200px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableHeader {\n" +
                "\t\t\tbackground: #5514B4;\n" +
                "\t\t\tpadding: 2px;\n" +
                "\t\t\tcolor: #FFF;\n" +
                "\t\t\tfont-weight: bold;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableSubHeader {\n" +
                "\t\t\tbackground-color: #888;\n" +
                "\t\t\tcolor: #FFF;\n" +
                "\t\t\tpadding: 2px;\n" +
                "\t\t\tfont-weight: bold;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableElement {\n" +
                "\t\t    background: #e3dfde;\n" +
                "\t\t    color: black;\n" +
                "\t\t    font-weight: normal;\n" +
                "\t\t    font-family: Arial;\n" +
                "\t\t    padding: 6px;\n" +
                "\t\t    border: 1px solid #ccc;\n" +
                "\t\t    text-align: left;\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"Heading\">Order Details</div>\n" +
                "<table id=\"OrderTable\">\n" +
                "    <tr><td class=\"TableHeader\">Customer</td><td class=\"TableElement\">sky</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">BT Reference</td><td class=\"TableElement\">BT-QBTDC-1</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">Quote Initiated Date</td><td class=\"TableElement\">25/05/2022</td></tr>\n" +
                "    \n" +
                "</table>\n" +
                "\n" +
                "<div>\n" +
                "    <p>Please be advised your quote is delayed because of the below reason.</p>\n" +
                "    <p>Please contact the wholesalepricing@bt.com for further support</p>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<p>Kind Regards,</p>\n" +
                "<p>BT Ireland Wholesale Pricing Team</p>\n" +
                "\n" +
                "\n" +
                "<div class=\"Heading\">Circuit Details</div>\n" +
                "<table id=\"CircuitsTable\">\n" +
                "    <tr class=\"TableHeader\">\n" +
                "        <td rowspan=\"2\" >Quote ID</td>\n" +
                "        <td rowspan=\"2\">Group</td>\n" +
                "        <td rowspan=\"2\">Status</td>\n" +
                "        <td rowspan=\"2\">Product</td>\n" +
                "        <td rowspan=\"2\">Connection Type</td>\n" +
                "        <td colspan=\"3\">A-End (End Customer)</td>\n" +
                "        <td>B-End (PoP)</td>\n" +
                "        <td>Logical</td>\n" +
                "        <td colspan=\"2\">Price</td>\n" +
                "    </tr>\n" +
                "    <tr class=\"TableSubHeader\">\n" +
                "        <td>Eircode</td>\n" +
                "        <td>Access</td>\n" +
                "        <td>Delivery</td>\n" +
                "        <td>Handoff</td>\n" +
                "        <td>Access</td>\n" +
                "        <td>NRC</td>\n" +
                "        <td>RC</td>\n" +
                "    </tr>\n" +
                "    \n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td rowspan=\"3\"><span>1</span></td>\n" +
                "            <td rowspan=\"3\"><span>G1</span></td>\n" +
                "            <td rowspan=\"3\"><span>Delayed</span></td>\n" +
                "            <td rowspan=\"3\"><div><span>WEC</span><span> /31</span></div><div><span>5 Years</span></div></td>\n" +
                "            <td rowspan=\"3\"><span>ETHERWAY_DIVERSE</span></td>\n" +
                "            <td><span>A00F027</span></td>\n" +
                "            <td><span>10 Gb</span><div>Enhanced SLA</div></td>\n" +
                "            <td>BT On-Net</td>\n" +
                "            <td>Equinix DB1 - Unit 4027 Citywest</td>\n" +
                "            <td><span>500 Mb</span><div>Premium 100 (100% AF)</div></td>\n" +
                "            <td rowspan=\"3\"><span>&euro; </span><span></span></td>\n" +
                "            <td rowspan=\"3\"><span>&euro; </span><span></span></td>\n" +
                "        </tr>\n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td colspan=\"5\">DUNBEGGAN, AUGHNACLIFFE, LONGFORD</td>\n" +
                "        </tr>\n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td colspan=\"5\">Delayed due to customer</td>\n" +
                "        </tr>\n" +
                "    \n" +
                "\n" +
                "</table>\n" +
                "\n\n"+
                "\n" +
                "</body>\n" +
                "</html>";
    }

    public static String getQbtdcCompleteEmailTextForEmailAll() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "    <style>\n" +
                "\t\tbody,\n" +
                "\n" +
                "\t\t.Heading {\n" +
                "\t\t    color:#6600cc;\n" +
                "\t\t    font-weight:bold;\n" +
                "\t\t    font-family: Arial;\n" +
                "\t\t    font-size:16px;\n" +
                "\t\t    padding-left: 5px;\n" +
                "\t\t    padding-top: 15px;\n" +
                "\t\t    padding-bottom: 15px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.p1 {\n" +
                "\t\t    font: 13px sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t\tfont-weight: 100;\n" +
                "\t\t\tcolor: black;\n" +
                "\t    }\n" +
                "\n" +
                "\t\tp {\n" +
                "\t\t    font: 14px Arial, Helvetica, sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t\tcolor: black;\n" +
                "\t    }\n" +
                "\n" +
                "\t\ttable {\n" +
                "\t\t\tborder-collapse: separate;\n" +
                "\t\t\tborder-spacing: 2px;\n" +
                "\t\t\tborder-style: none;\n" +
                "\t\t\tmso-line-height-rule: exactly;\n" +
                "\t\t\ttext-indent: initial;\n" +
                "\t\t\tpadding-top: 10px;\n" +
                "\t\t\tfont: 14px Arial, Helvetica, sans-serif;\n" +
                "\t\t\tline-height: 14px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable td {\n" +
                "\t\t\tpadding: 10px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable#CircuitsTable {\n" +
                "\t\t\twidth: 100%;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable#OrderTable tr td {\n" +
                "\t\t    min-width:200px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableHeader {\n" +
                "\t\t\tbackground: #5514B4;\n" +
                "\t\t\tpadding: 2px;\n" +
                "\t\t\tcolor: #FFF;\n" +
                "\t\t\tfont-weight: bold;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableSubHeader {\n" +
                "\t\t\tbackground-color: #888;\n" +
                "\t\t\tcolor: #FFF;\n" +
                "\t\t\tpadding: 2px;\n" +
                "\t\t\tfont-weight: bold;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.TableElement {\n" +
                "\t\t    background: #e3dfde;\n" +
                "\t\t    color: black;\n" +
                "\t\t    font-weight: normal;\n" +
                "\t\t    font-family: Arial;\n" +
                "\t\t    padding: 6px;\n" +
                "\t\t    border: 1px solid #ccc;\n" +
                "\t\t    text-align: left;\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"Heading\">Order Details</div>\n" +
                "<table id=\"OrderTable\">\n" +
                "    <tr><td class=\"TableHeader\">Customer</td><td class=\"TableElement\">sky</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">BT Reference</td><td class=\"TableElement\">BT-QBTDC-1</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">Quote Initiated Date</td><td class=\"TableElement\">25/05/2022</td></tr>\n" +
                "    <tr><td class=\"TableHeader\">Quote Date</td><td class=\"TableElement\">25/05/2022</td></tr>\n" +
                "</table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<p>Please see below for your requested quotation.</p>\n" +
                "<p>narrative</p>\n" +
                "<p>Kind Regards,</p>\n" +
                "<p>BT Ireland Wholesale Pricing Team</p>\n" +
                "\n" +
                "\n" +
                "<div class=\"Heading\">Circuit Details</div>\n" +
                "<table id=\"CircuitsTable\">\n" +
                "    <tr class=\"TableHeader\">\n" +
                "        <td rowspan=\"2\" >Quote ID</td>\n" +
                "        <td rowspan=\"2\">Group</td>\n" +
                "        <td rowspan=\"2\">Status</td>\n" +
                "        <td rowspan=\"2\">Product</td>\n" +
                "        <td rowspan=\"2\">Connection Type</td>\n" +
                "        <td colspan=\"3\">A-End (End Customer)</td>\n" +
                "        <td>B-End (PoP)</td>\n" +
                "        <td>Logical</td>\n" +
                "        <td colspan=\"2\">Price</td>\n" +
                "    </tr>\n" +
                "    <tr class=\"TableSubHeader\">\n" +
                "        <td>Eircode</td>\n" +
                "        <td>Access</td>\n" +
                "        <td>Delivery</td>\n" +
                "        <td>Handoff</td>\n" +
                "        <td>Access</td>\n" +
                "        <td>NRC</td>\n" +
                "        <td>RC</td>\n" +
                "    </tr>\n" +
                "    \n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td rowspan=\"3\"><span>1</span></td>\n" +
                "            <td rowspan=\"3\"><span>G1</span></td>\n" +
                "            <td rowspan=\"3\"><span>Rejected</span></td>\n" +
                "            <td rowspan=\"3\"><div><span>WEC</span><span> /31</span></div><div><span>5 Years</span></div></td>\n" +
                "            <td rowspan=\"3\"><span>ETHERWAY_DIVERSE</span></td>\n" +
                "            <td><span>A00F027</span></td>\n" +
                "            <td><span>10 Gb</span><div>Enhanced SLA</div></td>\n" +
                "            <td></td>\n" +
                "            <td>Equinix DB1 - Unit 4027 Citywest</td>\n" +
                "            <td><span>500 Mb</span><div>Premium 100 (100% AF)</div></td>\n" +
                "            <td rowspan=\"3\"><span>&euro; </span><span></span></td>\n" +
                "            <td rowspan=\"3\"><span>&euro; </span><span></span></td>\n" +
                "        </tr>\n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td colspan=\"5\"></td>\n" +
                "        </tr>\n" +
                "        <tr class=\"TableElement\">\n" +
                "            <td colspan=\"5\">Cannot be Quoted</td>\n" +
                "        </tr>\n" +
                "    \n" +
                "\n" +
                "</table>\n" +
                "\n" +
                "<div>\n" +
                "    <p class=\"p1\">Quotes are in Euro, exclude VAT, and can change subject to survey, specified contract " +
                "minimum" +
                " term, site establishment costs, and capacity check. Quotes are valid for 90 days. All pricing is " +
                "subject to a final engineering survey which may result in additional customer costs. Pricing " +
                "excludes customer site costs. Additional network build costs may be notified in accordance with Clause 2.2 of the General Service Schedule. For services that reside at a customer contracted third party site (including BT and non-BT Data Centre locations), BT will deliver to the agreed demarcation. For a Data Centre, this will be the Meet-Me Room (MMR). The customer is responsible for cabling from the MMR to the customer cabinet. BT is not responsible for cable routing within third-party locations (including Data Centres). BT applies an uplift for third party charges it incurs such as ISM, DAA etc.</p>\n"+
                "    <p class=\"p1\">A standard install applies on all new installations of € 1,000 per Data Centre service " +
                "instance; and € 2,400 otherwise. If the quote is for a multi-year term, the standard install" +
                " is often discounted to € 0. Orders that are in Customer Delay for greater than an aggregate of 60 " +
                "working days will be deemed to be cancelled unless a revised due date can be mutually agreed. If an " +
                "order is cancelled after being in Customer Delay for an aggregate of 60 working days, the full connection charge will be billed. This will be the standard install (€ 1,000 or € 2,400 dependent on location), any related third-party charges incurred by BT and any exceptional costs previously agreed with the customer. For Etherflow pricing, the initial 1/10 Gig Etherway will need to be in place at your PoP.</p>\n"+
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }
}
