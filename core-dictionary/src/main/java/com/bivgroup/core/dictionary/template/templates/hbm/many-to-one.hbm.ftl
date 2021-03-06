<many-to-one
        name="${property.name}"
		<#if !c2h.isDynamicModel()>
        class="${c2j.getJavaTypeName(property, false)}"
		<#else>
			<#if property.value.referencedEntityName?exists>
			entity-name="${property.value.referencedEntityName}"
			</#if>
		</#if>
<#if !c2h.isManyToOne(property)>

</#if>
<#if property.value.referencedPropertyName?exists>
        property-ref="${property.value.referencedPropertyName}"
</#if>
<#if !property.updateable>
        update="false"
</#if>
<#if !property.insertable>
        insert="false"
</#if>
<#if !property.basicPropertyAccessor>
        access="${property.propertyAccessorName}"
</#if>
<#if property.cascade != "none">
        cascade="${property.cascade}"
</#if>
<#assign fetchmode = c2h.getFetchMode(property)>
<#if fetchmode != "default">
        fetch="${fetchmode}"
</#if>
<#if !property.optimisticLocked>
        optimistic-lock="false"
</#if>
<#if property.value.hasFormula()>
    <#assign formula = c2h.getFormulaForProperty(property)>
    <#if formula?exists>
        formula="${formula.text}"
    </#if>
</#if>
        >
<#assign metaattributable=property>
	<#include "meta.hbm.ftl">    
<#foreach column in property.columnIterator>
    <#include "column.hbm.ftl">
</#foreach>
</many-to-one>
