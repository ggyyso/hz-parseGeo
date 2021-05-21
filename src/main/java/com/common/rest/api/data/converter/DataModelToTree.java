package com.common.rest.api.data.converter;

import com.common.rest.api.data.AbstractTreeModel;

import java.util.*;

/**
 * Created by zzge163 on 2019/3/7.
 */
public class DataModelToTree<T extends AbstractTreeModel> {
    private List<T> changeList;
    private String topId = null;

    public DataModelToTree(List<T> list) {
        this.changeList = list;
    }

    public DataModelToTree(List<T> list, String topId) {
        this.changeList = list;
        this.topId = topId;
    }

    public List<T> change() {
        return this.changeByLoop();
    }

    // 两层循环组织树结构
    public List<T> changeByLoop() {
        ArrayList trees = new ArrayList();

        for (AbstractTreeModel treeNode : this.changeList) {
            if (treeNode.getParentId() == null
                    || treeNode.getParentId() == 0) {
                trees.add(treeNode);
            }

            for (AbstractTreeModel it : this.changeList) {
                if (it.getParentId() != null
                        && it.getParentId().equals(treeNode.getId())) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setLeaf(false);
                        treeNode.setChildren(new ArrayList<T>());
                    }
                    treeNode.getChildren().add(it);
                }
            }
        }
        return trees;
    }

    // 递归方法组织树结构
    public List<T> changeByRecursive() {
        List trees = new ArrayList();
        for (AbstractTreeModel treeNode : this.changeList) {
            if (treeNode.getParentId() == null
                    || treeNode.getParentId() == 0) {
                trees.add(findChildren(treeNode, this.changeList));
            }
        }

        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public AbstractTreeModel findChildren(AbstractTreeModel treeNode, List<T> treeNodes) {
        for (AbstractTreeModel it : treeNodes) {
            if (treeNode.getId().equals(it.getParentId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setLeaf(false);
                    treeNode.setChildren(new ArrayList<T>());
                }
                treeNode.getChildren().add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }
}
