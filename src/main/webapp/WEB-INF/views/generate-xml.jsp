<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Download Table XML</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script>
        function populateDropdown() {
            $.ajax({
                url: '/spring-web-0.0.1/table-list',
                type: 'GET',
                success: function(data) {
                    var dropdown = $('#myDropdown');
                    dropdown.empty();
                    data.forEach(function(value) {
                        dropdown.append($('<option>', { value: value, text: value }));
                    });
                },
                error: function() {
                    console.error('Error fetching data from the API');
                }
            });
        }

        function submitForm() {
            var selectedValue = $('#myDropdown').val();
            // $.ajax({
            //     url: '/spring-web-0.0.1/download-xml?table=' + selectedValue,
            //     type: 'GET'
            // });
            var form = $('<form>', {
                'action': '/spring-web-0.0.1/download-xml',
                'method': 'post'
            });
            form.append($('<input>', {
                'type': 'hidden',
                'name': 'selectedValue',
                'value': selectedValue
            }));
            form.appendTo('body').submit().remove();
        }

        $(document).ready(function() {
            populateDropdown();
        });
    </script>
</head>
<body>
<h2>Download Table XML</h2>
<form>
    <label for="myDropdown">Select table:</label>
    <select id="myDropdown"></select>
    <button type="button" onclick="submitForm()">Submit</button>
</form>
</body>
</html>
