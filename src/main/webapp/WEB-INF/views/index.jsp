<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload files form</title>
</head>
<body>
    <div>
        <form action="upload-files" method="post" enctype="multipart/form-data">
            <fieldset>
                <legend>Upload files</legend>
                <p>
                    <label for="files">Select files (hold 'ctrl' to select multiple) </label><br>
                    <input type="file" name="files" multiple="multiple"/><br>
                </p>
                <p id="buttons">
                    <input id="submit" type="submit" value="Upload">
                </p>
            </fieldset>
        </form>
    </div>
</body>
</html>