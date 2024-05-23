package com.atguigu.yygh.msm.service.impl;


import com.atguigu.yygh.msm.service.MsmService;
import com.atguigu.yygh.vo.msm.MsmVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

@Service
public class MsmServiceImpl implements MsmService {

    private static String from = "";// The sender's email address
    private static String user = "";// Sender's title, same as email address
    private static String password = "";// Authorization code for sender's email
    private static String name = "";

    @Override
    public boolean send(String mail, String code) {

        if(StringUtils.isEmpty(mail)) {
            return false;
        }

        if(StringUtils.isEmpty(code)) {
            return false;
        }
        Date date = new Date();

        boolean flag = sendMail(mail, makeContent(name,code), "verification code");
        return flag;
    }

    @Override
    public boolean send(MsmVo msmVo) {
        if(!StringUtils.isEmpty(msmVo.getMail())) {
            if ("order".equals(msmVo.getTemplateCode())){
                return sendMail(msmVo.getMail(),makeContent(msmVo.getParam(),name),"【"+msmVo.getParam().get("hosname")+"】：pending payment reminder");
            }else if ("cancel order".equals(msmVo.getTemplateCode())){
                return sendMail(msmVo.getMail(),makeContent(name),"【"+msmVo.getParam().get("hosname")+"】：cancel successful");
            }else if ("payment successful".equals(msmVo.getTemplateCode())){
                return sendMail(msmVo.getMail(),makeContent(name,msmVo.getParam()),"【"+msmVo.getParam().get("hosname")+"】：appointment made");
            }else if ("refunded".equals(msmVo.getTemplateCode())){
                return sendMail(msmVo.getMail(),makeContent(name,msmVo.getParam(),1),"【"+msmVo.getParam().get("hosname")+"】：refund received");
            }else if ("check in reminder".equals(msmVo.getTemplateCode())){
                return sendMail(msmVo.getMail(),makeContent(name,msmVo.getParam(),""),"【"+msmVo.getParam().get("hosname")+"】：check in reminder");
            }
        }
        return false;
    }

    /**
     *
     * @param to
     * @param text
     * @param title
     * @return
     */
    public static boolean sendMail(String to, String text, String title) {
        Properties props = new Properties();
        props.setProperty("", "smtp.163.com"); //Set the properties of the email server that sends emails
        props.put("mail.smtp.host", "smtp.163.com"); // authorization
        props.put("mail.smtp.auth", "true"); // use props obj to set a session
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session); //sender email address
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); // receiver email address
            message.setSubject(title); // title
            Multipart multipart = new MimeMultipart(); // content and attachments
            BodyPart contentPart = new MimeBodyPart(); // text content
            contentPart.setContent(text, "text/html;charset=utf-8");
            multipart.addBodyPart(contentPart);
            message.setContent(multipart);
            message.saveChanges(); // save
            Transport transport = session.getTransport("smtp"); // connect server
            transport.connect("smtp.163.com", user, password); // send email
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String makeContent(String name,Map<String, Object> params,String m){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());

        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title></title>\n" +
                "<style>\n" +
                ".qmbox {\n" +
                "\tpadding: 0;\n" +
                "}\n" +
                ".qm_con_body_content {\n" +
                "\theight: auto;\n" +
                "\tmin-height: 100px;\n" +
                "\t_height: 100px;\n" +
                "\tword-wrap: break-word;\n" +
                "\tfont-size: 14px;\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "}\n" +
                ".body {\n" +
                "\tline-height: 170%;\n" +
                "}\n" +
                "BODY {\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "\tfont-size: 12px;\n" +
                "\t-webkit-font-smoothing: subpixel-antialiased;\n" +
                "}\n" +
                "BODY {\n" +
                "\tmargin: 0;\n" +
                "\tpadding: 0;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground-color: #fff;\n" +
                "\tfont-size: 12px;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground: #fff;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground: #fff;\n" +
                "\tcolor: #000;\n" +
                "\tfont-weight: normal;\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "\tpadding: 0 7px 6px 4px;\n" +
                "\tmargin: 0;\n" +
                "}\n" +
                "HTML {\n" +
                "\ttop: 0px;\n" +
                "}\n" +
                ".body P {\n" +
                "\tline-height: 170%;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "\n" +
                "<BODY mu=\"mu\" md=\"md\" module=\"qmReadMail\" context=\"ZC1912-rQ7uXSy7P7HThxdLFCOuY92\"><DIV class=mailcontainer id=qqmail_mailcontainer>\n" +
                "<DIV id=mainmail style=\"MARGIN-BOTTOM: 12px; POSITION: relative; Z-INDEX: 1\">\n" +
                "<DIV class=body id=contentDiv style=\"FONT-SIZE: 14px; HEIGHT: auto; POSITION: relative; ZOOM: 1; PADDING-BOTTOM: 10px; PADDING-TOP: 15px; PADDING-LEFT: 15px; Z-INDEX: 1; LINE-HEIGHT: 1.7; PADDING-RIGHT: 15px\" onmouseover=getTop().stopPropagation(event); onclick=\"getTop().preSwapLink(event, 'html', 'ZC1912-rQ7uXSy7P7HThxdLFCOuY92');\">\n" +
                "<DIV id=qm_con_body>\n" +
                "<DIV class=\"qmbox qm_con_body_content qqmail_webmail_only\" id=mailContentContainer>\n" +
                "<DIV class=main style=\"OVERFLOW: hidden; WIDTH: 100%; BACKGROUND-COLOR: #f7f7f7\">\n" +
                "<DIV class=content style=\"BORDER-TOP: #cccccc 1px solid; BORDER-RIGHT: #cccccc 1px solid; BACKGROUND: #ffffff; BORDER-BOTTOM: #cccccc 1px solid; PADDING-BOTTOM: 10px; PADDING-TOP: 10px; PADDING-LEFT: 25px; BORDER-LEFT: #cccccc 1px solid; MARGIN: 50px; PADDING-RIGHT: 25px\">\n" +
                "<DIV class=header style=\"MARGIN-BOTTOM: 30px\">\n" +
                "<P>"+params.get("name")+"：</P></DIV>\n" +
                "<P>Hi！Your reservation【"+params.get("title")+"】"+params.get("reserveDate")+", please check in on time</P>\n" +
                "<P><SPAN style=\"FONT-SIZE: 16px; FONT-WEIGHT: bold; COLOR: #f90\"></SPAN></P>\n" +
                "<P>1、Please confirm if the patient's information is accurate；<br/>" +
                "<span style=\"color: red\"\n" +
                "              >2、On the day of the appointment, it is necessary to\n" +
                params.get("fetchTime")+"\n" +
                "              Picking up a number at the hospital, failure to do so will be considered a breach of contract, and the number will not be returned or exchanged；</span\n" +
                "            ><br />\n" +
                "            3、Refund Before"+params.get("quitTime")+"you can cancel and get refund online\n" +
                "            ，otherwise you will waste your money；<br />\n" +
                "            4、；<br />\n" +
                "            5、。</P>\n" +

                "<DIV class=footer style=\"MARGIN-TOP: 30px\">\n" +
                "<P>"+name+"</P>\n" +
                "<P><SPAN style=\"BORDER-BOTTOM: #ccc 1px dashed; POSITION: relative; _display: inline-block\" t=\"5\" times=\"\" isout=\"0\">"+date+"</SPAN></P></DIV>\n" +
                "<DIV class=tip style=\"COLOR: #cccccc; TEXT-ALIGN: center\">This email is automatically sent by the system, please do not reply </DIV></DIV></DIV></DIV></DIV></DIV></DIV></DIV></BODY>\n" +
                "</html>\n";

        return content;
    }

    private String makeContent(String name,Map<String, Object> params,Integer i){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());

        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title></title>\n" +
                "<style>\n" +
                ".qmbox {\n" +
                "\tpadding: 0;\n" +
                "}\n" +
                ".qm_con_body_content {\n" +
                "\theight: auto;\n" +
                "\tmin-height: 100px;\n" +
                "\t_height: 100px;\n" +
                "\tword-wrap: break-word;\n" +
                "\tfont-size: 14px;\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "}\n" +
                ".body {\n" +
                "\tline-height: 170%;\n" +
                "}\n" +
                "BODY {\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "\tfont-size: 12px;\n" +
                "\t-webkit-font-smoothing: subpixel-antialiased;\n" +
                "}\n" +
                "BODY {\n" +
                "\tmargin: 0;\n" +
                "\tpadding: 0;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground-color: #fff;\n" +
                "\tfont-size: 12px;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground: #fff;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground: #fff;\n" +
                "\tcolor: #000;\n" +
                "\tfont-weight: normal;\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "\tpadding: 0 7px 6px 4px;\n" +
                "\tmargin: 0;\n" +
                "}\n" +
                "HTML {\n" +
                "\ttop: 0px;\n" +
                "}\n" +
                ".body P {\n" +
                "\tline-height: 170%;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "\n" +
                "<BODY mu=\"mu\" md=\"md\" module=\"qmReadMail\" context=\"ZC1912-rQ7uXSy7P7HThxdLFCOuY92\"><DIV class=mailcontainer id=qqmail_mailcontainer>\n" +
                "<DIV id=mainmail style=\"MARGIN-BOTTOM: 12px; POSITION: relative; Z-INDEX: 1\">\n" +
                "<DIV class=body id=contentDiv style=\"FONT-SIZE: 14px; HEIGHT: auto; POSITION: relative; ZOOM: 1; PADDING-BOTTOM: 10px; PADDING-TOP: 15px; PADDING-LEFT: 15px; Z-INDEX: 1; LINE-HEIGHT: 1.7; PADDING-RIGHT: 15px\" onmouseover=getTop().stopPropagation(event); onclick=\"getTop().preSwapLink(event, 'html', 'ZC1912-rQ7uXSy7P7HThxdLFCOuY92');\">\n" +
                "<DIV id=qm_con_body>\n" +
                "<DIV class=\"qmbox qm_con_body_content qqmail_webmail_only\" id=mailContentContainer>\n" +
                "<DIV class=main style=\"OVERFLOW: hidden; WIDTH: 100%; BACKGROUND-COLOR: #f7f7f7\">\n" +
                "<DIV class=content style=\"BORDER-TOP: #cccccc 1px solid; BORDER-RIGHT: #cccccc 1px solid; BACKGROUND: #ffffff; BORDER-BOTTOM: #cccccc 1px solid; PADDING-BOTTOM: 10px; PADDING-TOP: 10px; PADDING-LEFT: 25px; BORDER-LEFT: #cccccc 1px solid; MARGIN: 50px; PADDING-RIGHT: 25px\">\n" +
                "<DIV class=header style=\"MARGIN-BOTTOM: 30px\">\n" +
                "<P>"+params.get("name")+"：</P></DIV>\n" +
                "<P>Hi！Your reservation【"+params.get("title")+"】"+params.get("reserveDate")+"has been cancelled</P>\n" +
                "<P>The fee will be refunded to your account within 2 hours. Please check your account carefully</P>\n"+
                "<DIV class=footer style=\"MARGIN-TOP: 30px\">\n" +
                "<P>"+name+"</P>\n" +
                "<P><SPAN style=\"BORDER-BOTTOM: #ccc 1px dashed; POSITION: relative; _display: inline-block\" t=\"5\" times=\"\" isout=\"0\">"+date+"</SPAN></P></DIV>\n" +
                "<DIV class=tip style=\"COLOR: #cccccc; TEXT-ALIGN: center\">This email is automatically sent by the system, please do not reply </DIV></DIV></DIV></DIV></DIV></DIV></DIV></DIV></BODY>\n" +
                "</html>\n";

        return content;
    }
     //Success message
    private String makeContent(String name,Map<String, Object> params){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());

        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title></title>\n" +
                "<style>\n" +
                ".qmbox {\n" +
                "\tpadding: 0;\n" +
                "}\n" +
                ".qm_con_body_content {\n" +
                "\theight: auto;\n" +
                "\tmin-height: 100px;\n" +
                "\t_height: 100px;\n" +
                "\tword-wrap: break-word;\n" +
                "\tfont-size: 14px;\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "}\n" +
                ".body {\n" +
                "\tline-height: 170%;\n" +
                "}\n" +
                "BODY {\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "\tfont-size: 12px;\n" +
                "\t-webkit-font-smoothing: subpixel-antialiased;\n" +
                "}\n" +
                "BODY {\n" +
                "\tmargin: 0;\n" +
                "\tpadding: 0;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground-color: #fff;\n" +
                "\tfont-size: 12px;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground: #fff;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground: #fff;\n" +
                "\tcolor: #000;\n" +
                "\tfont-weight: normal;\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "\tpadding: 0 7px 6px 4px;\n" +
                "\tmargin: 0;\n" +
                "}\n" +
                "HTML {\n" +
                "\ttop: 0px;\n" +
                "}\n" +
                ".body P {\n" +
                "\tline-height: 170%;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "\n" +
                "<BODY mu=\"mu\" md=\"md\" module=\"qmReadMail\" context=\"ZC1912-rQ7uXSy7P7HThxdLFCOuY92\"><DIV class=mailcontainer id=qqmail_mailcontainer>\n" +
                "<DIV id=mainmail style=\"MARGIN-BOTTOM: 12px; POSITION: relative; Z-INDEX: 1\">\n" +
                "<DIV class=body id=contentDiv style=\"FONT-SIZE: 14px; HEIGHT: auto; POSITION: relative; ZOOM: 1; PADDING-BOTTOM: 10px; PADDING-TOP: 15px; PADDING-LEFT: 15px; Z-INDEX: 1; LINE-HEIGHT: 1.7; PADDING-RIGHT: 15px\" onmouseover=getTop().stopPropagation(event); onclick=\"getTop().preSwapLink(event, 'html', 'ZC1912-rQ7uXSy7P7HThxdLFCOuY92');\">\n" +
                "<DIV id=qm_con_body>\n" +
                "<DIV class=\"qmbox qm_con_body_content qqmail_webmail_only\" id=mailContentContainer>\n" +
                "<DIV class=main style=\"OVERFLOW: hidden; WIDTH: 100%; BACKGROUND-COLOR: #f7f7f7\">\n" +
                "<DIV class=content style=\"BORDER-TOP: #cccccc 1px solid; BORDER-RIGHT: #cccccc 1px solid; BACKGROUND: #ffffff; BORDER-BOTTOM: #cccccc 1px solid; PADDING-BOTTOM: 10px; PADDING-TOP: 10px; PADDING-LEFT: 25px; BORDER-LEFT: #cccccc 1px solid; MARGIN: 50px; PADDING-RIGHT: 25px\">\n" +
                "<DIV class=header style=\"MARGIN-BOTTOM: 30px\">\n" +
                "<P>"+params.get("name")+"：</P></DIV>\n" +
                "<P>Hi！You have reserved【"+params.get("title")+"】"+params.get("reserveDate")+"</P>\n" +
                "<P><SPAN style=\"FONT-SIZE: 16px; FONT-WEIGHT: bold; COLOR: #f90\">notice</SPAN></P>\n" +
                "<P>1、Please confirm if the patient's information is accurate；<br/>" +
                "<span style=\"color: red\"\n" +
                "              >2、Check in before\n" +
                params.get("fetchTime")+"\n" +
                "              or you will waste your chance；</span\n" +
                "            ><br />\n" +
                "            3、before "+formatDate((Long)params.get("quitTime"))+"you can cancel your reservation\n" +
                "            ，or you will loose this chance；<br />\n" +
                "            4、Please bring your insurance card and valid ID；<br />\n" +
                "            5、;</P>\n" +

                "<DIV class=footer style=\"MARGIN-TOP: 30px\">\n" +
                "<P>"+name+"</P>\n" +
                "<P><SPAN style=\"BORDER-BOTTOM: #ccc 1px dashed; POSITION: relative; _display: inline-block\" t=\"5\" times=\"\" isout=\"0\">"+date+"</SPAN></P></DIV>\n" +
                "<DIV class=tip style=\"COLOR: #cccccc; TEXT-ALIGN: center\">This email is automatically sent by the system, please do not reply </DIV></DIV></DIV></DIV></DIV></DIV></DIV></DIV></BODY>\n" +
                "</html>\n";

        return content;
    }

    private String makeContent(String name){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());

        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title></title>\n" +
                "<style>\n" +
                ".qmbox {\n" +
                "\tpadding: 0;\n" +
                "}\n" +
                ".qm_con_body_content {\n" +
                "\theight: auto;\n" +
                "\tmin-height: 100px;\n" +
                "\t_height: 100px;\n" +
                "\tword-wrap: break-word;\n" +
                "\tfont-size: 14px;\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "}\n" +
                ".body {\n" +
                "\tline-height: 170%;\n" +
                "}\n" +
                "BODY {\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "\tfont-size: 12px;\n" +
                "\t-webkit-font-smoothing: subpixel-antialiased;\n" +
                "}\n" +
                "BODY {\n" +
                "\tmargin: 0;\n" +
                "\tpadding: 0;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground-color: #fff;\n" +
                "\tfont-size: 12px;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground: #fff;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground: #fff;\n" +
                "\tcolor: #000;\n" +
                "\tfont-weight: normal;\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "\tpadding: 0 7px 6px 4px;\n" +
                "\tmargin: 0;\n" +
                "}\n" +
                "HTML {\n" +
                "\ttop: 0px;\n" +
                "}\n" +
                ".body P {\n" +
                "\tline-height: 170%;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "\n" +
                "<BODY mu=\"mu\" md=\"md\" module=\"qmReadMail\" context=\"ZC1912-rQ7uXSy7P7HThxdLFCOuY92\"><DIV class=mailcontainer id=qqmail_mailcontainer>\n" +
                "<DIV id=mainmail style=\"MARGIN-BOTTOM: 12px; POSITION: relative; Z-INDEX: 1\">\n" +
                "<DIV class=body id=contentDiv style=\"FONT-SIZE: 14px; HEIGHT: auto; POSITION: relative; ZOOM: 1; PADDING-BOTTOM: 10px; PADDING-TOP: 15px; PADDING-LEFT: 15px; Z-INDEX: 1; LINE-HEIGHT: 1.7; PADDING-RIGHT: 15px\" onmouseover=getTop().stopPropagation(event); onclick=\"getTop().preSwapLink(event, 'html', 'ZC1912-rQ7uXSy7P7HThxdLFCOuY92');\">\n" +
                "<DIV id=qm_con_body>\n" +
                "<DIV class=\"qmbox qm_con_body_content qqmail_webmail_only\" id=mailContentContainer>\n" +
                "<DIV class=main style=\"OVERFLOW: hidden; WIDTH: 100%; BACKGROUND-COLOR: #f7f7f7\">\n" +
                "<DIV class=content style=\"BORDER-TOP: #cccccc 1px solid; BORDER-RIGHT: #cccccc 1px solid; BACKGROUND: #ffffff; BORDER-BOTTOM: #cccccc 1px solid; PADDING-BOTTOM: 10px; PADDING-TOP: 10px; PADDING-LEFT: 25px; BORDER-LEFT: #cccccc 1px solid; MARGIN: 50px; PADDING-RIGHT: 25px\">\n" +
                "<DIV class=header style=\"MARGIN-BOTTOM: 30px\">\n" +
                "<P>"+name+"：</P></DIV>\n" +
                "<P><SPAN style=\"FONT-SIZE: 16px; FONT-WEIGHT: bold; COLOR: #f90\">Your reservation has been cancelled！</SPAN><SPAN style=\"COLOR: #000000\"></P>\n" +
                "<DIV class=footer style=\"MARGIN-TOP: 30px\">\n" +
                "<P><SPAN style=\"BORDER-BOTTOM: #ccc 1px dashed; POSITION: relative; _display: inline-block\" t=\"5\" times=\"\" isout=\"0\">"+date+"</SPAN></P></DIV>\n" +
                "<DIV class=tip style=\"COLOR: #cccccc; TEXT-ALIGN: center\">This email is automatically sent by the system, please do not reply </DIV></DIV></DIV></DIV></DIV></DIV></DIV></DIV></BODY>\n" +
                "</html>\n";

        return content;
    }


    private String makeContent(Map<String,Object> params, String name){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());

        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title></title>\n" +
                "<style>\n" +
                ".qmbox {\n" +
                "\tpadding: 0;\n" +
                "}\n" +
                ".qm_con_body_content {\n" +
                "\theight: auto;\n" +
                "\tmin-height: 100px;\n" +
                "\t_height: 100px;\n" +
                "\tword-wrap: break-word;\n" +
                "\tfont-size: 14px;\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "}\n" +
                ".body {\n" +
                "\tline-height: 170%;\n" +
                "}\n" +
                "BODY {\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "\tfont-size: 12px;\n" +
                "\t-webkit-font-smoothing: subpixel-antialiased;\n" +
                "}\n" +
                "BODY {\n" +
                "\tmargin: 0;\n" +
                "\tpadding: 0;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground-color: #fff;\n" +
                "\tfont-size: 12px;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground: #fff;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground: #fff;\n" +
                "\tcolor: #000;\n" +
                "\tfont-weight: normal;\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "\tpadding: 0 7px 6px 4px;\n" +
                "\tmargin: 0;\n" +
                "}\n" +
                "HTML {\n" +
                "\ttop: 0px;\n" +
                "}\n" +
                ".body P {\n" +
                "\tline-height: 170%;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "\n" +
                "<BODY mu=\"mu\" md=\"md\" module=\"qmReadMail\" context=\"ZC1912-rQ7uXSy7P7HThxdLFCOuY92\"><DIV class=mailcontainer id=qqmail_mailcontainer>\n" +
                "<DIV id=mainmail style=\"MARGIN-BOTTOM: 12px; POSITION: relative; Z-INDEX: 1\">\n" +
                "<DIV class=body id=contentDiv style=\"FONT-SIZE: 14px; HEIGHT: auto; POSITION: relative; ZOOM: 1; PADDING-BOTTOM: 10px; PADDING-TOP: 15px; PADDING-LEFT: 15px; Z-INDEX: 1; LINE-HEIGHT: 1.7; PADDING-RIGHT: 15px\" onmouseover=getTop().stopPropagation(event); onclick=\"getTop().preSwapLink(event, 'html', 'ZC1912-rQ7uXSy7P7HThxdLFCOuY92');\">\n" +
                "<DIV id=qm_con_body>\n" +
                "<DIV class=\"qmbox qm_con_body_content qqmail_webmail_only\" id=mailContentContainer>\n" +
                "<DIV class=main style=\"OVERFLOW: hidden; WIDTH: 100%; BACKGROUND-COLOR: #f7f7f7\">\n" +
                "<DIV class=content style=\"BORDER-TOP: #cccccc 1px solid; BORDER-RIGHT: #cccccc 1px solid; BACKGROUND: #ffffff; BORDER-BOTTOM: #cccccc 1px solid; PADDING-BOTTOM: 10px; PADDING-TOP: 10px; PADDING-LEFT: 25px; BORDER-LEFT: #cccccc 1px solid; MARGIN: 50px; PADDING-RIGHT: 25px\">\n" +
                "<DIV class=header style=\"MARGIN-BOTTOM: 30px\">\n" +
                "<P>"+params.get("name")+"：</P></DIV>\n" +
                "<P>Hi！Your are reserving【"+params.get("title")+"】"+params.get("reserveDate")+", the price is：</P>\n" +
                "<P><SPAN style=\"FONT-SIZE: 16px; FONT-WEIGHT: bold; COLOR: #f90\">"+params.get("amount")+"$</SPAN><SPAN style=\"COLOR: #000000\">(please pay ASAP)</SPAN></P>\n" +
                "<DIV class=footer style=\"MARGIN-TOP: 30px\">\n" +
                "<P>"+name+"</P>\n" +
                "<P><SPAN style=\"BORDER-BOTTOM: #ccc 1px dashed; POSITION: relative; _display: inline-block\" t=\"5\" times=\"\" isout=\"0\">"+date+"</SPAN></P></DIV>\n" +
                "<DIV class=tip style=\"COLOR: #cccccc; TEXT-ALIGN: center\">This email is automatically sent by the system, please do not reply </DIV></DIV></DIV></DIV></DIV></DIV></DIV></DIV></BODY>\n" +
                "</html>\n";

        return content;
    }
    //login content
    private String makeContent(String name,String code){
        //timestamp
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());

        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title></title>\n" +
                "<style>\n" +
                ".qmbox {\n" +
                "\tpadding: 0;\n" +
                "}\n" +
                ".qm_con_body_content {\n" +
                "\theight: auto;\n" +
                "\tmin-height: 100px;\n" +
                "\t_height: 100px;\n" +
                "\tword-wrap: break-word;\n" +
                "\tfont-size: 14px;\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "}\n" +
                ".body {\n" +
                "\tline-height: 170%;\n" +
                "}\n" +
                "BODY {\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "\tfont-size: 12px;\n" +
                "\t-webkit-font-smoothing: subpixel-antialiased;\n" +
                "}\n" +
                "BODY {\n" +
                "\tmargin: 0;\n" +
                "\tpadding: 0;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground-color: #fff;\n" +
                "\tfont-size: 12px;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground: #fff;\n" +
                "}\n" +
                "BODY {\n" +
                "\tbackground: #fff;\n" +
                "\tcolor: #000;\n" +
                "\tfont-weight: normal;\n" +
                "\tfont-family: \"lucida Grande\", Verdana, \"Microsoft YaHei\";\n" +
                "\tpadding: 0 7px 6px 4px;\n" +
                "\tmargin: 0;\n" +
                "}\n" +
                "HTML {\n" +
                "\ttop: 0px;\n" +
                "}\n" +
                ".body P {\n" +
                "\tline-height: 170%;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "\n" +
                "<BODY mu=\"mu\" md=\"md\" module=\"qmReadMail\" context=\"ZC1912-rQ7uXSy7P7HThxdLFCOuY92\"><DIV class=mailcontainer id=qqmail_mailcontainer>\n" +
                "<DIV id=mainmail style=\"MARGIN-BOTTOM: 12px; POSITION: relative; Z-INDEX: 1\">\n" +
                "<DIV class=body id=contentDiv style=\"FONT-SIZE: 14px; HEIGHT: auto; POSITION: relative; ZOOM: 1; PADDING-BOTTOM: 10px; PADDING-TOP: 15px; PADDING-LEFT: 15px; Z-INDEX: 1; LINE-HEIGHT: 1.7; PADDING-RIGHT: 15px\" onmouseover=getTop().stopPropagation(event); onclick=\"getTop().preSwapLink(event, 'html', 'ZC1912-rQ7uXSy7P7HThxdLFCOuY92');\">\n" +
                "<DIV id=qm_con_body>\n" +
                "<DIV class=\"qmbox qm_con_body_content qqmail_webmail_only\" id=mailContentContainer>\n" +
                "<DIV class=main style=\"OVERFLOW: hidden; WIDTH: 100%; BACKGROUND-COLOR: #f7f7f7\">\n" +
                "<DIV class=content style=\"BORDER-TOP: #cccccc 1px solid; BORDER-RIGHT: #cccccc 1px solid; BACKGROUND: #ffffff; BORDER-BOTTOM: #cccccc 1px solid; PADDING-BOTTOM: 10px; PADDING-TOP: 10px; PADDING-LEFT: 25px; BORDER-LEFT: #cccccc 1px solid; MARGIN: 50px; PADDING-RIGHT: 25px\">\n" +
                "<DIV class=header style=\"MARGIN-BOTTOM: 30px\">\n" +
                "<P>Dear user：</P></DIV>\n" +
                "<P>Hi！Your verification code is ：</P>\n" +
                "<P><SPAN style=\"FONT-SIZE: 18px; FONT-WEIGHT: bold; COLOR: #f90\">"+code+"</SPAN><SPAN style=\"COLOR: #000000\">(please input the code within 10 min)</SPAN></P>\n" +
                "<DIV class=footer style=\"MARGIN-TOP: 30px\">\n" +
                "<P>"+name+"</P>\n" +
                "<P><SPAN style=\"BORDER-BOTTOM: #ccc 1px dashed; POSITION: relative; _display: inline-block\" t=\"5\" times=\"\" isout=\"0\">"+date+"</SPAN></P></DIV>\n" +
                "<DIV class=tip style=\"COLOR: #cccccc; TEXT-ALIGN: center\">This email is automatically sent by the system, please do not reply </DIV></DIV></DIV></DIV></DIV></DIV></DIV></DIV></BODY>\n" +
                "</html>\n";

        return content;
    }

    //timestamp to time
    private String formatDate(Long time){
        if (time!=null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(new Date(time));
        }
        return "";
    }

}