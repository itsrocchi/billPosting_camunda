package it.univaq.billposting.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.camunda.spin.json.SpinJsonNode;
import org.springframework.stereotype.Service;

import it.univaq.billposting.model.ClientData;
import it.univaq.billposting.model.UserData;
import it.univaq.billposting.model.ZoneData;

@Service("billpostingService")
public class BillPostingService {
	
	
	public UserData unmarshallUserData(SpinJsonNode userDataJSON) {
		return userDataJSON.mapTo(UserData.class);
	}

	public List<ZoneData> unmarshallZoneList(SpinJsonNode zoneListJSON) {
		
		return zoneListJSON.mapTo("java.util.ArrayList<it.univaq.billposting.model.ZoneData>");
	}

				
	public List<ZoneData> unmarshallZoneListFiltered(SpinJsonNode zoneListJSON, ClientData clientData) {
        List<ZoneData> zones = zoneListJSON.mapTo("java.util.ArrayList<it.univaq.billposting.model.ZoneData>");
        //If the client does not specify the strat value in the req, it is automatically set to 0, so if strat is 0
        //then the first strategy is automatically selected
        if (clientData.getStrat() == 1) {
            return filterZonesStrategy1(zones, clientData);
        } else if (clientData.getStrat() == 0){ //default strat
        	return filterZonesStrategy1(zones, clientData);
        } else if (clientData.getStrat() == 2) {
            return filterZonesStrategy2(zones, clientData);
        } else {
            throw new IllegalArgumentException("Invalid strategy value: " + clientData.getStrat());
        }
    }

    private List<ZoneData> filterZonesStrategy1(List<ZoneData> zones, ClientData clientData) {
    	//This strategy aims to prioritize selecting the most expensive zones within the budget for each city.
    	//The rationale behind this might be to target premium zones first
    	//which could be more desirable for various reasons (e.g., higher visibility, better locations, etc.).
        List<ZoneData> selectedZones = new ArrayList<>();
        for (String city : clientData.getCities()) {
            int budget = clientData.getBudgets().get(clientData.getCities().indexOf(city));
            List<ZoneData> cityZones = getZonesForCity(city, zones);
            Collections.sort(cityZones, Comparator.comparingDouble(ZoneData::getPrice).reversed());
            int totalCost = 0;
            for (ZoneData zone : cityZones) {
                if (totalCost + zone.getPrice() <= budget) {
                    selectedZones.add(zone);
                    totalCost += zone.getPrice();
                } else {
                    break;
                }
            }
        }
        return selectedZones;
    }

    private List<ZoneData> filterZonesStrategy2(List<ZoneData> zones, ClientData clientData) {
    	// This strategy aims to select as many zones as possible within the given budget for each city.
    	// This might be useful if the objective is to maximize coverage or exposure.
    	
        List<ZoneData> selectedZones = new ArrayList<>();
        for (String city : clientData.getCities()) {
            int budget = clientData.getBudgets().get(clientData.getCities().indexOf(city));
            List<ZoneData> cityZones = getZonesForCity(city, zones);
            cityZones.sort(Comparator.comparingDouble(ZoneData::getPrice));
            int totalCost = 0;
            for (ZoneData zone : cityZones) {
                if (totalCost + zone.getPrice() <= budget) {
                    selectedZones.add(zone);
                    totalCost += zone.getPrice();
                } else {
                    break;
                }
            }
        }
        return selectedZones;
    }
	
	private static List<ZoneData> getZonesForCity(String city, List<ZoneData> zones) {
		List<ZoneData> cityZones = new ArrayList<>();
		for (ZoneData zone : zones) {
			if (zone.getCity().equalsIgnoreCase(city)) {
				cityZones.add(zone);
			}
		}
		return cityZones;
	}

	
}
