<?php
session_start();
require_once "pdo.php";

if (!isset($_GET['profile_id'])) {
    $_SESSION['error'] = "Missing profile_id";
    header('Location: index.php');
    return;
}

$stmt = $pdo->prepare("SELECT * FROM Profile WHERE profile_id = :xyz");
$stmt->execute(array(":xyz" => $_GET['profile_id']));
$profile = $stmt->fetch(PDO::FETCH_ASSOC);

if ($profile === false) {
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
<style>
.profile-img-large { max-width: 300px; max-height: 300px; border: 2px solid #ccc; border-radius: 10px; margin-bottom: 20px;}
</style>
</head>
<body>
<div class="container">
<h1>Profile information</h1>
<div class="panel panel-default">
  <div class="panel-body">
    <p><strong>First Name:</strong> <?= htmlentities($profile['first_name']) ?></p>
    <p><strong>Last Name:</strong> <?= htmlentities($profile['last_name']) ?></p>
    <p><strong>Email:</strong> <?= htmlentities($profile['email']) ?></p>
    <p><strong>Headline:</strong><br/>
    <?= htmlentities($profile['headline']) ?></p>
    <p><strong>Summary:</strong><br/>
    <?= htmlentities($profile['summary']) ?></p>

    <?php if (!empty($profile['url'])): ?>
        <p><strong>Image:</strong></p>
        <img src="<?= htmlentities($profile['url']) ?>" class="profile-img-large" alt="Profile Image">
    <?php endif; ?>
  </div>
</div>
<p>
<a href="index.php" class="btn btn-primary">Done</a>
</p>
</div>
</body>
</html>
