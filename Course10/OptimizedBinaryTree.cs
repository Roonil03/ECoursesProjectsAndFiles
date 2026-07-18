using System;

namespace AlgorithmSuite
{
    public class AVLNode
    {
        public int Key { get; set; }
        public int Height { get; set; } = 1;
        public AVLNode? Left { get; set; }
        public AVLNode? Right { get; set; }

        public AVLNode(int key) => Key = key;
    }

    public class AVLTree
    {
        public AVLNode? Root { get; private set; }

        private int GetHeight(AVLNode? node) => node?.Height ?? 0;

        private int GetBalance(AVLNode? node) => node == null ? 0 : GetHeight(node.Left) - GetHeight(node.Right);

        private AVLNode RightRotate(AVLNode y)
        {
            var x = y.Left ?? throw new InvalidOperationException();
            var t2 = x.Right;

            x.Right = y;
            y.Left = t2;

            y.Height = Math.Max(GetHeight(y.Left), GetHeight(y.Right)) + 1;
            x.Height = Math.Max(GetHeight(x.Left), GetHeight(x.Right)) + 1;

            return x;
        }

        private AVLNode LeftRotate(AVLNode x)
        {
            var y = x.Right ?? throw new InvalidOperationException();
            var t2 = y.Left;

            y.Left = x;
            x.Right = t2;

            x.Height = Math.Max(GetHeight(x.Left), GetHeight(x.Right)) + 1;
            y.Height = Math.Max(GetHeight(y.Left), GetHeight(y.Right)) + 1;

            return y;
        }

        public void Insert(int key) => Root = InsertNode(Root, key);

        private AVLNode InsertNode(AVLNode? node, int key)
        {
            if (node == null) return new AVLNode(key);

            if (key < node.Key)
                node.Left = InsertNode(node.Left, key);
            else if (key > node.Key)
                node.Right = InsertNode(node.Right, key);
            else
                return node;

            node.Height = 1 + Math.Max(GetHeight(node.Left), GetHeight(node.Right));
            int balance = GetBalance(node);

            if (balance > 1 && key < (node.Left?.Key ?? 0))
                return RightRotate(node);

            if (balance < -1 && key > (node.Right?.Key ?? 0))
                return LeftRotate(node);

            if (balance > 1 && key > (node.Left?.Key ?? 0))
            {
                node.Left = LeftRotate(node.Left!);
                return RightRotate(node);
            }

            if (balance < -1 && key < (node.Right?.Key ?? 0))
            {
                node.Right = RightRotate(node.Right!);
                return LeftRotate(node);
            }

            return node;
        }

        public bool Search(int key)
        {
            var current = Root;
            while (current != null)
            {
                if (key == current.Key) return true;
                current = key < current.Key ? current.Left : current.Right;
            }
            return false;
        }
    }
}
