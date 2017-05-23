<#assign value = property.value>
<#assign keyValue = value.getKey()>
<#assign elementValue = value.getElement()>
<#assign indexValue = value.getIndex()>
<#assign elementTag = c2h.getCollectionElementTag(property)>

<list name="${property.name}" inverse="${value.inverse?string}"
<#include "collection-tableattr.hbm.ftl">
      lazy="${c2h.getCollectionLazy(value)}"
<#if property.cascade != "none">
      cascade="${property.cascade}"
</#if>
<#if !property.basicPropertyAccessor>
      access="${property.propertyAccessorName}"
</#if>

<#if c2h.hasFetchMode(property)> fetch="${c2h.getFetchMode(property)}"</#if>>
<#assign metaattributable=property>
<#include "meta.hbm.ftl">
<#include "key.hbm.ftl">
<#if !c2h.isOrderBy(property)>
	<#assign orderbycolumn = value.getOrderBy()>
	<index column="${orderbycolumn}"/>
</#if>
<#if !c2h.isHaveIndex(property)>	
	<list-index>
		<#foreach column in indexValue.columnIterator>
			<#include "column.hbm.ftl">
		</#foreach>
	</list-index>
</#if>	
    	
<#include "${elementTag}-element.hbm.ftl">
</list>


