package template.springboot.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class Item {

    @Id @GeneratedValue
    private Integer ID;

    private String NAME;
    private Integer PRICE;
    private String PICTURE;
    private String DETAIL;
    private Integer RECOMMENDED;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Integer getPRICE() {
        return PRICE;
    }

    public void setPRICE(Integer PRICE) {
        this.PRICE = PRICE;
    }

    public String getPICTURE() {
        return PICTURE;
    }

    public void setPICTURE(String PICTURE) {
        this.PICTURE = PICTURE;
    }

    public String getDETAIL() {
        return DETAIL;
    }

    public void setDETAIL(String DETAIL) {
        this.DETAIL = DETAIL;
    }

    public Integer getRECOMMENDED() {
        return RECOMMENDED;
    }

    public void setRECOMMENDED(Integer RECOMMENDED) {
        this.RECOMMENDED = RECOMMENDED;
    }
}
