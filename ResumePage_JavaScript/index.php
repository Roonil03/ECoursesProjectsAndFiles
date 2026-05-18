<?php
session_start();
require_once "pdo.php";

$search = isset($_GET['search']) ? $_GET['search'] : '';
$page = isset($_GET['page']) ? (int)$_GET['page'] : 1;
if ($page < 1) $page = 1;
$limit = 10;
$offset = ($page - 1) * $limit;

if ($search !== '') {
    $count_stmt = $pdo->prepare('SELECT COUNT(*) FROM Profile WHERE first_name LIKE :s OR last_name LIKE :s OR email LIKE :s OR headline LIKE :s');
    $count_stmt->execute(array(':s' => '%'.$search.'%'));
    $total_rows = $count_stmt->fetchColumn();

    $stmt = $pdo->prepare('SELECT profile_id, user_id, first_name, last_name, headline, url FROM Profile WHERE first_name LIKE :s OR last_name LIKE :s OR email LIKE :s OR headline LIKE :s LIMIT :limit OFFSET :offset');
    $stmt->bindValue(':s', '%'.$search.'%');
    $stmt->bindValue(':limit', $limit, PDO::PARAM_INT);
    $stmt->bindValue(':offset', $offset, PDO::PARAM_INT);
    $stmt->execute();
} else {
    $count_stmt = $pdo->query('SELECT COUNT(*) FROM Profile');
    $total_rows = $count_stmt->fetchColumn();

    $stmt = $pdo->prepare('SELECT profile_id, user_id, first_name, last_name, headline, url FROM Profile LIMIT :limit OFFSET :offset');
    $stmt->bindValue(':limit', $limit, PDO::PARAM_INT);
    $stmt->bindValue(':offset', $offset, PDO::PARAM_INT);
    $stmt->execute();
}
$profiles = $stmt->fetchAll(PDO::FETCH_ASSOC);
$total_pages = ceil($total_rows / $limit);
?>
<!DOCTYPE html>
<html>
<head>
<title>Aryan's Resume Registry a8ac02c1</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<style>
.profile-img { width: 50px; height: 50px; object-fit: cover; border-radius: 50%; }
</style>
</head>
<body>
<div class="container">
<h1>Aryan's Resume Registry</h1>
<?php
if (isset($_SESSION['error'])) {
    echo '<p style="color:red">'.htmlentities($_SESSION['error'])."</p>\n";
    unset($_SESSION['error']);
}
if (isset($_SESSION['success'])) {
    echo '<p style="color:green">'.htmlentities($_SESSION['success'])."</p>\n";
    unset($_SESSION['success']);
}
?>

<form method="GET" action="index.php" class="form-inline" style="margin-bottom: 20px;">
    <div class="form-group">
        <input type="text" name="search" class="form-control" placeholder="Search profiles" value="<?= htmlentities($search) ?>">
    </div>
    <button type="submit" class="btn btn-default">Search</button>
    <a href="index.php" class="btn btn-default">Clear</a>
</form>

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
            <th>Image</th>
            <?php if (isset($_SESSION['user_id'])): ?>
                <th>Action</th>
            <?php endif; ?>
        </tr>
    </thead>
    <tbody>
    <?php foreach ($profiles as $row): ?>
        <tr>
            <td><a href="view.php?profile_id=<?= $row['profile_id'] ?>"><?= htmlentities($row['first_name'] . ' ' . $row['last_name']) ?></a></td>
            <td><?= htmlentities($row['headline']) ?></td>
            <td>
                <?php if (!empty($row['url'])): ?>
                    <img src="<?= htmlentities($row['url']) ?>" class="profile-img" alt="Profile Image">
                <?php endif; ?>
            </td>
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

<?php if ($total_pages > 1): ?>
    <nav>
        <ul class="pagination">
            <?php if ($page > 1): ?>
                <li><a href="index.php?page=<?= $page - 1 ?>&search=<?= urlencode($search) ?>">Back</a></li>
            <?php endif; ?>
            <?php if ($page < $total_pages): ?>
                <li><a href="index.php?page=<?= $page + 1 ?>&search=<?= urlencode($search) ?>">Next</a></li>
            <?php endif; ?>
        </ul>
    </nav>
<?php endif; ?>

<?php else: ?>
    <p>No rows found</p>
<?php endif; ?>

<?php if (isset($_SESSION['user_id'])): ?>
    <p><a href="add.php">Add New Entry</a></p>
<?php endif; ?>

</div>
</body>
</html>
