package com.common.rest.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
/**
 * description: AbstractTreeModel <br>
 * date: 2019/8/15 18:19 <br>
 * version: 1.0 <br>
 *
 * @author: zhangzhe <br>
 */

public abstract class AbstractTreeModel<T> extends AbstractModel<T> {
    @JsonProperty("index")
    @ApiModelProperty("id")
    protected Integer id;

    @ApiModelProperty("父节点ID")
    protected Integer parentId;

    @JsonProperty("list")
    @ApiModelProperty("子节点")
    protected List<AbstractTreeModel> children;

    @ApiModelProperty("是否叶子节点")
    protected boolean isLeaf = true;

    public AbstractTreeModel() {
    }

    public AbstractTreeModel(T model) {
        super(model);
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<AbstractTreeModel> getChildren() {
        return this.children;
    }

    public void setChildren(List<AbstractTreeModel> children) {
        this.children = children;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isLeaf() {
        return this.isLeaf;
    }

    public void setLeaf(boolean leaf) {
        this.isLeaf = leaf;
    }
}
