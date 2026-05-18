<?php
session_start();
require_once "pdo.php";
require_once "util.php";

if (!isset($_SESSION['user_id'])) {
    die("ACCESS DENIED");
}

if (isset($_POST['cancel'])) {
    header('Location: index.php');
    return;
}

if (isset($_POST['delete']) && isset($_POST['profile_id'])) {
    $sql = "DELETE FROM Profile WHERE profile_id = :zip AND user_id = :uid";
    $stmt = $pdo->prepare($sql);
    $stmt->execute(array(':zip' => $_POST['profile_id'], ':uid' => $_SESSION['user_id']));
    $_SESSION['success'] = 'Profile deleted';
    header('Location: index.php');
    return;
}

if (!isset($_GET['profile_id'])) {
    $_SESSION['error'] = "Missing profile_id";
    header('Location: index.php');
    return;
}

$stmt = $pdo->prepare("SELECT first_name, last_name, profile_id FROM Profile WHERE profile_id = :xyz AND user_id = :uid");
$stmt->execute(array(":xyz" => $_GET['profile_id'], ":uid" => $_SESSION['user_id']));
$row = $stmt->fetch(PDO::FETCH_ASSOC);

if ($row === false) {
    $_SESSION['error'] = 'Bad value for profile_id';
    header('Location: index.php');
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
<h1>Deleting Profile</h1>
<form method="post" action="delete.php">
<p>First Name: <?= htmlentities($row['first_name']) ?></p>
<p>Last Name: <?= htmlentities($row['last_name']) ?></p>
<input type="hidden" name="profile_id" value="<?= htmlentities($row['profile_id']) ?>">
<p>
<input type="submit" class="btn btn-danger" name="delete" value="Delete">
<input type="submit" class="btn btn-default" name="cancel" value="Cancel">
</p>
</form>
</div>
</body>
</html>
