set -e

read -r UPSTREAM_REPO
read -r FIRST_NAME
read -r CERT_NAME

if gh auth status >/dev/null 2>&1; then
    echo "You are already logged into github.com."
else
    echo "Authentication required. Please follow the terminal prompts:"
    gh auth login
fi

gh repo fork "$UPSTREAM_REPO" --clone=false
sleep 3

USERNAME=$(gh api user -q .login)
REPO_NAME=$(echo "$UPSTREAM_REPO" | cut -d'/' -f2)
gh repo clone "$USERNAME/$REPO_NAME"
cd "$REPO_NAME"
git checkout -b my-branch
echo "First Name: $FIRST_NAME" >> class.md
echo "Certification: $CERT_NAME" >> class.md

git add class.md
git commit -m 'minor changes'
git push origin my-branch

PR_URL=$(gh pr create --repo "$UPSTREAM_REPO" --base main --head "$USERNAME:my-branch" --title "Minor changes" --body "Added $FIRST_NAME working on $CERT_NAME.")
echo "$PR_URL"