package com.scoreanalysis.bean;

import java.util.ArrayList;
import java.util.List;

public class StuClassExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StuClassExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andClsidIsNull() {
            addCriterion("clsid is null");
            return (Criteria) this;
        }

        public Criteria andClsidIsNotNull() {
            addCriterion("clsid is not null");
            return (Criteria) this;
        }

        public Criteria andClsidEqualTo(String value) {
            addCriterion("clsid =", value, "clsid");
            return (Criteria) this;
        }

        public Criteria andClsidNotEqualTo(String value) {
            addCriterion("clsid <>", value, "clsid");
            return (Criteria) this;
        }

        public Criteria andClsidGreaterThan(String value) {
            addCriterion("clsid >", value, "clsid");
            return (Criteria) this;
        }

        public Criteria andClsidGreaterThanOrEqualTo(String value) {
            addCriterion("clsid >=", value, "clsid");
            return (Criteria) this;
        }

        public Criteria andClsidLessThan(String value) {
            addCriterion("clsid <", value, "clsid");
            return (Criteria) this;
        }

        public Criteria andClsidLessThanOrEqualTo(String value) {
            addCriterion("clsid <=", value, "clsid");
            return (Criteria) this;
        }

        public Criteria andClsidLike(String value) {
            addCriterion("clsid like", value, "clsid");
            return (Criteria) this;
        }

        public Criteria andClsidNotLike(String value) {
            addCriterion("clsid not like", value, "clsid");
            return (Criteria) this;
        }

        public Criteria andClsidIn(List<String> values) {
            addCriterion("clsid in", values, "clsid");
            return (Criteria) this;
        }

        public Criteria andClsidNotIn(List<String> values) {
            addCriterion("clsid not in", values, "clsid");
            return (Criteria) this;
        }

        public Criteria andClsidBetween(String value1, String value2) {
            addCriterion("clsid between", value1, value2, "clsid");
            return (Criteria) this;
        }

        public Criteria andClsidNotBetween(String value1, String value2) {
            addCriterion("clsid not between", value1, value2, "clsid");
            return (Criteria) this;
        }

        public Criteria andClsNameIsNull() {
            addCriterion("cls_name is null");
            return (Criteria) this;
        }

        public Criteria andClsNameIsNotNull() {
            addCriterion("cls_name is not null");
            return (Criteria) this;
        }

        public Criteria andClsNameEqualTo(String value) {
            addCriterion("cls_name =", value, "clsName");
            return (Criteria) this;
        }

        public Criteria andClsNameNotEqualTo(String value) {
            addCriterion("cls_name <>", value, "clsName");
            return (Criteria) this;
        }

        public Criteria andClsNameGreaterThan(String value) {
            addCriterion("cls_name >", value, "clsName");
            return (Criteria) this;
        }

        public Criteria andClsNameGreaterThanOrEqualTo(String value) {
            addCriterion("cls_name >=", value, "clsName");
            return (Criteria) this;
        }

        public Criteria andClsNameLessThan(String value) {
            addCriterion("cls_name <", value, "clsName");
            return (Criteria) this;
        }

        public Criteria andClsNameLessThanOrEqualTo(String value) {
            addCriterion("cls_name <=", value, "clsName");
            return (Criteria) this;
        }

        public Criteria andClsNameLike(String value) {
            addCriterion("cls_name like", value, "clsName");
            return (Criteria) this;
        }

        public Criteria andClsNameNotLike(String value) {
            addCriterion("cls_name not like", value, "clsName");
            return (Criteria) this;
        }

        public Criteria andClsNameIn(List<String> values) {
            addCriterion("cls_name in", values, "clsName");
            return (Criteria) this;
        }

        public Criteria andClsNameNotIn(List<String> values) {
            addCriterion("cls_name not in", values, "clsName");
            return (Criteria) this;
        }

        public Criteria andClsNameBetween(String value1, String value2) {
            addCriterion("cls_name between", value1, value2, "clsName");
            return (Criteria) this;
        }

        public Criteria andClsNameNotBetween(String value1, String value2) {
            addCriterion("cls_name not between", value1, value2, "clsName");
            return (Criteria) this;
        }

        public Criteria andClsMajorIsNull() {
            addCriterion("cls_major is null");
            return (Criteria) this;
        }

        public Criteria andClsMajorIsNotNull() {
            addCriterion("cls_major is not null");
            return (Criteria) this;
        }

        public Criteria andClsMajorEqualTo(String value) {
            addCriterion("cls_major =", value, "clsMajor");
            return (Criteria) this;
        }

        public Criteria andClsMajorNotEqualTo(String value) {
            addCriterion("cls_major <>", value, "clsMajor");
            return (Criteria) this;
        }

        public Criteria andClsMajorGreaterThan(String value) {
            addCriterion("cls_major >", value, "clsMajor");
            return (Criteria) this;
        }

        public Criteria andClsMajorGreaterThanOrEqualTo(String value) {
            addCriterion("cls_major >=", value, "clsMajor");
            return (Criteria) this;
        }

        public Criteria andClsMajorLessThan(String value) {
            addCriterion("cls_major <", value, "clsMajor");
            return (Criteria) this;
        }

        public Criteria andClsMajorLessThanOrEqualTo(String value) {
            addCriterion("cls_major <=", value, "clsMajor");
            return (Criteria) this;
        }

        public Criteria andClsMajorLike(String value) {
            addCriterion("cls_major like", value, "clsMajor");
            return (Criteria) this;
        }

        public Criteria andClsMajorNotLike(String value) {
            addCriterion("cls_major not like", value, "clsMajor");
            return (Criteria) this;
        }

        public Criteria andClsMajorIn(List<String> values) {
            addCriterion("cls_major in", values, "clsMajor");
            return (Criteria) this;
        }

        public Criteria andClsMajorNotIn(List<String> values) {
            addCriterion("cls_major not in", values, "clsMajor");
            return (Criteria) this;
        }

        public Criteria andClsMajorBetween(String value1, String value2) {
            addCriterion("cls_major between", value1, value2, "clsMajor");
            return (Criteria) this;
        }

        public Criteria andClsMajorNotBetween(String value1, String value2) {
            addCriterion("cls_major not between", value1, value2, "clsMajor");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}