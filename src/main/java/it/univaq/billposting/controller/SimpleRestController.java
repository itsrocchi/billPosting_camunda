package it.univaq.billposting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activity.InvalidActivityException;

import org.camunda.bpm.engine.MismatchingMessageCorrelationException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationResultWithVariables;

import it.univaq.billposting.model.*;

@RestController //restcontroller ci da modo di dichiarare dei metodi che vengono esposti via endpoint
public class SimpleRestController {

	@Autowired
	private RuntimeService runtimeService;
	
	@SuppressWarnings("unchecked") //altrimenti ho un warning sulla lista zones idk
	@PostMapping("req")
	public Map<String,Object> req(@RequestBody ClientData clientData){
		
		Map<String,Object> response = new HashMap<>();
		
		try {
			MessageCorrelationResultWithVariables correlation = runtimeService
				.createMessageCorrelation("req")
				.setVariable("clientData", clientData)
				.correlateWithResultAndVariables(false);

			List<ZoneData> zones = (List<ZoneData>) correlation.getVariables().getValue("zoneList", Object.class);
			float price = 0;
			for (ZoneData zone : zones) {
				price += zone.getPrice();
			}
	
			
			String requestId = correlation.getVariables().getValue("requestId", String.class);
			
			
			response.put("availableZones", zones);
			response.put("totalPrice", Math.round(price*100.0)/100.0);
			response.put("requestId", requestId);
			
			
		}catch (org.camunda.bpm.engine.ProcessEngineException e) {
			response.put("Error", e.getMessage());
			response.put("Hint", "If you get an 'Input is empty' exception, probably you chose a wrong username");
			response.put("Advice", "internal server error, check the logs");
			System.out.println("An internal server error has occurred, check the logs for more info");
		}
		return response;
	}
	
	@GetMapping("decision/{requestId}/{decision}")
	public Map<String,Object> getBillingInfo(@PathVariable String requestId, @PathVariable String decision) {

		Map<String, Object> response = new HashMap<>();

		try {
			MessageCorrelationResultWithVariables correlation = runtimeService
					.createMessageCorrelation("decision")
					.processInstanceVariableEquals("requestId", requestId)
					.setVariable("decision", decision)
					.correlateWithResultAndVariables(false);

			BillingInfo billingInformation = new BillingInfo();
			if(decision.equals("confirm")) {
				billingInformation.setAccountHolder(correlation.getVariables().getValue("accountHolder", String.class));
				billingInformation.setInvoiceNumber(correlation.getVariables().getValue("invoiceNumber", String.class));
				double amountDue = Float.parseFloat(correlation.getVariables().getValue("amountDue", String.class));
				billingInformation.setAmountDue(Math.round(amountDue*100.0)/100.0);
				response.put("billingInformation", billingInformation);
			} else if(decision.equals("cancel")) {
				response.put("billingInformation", billingInformation);
			} else {
				response.put("Error", "Invalid decision, try with 'confirm' or 'cancel' ");
				/* 
				 * potevo anche fare:
				 * throw new IllegalArgumentException("Invalid decision");
				 * ma in questo modo l'utente verifica qual è il problema sia dalla risposta sia dai log
				 * (non vede da nessuna parte un errore 500)
				 */
			}
		} catch (MismatchingMessageCorrelationException ex) {
			response.put("Error", "no existing request with the specified id");
			System.out.println("There is no existing request with the specified id");
		}
		return response;
	}
	
	@GetMapping("decision/{requestId}")
	public void getBillingInfo(@PathVariable String requestId) {
		//esempio inverso rispetto a ciò che avviene a riga 89, qui ottengo su terminale exception e stack trace
		//mentre in postman ottengo un errore 500
		throw new IllegalArgumentException("You must specify both request id and decision in the url!");
	}
	
}
