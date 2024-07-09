[
 Username: ${userData.username},
 Name: ${userData.name},
 Surname: ${userData.surname},
 E-mail: ${userData.email},
 Cities:
 <#list clientData.cities as city>
   - ${city}
 </#list>
 Budgets:
 <#list clientData.budgets as budget>
   - ${budget}
 </#list>
 Requested format: ${clientData.format},
 RequestId: ${requestId},
 Zones: 
 <#list zoneList as zone>
     - ${zone.name} - ${zone.city} (ID: ${zone.id}, Price: ${zone.price})
 </#list>
 InvoiceNumber: ${invoiceNumber},
 AmountDue: ${amountDue}
]