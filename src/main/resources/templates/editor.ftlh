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
<h2>Редактируй свой GPX файл здесь!</h2>
<form method="POST" action="/editGPX">
    <h3>Выберите, куда вставить extensions:</h3>
    <ul>
        <li>1 - в тег gpx</li>
        <li>2 - в metadata</li>
        <li>3 - в waypoint</li>
        <li>4 - в route</li>
        <li>5 - в route point</li>
        <li>6 - в track</li>
        <li>7 - в track segment</li>
        <li>8 - в track segment point</li>
    </ul>
    <label for="tag">Введите число: </label>
    <input type="number" name="tag" id="tag"><br />
    <h4>Два параметра ниже нужно указывать, только если вы попадаете под описанные условия</h4>
    <label for="tagLevel1">Если вы хотите добавить extensions <b>не</b> в теги <i>gpx</i> или <i>metadata</i>, то<br />введите порядковый номер выбранного элемента относительно его родительского тега: </label>
    <input type="number" name="tagLevel1" id="tagLevel1"><br /><br />
    <label for="tagLevel2">Если вы хотите добавить extensions <b>в</b> <i>route point</i>, <i>track segment</i> или <i>track segment point</i>, то <br />введите также порядковый номер выбранного элемента относительно его родительского тега: </label>
    <input type="number" name="tagLevel2" id="tagLevel2"><br /><br />
    <label for="trk_seg_point">Для <i>track segment point</i> также укажите его порядковый номер в родительском track segment: </label>
    <input type="number" name="trk_seg_point" id="trk_seg_point"><br />
    <h3>Введите extensions</h3>
    <input type="submit" value="Добавить extensions">
</form>
</body>
</html>