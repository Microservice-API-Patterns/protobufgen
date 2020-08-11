syntax = "${syntax}";

<#if package?has_content>
package ${package};

</#if>
<#list importStatements as import>
import <#if import.isPublic()>public </#if>"${import.fileName}";
</#list>
<#if importStatements?has_content>

</#if>
<#list messages as message>
<#if message.comment?has_content>
/* ${message.comment} */
</#if>
message ${message.name} {
  <#list message.fields as field>
  <#if field.repeated>repeated </#if>${field.type} ${field.name} = ${field.number};<#if field.comment?has_content> // ${field.comment}</#if>
  </#list>
}

</#list>
<#list enums as enum>
<#if enum.comment?has_content>
/* ${enum.comment} */
</#if>
enum ${enum.name} {
  <#list enum.fields as field>
  ${field.name} = ${field.value};<#if field.comment?has_content> // ${field.comment}</#if>
  </#list>
}

</#list>
<#list services as service>
<#if service.comment?has_content>
/* ${service.comment} */
</#if>
service ${service.name} {
  <#list service.remoteProcedureCalls as rpc>
  rpc ${rpc.name}(<#if rpc.isInputStreamed()>stream </#if>${rpc.input.name}) returns (<#if rpc.isOutputStreamed()>stream </#if>${rpc.output.name});<#if rpc.comment?has_content> // ${rpc.comment}</#if>
  </#list>
}

</#list>