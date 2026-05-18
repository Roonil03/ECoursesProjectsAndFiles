<?php
session_start();
require_once "pdo.php";
require_once "util.php";

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

$positions = loadPos($pdo, $_GET['profile_id']);
$educations = loadEdu($pdo, $_GET['profile_id']);
?>
<!DOCTYPE html>
<html>
<head>
<title>Aryan's Resume Registry a8ac02c1</title>
<?php require_once "head.php"; ?>
</head>
<body>
<div class="container">
<h1>Profile information</h1>
<p>First Name: <?= htmlentities($profile['first_name']) ?></p>
<p>Last Name: <?= htmlentities($profile['last_name']) ?></p>
<p>Email: <?= htmlentities($profile['email']) ?></p>
<p>Headline:<br/><?= htmlentities($profile['headline']) ?></p>
<p>Summary:<br/><?= htmlentities($profile['summary']) ?></p>

<?php if (count($educations) > 0): ?>
    <p>Education</p>
    <ul>
    <?php foreach ($educations as $edu): ?>
        <li><?= htmlentities($edu['year']) ?>: <?= htmlentities($edu['name']) ?></li>
    <?php endforeach; ?>
    </ul>
<?php endif; ?>

<?php if (count($positions) > 0): ?>
    <p>Position</p>
    <ul>
    <?php foreach ($positions as $pos): ?>
        <li><?= htmlentities($pos['year']) ?>: <?= htmlentities($pos['description']) ?></li>
    <?php endforeach; ?>
    </ul>
<?php endif; ?>

<p><a href="index.php">Done</a></p>
</div>
</body>
</html>
