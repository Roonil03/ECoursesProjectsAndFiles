gh auth login
gh repo create repo-exercise --public --add-readme
USERNAME=$(gh api user -q .login)
gh repo clone "$USERNAME/repo-exercise"
cd repo-exercise
echo "Exercise completed successfully!" > result.txt
git status
sleep 2
git add result.txt
git status
sleep 2
git commit -m "Successful exercise"
git push
sleep 1
gh repo view --web