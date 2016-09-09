package com.pgs.model;

import javax.xml.bind.annotation.*;

/**
 * Created by jpolitowicz on 08.09.2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ErrorResponse {
    @XmlElement
    private String type;

    @XmlElement
    private String title;

    @XmlElement
    private int status;

    @XmlElement
    private String detail;

    @XmlElement
    private String instance;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }
}
