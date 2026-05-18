<?php
session_start();
require_once "pdo.php";
require_once "util.php";

$stmt = $pdo->query('SELECT profile_id, user_id, first_name, last_name, headline FROM Profile');
$profiles = $stmt->fetchAll(PDO::FETCH_ASSOC);
?>
<!DOCTYPE html>
<html>
<head>
<title>Aryan's Resume Registry a8ac02c1</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
</head>
<body>
<div class="container">
<h1>Aryan's Resume Registry a8ac02c1</h1>
<?php flashMessages(); ?>

<?php if (isset($_SESSION['user_id'])): ?>
    <p><a href="logout.php">Logout</a></p>
<?php else: ?>
    <p><a href="login.php">Please log in</a></p>
<?php endif; ?>

<?php if (count($profiles) > 0): ?>
<table class="table">
    <thead>
        <tr>
            <th>Name</th>
            <th>Headline</th>
            <?php if (isset($_SESSION['user_id'])): ?><th>Action</th><?php endif; ?>
        </tr>
    </thead>
    <tbody>
    <?php foreach ($profiles as $row): ?>
        <tr>
            <td><a href="view.php?profile_id=<?= $row['profile_id'] ?>"><?= htmlentities($row['first_name'] . ' ' . $row['last_name']) ?></a></td>
            <td><?= htmlentities($row['headline']) ?></td>
            <?php if (isset($_SESSION['user_id'])): ?>
                <td>
                    <?php if ($_SESSION['user_id'] == $row['user_id']): ?>
                        <a href="edit.php?profile_id=<?= $row['profile_id'] ?>">Edit</a>
                        <a href="delete.php?profile_id=<?= $row['profile_id'] ?>">Delete</a>
                    <?php endif; ?>
                </td>
            <?php endif; ?>
        </tr>
    <?php endforeach; ?>
    </tbody>
</table>
<?php else: ?>
    <p>No rows found</p>
<?php endif; ?>

<?php if (isset($_SESSION['user_id'])): ?>
    <p><a href="add.php">Add New Entry</a></p>
<?php endif; ?>
</div>
</body>
</html>
