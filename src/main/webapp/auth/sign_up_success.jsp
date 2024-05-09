<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="../css/sign.css" rel="stylesheet">
    <link rel="stylesheet" as="style" crossorigin href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.9/dist/web/static/pretendard.min.css" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script type="module" src="../js/sign.js"></script>
    <title>Document</title>
    <style>
        #message {
            margin: 20px 0;
        }
        #regbtn{
            box-sizing: border-box;
            padding: 5px 10px;
        }
    </style>
</head>

<body>
    <div id="container">
        <h1>Sign-up successful!</h1>
        
        <div id="answer">
            <p id="message">ȯ���մϴ�, <b>${nicname}</b>��! ������ ���������� �̷�������ϴ�. ���� �����غ�����!</p>
            <a id="regbtn" href="sign-in">�α���</a>
        </div>
        
    </div>
</body>

</html>