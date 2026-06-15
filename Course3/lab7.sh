set -e
read -r UPSTREAM_REPO

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

cat << 'EOF' > class.txt
Crimson
Orange
Blue
Cyan
Yellow
Charcoal
Khaki
Coral
Silver
Fuchsia
Purple
Brown
Red
EOF

git diff || true
echo "Press [ENTER] to stage, commit, and push these changes..."
read -r

git add class.txt
git commit -m 'some message'
git push