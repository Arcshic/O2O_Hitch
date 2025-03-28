package com.heima.modules.vo;

import com.heima.commons.domin.po.PO;
import com.heima.commons.domin.vo.VO;
import com.heima.commons.enums.InitialResolverType;
import com.heima.commons.groups.Group;
import com.heima.commons.initial.annotation.InitialResolver;
import com.heima.modules.po.AttachmentPO;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

public class AttachmentVO implements VO {
    /**
     * 主键
     */
    @InitialResolver(resolver = InitialResolverType.GEN_SNOWFLAKE_ID, groups = {Group.Create.class})
    @NotEmpty(message = "ID不能为空", groups = {Group.Update.class})
    private String id;
    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件URL
     */
    private String url;

    /**
     * 文件大小
     */
    private long lenght;

    //文件扩展名
    private String ext;

    //文件MD5摘要值
    private String md5;

    /**
     * 认证状态 未认证：0认证成功：1认证失败：2
     */
    private String status;

    /**
     * 乐观锁
     */
    private Integer revision;


    /**
     * 创建人
     */
    @InitialResolver(resolver = InitialResolverType.CURRENTA_ACCOUNT, groups = {Group.Create.class})
    private String createdBy;

    /**
     * 创建时间
     */
    @InitialResolver(resolver = InitialResolverType.CURRENT_DATE, groups = {Group.Create.class})
    private Date createdTime;

    /**
     * 更新人
     */
    @InitialResolver(resolver = InitialResolverType.CURRENTA_ACCOUNT, groups = {Group.Update.class})
    private String updatedBy;

    /**
     * 更新时间
     */
    @InitialResolver(resolver = InitialResolverType.CURRENT_DATE, groups = {Group.Update.class})
    private Date updatedTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getLenght() {
        return lenght;
    }

    public void setLenght(long lenght) {
        this.lenght = lenght;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public Class getPO() {
        return AttachmentPO.class;
    }
}
