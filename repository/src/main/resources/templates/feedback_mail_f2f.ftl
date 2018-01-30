<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Notifications - Feedback Activities from the last 2 weeks</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

    <!-- use the font -->
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            font-size: 48px;
        }

        a.button:link, a.button:visited {
            background-color: #f4c321;
            color: white;
            padding: 14px 25px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
        }


        a.button:hover, a.button:active {
            background-color: red;
        }

    </style>
</head>
<body style="margin: 0; padding: 0;">

<table align="center" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse;">
    <tr>
        <td align="center" bgcolor="#99d22b" style="padding: 40px 0 30px 0;">
            <#--<img src="F:/SuperSede/monitor_feedback/repository/src/main/resources/icon_notification_red.png" alt="notification icon" style="display: block;" />-->
            <#--<img src="/icon_notification_red.png" width="100" height="100" alt="notification icon" style="display: block;" />-->
                <div class="alert alert-info">
                    <strong>F2F Central - Notifications!</strong>
                </div>
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
            <h2><b>Your Feedbacks</b></h2>
            <ul>
                <#list user_feedbacks as user_feedback>
                    <li>Feedback Title: ${user_feedback.title}
                        <ul>
                            <#if user_feedback.textFeedbacks??>
                                <#list user_feedback.textFeedbacks as textFeedback>
                                    <li><i>Feedback Text: </i>${textFeedback.text}</li>
                                </#list>
                            <#else>
                                <li>No text feedbacks</li>
                            </#if>
                        </ul>
                    </li>
                </#list>
            </ul>
        </td>
    </tr>
    <tr>
        <td bgcolor="#ea8f2e" style="padding: 40px 30px 40px 30px;">
            <h2><b>Community Feedbacks from the Forum</b></h2>
            <ul>
            <#list forum_feedbacks as forum_feedback>
                <li>Feedback Title: ${forum_feedback.title}
                    <ul>
                        <#if forum_feedback.textFeedbacks??>
                            <#list forum_feedback.textFeedbacks as textFeedback>
                                <li><i>Feedback Text: </i>${textFeedback.text}</li>
                            </#list>
                        <#else>
                            <li>No text feedbacks</li>
                        </#if>
                    </ul>
                </li>
            </#list>
            </ul>
        </td>
    </tr>
    <tr>
        <td bgcolor="#b6b6b6" style="padding: 30px 30px 30px 30px;">
            <p>Best Regards,</p>
            <p>Feedback-To-Feebdack Central</p>
        </td>
    </tr>
    <tr>
        <td>
            <p>You can explore more by clicking the button below: </p> <br/>
            <a class="button" href="http://f2f.ronnieschaniel.com/">Visit F2F Central</a>
        </td>
    </tr>
</table>
<p>
    ----------------------------------------------------------------------------<br/>
    This is an automatically generated message. Do not reply.
</p>

</body>
</html>