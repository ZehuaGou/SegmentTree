/**
 * @author Li Ersan
 */
public class SegmentTree<E> {

    private E[] tree;

    private E[] data;

    private Merger<E> merger;

    public SegmentTree(E[] arr, Merger<E> merger) {
        this.merger = merger;

        data = (E[]) new Object[arr.length];
        for (int i = 0; i < arr.length; i++) {
            data[i] = arr[i];
        }

        tree = (E[]) new Object[4 * arr.length];
        buildSegmentTree(0, 0, data.length - 1);
    }

    /**
     * 在treeIndex的位置创建表示区间[left...right]的线段树
     *
     * @param treeIndex
     * @param left
     * @param right
     */
    private void buildSegmentTree(int treeIndex, int left, int right) {
        if (left == right) {
            tree[treeIndex] = data[left];
            return;
        }

        int leftTreeIndex = leftChild(treeIndex);
        int rightTreeIndex = rightChild(treeIndex);

        int mid = left + (right - left) / 2;
        buildSegmentTree(leftTreeIndex, left, mid);
        buildSegmentTree(rightTreeIndex, mid + 1, right);

        tree[treeIndex] = merger.merge(tree[leftTreeIndex], tree[rightTreeIndex]);
    }

    public int getSize() {
        return data.length - 1;
    }

    public E get(int index) {

        if (index < 0 || index >= data.length) {
            throw new IllegalArgumentException("下标越界");
        }

        return data[index];
    }

    /**
     * 返回完全二叉树的数组表示中，一个索引所表示的元素的左孩子结点的索引
     *
     * @param index
     * @return
     */
    private int leftChild(int index) {
        return 2 * index + 1;
    }

    /**
     * 返回完全二叉树的数组表示中，一个索引所表示的元素的右孩子结点的索引
     *
     * @param index
     * @return
     */
    private int rightChild(int index) {
        return 2 * index + 2;
    }

    /**
     * 返回区间[queryL, queryR]的值
     *
     * @param queryL
     * @param queryR
     * @return
     */
    public E query(int queryL, int queryR) {
        if (queryL < 0 || queryL >= data.length || queryR < 0 || queryR >= data.length) {
            throw new IllegalArgumentException("下标越界");
        }

        return query(0, 0, data.length - 1, queryL, queryR);
    }

    /**
     * 在以treeIndex为根的线段树中[left...right]的范围里，搜索区间[queryL...queryR]的值
     *
     * @param treeIndex
     * @param left
     * @param right
     * @param queryL
     * @param queryR
     * @return
     */
    private E query(int treeIndex, int left, int right, int queryL, int queryR) {

        if (left == queryL && right == queryR) {
            return tree[treeIndex];
        }

        int mid = left + (right - left) / 2;
        int leftTreeIndex = leftChild(treeIndex);
        int rightTreeIndex = rightChild(treeIndex);

        if (queryL >= mid + 1) {
            return query(rightTreeIndex, mid + 1, right, queryL, queryR);
        } else if (queryR <= mid) {
            return query(leftTreeIndex, left, mid, queryL, queryR);
        }

        E leftResult = query(leftTreeIndex, left, mid, queryL, mid);
        E rightResult = query(rightTreeIndex, mid + 1, right, mid + 1, queryR);

        return merger.merge(leftResult, rightResult);
    }

    /**
     * 将index位置的值，更新为e
     *
     * @param index
     * @param e
     */
    public void set(int index, E e) {
        if (index < 0 || index >= data.length) {
            throw new IllegalArgumentException("下标越界");
        }

        data[index] = e;
        set(0, 0, data.length - 1, index, e);
    }

    /**
     * 在以treeIndex为根的线段树中更新index的值为e
     *
     * @param treeIndex
     * @param left
     * @param right
     * @param index
     * @param e
     */
    private void set(int treeIndex, int left, int right, int index, E e) {
        if (left == right) {
            tree[treeIndex] = e;
            return;
        }

        int mid = left + (right - left) / 2;
        int leftTreeIndex = leftChild(treeIndex);
        int rightTreeIndex = rightChild(treeIndex);

        if (index >= mid + 1) {
            set(rightTreeIndex, mid + 1, right, index, e);
        } else { // index <= mid
            set(leftTreeIndex, left, mid, index, e);
        }

        tree[treeIndex] = merger.merge(tree[leftTreeIndex], tree[rightTreeIndex]);
    }

    @Override
    public String toString() {

        StringBuilder res = new StringBuilder();
        res.append('[');
        for (int i = 0; i < tree.length; i++) {
            if (tree[i] != null) {
                res.append(tree[i]);
            } else {
                res.append("null");
            }

            if (i != tree.length - 1) {
                res.append(",");
            }
        }
        res.append(']');

        return res.toString();
    }
}
