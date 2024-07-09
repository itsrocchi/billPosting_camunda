<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:pos="http://disim.univaq.it/services/postingservice">
   <soapenv:Header/>
   <soapenv:Body>
      <pos:availabilityRequest>
         <pos:applicant>
            <pos:name>${userData.name}</pos:name>
            <pos:surname>${userData.surname}</pos:surname>
            <pos:taxCode>${userData.taxCode}</pos:taxCode>
            <pos:address>${userData.address}</pos:address>
            <pos:city>${userData.city}</pos:city>
            <pos:zip>${userData.zipCode}</pos:zip>
            <pos:email>${userData.email}</pos:email>
         </pos:applicant>
         <pos:posting>
            <pos:posterFormat>${clientData.format}</pos:posterFormat>
            <#list zoneList as zone >
            <pos:zone>
               <pos:id>${zone.id}</pos:id>
               <pos:city>${zone.city}</pos:city>
            </pos:zone>
            </#list>
         </pos:posting>
      </pos:availabilityRequest>
   </soapenv:Body>
</soapenv:Envelope>