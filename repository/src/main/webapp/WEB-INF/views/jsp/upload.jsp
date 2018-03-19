<%--
  Created by IntelliJ IDEA.
  User: Aydinli
  Date: 29.01.2018
  Time: 10:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<body>
<h1>Spring MVC file upload example</h1>

<form method="POST" action="${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks//upload" enctype="multipart/form-data">
    <input type="file" name="file" /><br/>
    <input type="submit" value="Submit" />
</form>

</body>
</html>
