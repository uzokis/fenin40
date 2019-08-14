/*
 * This file is generated by jOOQ.
 */
package eu.fancybrackets.template.jooq.generated.tables.pojos;


import java.io.Serializable;
import java.sql.Timestamp;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Measurement implements Serializable {

    private static final long serialVersionUID = -711842651;

    private Integer   id;
    private String    containerId;
    private String    value;
    private Timestamp measureTime;

    public Measurement() {}

    public Measurement(Measurement value) {
        this.id = value.id;
        this.containerId = value.containerId;
        this.value = value.value;
        this.measureTime = value.measureTime;
    }

    public Measurement(
        Integer   id,
        String    containerId,
        String    value,
        Timestamp measureTime
    ) {
        this.id = id;
        this.containerId = containerId;
        this.value = value;
        this.measureTime = measureTime;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContainerId() {
        return this.containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Timestamp getMeasureTime() {
        return this.measureTime;
    }

    public void setMeasureTime(Timestamp measureTime) {
        this.measureTime = measureTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Measurement (");

        sb.append(id);
        sb.append(", ").append(containerId);
        sb.append(", ").append(value);
        sb.append(", ").append(measureTime);

        sb.append(")");
        return sb.toString();
    }
}