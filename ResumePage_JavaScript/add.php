<?php
session_start();
require_once "pdo.php";

if (!isset($_SESSION['user_id'])) {
    die("Not logged in");
}

if (isset($_POST['cancel'])) {
    header("Location: index.php");
    return;
}

if (isset($_POST['first_name']) && isset($_POST['last_name']) && isset($_POST['email']) && isset($_POST['headline']) && isset($_POST['summary'])) {
    if (strlen($_POST['first_name']) < 1 || strlen($_POST['last_name']) < 1 || strlen($_POST['email']) < 1 || strlen($_POST['headline']) < 1 || strlen($_POST['summary']) < 1) {
        $_SESSION['error'] = "All fields are required";
        header("Location: add.php");
        return;
    }

    if (strpos($_POST['email'], '@') === false) {
        $_SESSION['error'] = "Email address must contain @";
        header("Location: add.php");
        return;
    }

    $url = isset($_POST['url']) ? $_POST['url'] : '';
    if (strlen($url) > 0) {
        if (!preg_match('#^https?://#i', $url)) {
             $_SESSION['error'] = "URL must start with http:// or https://";
             header("Location: add.php");
             return;
        }
        
        $ch = curl_init($url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_NOBODY, true);
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
        curl_setopt($ch, CURLOPT_TIMEOUT, 5);
        curl_exec($ch);
        $httpcode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        curl_close($ch);
        
        if ($httpcode < 200 || $httpcode >= 400) {
            $_SESSION['error'] = "URL does not exist or is unreachable";
            header("Location: add.php");
            return;
        }
    }

    $stmt = $pdo->prepare('INSERT INTO Profile (user_id, first_name, last_name, email, headline, summary, url) VALUES (:uid, :fn, :ln, :em, :he, :su, :url)');
    $stmt->execute(array(
        ':uid' => $_SESSION['user_id'],
        ':fn' => $_POST['first_name'],
        ':ln' => $_POST['last_name'],
        ':em' => $_POST['email'],
        ':he' => $_POST['headline'],
        ':su' => $_POST['summary'],
        ':url' => $url
    ));
    $_SESSION['success'] = "Profile added";
    header("Location: index.php");
    return;
}
?>
<!DOCTYPE html>
<html>
<head>
<title>Aryan's Resume Registry a8ac02c1</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
</head>
<body>
<div class="container">
<h1>Adding Profile for <?= htmlentities($_SESSION['name']) ?></h1>
<?php
if (isset($_SESSION['error'])) {
    echo '<p style="color:red">'.htmlentities($_SESSION['error'])."</p>\n";
    unset($_SESSION['error']);
}
?>
<form method="post">
<div class="form-group">
<label>First Name:</label>
<input type="text" name="first_name" class="form-control">
</div>
<div class="form-group">
<label>Last Name:</label>
<input type="text" name="last_name" class="form-control">
</div>
<div class="form-group">
<label>Email:</label>
<input type="text" name="email" class="form-control">
</div>
<div class="form-group">
<label>Headline:</label>
<input type="text" name="headline" class="form-control">
</div>
<div class="form-group">
<label>Summary:</label>
<textarea name="summary" rows="8" class="form-control"></textarea>
</div>
<div class="form-group">
<label>Image URL (Optional):</label>
<input type="text" name="url" class="form-control">
</div>
<p>
<input type="submit" class="btn btn-primary" value="Add">
<input type="submit" name="cancel" class="btn btn-default" value="Cancel">
</p>
</form>
</div>
</body>
</html>
