<?php
session_start();
require_once "pdo.php";
require_once "util.php";

if (!isset($_SESSION['user_id'])) {
    die("ACCESS DENIED");
}

if (!isset($_GET['profile_id']) && !isset($_POST['profile_id'])) {
    $_SESSION['error'] = "Missing profile_id";
    header('Location: index.php');
    return;
}

if (isset($_POST['cancel'])) {
    header("Location: index.php");
    return;
}

$profile_id = isset($_POST['profile_id']) ? $_POST['profile_id'] : $_GET['profile_id'];

if (isset($_POST['first_name']) && isset($_POST['last_name']) && isset($_POST['email']) && isset($_POST['headline']) && isset($_POST['summary'])) {
    $msg = validateProfile();
    if (is_string($msg)) {
        $_SESSION['error'] = $msg;
        header("Location: edit.php?profile_id=" . $_POST['profile_id']);
        return;
    }

    $msg = validatePos();
    if (is_string($msg)) {
        $_SESSION['error'] = $msg;
        header("Location: edit.php?profile_id=" . $_POST['profile_id']);
        return;
    }

    $msg = validateEdu();
    if (is_string($msg)) {
        $_SESSION['error'] = $msg;
        header("Location: edit.php?profile_id=" . $_POST['profile_id']);
        return;
    }

    $sql = "UPDATE Profile SET first_name = :fn, last_name = :ln, email = :em, headline = :he, summary = :su WHERE profile_id = :pid AND user_id = :uid";
    $stmt = $pdo->prepare($sql);
    $stmt->execute(array(
        ':fn' => $_POST['first_name'],
        ':ln' => $_POST['last_name'],
        ':em' => $_POST['email'],
        ':he' => $_POST['headline'],
        ':su' => $_POST['summary'],
        ':pid' => $_POST['profile_id'],
        ':uid' => $_SESSION['user_id']
    ));

    // Delete existing education and reinsert
    $stmt = $pdo->prepare('DELETE FROM Education WHERE profile_id=:pid');
    $stmt->execute(array(':pid' => $_POST['profile_id']));
    insertEducations($pdo, $_POST['profile_id']);

    // Intricate update for positions
    $stmt = $pdo->prepare('SELECT position_id FROM Position WHERE profile_id = :pid');
    $stmt->execute(array(':pid' => $_POST['profile_id']));
    $existing_positions = array();
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        $existing_positions[] = $row['position_id'];
    }

    $rank = 1;
    for ($i=1; $i<=9; $i++) {
        if (!isset($_POST['pos_year'.$i])) continue;
        if (!isset($_POST['pos_desc'.$i])) continue;

        $year = $_POST['pos_year'.$i];
        $desc = $_POST['pos_desc'.$i];
        $pos_id = isset($_POST['position_id'.$i]) ? $_POST['position_id'.$i] : null;

        if ($pos_id && in_array($pos_id, $existing_positions)) {
            $stmt = $pdo->prepare('UPDATE Position SET rank=:rank, year=:year, description=:desc WHERE position_id=:pos_id AND profile_id=:pid');
            $stmt->execute(array(':rank' => $rank, ':year' => $year, ':desc' => $desc, ':pos_id' => $pos_id, ':pid' => $_POST['profile_id']));
            
            $key = array_search($pos_id, $existing_positions);
            unset($existing_positions[$key]);
        } else {
            $stmt = $pdo->prepare('INSERT INTO Position (profile_id, rank, year, description) VALUES (:pid, :rank, :year, :desc)');
            $stmt->execute(array(':pid' => $_POST['profile_id'], ':rank' => $rank, ':year' => $year, ':desc' => $desc));
        }
        $rank++;
    }

    foreach ($existing_positions as $del_id) {
        $stmt = $pdo->prepare('DELETE FROM Position WHERE position_id=:pos_id AND profile_id=:pid');
        $stmt->execute(array(':pos_id' => $del_id, ':pid' => $_POST['profile_id']));
    }

    $_SESSION['success'] = "Profile updated";
    header("Location: index.php");
    return;
}

$stmt = $pdo->prepare("SELECT * FROM Profile WHERE profile_id = :prof AND user_id = :uid");
$stmt->execute(array(":prof" => $profile_id, ":uid" => $_SESSION['user_id']));
$profile = $stmt->fetch(PDO::FETCH_ASSOC);

if ($profile === false) {
    $_SESSION['error'] = "Could not load profile";
    header('Location: index.php');
    return;
}

$positions = loadPos($pdo, $profile_id);
$educations = loadEdu($pdo, $profile_id);
?>
<!DOCTYPE html>
<html>
<head>
<title>Aryan's Resume Registry a8ac02c1</title>
<?php require_once "head.php"; ?>
</head>
<body>
<div class="container">
<h1>Editing Profile for <?= htmlentities($_SESSION['name']) ?></h1>
<?php flashMessages(); ?>
<form method="post" action="edit.php">
<input type="hidden" name="profile_id" value="<?= htmlentities($profile['profile_id']) ?>">
<p>First Name:
<input type="text" name="first_name" size="60" value="<?= htmlentities($profile['first_name']) ?>"></p>
<p>Last Name:
<input type="text" name="last_name" size="60" value="<?= htmlentities($profile['last_name']) ?>"></p>
<p>Email:
<input type="text" name="email" size="30" value="<?= htmlentities($profile['email']) ?>"></p>
<p>Headline:<br/>
<input type="text" name="headline" size="80" value="<?= htmlentities($profile['headline']) ?>"></p>
<p>Summary:<br/>
<textarea name="summary" rows="8" cols="80"><?= htmlentities($profile['summary']) ?></textarea></p>

<p>
Education: <input type="submit" id="addEdu" value="+">
<div id="edu_fields">
<?php
$eduCount = 0;
foreach($educations as $edu) {
    $eduCount++;
    echo '<div id="edu'.$eduCount.'">';
    echo '<p>Year: <input type="text" name="edu_year'.$eduCount.'" value="'.htmlentities($edu['year']).'">';
    echo '<input type="button" value="-" onclick="$(\'#edu'.$eduCount.'\').remove();return false;"></p>';
    echo '<p>School: <input type="text" size="80" name="edu_school'.$eduCount.'" class="school" value="'.htmlentities($edu['name']).'" /></p>';
    echo '</div>';
}
?>
</div>
</p>

<p>
Position: <input type="submit" id="addPos" value="+">
<div id="position_fields">
<?php
$posCount = 0;
foreach($positions as $pos) {
    $posCount++;
    echo '<div id="position'.$posCount.'">';
    echo '<p>Year: <input type="text" name="pos_year'.$posCount.'" value="'.htmlentities($pos['year']).'">';
    echo '<input type="button" value="-" onclick="$(\'#position'.$posCount.'\').remove();return false;"></p>';
    echo '<textarea name="pos_desc'.$posCount.'" rows="8" cols="80">'.htmlentities($pos['description']).'</textarea>';
    echo '<input type="hidden" name="position_id'.$posCount.'" value="'.$pos['position_id'].'">';
    echo '</div>';
}
?>
</div>
</p>
<p>
<input type="submit" value="Save">
<input type="submit" name="cancel" value="Cancel">
</p>
</form>
<script>
countPos = <?= $posCount ?>;
countEdu = <?= $eduCount ?>;

$(document).ready(function(){
    window.console && console.log('Document ready called');
    
    $('.school').autocomplete({ source: "school.php" });

    $('#addPos').click(function(event){
        event.preventDefault();
        if (countPos >= 9) {
            alert("Maximum of nine position entries exceeded");
            return;
        }
        countPos++;
        window.console && console.log("Adding position "+countPos);
        $('#position_fields').append(
            '<div id="position'+countPos+'"> \
            <p>Year: <input type="text" name="pos_year'+countPos+'" value="" /> \
            <input type="button" value="-" \
                onclick="$(\'#position'+countPos+'\').remove();return false;"></p> \
            <textarea name="pos_desc'+countPos+'" rows="8" cols="80"></textarea>\
            </div>');
    });

    $('#addEdu').click(function(event){
        event.preventDefault();
        if (countEdu >= 9) {
            alert("Maximum of nine education entries exceeded");
            return;
        }
        countEdu++;
        window.console && console.log("Adding education "+countEdu);
        $('#edu_fields').append(
            '<div id="edu'+countEdu+'"> \
            <p>Year: <input type="text" name="edu_year'+countEdu+'" value="" /> \
            <input type="button" value="-" onclick="$(\'#edu'+countEdu+'\').remove();return false;"></p> \
            <p>School: <input type="text" size="80" name="edu_school'+countEdu+'" class="school" value="" /></p>\
            </div>'
        );
        $('.school').autocomplete({ source: "school.php" });
    });
});
</script>
</div>
</body>
</html>
