<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>Upload files form</title>
</head>
<body>
    <div>
        <c:forEach items="${details.errors}" var="error">
	        <span class="error">${error}</span>
	    </c:forEach>
    </div>
    <div>
    	<p>Word counts:</p>
	    <c:forEach items="${details.wordMap}" var="word">
	        <span>${word.key} is mentioned ${word.value} time(s)</span><br>
	    </c:forEach>
    </div>
</body>
</html>