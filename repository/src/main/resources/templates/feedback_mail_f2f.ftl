<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Notifications - Feedback Activities from the last 2 weeks</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

    <!-- use the font -->
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            font-size: 48px;
        }
    </style>
</head>
<body style="margin: 0; padding: 0;">

<table align="center" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse;">
    <tr>
        <td align="center" bgcolor="#78ab46" style="padding: 40px 0 30px 0;">
            <#--<img src="F:/SuperSede/monitor_feedback/repository/src/main/resources/icon_notification_red.png" alt="notification icon" style="display: block;" />-->
            <img src="cid:logo.png" alt="notification icon" style="display: block;" />
        </td>
    </tr>
    <tr>
        <td bgcolor="#eaeaea" style="padding: 40px 30px 40px 30px;">
            <p>Dear ${enduser.username},</p>
            <p>Below you can get an overview of your feedbacks and the feedbacks
                from the forum</p>
            <p>Enjoy exploring</p>
        </td>
    </tr>
    <tr>
        <td bgcolor="#eacb60" style="padding: 40px 30px 40px 30px;">
            <p>Your Feedbacks</p>
            <#list user_feedbacks as user_feedback>
                <p><b>User Feedback Title: </b>${user_feedback.title}</p>
            </#list>
        </td>
    </tr>
    <tr>
        <td bgcolor="#ea8f2e" style="padding: 40px 30px 40px 30px;">
            <p>Feedbacks Forum</p>
            <#list forum_feedbacks as forum_feedback>
                <p><b>Forum Feedback Title: </b>${forum_feedback.title}</p>
            </#list>
        </td>
    </tr>
    <tr>
        <td bgcolor="#777777" style="padding: 30px 30px 30px 30px;">
            <p>Best Regards,</p>
            <p>Feedback-To-Feebdack Central</p>
        </td>
    </tr>
</table>
<p>
    ----------------------------------------------------------------------------<br/>
    This is an automatically generated message. Do not reply.
</p>

</body>
</html>