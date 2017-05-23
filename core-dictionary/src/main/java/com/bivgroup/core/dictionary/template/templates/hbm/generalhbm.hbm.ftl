<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">

<hibernate-mapping>
    <!-- Generated ${date} ${module} ${version} -->

<#if c2h.isImportData()>
    <#include "import.hbm.ftl">
</#if>
<#if c2h.isNamedQueries()>
    <#include "query.hbm.ftl">
</#if>
<#if c2h.isNamedSQLQueries()>
    <#include "sql-query.hbm.ftl">
</#if>
<#if c2h.isFilterDefinitions()>
    <#include "filter-def.hbm.ftl">
</#if>

</hibernate-mapping>
