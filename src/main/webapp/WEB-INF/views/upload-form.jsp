<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>File Upload Form</title>
    <script>
        function validateFile() {
            var fileInput = document.getElementById('file');
            if (fileInput.files.length > 0) {
                var fileName = fileInput.files[0].name;
                var fileSize = fileInput.files[0].size; // bytes
                var maxSize = 100 * 1024 * 1024; // 100 MB
                var allowedExtensions = /(\.xml)$/i;

                if (!allowedExtensions.exec(fileName)) {
                    alert('Invalid file extension. Please choose a file with a .xml extension.');
                    fileInput.value = '';
                    return false;
                }

                if (fileSize > maxSize) {
                    alert('File size exceeds 100 MB. Please choose a smaller file.');
                    fileInput.value = '';
                    return false;
                }
            }
            return true;
        }
    </script>
</head>
<body>
<h2>File Upload Form</h2>
<form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data" onsubmit="return validateFile();">
    Select an xml file to upload:
    <input type="file" name="file" id="file">
    <br>
    <input type="submit" value="Upload">
</form>
</body>
</html>
