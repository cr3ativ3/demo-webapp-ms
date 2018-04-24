<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<title>Upload files form</title>
</head>
<body>
    <div>
        <form:form action="upload-files" method="post" enctype="multipart/form-data">
            <fieldset>
                <legend>Upload files</legend>
                <p>
                    <label for="files">Select files (hold 'ctrl' to select multiple) </label><br>
                    <input type="file" name="files" multiple="multiple"/>
                </p>
                <p id="buttons">
                    <input id="submit" type="submit" tabindex="5" value="Upload">
                </p>
            </fieldset>
        </form:form>
    </div>
</body>
</html>