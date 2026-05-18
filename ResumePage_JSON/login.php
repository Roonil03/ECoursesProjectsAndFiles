<?php
session_start();
require_once "pdo.php";
require_once "util.php";

$salt = 'XyZzy12*_';

if (isset($_POST['cancel'])) {
    header("Location: index.php");
    return;
}

if (isset($_POST['email']) && isset($_POST['pass'])) {
    if (strlen($_POST['email']) < 1 || strlen($_POST['pass']) < 1) {
        $_SESSION['error'] = "User name and password are required";
        header("Location: login.php");
        return;
    }
    
    $check = hash('md5', $salt.$_POST['pass']);
    $stmt = $pdo->prepare('SELECT user_id, name FROM users WHERE email = :em AND password = :pw');
    $stmt->execute(array(':em' => $_POST['email'], ':pw' => $check));
    $row = $stmt->fetch(PDO::FETCH_ASSOC);

    if ($row !== false) {
        $_SESSION['name'] = $row['name'];
        $_SESSION['user_id'] = $row['user_id'];
        header("Location: index.php");
        return;
    } else {
        $_SESSION['error'] = "Incorrect password";
        header("Location: login.php");
        return;
    }
}
?>
<!DOCTYPE html>
<html>
<head>
<title>Aryan's Resume Registry a8ac02c1</title>
<?php require_once "head.php"; ?>
<script>
function doValidate() {
    console.log('Validating...');
    try {
        let em = document.getElementById('email').value;
        let pw = document.getElementById('id_1723').value;
        console.log("Validating email=" + em + " pw=" + pw);
        if (em == null || em == "" || pw == null || pw == "") {
            alert("Both fields must be filled out");
            return false;
        }
        if (em.indexOf('@') == -1) {
            alert("Email address must contain @");
            return false;
        }
        return true;
    } catch(e) {
        return false;
    }
    return false;
}
</script>
</head>
<body>
<div class="container">
<h1>Please Log In</h1>
<?php flashMessages(); ?>
<form method="POST" action="login.php">
<div class="form-group">
<label for="email">Email</label>
<input type="text" name="email" id="email" class="form-control">
</div>
<div class="form-group">
<label for="id_1723">Password</label>
<input type="password" name="pass" id="id_1723" class="form-control">
</div>
<input type="submit" class="btn btn-primary" onclick="return doValidate();" value="Log In">
<input type="submit" name="cancel" class="btn btn-default" value="Cancel">
</form>
</div>
</body>
</html>
