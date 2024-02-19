<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>GPX editor</title>
    <style>
        html {
          display: flex;
          flex-direction: column;
          align-items: center;
        }
    </style>
</head>
<body>
<h1>Данный сайт предоставляет возможность редактировать GPX файлы!</h1>
<form method="POST" enctype="multipart/form-data" action="/uploadFile">
<h3>Чтобы начать работу с сайтом, загрузите GPX файл:</h3>
    <input type="file" name="file"><br />
    <input type="submit" value="Загрузить">
</form>
</body>
</html>