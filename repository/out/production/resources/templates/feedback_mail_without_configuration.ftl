<html>
<body>
Hello,<br/>
A new feedback was sent for application ${feedback.applicationId}.

Unfortunately the orchestrator was not available. So, the configuration of the mechanisms could not be fetched.

<p>User ID: ${feedback.userIdentification}</p>

<p>
    <b>Text Feedback</b><br/>
<#if feedback.textFeedbacks??>
    <#list feedback.textFeedbacks as textFeedback>
        ${textFeedback.mechanismId}:<br />
        ${textFeedback.text}
    </#list>
<#else>
    No text feedbacks
</#if>
</p>

<p>
    <b>Ratings</b><br/>
<#if feedback.ratingFeedbacks??>

    <#list feedback.ratingFeedbacks as ratingFeedback>
        ${ratingFeedback.mechanismId}: ${ratingFeedback.rating}
    </#list>
<#else>
    No rating feedbacks
</#if>
</p>

<p>
    <b>Categories</b><br/>
<#if feedback.categoryFeedbacks??>

    <#list feedback.categoryFeedbacks as categoryFeedback>
        ${categoryFeedback.mechanismId}:

        <#if categoryFeedback.parameterId??>
            ${categoryFeedback.parameterId}
        <#else>
            ${categoryFeedback.text}
        </#if>
        <br />
    </#list>
<#else>
    No category feedbacks
</#if>
</p>

<p>
    <b>Attachments</b><br/>
<#if feedback.attachmentFeedbacks??>

    <#list feedback.attachmentFeedbacks as attachmentFeedback>
        Attachment: ${attachmentFeedback.path}
    </#list>
<#else>
    No attachment feedbacks
</#if>
</p>

<p>
    <b>Screenshots</b><br/>
<#if feedback.screenshotFeedbacks??>

    <#list feedback.screenshotFeedbacks as screenshotFeedback>
        Screenshot: ${screenshotFeedback.path}
    </#list>
<#else>
    No screenshot feedbacks
</#if>
</p>

<p>
    <b>Audios</b><br/>
<#if feedback.audioFeedbacks??>
    <#list feedback.audioFeedbacks as audioFeedback>
        Audio: ${audioFeedback.path}
    </#list>
<#else>
    No audio feedbacks
</#if>
</p>

<p>
    <b>Context Information</b><br />
    <#if feedback.contextInformation??>
        Resolution: ${(feedback.contextInformation.resolution)!} <br />
        User agent: ${(feedback.contextInformation.userAgent)!} <br />
        <#if feedback.contextInformation.androidVersion??>
            Android version: ${(feedback.contextInformation.androidVersion)!} <br />
        </#if>
        Local time: ${(feedback.contextInformation.localTime)!} <br />
        Time zone: ${(feedback.contextInformation.timeZone)!} <br />
        Device pixel ratio: ${(feedback.contextInformation.devicePixelRatio)!} <br />
        URL: ${(feedback.contextInformation.url)!} <br />
    </#if>
</p>

<p>
    Best regards,<br/>
    Feedback Gathering
</p>
<p>
    ----------------------------------------------------------------------------<br/>
    This is an automatically generated message. Do not reply.
</p>
</body>
</html>