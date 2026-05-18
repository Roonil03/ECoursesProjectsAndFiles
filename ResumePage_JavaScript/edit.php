<?php
session_start();
require_once "pdo.php";

if (!isset($_SESSION['user_id'])) {
    die("Not logged in");
}

if (!isset($_GET['profile_id']) && !isset($_POST['profile_id'])) {
    $_SESSION['error'] = "Missing profile_id";
    header('Location: index.php');
    return;
}

$profile_id = isset($_GET['profile_id']) ? $_GET['profile_id'] : $_POST['profile_id'];

$stmt = $pdo->prepare("SELECT * FROM Profile WHERE profile_id = :prof AND user_id = :uid");
$stmt->execute(array(":prof" => $profile_id, ":uid" => $_SESSION['user_id']));
$profile = $stmt->fetch(PDO::FETCH_ASSOC);

if ($profile === false) {
    $_SESSION['error'] = "Could not load profile";
    header('Location: index.php');
    return;
}

if (isset($_POST['cancel'])) {
    header("Location: index.php");
    return;
}

if (isset($_POST['first_name']) && isset($_POST['last_name']) && isset($_POST['email']) && isset($_POST['headline']) && isset($_POST['summary'])) {
    if (strlen($_POST['first_name']) < 1 || strlen($_POST['last_name']) < 1 || strlen($_POST['email']) < 1 || strlen($_POST['headline']) < 1 || strlen($_POST['summary']) < 1) {
        $_SESSION['error'] = "All fields are required";
        header("Location: edit.php?profile_id=" . $_POST['profile_id']);
        return;
    }

    if (strpos($_POST['email'], '@') === false) {
        $_SESSION['error'] = "Email address must contain @";
        header("Location: edit.php?profile_id=" . $_POST['profile_id']);
        return;
    }

    $url = isset($_POST['url']) ? $_POST['url'] : '';
    if (strlen($url) > 0) {
        if (!preg_match('#^https?://#i', $url)) {
             $_SESSION['error'] = "URL must start with http:// or https://";
             header("Location: edit.php?profile_id=" . $_POST['profile_id']);
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
            header("Location: edit.php?profile_id=" . $_POST['profile_id']);
            return;
        }
    }

    $sql = "UPDATE Profile SET first_name = :fn, last_name = :ln, email = :em, headline = :he, summary = :su, url = :url
            WHERE profile_id = :pid AND user_id = :uid";
    $stmt = $pdo->prepare($sql);
    $stmt->execute(array(
        ':fn' => $_POST['first_name'],
        ':ln' => $_POST['last_name'],
        ':em' => $_POST['email'],
        ':he' => $_POST['headline'],
        ':su' => $_POST['summary'],
        ':url' => $url,
        ':pid' => $_POST['profile_id'],
        ':uid' => $_SESSION['user_id']
    ));
    $_SESSION['success'] = "Profile updated";
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
<h1>Editing Profile for <?= htmlentities($_SESSION['name']) ?></h1>
<?php
if (isset($_SESSION['error'])) {
    echo '<p style="color:red">'.htmlentities($_SESSION['error'])."</p>\n";
    unset($_SESSION['error']);
}
?>
<form method="post" action="edit.php">
<input type="hidden" name="profile_id" value="<?= htmlentities($profile['profile_id']) ?>">
<div class="form-group">
<label>First Name:</label>
<input type="text" name="first_name" class="form-control" value="<?= htmlentities($profile['first_name']) ?>">
</div>
<div class="form-group">
<label>Last Name:</label>
<input type="text" name="last_name" class="form-control" value="<?= htmlentities($profile['last_name']) ?>">
</div>
<div class="form-group">
<label>Email:</label>
<input type="text" name="email" class="form-control" value="<?= htmlentities($profile['email']) ?>">
</div>
<div class="form-group">
<label>Headline:</label>
<input type="text" name="headline" class="form-control" value="<?= htmlentities($profile['headline']) ?>">
</div>
<div class="form-group">
<label>Summary:</label>
<textarea name="summary" rows="8" class="form-control"><?= htmlentities($profile['summary']) ?></textarea>
</div>
<div class="form-group">
<label>Image URL (Optional):</label>
<input type="text" name="url" class="form-control" value="<?= htmlentities($profile['url']) ?>">
</div>
<p>
<input type="submit" class="btn btn-primary" value="Save">
<input type="submit" name="cancel" class="btn btn-default" value="Cancel">
</p>
</form>
</div>
</body>
</html>
