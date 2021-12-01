package org.rdlinux.ezmybatis.core.sqlpart;

import org.rdlinux.ezmybatis.core.sqlpart.selectfield.EzSelectField;

import java.util.List;

public class EzSelect {
    private List<EzSelectField> selectFields;

    public EzSelect(List<EzSelectField> selectFields) {
        this.selectFields = selectFields;
    }

    public List<EzSelectField> getSelectFields() {
        return this.selectFields;
    }

    public void setSelectFields(List<EzSelectField> selectFields) {
        this.selectFields = selectFields;
    }
}
